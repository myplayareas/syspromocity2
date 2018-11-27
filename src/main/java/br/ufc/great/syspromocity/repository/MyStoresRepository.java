package br.ufc.great.syspromocity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ufc.great.syspromocity.model.MyStores;
import br.ufc.great.syspromocity.model.Users;

@Repository
public interface MyStoresRepository extends JpaRepository<MyStores, Long> {
	MyStores findByUser(Users user);
}
