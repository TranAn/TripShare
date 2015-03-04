package com.itpro.tripshare.shared;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

@Entity(name = "Part")
@SuppressWarnings("serial")
@Unindex
public class Part implements Serializable {
	@Index
	@Id
	Long id;
	@Index
	Long picture;
	Date createDate;
	Locate locate;

	public Locate getLocate() {
		return locate;
	}

	public void setLocate(Locate locate) {
		this.locate = locate;
	}

	String description;

	public Long getPicture() {
		return picture;
	}

	public void setPicture(Long picture) {
		this.picture = picture;
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
