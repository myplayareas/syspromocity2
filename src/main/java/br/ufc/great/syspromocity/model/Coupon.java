package br.ufc.great.syspromocity.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class Coupon extends AbstractModel<Long>{
	
	@Column(length=255)
	private String description;
	private float discount;
	private String qrCode;
	
	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(fetch = FetchType.LAZY)
	List<Users> users = new LinkedList<Users>();
	
	private boolean activated; // o cupom foi ativado por 3 amigos
	private boolean consumed; //quando o limite de vezes é alcançado, o cupom não pode mais ser consumido
	private boolean awarded; //o cupom se torna premiado quando 3 amigos ativam ao mesmo tempo dentro da vizinhança
	
	private int limitedToUse=4; //limite de vezes que pode ser usado
	private int used=0;
	
	public Coupon() {
		this.activated=false;
		this.consumed=false;
		this.awarded=false;
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

	public int getLimitedToUse() {
		return limitedToUse;
	}

	public void setLimitedToUse(int limit) {
		this.limitedToUse = limit;
	}
	
	public boolean incrementUse() {
		if (used < limitedToUse) {
			used = used + 1;
			return true;
		}
		this.consumed=true;
		return false;
	}

	public boolean isAwarded() {
		return awarded;
	}

	public void setAwarded(boolean awarded) {
		this.awarded = awarded;
	}

	public int getUsed() {
		return used;
	}
	
    /**
     * Checa se um cupom é valido
     * @param IdCoupon
     * @return
     */
    public boolean isValidCoupon() {
    	boolean valid=false;    	    	    	

    	//Não foi ativado, não foi consumido (um cupom tem um limite de uso) e não foi premiado
    	if (!isActivated() && !isConsumed() && !isAwarded()) {
    		valid = true;
    	}else {
    		valid = false;
    	}    		

    	return valid;
    }

}