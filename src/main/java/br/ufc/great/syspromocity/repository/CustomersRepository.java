package br.ufc.great.syspromocity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.syspromocity.model.Customers;

/**
 * Interface de Repositório de Cliente baseada no JPARepository do Spring
 * @author armandosoaressousa
 *
 */
@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> {

}
