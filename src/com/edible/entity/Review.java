package com.edible.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论类，属性包括评论id，评论内容，食物评分以及评论者信息
 * @author mingjiang
 *
 */
public class Review implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String comment;
	private int rate;
	private int liked;
	private Date reviewTime;
	private User reviewer;

	
	public Review() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public int getLiked() {
		return liked;
	}

	public void setLiked(int liked) {
		this.liked = liked;
	}

	public User getReviewer() {
		return reviewer;
	}

	public void setReviewer(User reviewer) {
		this.reviewer = reviewer;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	@Override
	public String toString() {
		return "Review{" +
				"id=" + id +
				", comment='" + comment + '\'' +
				", rate=" + rate +
				", liked=" + liked +
				", reviewTime=" + reviewTime +
				", reviewer=" + reviewer +
				'}';
	}
}
