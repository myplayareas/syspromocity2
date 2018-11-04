package br.ufc.great.syspromocity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.ufc.great.syspromocity.model.Promotion;
import br.ufc.great.syspromocity.repository.PromotionsRepository;


/**
 * Class the manipulate the repository of Promotions
 * @author armandosoaressousa
 *
 */
@Service
public class PromotionsService extends AbstractService<Promotion, Long>{
	
	@Autowired
	private PromotionsRepository promotionsRepository;
	
	@Override
	protected JpaRepository<Promotion, Long> getRepository() {
		return promotionsRepository;
	}
	
}