package com.itpro.tripshare.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

@SuppressWarnings("serial")
@Entity(name = "Trip")
@Unindex
public class Trip implements Serializable {
	@Index
	@Id
	Long id;
	@Index
	Long owner;
	@Index
	List<Long> companion;
	@Index
	List<Long> destination;
	@Index
	List<Long> gallery;
	@Index
	String name;
	Date createDate;
	String description;

	Journey journey;

	public Journey getJourney() {
		return journey;
	}

	public void setJourney(Journey journey) {
		this.journey = journey;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOwner() {
		return owner;
	}

	public void setOwner(Long owner) {
		this.owner = owner;
	}

	public List<Long> getCompanion() {
		return companion;
	}

	public void setCompanion(List<Long> companion) {
		this.companion = companion;
	}

	public List<Long> getDestination() {
		return destination;
	}

	public void setDestination(List<Long> destination) {
		this.destination = destination;
	}

	public List<Long> getGallery() {
		return gallery;
	}

	public void setGallery(List<Long> gallery) {
		this.gallery = gallery;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
