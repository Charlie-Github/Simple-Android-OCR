package com.edible.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 图片信息类
 * @author mingjiang
 *
 */
public class Image implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String message;
	private Date uploadTime;
	private User uploader;
	
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
	
	public User getUploader() {
		return uploader;
	}
	
	public void setUploader(User uploader) {
		this.uploader = uploader;
	}
	
	public Date getUploadTime() {
		return uploadTime;
	}
	
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Image{" +
				"id=" + id +
				", name='" + name + '\'' +
				", message='" + message + '\'' +
				", uploadTime=" + uploadTime +
				", uploader=" + uploader +
				'}';
	}
}
