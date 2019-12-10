package com.springboot.appbanco.service;

import com.springboot.appbanco.model.Client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IClientService {

	public Flux<Client> findAll();

	public Mono<Client> findById(String id);

	public Mono<Client> create(Client perso);

	public Mono<Client> update(Client perso, String id);

	public Mono<Void> delete(String id);
	
	
	public Flux<Client> findClientType(String descType);
	public Mono<Client> findNroDoc(String descType);
}
