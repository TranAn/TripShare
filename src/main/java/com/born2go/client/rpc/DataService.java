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

	// trip
	public Trip insertTrip(Trip trip, String accessToken);

	public Trip findTrip(Long idTrip);

	public Trip updateTrip(Trip trip);

	public void removeTrip(Long idTrip);

	// part
	public Path insertPart(Path part, Long tripId, String accessToken);

	public Path findPart(Long idPart);
	
	public List<Path> listOfPath(List<Long> idsPath);

	public Path updatePart(Path part);

	public void removePart(Long idPart);

	// user
	public void insertUser(User user);

	public User findUser(String idUser);

	public User updateUser(User user);

	public void removeUser(String idUser);

	// picture
	public String getUploadUrl();

	public void deletePicture(Long idPicture);

	public Picture findPicture(Long idPicture);
	
	public List<Picture> listPicture(List<Long> idsPicture);

	public void insertPicture(Picture picture);

}
