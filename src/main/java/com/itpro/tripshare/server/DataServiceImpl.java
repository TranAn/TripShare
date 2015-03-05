package com.itpro.tripshare.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.itpro.tripshare.client.rpc.DataService;
import com.itpro.tripshare.shared.Part;
import com.itpro.tripshare.shared.Picture;
import com.itpro.tripshare.shared.Trip;
import com.itpro.tripshare.shared.User;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {

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

	private User exportUser = null;

	@Override
	public User insertUser(User user) {
		Key<User> key = ofy().save().entity(user).now();
		exportUser = ofy().load().key(key).now();
		return exportUser;
	}

	@Override
	public User findUser(Long idUser) {
		User oldData = ofy().load().type(User.class).id(idUser).now();
		return oldData;
	}

	@Override
	public User updateUser(User user) {
		User oldData = findUser(user.getId());
		if (oldData != null) {
			Key<User> key = ofy().save().entity(user).now();
			exportUser = ofy().load().key(key).now();
		} else
			exportUser = null;

		return exportUser;
	}

	@Override
	public void removeUser(Long idUser) {
		User oldData = findUser(idUser);
		ofy().delete().entity(oldData);

	}

	private BlobstoreService blogStoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	private final BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@Override
	public String getURLUpload() {
		return blogStoreService.createUploadUrl("/upload");
	}

	@Override
	public void insertPicture(Picture picture) {
		ofy().save().entity(picture);
	}

	@Override
	public Picture findPicture(Long idPicture) {
		Picture p = ofy().load().type(Picture.class).id(idPicture).now();
		return p;
	}

	@Override
	public void deletePicture(Long idPicture) {
		Picture p = findPicture(idPicture);
		BlobKey blobKey = new BlobKey(p.getKey());
		blobstoreService.delete(blobKey);
		ofy().delete().entity(p);

	}

}
