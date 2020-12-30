package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransfer;
import com.db.awmd.challenge.exception.FundDepositAccountException;
import com.db.awmd.challenge.exception.FundTransferAccountException;
import com.db.awmd.challenge.exception.FundWithdrawlAccountException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.EmailNotificationService;
import com.db.awmd.challenge.web.AccountsController;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class FundTransferRepositoryInMemory implements FundTransferRepository {

private EmailNotificationService ens=new EmailNotificationService();
private FundTransfer fundTransfer=null;
private Map<String, Account> accounts=null;
@Autowired
private AccountsService accountsService;
public FundTransferRepositoryInMemory() {
}
public FundTransferRepositoryInMemory(FundTransfer fundTransfer) {
	this.fundTransfer=fundTransfer;
}

@Override
public void fundTransfer(FundTransfer fundTransfer) throws FundTransferAccountException {
	log.info("FundTransfer account started {}", fundTransfer);
	FundTransferRepositoryInMemory ftrim=new FundTransferRepositoryInMemory(fundTransfer);
	ftrim.accounts=accountsService.getAccounts();
	boolean validfund=validateFundTransfer(ftrim.fundTransfer,ftrim.accounts);
	if(validfund) {
		log.info("validation on FundTransfer is completed successfuuly {}", fundTransfer);
		FundTransferHandling fth=new FundTransferHandling(ftrim.fundTransfer,ftrim.accounts);
		Thread t=new Thread(fth);
	    t.start();

		}
}  	

public boolean validateFundTransfer(FundTransfer fundTransfer,Map<String, Account> accounts) {
	log.info("validateFundTransfer is started {}", fundTransfer + " accounts="+accounts);
	Account fromIdAccount=accounts.get(fundTransfer.getAccountFromId());
	Account toIdAccount=accounts.get(fundTransfer.getAccountToId());
	
	if(fundTransfer.getAccountFromId()==null||fundTransfer.getAccountToId()==null) {
		
		throw new FundTransferAccountException("FromAccount and ToAccount should not be empty.");
	}
	if(fundTransfer.getAccountFromId().equals(fundTransfer.getAccountToId())) {
		
		throw new FundTransferAccountException("FromAccount and ToAccount should be different.");
	}
	if(fundTransfer.getTransferAmount().equals(BigDecimal.ZERO)||fundTransfer.getTransferAmount().compareTo(BigDecimal.ZERO)<0) {
		
		throw new FundTransferAccountException("FundAmount should be greater than zero.");
	}
	if(toIdAccount==null) {
		
		throw new FundTransferAccountException("ToAccount is not a valid number. Please verify once.");
	}
	else {
		BigDecimal amount=toIdAccount.getBalance();
		 if (amount.compareTo(BigDecimal.ZERO) <0) { 
        	throw new FundTransferAccountException("The Transaction should not possible for an account end with negative balance"); 
        }
	}
	if(fromIdAccount==null) {
		
		throw new FundTransferAccountException("FromAccount is not a valid number. Please verify once.");
	}else {
		BigDecimal amount=fromIdAccount.getBalance();
		BigDecimal balanceAmount=amount.subtract(fundTransfer.getTransferAmount());
		
		if (balanceAmount.compareTo(BigDecimal.ZERO) == 0) { 
			
        } 
        else if (balanceAmount.compareTo(BigDecimal.ZERO) == 1) { 
        	
        } 
        else { 
        	
        	throw new FundTransferAccountException("Insufficient Balance."); 
        } 
	}
	
	return true;
	
}

}

