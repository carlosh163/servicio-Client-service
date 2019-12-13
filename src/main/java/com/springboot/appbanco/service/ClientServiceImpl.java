package com.springboot.appbanco.service;

import java.util.List;

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
	public Flux<Client> create(List<Client> persoClie) {
		Flux<Client> fClie = null;
		for(Client  objC:persoClie) {
			fClie.just(objC);
			repo.save(objC).subscribe();
		}
		return fClie;
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

	/*@Override
	public Flux<Client> findClientType(String descType) {
		// TODO Auto-generated method stub
		return repo.findAll().filter(client -> client.getNroDocumento().equals(descType)).
				flatMap(cliente ->{
					Flux<Client> Fcliente  = cli;
					return Fcliente;
				});
	}*/
	

}
