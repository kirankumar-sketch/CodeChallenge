package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransfer;
import com.db.awmd.challenge.repository.AccountsRepository;
import com.db.awmd.challenge.repository.FundTransferRepository;

import lombok.Getter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;
  
  @Autowired
  public AccountsService(AccountsRepository accountsRepository,FundTransferRepository fundTransferRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }
 
  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }

public Map<String, Account> getAccounts() {
	return this.accountsRepository.getAccounts();
	}


}
