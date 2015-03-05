package com.itpro.tripshare.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.itpro.tripshare.shared.Part;
import com.itpro.tripshare.shared.Picture;
import com.itpro.tripshare.shared.Trip;
import com.itpro.tripshare.shared.User;

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

	// user

	void insertUser(User user, AsyncCallback<User> callback);

	void findUser(Long idUser, AsyncCallback<User> callback);

	void updateUser(User user, AsyncCallback<User> callback);

	void removeUser(Long idUser, AsyncCallback<Void> callback);

	// picture

	void getURLUpload(AsyncCallback<String> callback);

	void deletePicture(Long idPicture, AsyncCallback<Void> callback);

	void findPicture(Long idPicture, AsyncCallback<Picture> callback);

	void insertPicture(Picture picture, AsyncCallback<Void> callback);

}
