package com.springboot.appbanco.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.repo.IClientRepo;

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
		// TODO Auto-generated method stub
		return repo.findById(id);
	}

	@Override
	public Mono<Client> create(Client persoClie) {
		return repo.save(persoClie);
	}

	@Override
	public Mono<Client> update(Client persoClie, String id) {
		// TODO Auto-generated method stub
		return repo.findById(id).flatMap(client ->{
			
			//char estado =client.getEstado();
			
			client.setNombres(persoClie.getNombres());
			client.setApellidos(persoClie.getApellidos());
			client.setTipoDocumento(persoClie.getTipoDocumento());
			client.setNroDocumento(persoClie.getNroDocumento());
			client.setEstado(persoClie.getEstado());
			//client.setAccountsList(accountsList);
			return repo.save(client);
		});
	}

	@Override
	public Mono<Void> delete(String id) {
		// TODO Auto-generated method stub
		return repo.findById(id).flatMap(client -> repo.delete(client));
	}
	

}
