package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransfer;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.FundTransferAccountException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.FundTransferService;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FundTransferServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Autowired
  private FundTransferService fundTransferService;
  
  private FundTransfer fundTransfer;

  
  @Test
  public void transfer_failsOnFromIdNotExist() throws Exception {
    String toId = "Id-101";
    Account account = new Account(toId);
    this.accountsService.createAccount(account);
    try {
    	this.fundTransfer=new FundTransfer("101", "Id-101");
    	BigDecimal transferAmount=new BigDecimal(100);
    	this.fundTransfer.setTransferAmount(transferAmount);
    	fundTransferService.fundTransfer(this.fundTransfer);
      fail("Should have failed when from account is not exists");
    } catch (FundTransferAccountException ex) {
      assertThat(ex.getMessage()).isEqualTo("FromAccount is not a valid number. Please verify once.");
    }
  }
  

  @Test
  public void transfer_failsOnToIdNotExist() throws Exception {
    String fromId = "Id-201";
    Account account = new Account(fromId);
    BigDecimal currentBalance=new BigDecimal(100);
    account.setBalance(currentBalance);
    this.accountsService.createAccount(account);
    try {
    	this.fundTransfer=new FundTransfer("Id-201", "201");
    	BigDecimal transferbalance=new BigDecimal(10);
    	this.fundTransfer.setTransferAmount(transferbalance);
    	fundTransferService.fundTransfer(this.fundTransfer);
      fail("Should have failed when transfer on same accounts");
    } catch (FundTransferAccountException ex) {
      assertThat(ex.getMessage()).isEqualTo("ToAccount is not a valid number. Please verify once.");
    }
  }

  @Test
  public void transfer_failsOnNegativeBalanceTransfer() throws Exception {
    String uniqueId = "Id-998";
    Account account = new Account(uniqueId);
    BigDecimal currentBalance=new BigDecimal(100);
    account.setBalance(currentBalance);
    this.accountsService.createAccount(account);
    String uniqueId1 = "Id-999";
    Account account1 = new Account(uniqueId1);
    BigDecimal currentBal=new BigDecimal(100);
    account.setBalance(currentBal);
    this.accountsService.createAccount(account1);
    try {
    	this.fundTransfer=new FundTransfer("Id-998", "Id-999");
    	BigDecimal bd=new BigDecimal(-10);
    	this.fundTransfer.setTransferAmount(bd);
    	fundTransferService.fundTransfer(this.fundTransfer);
      fail("Should have failed when transfer amount is negative");
    } catch (FundTransferAccountException ex) {
      assertThat(ex.getMessage()).isEqualTo("FundAmount should be greater than zero.");
    }
  }
    
  @Test
  public void transfer_failsOnTransferBetweenUniqueIds() throws Exception {
    String uniqueId = "Id-901";
    Account account = new Account(uniqueId);
    BigDecimal currentBalance=new BigDecimal(100);
    account.setBalance(currentBalance);
    this.accountsService.createAccount(account);
    try {
    	this.fundTransfer=new FundTransfer("Id-901", "Id-901");
    	BigDecimal bd=new BigDecimal(10);
    	this.fundTransfer.setTransferAmount(bd);
    	fundTransferService.fundTransfer(this.fundTransfer);
      fail("Should have failed when transfer on same accounts");
    } catch (FundTransferAccountException ex) {
      assertThat(ex.getMessage()).isEqualTo("FromAccount and ToAccount should be different.");
    }

  }
  @Test
  public void transfer_failsOnInfufficientFunds() throws Exception {
    String uniqueId = "Id-501";
    Account accountTo = new Account(uniqueId);
    BigDecimal firstAccount=new BigDecimal(100);
    accountTo.setBalance(firstAccount);
    this.accountsService.createAccount(accountTo);
    String uniqueId1 = "Id-502";
    Account accountFrom = new Account(uniqueId1);
    BigDecimal firstAccount1=new BigDecimal(100);
    accountFrom.setBalance(firstAccount1);
    this.accountsService.createAccount(accountFrom);
    
    try {
    	this.fundTransfer=new FundTransfer("Id-501", "Id-502");
    	BigDecimal bd=new BigDecimal(1000);
    	this.fundTransfer.setTransferAmount(bd);
    	fundTransferService.fundTransfer(this.fundTransfer);
      fail("Should have failed when transfer on same accounts");
    } catch (FundTransferAccountException ex) {
      assertThat(ex.getMessage()).isEqualTo("Insufficient Balance.");
    }

  }
}
