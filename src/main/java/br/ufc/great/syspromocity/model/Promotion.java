package br.ufc.great.syspromocity.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Promotion extends AbstractModel<Long>{
	
	private String description;
	private Date fromDate;
	private Date toDate;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Coupon> coupons;
	
	public Promotion() {
	}
	
	public Promotion(Long id, String description, List<Coupon> CouponList) {
		super();
		this.description = description;
		this.coupons = CouponList;
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

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}
			
}