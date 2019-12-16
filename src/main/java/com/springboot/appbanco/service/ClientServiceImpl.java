package com.springboot.appbanco.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.appbanco.model.Account;
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
					Account objAcNew = new Account();
					
					objAcNew.setAccountNumber(account.getAccountNumber());
					objAcNew.setOpeningDate(account.getOpeningDate());
					objAcNew.setBalance(account.getBalance());
					objAcNew.setAccountstatus(account.getAccountstatus());
					
					
					objC.getAccountList().add(objAcNew); //List<Client> info..
					
					
					return repo.save(objC);
				});
			
		/*return Flux.fromIterable(lstClient).flatMap(objClient ->{
			/*List<Account> lstAT = new ArrayList<>(); 
			lstAT.add(account);
			
			objClient.setAccountList(lstAT);
			//repo.save(objClient);
			
			//return Flux.empty();
			return repo.save(objClient);
		});*/
		/*Client objC = null;
		return Mono.just(repo.save(objC));*/
		
		//return Mono.empty();
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
