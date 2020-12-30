package com.db.awmd.challenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Data
public class FundTransfer {

  @NotNull
  @NotEmpty
  private final String accountFromId;

  @NotNull
  @NotEmpty
  private final String accountToId;

  @NotNull
  private BigDecimal transferAmount;
  

  
  public FundTransfer(String accountFromId,String accountToId) {
    this.accountFromId = accountFromId;
    this.accountToId=accountToId;
    this.transferAmount = BigDecimal.ZERO;
  }
  
  @JsonCreator
  public FundTransfer(@JsonProperty("accountFromId") String accountFromId,
    @JsonProperty("accountToId") String accountToId,@JsonProperty("transferAmount") BigDecimal transferAmount) {
    this.accountFromId = accountFromId;
    this.accountToId = accountToId;
    this.transferAmount =transferAmount;
  }
  
  
}
