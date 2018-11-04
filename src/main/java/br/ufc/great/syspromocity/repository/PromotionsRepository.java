package br.ufc.great.syspromocity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.syspromocity.model.Promotion;


@Repository
public interface PromotionsRepository extends JpaRepository<Promotion, Long>{

}
