package com.itpro.tripshare.server;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.itpro.tripshare.client.rpc.PictureService;
import com.itpro.tripshare.shared.Picture;
import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class PictureServiceImpl extends RemoteServiceServlet implements
		PictureService {
	private BlobstoreService blogStoreService = BlobstoreServiceFactory
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

	private final BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@Override
	public void deletePicture(Long idPicture) {
		Picture p = findPicture(idPicture);
		BlobKey blobKey = new BlobKey(p.getKey());
		blobstoreService.delete(blobKey);
		ofy().delete().entity(p);

	}

}
