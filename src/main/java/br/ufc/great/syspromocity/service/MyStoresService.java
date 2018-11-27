package br.ufc.great.syspromocity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.ufc.great.syspromocity.model.MyStores;
import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.repository.MyStoresRepository;

@Service
public class MyStoresService extends AbstractService<MyStores, Long>{

	@Autowired
	private MyStoresRepository myStoresRepository; 
	
	@Override
	protected JpaRepository<MyStores, Long> getRepository() {		
		return myStoresRepository;
	}
	
	public MyStores getMyStoresByUser(Users user) {
		return myStoresRepository.findByUser(user);
	}

}
