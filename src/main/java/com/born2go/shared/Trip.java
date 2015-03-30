package com.born2go.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@SuppressWarnings("serial")
@Entity(name = "Trip")
public class Trip implements Serializable {
	
	public enum TripStatus {
		ONGOING, COMPLETED;
	}

	@Id
	Long id;
	
	Long owner;
	List<Long> companion = new ArrayList<Long>();
	List<Long> destination = new ArrayList<Long>();
	List<Long> gallery = new ArrayList<Long>();
	
	String name;
	Date createDate;
	Date departureDate;
	String description;
	Journey journey;
	TripStatus status;

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

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}

	public TripStatus getStatus() {
		return status;
	}

	public void setStatus(TripStatus status) {
		this.status = status;
	}
	
}
