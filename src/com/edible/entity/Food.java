package com.edible.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 食物的详细信息类
 * @author mingjiang
 *
 */
public class Food implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String description;
	private Double avgRate;

	private Set<Review> reviews = new HashSet<Review> ();
	private Set<Image> images = new HashSet<Image> ();

	
	public Food() {}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public Set<Image> getImages() {
		return images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	public Double getAvgRate() {
		return avgRate;
	}

	public void setAvgRate(Double avgRate) {
		this.avgRate = avgRate;
	}


	@Override
	public String toString() {
		return "Food{" +
				"id=" + id +
				", name='" + name + '\'' +
				", description='" + description + '\'' +
				", avgRate=" + avgRate +
				", reviews=" + reviews +
				", images=" + images +
				'}';
	}
}
