package com.springboot.appbanco.repo;


import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.springboot.appbanco.model.Client;

@Repository
public interface IClientRepo extends ReactiveMongoRepository<Client,String>{

}
