package com.itpro.tripshare.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.itpro.tripshare.client.rpc.DataService;
import com.itpro.tripshare.shared.Part;
import com.itpro.tripshare.shared.Trip;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {

	// impls for trip
	private Trip exportTrip = null;

	@Override
	public Trip insertTrip(Trip trip) {
		trip.setCreateDate(new Date());
		Key<Trip> key = ofy().save().entity(trip).now();
		exportTrip = ofy().load().key(key).now();
		return exportTrip;
	}

	@Override
	public Trip findTrip(Long idTrip) {
		exportTrip = ofy().load().type(Trip.class).id(idTrip).now();
		return exportTrip;
	}

	@Override
	public Trip updateTrip(Trip trip) {
		Trip oldTrip = findTrip(trip.getId());
		if (oldTrip != null) {
			Key<Trip> key = ofy().save().entity(trip).now();
			exportTrip = ofy().load().key(key).now();
		} else
			exportTrip = null;
		return exportTrip;
	}

	boolean isRemove = false;

	@Override
	public void removeTrip(Long idTrip) {
		Trip oldTrip = findTrip(idTrip);
		if (oldTrip != null)
			ofy().delete().entity(oldTrip);

	}

	/**
	 * impls for part
	 */
	private Part exportPart = null;

	@Override
	public Part insertPart(Part part) {
		part.setCreateDate(new Date());
		Key<Part> key = ofy().save().entity(part).now();
		exportPart = ofy().load().key(key).now();
		return exportPart;

	}

	@Override
	public Part findPart(Long idPart) {
		Part oldData = ofy().load().type(Part.class).id(idPart).now();
		return oldData;
	}

	@Override
	public Part updatePart(Part part) {
		Part oldData = findPart(part.getId());
		if (oldData != null) {
			Key<Part> key = ofy().save().entity(part).now();
			exportPart = ofy().load().key(key).now();
		} else
			exportPart = null;

		return exportPart;
	}

	@Override
	public void removePart(Long idPart) {
		Part oldData = findPart(idPart);
		if (oldData != null)
			ofy().delete().entity(oldData);

	}

	

}
