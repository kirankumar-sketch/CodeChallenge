package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransfer;
import com.db.awmd.challenge.exception.FundDepositAccountException;
import com.db.awmd.challenge.exception.FundTransferAccountException;
import com.db.awmd.challenge.exception.FundWithdrawlAccountException;
import com.db.awmd.challenge.service.EmailNotificationService;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class FundTransferHandling implements Runnable {
	
	private EmailNotificationService ens=new EmailNotificationService();
	private FundTransfer fundTransfer=null;
	private Map<String, Account> accounts=null;
	
	public FundTransferHandling() {
	}
	public FundTransferHandling(FundTransfer fundTransfer, Map<String, Account> accounts) {
		this.fundTransfer=fundTransfer;
		this.accounts=accounts;
	}
	
	synchronized void fundTransfer(FundTransfer fundTransfer,Map<String, Account> accounts) throws FundTransferAccountException {
		
		synchronized (this.accounts) {
		try {
		
			boolean validwithdrawl=this.fundWithdrawl(this.fundTransfer,this.accounts);
			if(validwithdrawl) {
				boolean validdeposit=this.fundDeposit(this.fundTransfer,this.accounts);
				if(validdeposit) {
					log.info("FundTransfer is completed successfully");
				}else {
					log.info("FundTransfer process is failed. Please undo the withdrawl process from sender account");
			}
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		}
		
	}

	synchronized boolean fundDeposit(FundTransfer fundTransfer, Map<String, Account> accounts) throws FundDepositAccountException {
   
		log.info("FundDeposit account is started {}", fundTransfer);
		Account accountTo= accounts.get(fundTransfer.getAccountToId());
		BigDecimal currentbalance=accountTo.getBalance();
		BigDecimal totalamount=currentbalance.add(fundTransfer.getTransferAmount());
		Account accountUpdate= new Account(fundTransfer.getAccountToId(), totalamount);
		synchronized (this.accounts) {
		accounts.computeIfPresent(fundTransfer.getAccountToId(), (k, v) ->accountUpdate);
		ens.notifyAboutTransfer(accountUpdate, "The amount "+fundTransfer.getTransferAmount()+" is credited to your accountnumber from "+fundTransfer.getAccountFromId());
		}
		log.info("FundDeposit account is completed {}", fundTransfer);  
		return true;
	}
	synchronized boolean fundWithdrawl(FundTransfer fundTransfer, Map<String, Account> accounts) throws FundWithdrawlAccountException {
		
		log.info("fundWithdrawl account is started {}", fundTransfer);
		Account accountFrom= accounts.get(fundTransfer.getAccountFromId());
		BigDecimal currentbalance=accountFrom.getBalance();
		BigDecimal totalamount=currentbalance.subtract(fundTransfer.getTransferAmount());
		Account accountUpdate= new Account(fundTransfer.getAccountFromId(), totalamount);
		synchronized (this.accounts) {
		accounts.computeIfPresent(fundTransfer.getAccountFromId(), (k, v) ->accountUpdate);
	    ens.notifyAboutTransfer(accountUpdate, "The amount "+fundTransfer.getTransferAmount()+" is debited from your accountnumber and credited to "+fundTransfer.getAccountToId());
		}
		log.info("fundWithdrawl account is completed {}", fundTransfer);
		return true;
		
	}  	
	
	@Override
	public void run() {
		this.fundTransfer(this.fundTransfer, this.accounts);
	}
}


