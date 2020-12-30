package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransfer;
import com.db.awmd.challenge.repository.FundTransferRepository;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FundTransferService {

  @Getter
  private final FundTransferRepository fundTransferRepository;

  @Autowired
  public FundTransferService(FundTransferRepository fundTransferRepository) {
    this.fundTransferRepository = fundTransferRepository;
  }

public void fundTransfer(FundTransfer fundTransfer) {
	this.fundTransferRepository.fundTransfer(fundTransfer);
	
}
}
