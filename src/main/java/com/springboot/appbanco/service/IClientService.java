package com.springboot.appbanco.service;

import java.util.List;

import com.springboot.appbanco.model.Account;
import com.springboot.appbanco.model.Client;

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
}
