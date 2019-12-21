package com.springboot.appbanco.repo;


import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.appbanco.model.Account;
import com.springboot.appbanco.model.Client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IClientRepo extends ReactiveMongoRepository<Client,String>{

	Flux<Client> findByclientType(String typeClient);
	Mono<Client> findBydocumentNumber (String typeDoc);
	
	
	@Query("{'accountList.accountNumber' : ?0}")
	Flux<Client> findByClientByAccountNumber(Integer accountNumber);
	//findByFechaAperturaBetween(String fechaini,String fechaFIN);
	
	
	@Query("{'creditAccountList.accountNumber' : ?0}")
	Flux<Client> findByClientByAccountNumberListCredit(Integer accountNumber);
}
