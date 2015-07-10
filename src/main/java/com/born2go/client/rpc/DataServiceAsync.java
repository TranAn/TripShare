package com.born2go.client.rpc;

import java.util.List;

import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.born2go.shared.Trip;
import com.born2go.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DataServiceAsync {
	
	// exchange token
	void getLongLiveToken(String accessToken, AsyncCallback<String> callback) throws Exception;

	// trip
	void insertTrip(Trip trip, String accessToken, AsyncCallback<Trip> callback);

	void findTrip(Long idTrip, AsyncCallback<Trip> callback);
	
	void listOfTrip(List<Long> idsTrip, AsyncCallback<List<Trip>> callback);

	void updateTrip(Trip trip, String accessToken, AsyncCallback<Trip> callback);

	void removeTrip(Long idTrip, String accessToken, AsyncCallback<Void> callback);

	// part
	void insertPath(Path path, Long tripId, String accessToken, AsyncCallback<Path> callback);

	void findPath(Long idPath, AsyncCallback<Path> callback);
	
	void listOfPath(List<Long> idsPath, AsyncCallback<List<Path>> callback);

	void updatePath(Path path, String accessToken, AsyncCallback<Path> callback);

	void removePath(Long idPath, String accessToken, AsyncCallback<Void> callback);

	// user
	void insertUser(User user, AsyncCallback<Void> callback);

	void findUser(String idUser, AsyncCallback<User> callback);
	
	void listOfUser(List<String> idsUser, AsyncCallback<List<User>> callback);

//	void updateUser(User user, AsyncCallback<User> callback);

//	void removeUser(String idUser, AsyncCallback<Void> callback);

	// picture
	void getUploadUrl(Long pathId, Long tripId, String accessToken, AsyncCallback<String> callback);

	void deletePicture(Long idPicture, String accessToken, AsyncCallback<Void> callback);

	void findPicture(Long idPicture, AsyncCallback<Picture> callback);
	
	void listPicture(List<Long> idsPicture, AsyncCallback<List<Picture>> callback);

//	void insertPicture(Picture picture, AsyncCallback<Void> callback);
	
}
