package com.springboot.appbanco.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.springboot.appbanco.controller.ClientController;
import com.springboot.appbanco.model.Account;
import com.springboot.appbanco.model.AccountDTO;
import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.model.CreditAccount;
import com.springboot.appbanco.repo.IClientRepo;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientServiceImpl implements IClientService {

  private static Logger log = LoggerFactory.getLogger(ClientServiceImpl.class);
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
          objAcNew.setNumMaxDesposit(account.getNumMaxDesposit());
          objAcNew.setNumMaxRetirement(account.getNumMaxRetirement());
          objAcNew.setMinBalanceEndMonth(account.getMinBalanceEndMonth());
          objAcNew.setBankName(account.getBankName());
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
              objAcNew.setBankName(account.getBankName());              
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

@Override
public Flux<AccountDTO> getReportGeneralByBankAndByDateRange(String docuNumber, String bankName, String dateInit,
		String dateEnd) {
	// TODO Auto-generated method stub
	final LocalDate fecha1LD = LocalDate.parse(dateInit).minusDays(2);
    final LocalDate fecha2LD = LocalDate.parse(dateEnd).plusDays(1);
	 
	return repo.findBydocumentNumber(docuNumber).flatMapMany(c -> {
		
		//acDTO.setAccountNumber(accountNumber);
		
		return Flux.fromIterable(c.getAccountList());
	}).flatMap(lstAcc ->{
		AccountDTO acDTO;
		List<AccountDTO> lstacDTO = new ArrayList<AccountDTO>();
		log.info("Nombre Bank:"+lstAcc.getBankName());
		if(lstAcc.getBankName().equals(bankName)) {
			
			log.info("Ingreso porque es igual al bank.");
			acDTO= new AccountDTO();
			//return Flux.just(lstAcc);
			acDTO.setAccountNumber(lstAcc.getAccountNumber());
			acDTO.setProductType(lstAcc.getProductType());
			acDTO.setAccountType(lstAcc.getAccountType());
			acDTO.setOpeningDate(lstAcc.getOpeningDate());
			acDTO.setBalance(lstAcc.getBalance());
			acDTO.setBankName(lstAcc.getBankName());
			acDTO.setAccountstatus(lstAcc.getAccountstatus());
			
			lstacDTO.add(acDTO);
		}
		log.info("tmloLista"+lstacDTO.size());
			//return Flux.just(acDTO); 
		
		//return acDTO;
		//return Mono.empty();;
	//});
		return Flux.fromIterable(lstacDTO);
}).collectList().flatMapMany(dt -> {
	//dt.g
	log.info("tmloListaRBancaria"+dt.size());
	return repo.findBydocumentNumber(docuNumber).flatMapMany(c -> {
	
		
		return Flux.fromIterable(c.getCreditAccountList());
	}).flatMap(lstAcc ->{
		
		if(lstAcc.getBankName().equals(bankName)) {
			
			AccountDTO acDTO= new AccountDTO();
			acDTO.setAccountNumber(lstAcc.getAccountNumber());
			acDTO.setProductType(lstAcc.getProductType());
			acDTO.setAccountType(lstAcc.getAccountType());
			acDTO.setOpeningDate(lstAcc.getOpeningDate());
			acDTO.setBalance(lstAcc.getBalance());
			acDTO.setBankName(lstAcc.getBankName());
			acDTO.setAccountstatus(lstAcc.getAccountstatus());
			dt.add(acDTO);
			log.info("tmloListaFinal"+dt.size());
			return Flux.fromIterable(dt);
			//return Flux.just(acDTO); 
		}else {
			return Flux.empty();
			//return new AccountDTO();
		}
		
	}).filter(p -> p.getOpeningDate()
	        .toInstant().atZone(ZoneId.systemDefault())
	        .toLocalDate().isAfter(fecha1LD))
	      .filter(p -> p.getOpeningDate()
	        .toInstant().atZone(ZoneId.systemDefault())
	        .toLocalDate().isBefore(fecha2LD));
		
  });
  


  }

}
