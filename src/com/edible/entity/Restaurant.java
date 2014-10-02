package com.edible.entity;

import java.io.Serializable;

/**
 * Created by zhangyi667 on 14-9-16.
 */
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
	private String type;

	private Location location;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Restaurant{" +
				"id=" + id +
				", name='" + name + '\'' +
				", type='" + type + '\'' +
				", location=" + location +
				'}';
	}
}
