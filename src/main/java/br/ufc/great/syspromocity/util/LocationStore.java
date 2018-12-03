package br.ufc.great.syspromocity.util;

import br.ufc.great.syspromocity.model.Store;

public class LocationStore {
    private Long id;
    private double latitude;
    private double longitude; 
    private String name;
    private String address;
    private String city;
    private double radius;
    private String state;
    
    public LocationStore() {
    	
    }
    
    public LocationStore(Store store) {
    	this.id = store.getId();
    	this.latitude = store.getLatitude();
    	this.longitude = store.getLongitude();
    	this.name = store.getName();
    	this.address = store.getAddress();
    	this.city = store.getCity();
    	this.radius = store.getRadius();
    	this.state = store.getState();
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}    

}
