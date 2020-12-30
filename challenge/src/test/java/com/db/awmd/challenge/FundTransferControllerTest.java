package com.db.awmd.challenge;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.FundTransferService;

import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class FundTransferControllerTest {

  private MockMvc mockMvc;


  @Autowired
  private WebApplicationContext webApplicationContext;
  

  @Autowired
  private AccountsService accountsService;
  
  @Before
  public void prepareMockMvc() {
    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
 // Reset the existing accounts before each test.
    accountsService.getAccountsRepository().clearAccounts();
  }

 @Test
 public void validFundTransferCheck() throws Exception {
	 this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		      .content("{\"accountId\":\"Id-125\",\"balance\":10000}")).andExpect(status().isCreated());
		    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		    	      .content("{\"accountId\":\"Id-126\",\"balance\":10000}")).andExpect(status().isCreated());
	  this.mockMvc.perform(post("/v1/accounts/fundtransfer").contentType(MediaType.APPLICATION_JSON).
			  content("{\"accountFromId\":\"Id-125\",\"accountToId\":\"Id-126\",\"transferAmount\":1000}")).andExpect(status().isCreated());
	  
	}
 @Test
 public void inValidFundTransferCheck() throws Exception {
	 this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		      .content("{\"accountId\":\"Id-123\",\"balance\":100}")).andExpect(status().isCreated());
		    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		    	      .content("{\"accountId\":\"Id-124\",\"balance\":100}")).andExpect(status().isCreated());
	  this.mockMvc.perform(post("/v1/accounts/fundtransfer").contentType(MediaType.APPLICATION_JSON).
			  content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\",\"transferAmount\":1000}")).andExpect(status().isBadRequest());
	  
	  }
 @Test
 public void toAccountValidCheckPositive() throws Exception {
	 this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		      .content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());
		    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		    	      .content("{\"accountId\":\"Id-124\",\"balance\":1000}")).andExpect(status().isCreated());
	  this.mockMvc.perform(post("/v1/accounts/fundtransfer").contentType(MediaType.APPLICATION_JSON).
			  content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\",\"transferAmount\":100}")).andExpect(status().isCreated());
	  
	  }
 @Test
 public void toAccountValidCheckNegative() throws Exception {
	 this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		      .content("{\"accountId\":\"Id-1233\",\"balance\":100}")).andExpect(status().isCreated());
		    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		    	      .content("{\"accountId\":\"Id-124\",\"balance\":100}")).andExpect(status().isCreated());
	  this.mockMvc.perform(post("/v1/accounts/fundtransfer").contentType(MediaType.APPLICATION_JSON).
			  content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\",\"transferAmount\":1000}")).andExpect(status().isBadRequest());
	  
	  }
 @Test
 public void fromAccountValidCheckPositive() throws Exception {
	 this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		      .content("{\"accountId\":\"Id-123\",\"balance\":1000}")).andExpect(status().isCreated());
		    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		    	      .content("{\"accountId\":\"Id-124\",\"balance\":1000}")).andExpect(status().isCreated());
	  this.mockMvc.perform(post("/v1/accounts/fundtransfer").contentType(MediaType.APPLICATION_JSON).
			  content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\",\"transferAmount\":100}")).andExpect(status().isCreated());
	  
	  }
 @Test
 public void fromAccountValidCheckNegative() throws Exception {
	 this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		      .content("{\"accountId\":\"Id-123\",\"balance\":100}")).andExpect(status().isCreated());
		    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		    	      .content("{\"accountId\":\"Id-1244\",\"balance\":100}")).andExpect(status().isCreated());
	  this.mockMvc.perform(post("/v1/accounts/fundtransfer").contentType(MediaType.APPLICATION_JSON).
			  content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\",\"transferAmount\":1000}")).andExpect(status().isBadRequest());
	  
	  }
 @Test
 public void negativeFundTransferCheck() throws Exception {
	 this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		      .content("{\"accountId\":\"Id-123\",\"balance\":100}")).andExpect(status().isCreated());
		    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		    	      .content("{\"accountId\":\"Id-124\",\"balance\":100}")).andExpect(status().isCreated());
	  this.mockMvc.perform(post("/v1/accounts/fundtransfer").contentType(MediaType.APPLICATION_JSON).
			  content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\",\"transferAmount\":-10}")).andExpect(status().isBadRequest());
	  
	  }
 @Test
 public void insuffientFundsTransferCheck() throws Exception {
	 this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		      .content("{\"accountId\":\"Id-123\",\"balance\":100}")).andExpect(status().isCreated());
		    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
		    	      .content("{\"accountId\":\"Id-124\",\"balance\":100}")).andExpect(status().isCreated());
	  this.mockMvc.perform(post("/v1/accounts/fundtransfer").contentType(MediaType.APPLICATION_JSON).
			  content("{\"accountFromId\":\"Id-123\",\"accountToId\":\"Id-124\",\"transferAmount\":1000}")).andExpect(status().isBadRequest());
	  
	  }
	 	 
}
