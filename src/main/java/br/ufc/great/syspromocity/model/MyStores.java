package br.ufc.great.syspromocity.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;


@Entity
public class MyStores extends AbstractModel<Long>{
	@OneToOne
	private Users user;
	@OneToMany
	private List<Store> storeList = new LinkedList<Store>();
	
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public List<Store> getStoreList() {
		return storeList;
	}
	public void setStoreList(List<Store> storeList) {
		this.storeList = storeList;
	}
	public void addStore(Store store) {
		this.storeList.add(store);
	}

}
