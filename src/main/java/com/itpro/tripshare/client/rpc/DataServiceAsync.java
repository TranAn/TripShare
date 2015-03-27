package com.itpro.tripshare.client.rpc;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.itpro.tripshare.shared.Path;
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
	void insertPart(Path part, Long tripId, AsyncCallback<Path> callback);

	void findPart(Long idPart, AsyncCallback<Path> callback);
	
	void listOfPath(List<Long> idsPath, AsyncCallback<List<Path>> callback);

	void updatePart(Path part, AsyncCallback<Path> callback);

	void removePart(Long idPart, AsyncCallback<Void> callback);

	// user
	void insertUser(User user, AsyncCallback<Void> callback);

	void findUser(String idUser, AsyncCallback<User> callback);

	void updateUser(User user, AsyncCallback<User> callback);

	void removeUser(String idUser, AsyncCallback<Void> callback);

	// picture
	void getUploadUrl(AsyncCallback<String> callback);

	void deletePicture(Long idPicture, AsyncCallback<Void> callback);

	void findPicture(Long idPicture, AsyncCallback<Picture> callback);

	void insertPicture(Picture picture, AsyncCallback<Void> callback);
	
}
