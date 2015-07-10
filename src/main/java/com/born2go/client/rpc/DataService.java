package com.born2go.client.rpc;

import java.util.List;

import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.born2go.shared.Trip;
import com.born2go.shared.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("datastore")
public interface DataService extends RemoteService {
	
	// exchange token
	public String getLongLiveToken(String accessToken) throws Exception;

	// trip
	public Trip insertTrip(Trip trip, String accessToken);

	public Trip findTrip(Long idTrip);
	
	public List<Trip> listOfTrip(List<Long> idsTrip);

	public Trip updateTrip(Trip trip, String accessToken);

	public void removeTrip(Long idTrip, String accessToken);

	// part
	public Path insertPath(Path path, Long tripId, String accessToken);

	public Path findPath(Long idPath);
	
	public List<Path> listOfPath(List<Long> idsPath);

	public Path updatePath(Path path, String accessToken);

	public void removePath(Long idPath, String accessToken);

	// user
	public void insertUser(User user);

	public User findUser(String idUser);
	
	public List<User> listOfUser(List<String> idsUser);

//	public User updateUser(User user);

//	public void removeUser(String idUser);

	// picture
	public String getUploadUrl(Long pathId, Long tripId, String accessToken);

	public void deletePicture(Long idPicture, String accessToken);

	public Picture findPicture(Long idPicture);
	
	public List<Picture> listPicture(List<Long> idsPicture);

//	public void insertPicture(Picture picture);

}
