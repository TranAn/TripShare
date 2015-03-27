package com.itpro.tripshare.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.googlecode.objectify.Key;
import com.itpro.tripshare.shared.Path;
import com.itpro.tripshare.shared.Picture;

@SuppressWarnings("serial")
public class UploadService extends HttpServlet implements Servlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	
	ImagesService imagesService = ImagesServiceFactory.getImagesService();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKeys = blobs.get("filesUpload");
		
		if(blobKeys != null) {
			for(BlobKey key: blobKeys) {
				Picture file = new Picture();
				// get file name on blob info
				BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(key);
				String encodedFilename = URLEncoder.encode(blobInfo.getFilename(), "utf-8");
		    	encodedFilename.replaceAll("\\+", "%20");
		    	// set fileupload info
		    	file.setOnTrip(Long.valueOf(req.getParameter("tripId").replaceAll(",", "")));
		    	file.setOnPath(Long.valueOf(req.getParameter("pathId").replaceAll(",", "")));
		    	file.setKey(key.getKeyString());
		    	file.setServeUrl(imagesService.getServingUrl(ServingUrlOptions.Builder.withBlobKey(key)) + "=s1600");
				Key<Picture> keyPicture = ofy().save().entity(file).now();
				Picture exportPicture = ofy().load().key(keyPicture).now();
				
				if(req.getParameter("pathId").isEmpty()) {
					
				}
				else {
					Long pathid = Long.valueOf(req.getParameter("pathId").replaceAll(",", ""));
					Path path = ofy().load().type(Path.class).id(pathid).now();
					if(path != null) {
						path.getGallery().add(exportPicture.getId());
						ofy().save().entity(path);
					}
				}
			}
		}
		
	}
}
