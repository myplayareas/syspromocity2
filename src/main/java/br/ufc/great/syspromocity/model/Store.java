package br.ufc.great.syspromocity.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Store extends AbstractModel<Long>{
	private String name;
	private String address;
	private String city;
	private String state;
	private double latitude;
	private double longitude;
	private double radius;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Promotion> PromotionList = new LinkedList<Promotion>();
	
	public Store() {
	}
	
	public Store(String name, String address, String city, String state) {
		super();
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
	}
	
	public Long getId() {
		return super.getId();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public List<Promotion> getPromotionList() {
		return PromotionList;
	}
	public void setPromotionList(List<Promotion> promotionList) {
		PromotionList = promotionList;
	}

	public void addPromotion(Promotion promotion) {
		this.PromotionList.add(promotion);		
	}
	
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}