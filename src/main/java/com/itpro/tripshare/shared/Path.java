package com.itpro.tripshare.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity(name = "Path")
@SuppressWarnings("serial")
public class Path implements Serializable {
	
	@Id
	Long id;
	
	List<Long> gallery = new ArrayList<Long>();
	
	String title;
	Date createDate;
	Locate locate;
	String description;

	public Locate getLocate() {
		return locate;
	}

	public void setLocate(Locate locate) {
		this.locate = locate;
	}

	public List<Long> getGallery() {
		return gallery;
	}

	public void setGallery(List<Long> gallery) {
		this.gallery = gallery;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}