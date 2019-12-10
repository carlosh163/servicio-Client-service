package com.springboot.appbanco.repo;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.appbanco.model.Client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IClientRepo extends ReactiveMongoRepository<Client,String>{

	Flux<Client> findBytipoCliente(String typeClient);
	Mono<Client> findBynroDocumento (String typeDoc);
	
}
