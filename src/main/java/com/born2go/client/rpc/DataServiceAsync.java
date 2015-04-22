package com.born2go.client.rpc;

import java.util.List;

import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.born2go.shared.Trip;
import com.born2go.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {

	// trip
	void insertTrip(Trip trip, String accessToken, AsyncCallback<Trip> callback);

	void findTrip(Long idTrip, AsyncCallback<Trip> callback);
	
	void listOfTrip(List<Long> idsTrip, AsyncCallback<List<Trip>> callback);

	void updateTrip(Trip trip, AsyncCallback<Trip> callback);

	void removeTrip(Long idTrip, AsyncCallback<Void> callback);

	// part
	void insertPart(Path part, Long tripId, String accessToken, AsyncCallback<Path> callback);

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
	
	void listPicture(List<Long> idsPicture, AsyncCallback<List<Picture>> callback);

	void insertPicture(Picture picture, AsyncCallback<Void> callback);
	
}
