package com.springboot.appbanco.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
@Document(collection = "client")
public class Client extends Person{

	@Id
	private String idClient;

	
	private String clientType; // Personal o Empresarial.
	
	
	private List<Account> accountList = new ArrayList<Account>();
	
	private List<CreditAccount> creditAccountList = new ArrayList<CreditAccount>();
	
	private char state;
	
	public Client() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getIdClient() {
		return idClient;
	}

	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}
	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}


	
	public List<Account> getAccountList() {
		return accountList;
	}



	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
	}



	public char getState() {
		return state;
	}

	public void setState(char state) {
		this.state = state;
	}



	public List<CreditAccount> getCreditAccountList() {
		return creditAccountList;
	}



	public void setCreditAccountList(List<CreditAccount> creditAccountList) {
		this.creditAccountList = creditAccountList;
	}


	

	



	
	

	


	
	
}
