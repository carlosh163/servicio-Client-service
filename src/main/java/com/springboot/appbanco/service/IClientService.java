package com.springboot.appbanco.service;

import java.util.List;

import org.springframework.web.reactive.function.server.ServerResponse;

import com.springboot.appbanco.model.Account;
import com.springboot.appbanco.model.AccountDTO;
import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.model.CreditAccount;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClientService {

  public Flux<Client> findAll();

  public Mono<Client> findById(String id);

  public Flux<Client> create(Account account);

  public Mono<Client> update(Client perso, String id);

  public Mono<Void> delete(String id);
  
  
  public Flux<Client> findClientType(String descType);
  public Mono<Client> findNroDoc(String descType);
  
  public Flux<Client> createL(List<Client> client);
  
  public Flux<Client> findClientsByAccountNumber(Integer accNumber);
  
  public Mono<Client> createPClient(Client client);
  
  
  public Mono<Client> createClientACredit(CreditAccount account);
  
  public Flux<Client> findClientsByAccountNumberListCredit(Integer accNumber);
  
  //public Flux
  public Flux<AccountDTO> getReportGeneralByBankAndByDateRange(String docuNumber,String bankName, String dateInit, String dateEnd);
}
