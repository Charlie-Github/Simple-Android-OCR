package com.edible.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangyi667 on 14-9-16.
 */
public class Discount implements Serializable{

    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private String description;
    private String code;
    private Date startDate;
    private Date expiredDate;

	private Restaurant restaurant;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	@Override
	public String toString() {
		return "Discount{" +
				"id=" + id +
				", title='" + title + '\'' +
				", description='" + description + '\'' +
				", code='" + code + '\'' +
				", startDate=" + startDate +
				", expiredDate=" + expiredDate +
				", restaurant=" + restaurant.getName() +
				'}';
	}
}
