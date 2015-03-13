package com.itpro.tripshare.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.itpro.tripshare.shared.Part;
import com.itpro.tripshare.shared.Picture;
import com.itpro.tripshare.shared.Trip;
import com.itpro.tripshare.shared.User;

@RemoteServiceRelativePath("datastore")
public interface DataService extends RemoteService {

	// trip
	public Trip insertTrip(Trip trip);

	public Trip findTrip(Long idTrip);

	public Trip updateTrip(Trip trip);

	public void removeTrip(Long idTrip);

	// part
	public Part insertPart(Part part);

	public Part findPart(Long idPart);

	public Part updatePart(Part part);

	public void removePart(Long idPart);

	// user
	public void insertUser(User user);

	public User findUser(String idUser);

	public User updateUser(User user);

	public void removeUser(String idUser);

	// picture
	public String getURLUpload();

	public void deletePicture(Long idPicture);

	public Picture findPicture(Long idPicture);

	public void insertPicture(Picture picture);

}
