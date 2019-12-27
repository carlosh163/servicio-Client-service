package com.springboot.appbanco.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.springboot.appbanco.model.Account;
import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.model.CreditAccount;
import com.springboot.appbanco.repo.IClientRepo;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientServiceImpl implements IClientService {

  
  @Autowired
  IClientRepo repo;
  
  
  @Override
  public Flux<Client> findAll() {
    return repo.findAll();
  }

  @Override
  public Mono<Client> findById(String id) {
    return repo.findById(id);
  }

  @Override
  public Flux<Client> create(Account account) {
    //return repo.save(account);
    
    
    //List<Client> lstClient = account.getCustomerList();
    // De Mono a Flux:::: flatMapMany
    return Mono.just(account).map(objacc->{
      
      return objacc.getCustomerList();
    }).flatMapMany( lstC-> Flux.fromIterable(lstC))
        .flatMap(objC->{
          
          String DNI = objC.getDocumentNumber();
          
          
          Account objAcNew = new Account();
          
          objAcNew.setProductType(account.getProductType());
          objAcNew.setAccountType(account.getAccountType());
          objAcNew.setAccountNumber(account.getAccountNumber());
          objAcNew.setOpeningDate(account.getOpeningDate());
          objAcNew.setBalance(account.getBalance());
          objAcNew.setAccountstatus(account.getAccountstatus());
          objC.getAccountList().add(objAcNew); //List<Client> info..
          
          
          
          return repo.findBydocumentNumber(DNI).switchIfEmpty(Mono.just(objC)).flatMap(objAC ->{
            if(objAC.getIdClient()!=null) {
              
              
              return repo.findById(objAC.getIdClient()).flatMap(client ->{
                
                client.getAccountList().add(objAcNew);
                return repo.save(client);
              });
              
              
            }else {
              return repo.save(objAC);
            }
            
          });
          
          
          
        });
      
  }
  
  @Override
  public Flux<Client> createL(List<Client> client) {
    
    
    return Flux.fromIterable(client).flatMap(objClient ->{
      return repo.save(objClient);
    });
    
    
  }

  @Override
  public Mono<Client> update(Client persoClie, String id) {
    // TODO Auto-generated method stub
    return repo.findById(id).flatMap(client ->{
      
      //char estado =client.getEstado();
      
      /*client.setNombres(persoClie.getNombres());
      client.setApellidos(persoClie.getApellidos());
      client.setTipoCliente(persoClie.getTipoCliente());
      client.setTipoDocumento(persoClie.getTipoDocumento());
      client.setNroDocumento(persoClie.getNroDocumento());
      client.setEstado(persoClie.getEstado());*/
      //client.setAccountsList(accountsList);
      return repo.save(client);
    });
  }

  @Override
  public Mono<Void> delete(String id) {
    // TODO Auto-generated method stub
    return repo.findById(id).flatMap(client -> repo.delete(client));
  }

  @Override
  public Flux<Client> findClientType(String descType) {
    // TODO Auto-generated method stub
    return repo.findByclientType(descType);
  }

  @Override
  public Mono<Client> findNroDoc(String descType) {
    // TODO Auto-generated method stub
    return repo.findBydocumentNumber(descType);
  }

  @Override
  public Flux<Client> findClientsByAccountNumber(Integer accNumber) {
    return repo.findByClientByAccountNumber(accNumber);
  }

  @Override
  public Mono<Client> createPClient(Client clie) {
    return repo.save(clie);
  }

  @Override
  public Mono<Client> createClientACredit(CreditAccount account) {
    
    
    System.out.println("Ingreso a Crear un CLiente Credito...");
        return Mono.just(account).flatMap(objacc->{
          
              
              Client objC = objacc.getCustomer();
              
              String DNI = objC.getDocumentNumber();
              
              
              CreditAccount objAcNew = new CreditAccount();
              
              objAcNew.setProductType(account.getProductType());
              objAcNew.setAccountType(account.getAccountType());
              objAcNew.setAccountNumber(account.getAccountNumber());
              objAcNew.setOpeningDate(account.getOpeningDate());
              objAcNew.setBalance(account.getCreditLimit());
              objAcNew.setAccountstatus(account.getAccountstatus());              
              objAcNew.setConsumption(0);
              objAcNew.setCreditLimit(account.getCreditLimit());
              
              objC.getCreditAccountList().add(objAcNew); //List<Client> info..
              
              
              
              return repo.findBydocumentNumber(DNI).switchIfEmpty(Mono.just(objC)).flatMap(objAC ->{
                if(objAC.getIdClient()!=null) {
                  
                  
                  return repo.findById(objAC.getIdClient()).flatMap(client ->{
                    
                    client.getCreditAccountList().add(objAcNew);
                    return repo.save(client);
                  });
                  
                  
                }else {
                  return repo.save(objAC);
                }
                
              });
            });
  }

  

  /*@Override
  public Flux<Client> findClientType(String descType) {
    // TODO Auto-generated method stub
    return repo.findAll().filter(client -> client.getNroDocumento().equals(descType)).
        flatMap(cliente ->{
          Flux<Client> Fcliente  = cli;
          return Fcliente;
        });
  }*/
  
  @Override
  public Flux<Client> findClientsByAccountNumberListCredit(Integer accNumber) {
    return repo.findByClientByAccountNumberListCredit(accNumber);
  }
  

}
