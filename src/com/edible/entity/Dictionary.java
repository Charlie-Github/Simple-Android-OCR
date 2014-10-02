package com.edible.entity;

import java.io.Serializable;

/**
 * 所有字典类的父类，拥有字典的主要属性，包括title和对应的详细食物信息
 * @author mingjiang
 *
 */
public class Dictionary implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String title;
	protected Food food;

	public Dictionary() {}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	@Override
	public String toString() {
		return "Dictionary{" +
				"title='" + title + '\'' +
				", food=" + food +
				'}';
	}
}
