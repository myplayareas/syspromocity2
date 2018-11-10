package br.ufc.great.syspromocity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.syspromocity.model.Coupon;

@Repository
public interface CouponsRepository extends JpaRepository<Coupon, Long>{
	List<Coupon> findByDescription(String description);
}
