package br.ufc.great.syspromocity.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.ufc.great.syspromocity.model.Coupon;
import br.ufc.great.syspromocity.repository.CouponsRepository;

/**
 * Class the manipulate the repository of Coupons
 * @author armandosoaressousa
 *
 */
@Service
public class CouponsService extends AbstractService<Coupon, Long>{
	
	@Autowired
	private CouponsRepository couponsRepository;

	@Override
	protected JpaRepository<Coupon, Long> getRepository() {
		return couponsRepository;
	}
	
}
