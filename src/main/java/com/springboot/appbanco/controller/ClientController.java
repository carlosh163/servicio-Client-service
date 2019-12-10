package com.springboot.appbanco.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.service.IClientService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RefreshScope
@RestController
@RequestMapping("api/client")
public class ClientController {
	
	
	private static Logger log = LoggerFactory.getLogger(ClientController.class);

	@Autowired
	private Environment env;
	
	@Autowired
	private IClientService service;
	
	@Value("${configuracion.texto}")
	private String texto;
	
	@GetMapping("/obtener-config")
	public ResponseEntity<?> obtenerConfig(@Value("${server.port}") String puerto){
		log.info(texto);
		Map<String,String> json =  new HashMap<>();
		json.put("texto", texto);
		json.put("puerto", puerto);
		
		if(env.getActiveProfiles().length > 0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("auto.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("auto.email", env.getProperty("configuracion.autor.email"));
		}
		
		 return new ResponseEntity<Map<String,String>>(json,HttpStatus.OK);
	}
	
	
	@GetMapping
	public Flux<Client> findAll(){
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public Mono<Client> findById(@PathVariable String id){
		return service.findById(id);
	}
	
	@PostMapping
	public Mono<Client> create(@RequestBody Client perso){
		return service.create(perso);
	}
	
	
	@PutMapping("/{id}")
	public Mono<Client> update(@RequestBody Client perso, @PathVariable String id){
		return service.update(perso, id);
	}
	
	@DeleteMapping("/{id}")
	public Mono<Void> delete(@PathVariable String id){
		return service.delete(id);
	}
	
	
	//REQ01: BUSQUEDA POR TIPO DE CLIENTE.::
	
	@GetMapping("/BuscarClientePorTipo/{typeClient}")
	public Flux<Client> findByTypeClient(@PathVariable String typeClient){
		return service.findClientType(typeClient);
	}
	
	@GetMapping("/BuscarClientePorNroDoc/{nroDoc}")
	public Mono<Client> findByNroDoc(@PathVariable String nroDoc){
		return service.findNroDoc(nroDoc);
	}
	
}
