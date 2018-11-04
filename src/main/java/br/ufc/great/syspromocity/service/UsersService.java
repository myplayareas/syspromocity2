package br.ufc.great.syspromocity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.ufc.great.syspromocity.model.Users;
import br.ufc.great.syspromocity.repository.UsersRepository;

/**
 * Classe de serviço para consumir o repositório de dados de Usuário
 * @author armandosoaressousa
 *
 */
@Service
public class UsersService extends AbstractService<Users, Long>{

	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	protected JpaRepository<Users, Long> getRepository(){
		return usersRepository;
	}
}
