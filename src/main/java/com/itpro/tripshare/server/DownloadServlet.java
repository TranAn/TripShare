package com.itpro.tripshare.server;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class DownloadServlet extends HttpServlet {
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BlobstoreService blobstoreService = BlobstoreServiceFactory
				.getBlobstoreService();
		String blob = req.getParameter("downloadfile");
		BlobKey blobKey = new BlobKey(blob);
		blobstoreService.serve(blobKey, resp);

		BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);

		String encodedFilename = URLEncoder.encode(blobInfo.getFilename(),
				"utf-8");
		encodedFilename.replaceAll("\\+", "%20");
		resp.setContentType("application/octet-stream");

		resp.addHeader("Content-Disposition", "attachment; filename*=utf-8''"
				+ encodedFilename);
	}
}
