package com.db.awmd.challenge.repository;

import java.util.Map;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransfer;
import com.db.awmd.challenge.exception.FundDepositAccountException;
import com.db.awmd.challenge.exception.FundTransferAccountException;
import com.db.awmd.challenge.exception.FundWithdrawlAccountException;

public interface FundTransferRepository {
   
   void fundTransfer(FundTransfer fundTransfer) throws FundTransferAccountException;
   
}
