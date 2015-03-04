package com.itpro.tripshare.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.itpro.tripshare.shared.Part;
import com.itpro.tripshare.shared.Trip;

public interface DataServiceAsync {

	// trip
	void insertTrip(Trip trip, AsyncCallback<Trip> callback);

	void findTrip(Long idTrip, AsyncCallback<Trip> callback);

	void updateTrip(Trip trip, AsyncCallback<Trip> callback);

	void removeTrip(Long idTrip, AsyncCallback<Void> callback);

	// part

	void insertPart(Part part, AsyncCallback<Part> callback);

	void findPart(Long idPart, AsyncCallback<Part> callback);

	void updatePart(Part part, AsyncCallback<Part> callback);

	void removePart(Long idPart, AsyncCallback<Void> callback);

}
