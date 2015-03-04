package com.itpro.tripshare.server;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.itpro.tripshare.shared.Picture;

@SuppressWarnings("serial")
public class UploadServiceIpmls extends HttpServlet implements Servlet {
	final BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	final PictureServiceImpl pictureService = new PictureServiceImpl();

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKeys = blobs.get("uploadFile");
		String key = null;
		if (blobKeys == null || blobKeys.isEmpty()) {
			// resp.sendRedirect("/");
		} else {
			key = blobKeys.get(0).getKeyString();
			// resp.sendRedirect("/upload?blob-key=" + key);
			Picture p = new Picture();
			p.setKey(key);
			pictureService.insertPicture(p);
			 resp.setStatus(200);

		}
		System.out.println("doPost");
		return;
		 
	}

	// @Override
	// protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	//
	// String imageUrl = req.getParameter("blob-key");
	//
	// resp.setHeader("Content-Type", "text/html");
	//
	// // This is a bit hacky, but it'll work. We'll use this key in an Async
	// // service to
	// // fetch the image and image information
	// resp.getWriter().println(imageUrl);
	//
	// System.out.println("doGet");
	//
	// }

}
