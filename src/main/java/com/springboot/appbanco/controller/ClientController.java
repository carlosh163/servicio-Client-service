package com.springboot.appbanco.controller;

import java.util.HashMap;
import java.util.List;
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

import com.springboot.appbanco.model.Account;
import com.springboot.appbanco.model.Client;
import com.springboot.appbanco.model.CreditAccount;
import com.springboot.appbanco.service.IClientService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Api(tags = "Client")
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
	
	@ApiOperation(value = "Search all customers", notes = "Returning customers, each client has a list of their Bank Accounts and Credit Accounts.")
	@GetMapping
	public Flux<Client> findAll(){
		return service.findAll();
	}
	
	@GetMapping("/{id}")
	public Mono<Client> findById(@PathVariable String id){
		return service.findById(id);
	}
	
	@PostMapping
	public Flux<Client> create(@RequestBody Account account){
		return service.create(account);
	}
	
	@PostMapping("/RegistrarLocal")
	public Flux<Client> createsL(@RequestBody List<Client> client){
		return service.createL(client);
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
	
	
	@ApiOperation(value = "RQ10-Check the balance available on your products such as: bank accounts and credit cards.", notes = " It returns the Client's data with a list of its Bank Accounts and another list of Credit Accounts with all its values ​​per account.")
	@GetMapping("/BuscarClientePorNroDoc/{nroDoc}")
	public Mono<Client> findByNroDoc(@PathVariable String nroDoc){
		return service.findNroDoc(nroDoc);
	}
	
	//Consumo Trans:
	
		@PutMapping("/updateBalanceAccountByAccountNumber/{accountNumber}/{quantity}")
		public Flux<Client> updateBalanceAccountByAccountNumber(@PathVariable Integer accountNumber,@PathVariable double quantity){
			
			return service.findClientsByAccountNumber(accountNumber).flatMap(client ->{
				List<Account> listAcc = client.getAccountList();
				
				Account m = new Account();
				for(int i= 0;i<listAcc.size();i++) { //Account obj:listAcc
					Account obj = listAcc.get(i);
					
					if(  accountNumber.equals(obj.getAccountNumber()) ) {
						m.setAccountNumber(obj.getAccountNumber());
						m.setProductType(obj.getProductType());
						m.setAccountType(obj.getAccountType());
						
						m.setOpeningDate(obj.getOpeningDate());
						m.setAccountstatus(obj.getAccountstatus());
						
						m.setBalance(obj.getBalance()+quantity);
						
						listAcc.set(i, m);
					}
					
				}
				client.setAccountList(listAcc);
				return service.createPClient(client);
				
			});
			
		}
		
		
		
		@PutMapping("/updateBalanceAccountRetireByAccountNumber/{accountNumber}/{quantity}")
		public Flux<Client> updateBalanceAccountRetireByAccountNumber(@PathVariable Integer accountNumber,@PathVariable double quantity){
			
			return service.findClientsByAccountNumber(accountNumber).flatMap(client ->{
				List<Account> listAcc = client.getAccountList();
				
				Account m = new Account();
				for(int i= 0;i<listAcc.size();i++) { //Account obj:listAcc
					Account obj = listAcc.get(i);
					
					if(  accountNumber.equals(obj.getAccountNumber()) ) {
						
						
						//validacion sin negativo:
						if(obj.getBalance()-quantity>=0) {
							m.setAccountNumber(obj.getAccountNumber());
							m.setProductType(obj.getProductType());
							m.setAccountType(obj.getAccountType());
							
							m.setOpeningDate(obj.getOpeningDate());
							m.setAccountstatus(obj.getAccountstatus());
							
							m.setBalance(obj.getBalance()-quantity);
							
							listAcc.set(i, m);
							client.setAccountList(listAcc);
						}else {
							System.out.println("Error saldo insuficiente..");
							return Mono.empty();
						}
						
						
						
					}
					
				}
				
				return service.createPClient(client);
				
			});
			
		}
		
		
		//Apertura Cuenta Credito:::::
		
		
		@PostMapping("/SaveAccountCredit")
		public Mono<Client> createAccountCredit(@RequestBody CreditAccount account){
			return service.createClientACredit(account);
		}
		
		
		// Para Transacciones: Consumos:
		
		@PutMapping("/updateBalanceAccountByAccountNumberConsumer/{accountNumber}/{quantity}")
		public Flux<Client> updateBalanceAccountByAccountNumberConsumer(@PathVariable Integer accountNumber,@PathVariable double quantity){
			
			return service.findClientsByAccountNumberListCredit(accountNumber).flatMap(client ->{
				List<CreditAccount> listCredAcc = client.getCreditAccountList();
				
				CreditAccount m = new CreditAccount();
				for(int i= 0;i<listCredAcc.size();i++) { //Account obj:listAcc
					CreditAccount obj = listCredAcc.get(i);
					
					if(  accountNumber.equals(obj.getAccountNumber()) ) {
						
						
						
						if(quantity>obj.getBalance()) {
							System.out.println("No se puede consumir más de su Saldo Actual."+obj.getBalance());
							return Mono.empty();
						}else {
							
							
							
							m.setAccountNumber(obj.getAccountNumber());
							m.setProductType(obj.getProductType());
							m.setAccountType(obj.getAccountType());
							
							m.setOpeningDate(obj.getOpeningDate());
							m.setAccountstatus(obj.getAccountstatus());
							
							m.setBalance(obj.getBalance()-quantity);
							m.setConsumption(obj.getConsumption() + quantity);
							m.setCreditLimit(obj.getCreditLimit());
							
							listCredAcc.set(i, m);
							client.setCreditAccountList(listCredAcc);
							
							
						}
						
						
						
					}
					
				}
				
				return service.createPClient(client);
				
			});
			
		}
		
		
		
		@PutMapping("/updateBalanceAccounByAccountNumberPayment/{accountNumber}/{quantity}")
		public Flux<Client> updateBalanceAccounByAccountNumberPayment(@PathVariable Integer accountNumber,@PathVariable double quantity){
			
			return service.findClientsByAccountNumberListCredit(accountNumber).flatMap(client ->{
				List<CreditAccount> listCredAcc = client.getCreditAccountList();
				
				CreditAccount m = new CreditAccount();
				for(int i= 0;i<listCredAcc.size();i++) { //Account obj:listAcc
					CreditAccount obj = listCredAcc.get(i);
					
					if(  accountNumber.equals(obj.getAccountNumber()) ) {
						
						
						
						if(quantity>obj.getConsumption()) {
							System.out.println("No se puede pagar más de su Consumo Actual (Deuda)."+obj.getConsumption());
							return Mono.empty();
						}else {
							
							
							
							m.setAccountNumber(obj.getAccountNumber());
							m.setProductType(obj.getProductType());
							m.setAccountType(obj.getAccountType());
							
							m.setOpeningDate(obj.getOpeningDate());
							m.setAccountstatus(obj.getAccountstatus());
							
							m.setBalance(obj.getBalance()+quantity);
							m.setConsumption(obj.getConsumption() - quantity);
							m.setCreditLimit(obj.getCreditLimit());
							
							listCredAcc.set(i, m);
							client.setCreditAccountList(listCredAcc);
							
							
						}
						
						
						
					}
					
				}
				
				return service.createPClient(client);
				
			});
			
		}
		
		
	
}
