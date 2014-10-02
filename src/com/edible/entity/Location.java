package com.edible.entity;

import java.io.Serializable;

/**
 * Created by mingjiang on 9/18/14.
 */
public class Location implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String address;
	private String zipCode;
	private String city;
	private String state;
	private String country;
	private Double latitude;
	private Double longitude;
	private Restaurant restaurant;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "Location{" +
				"id=" + id +
				", address='" + address + '\'' +
				", zipCode='" + zipCode + '\'' +
				", city='" + city + '\'' +
				", state='" + state + '\'' +
				", country='" + country + '\'' +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", restaurant=" + restaurant +
				'}';
	}
}
