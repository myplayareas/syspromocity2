package br.ufc.great.syspromocity.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Coupon extends AbstractModel<Long>{
	
	@Column(length=255)
	private String description;
	private float discount;
	private String qrCode;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	List<Users> users;
	
	private boolean activated=false;
	
	private boolean consumed=false;
	
	public Coupon() {
	}
	
	public Coupon(Long id, String description, String qrCode) {
		super();
		this.description = description;
		this.qrCode = qrCode;
	}
	
	public Long getId() {
		return super.getId();
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public List<Users> getUsers() {
		return users;
	}

	public void setUsers(List<Users> users) {
		this.users = users;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public boolean isConsumed() {
		return consumed;
	}

	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}
}