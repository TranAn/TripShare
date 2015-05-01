package com.born2go.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.born2go.shared.Trip;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.gson.Gson;
import com.googlecode.objectify.Key;

public class BAPI extends HttpServlet implements Servlet{

	private static final long serialVersionUID = 1L;
	DataServiceImpl dataService = new DataServiceImpl();
	private BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	private ImagesService imagesService = ImagesServiceFactory.getImagesService();
	
	/* Upload from mobile app here
	 * 
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
		List<BlobKey> blobKeys = blobs.get("file");
		boolean error = false;
		String postHtml ="";
		String result;
		
		//We get trip_id, post_id, post_content first
		String tripStr = req.getParameter("trip_id");
		String postStr = req.getParameter("post_id");
		if (tripStr == null) tripStr = "0";
		if (postStr == null) postStr = "0";
		
		Long tripID, postID;
		try {
			tripID = Long.valueOf(tripStr);
			postID = Long.valueOf(postStr);
		} catch (NumberFormatException e) {
			deleteBlobKeysOnError(blobKeys, resp);
			return;
		}
		
		if (tripID == 0){
			deleteBlobKeysOnError(blobKeys, resp);
			return;
		}
		
		postHtml += req.getParameter("post_content") + "<br>";
		
		if(blobKeys != null) {
			for(BlobKey key: blobKeys) {
				// get file name on blob info
				BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(key);
				long size = blobInfo.getSize();
				if(size > 0){
					String encodedFilename = URLEncoder.encode(blobInfo.getFilename(), "utf-8");
			    	encodedFilename.replaceAll("\\+", "%20");
			    	// set fileupload info
			    	Picture file = new Picture();
		    		file.setOnTrip(tripID);
		    		file.setOnPath(postID);
			    	file.setKey(key.getKeyString());
			    	String servingUrl = imagesService.getServingUrl(ServingUrlOptions.Builder.withBlobKey(key)); 
			    	file.setServeUrl(servingUrl);
			    	postHtml += "<img alt=\"\" src=\"" + servingUrl + "\" /> <br>";
			    	
					Key<Picture> keyPicture = ofy().save().entity(file).now();
					Picture exportPicture = ofy().load().key(keyPicture).now();
					//save id on trip or path
					if(postID == 0) {
						Trip trip = ofy().load().type(Trip.class).id(tripID).now();
						if(trip != null) {
							trip.getGallery().add(exportPicture.getId());
							ofy().save().entity(trip);
						}
					}
					else {
						Path path = ofy().load().type(Path.class).id(postID).now();
						if(path != null) {
							path.getGallery().add(exportPicture.getId());
							ofy().save().entity(path);
						}
					}
				}else{
					blobstoreService.delete(key);
				}
			}
		}
		
		//It's time to create new post
		String accessToken = req.getParameter("access_token");
		Path newPost = new Path();
		newPost.setDescription(postHtml);
		Path insertedPost = dataService.insertPart(newPost, tripID, accessToken);
		
		JSONObject myObj = new JSONObject();
		
		resp.setContentType("application/json; charset=utf-8");

		if (insertedPost != null){
			String content = getPathContentByID(insertedPost.getId());
			result = createJSONResult("ok", "No error", content);
		} else {
			result = createJSONResult("error", "Can't insert new post. It's supposed trip_id is invalid.", postHtml);
		}
		resp.getWriter().write(result);
	}

	private String createJSONResult(String status, String err_msg, String content){
		JSONObject myObj = new JSONObject();
		try {
			myObj.append("status", status);
			myObj.append("err_msg", err_msg);
			myObj.append("content", content);
		} catch (JSONException e) {
			return "{\"status\":\"error\",\"err_msg\":\"Something's wrong in createJSONResult\",\"content\":\"\"}";
		}
		return myObj.toString();
	}
	
	private void deleteBlobKeysOnError(List<BlobKey> blobKeys, HttpServletResponse resp) throws IOException{
		if (blobKeys == null)
			return;
		for (BlobKey key: blobKeys) {
			blobstoreService.delete(key);
		}
		resp.setContentType("text/html; charset=utf-8");
		resp.getWriter().write(createJSONResult("error", "Something's wrong - deleteBlobKeysOnError", ""));
	}
	
	/**
	 * Supported Actions:
	 * getTrip: Return json data
	 * getPath: Return html
	 * getAllPost: Return json array data
	 * getUploadUrl: Return text
	 * getSetting: Return json data of current version, min mobile app version, adv rate (%) ....
	 * 			{"version":"10","minversion":"7","advrate":"0"}
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		String result = "";
		String action = req.getParameter("action");
		String id = req.getParameter("id");
		
		try {
			if (action.compareToIgnoreCase("getPath") == 0){
				result = getPathContentByID(Long.parseLong(id));
				resp.setContentType("text/html; charset=utf-8");
			}
			else if (action.compareToIgnoreCase("getTrip") == 0){
				result = getTripByID(Long.parseLong(id));
				resp.setContentType("application/json; charset=utf-8");
			}
			else if (action.compareToIgnoreCase("getAllPost") == 0){
				result = getAllPostByTripID(Long.parseLong(id));
				resp.setContentType("application/json; charset=utf-8");
			}
			else if (action.compareToIgnoreCase("getUploadUrl") == 0){
				String mode = req.getParameter("mode");
				if (mode != null)
					result = getUploadUrl(true);
				else 
					result = getUploadUrl(false);
				resp.setContentType("text/html; charset=utf-8");
			}
			else if (action.compareToIgnoreCase("getSetting") == 0){
				resp.setContentType("application/json; charset=utf-8");
				result = getSetting();
			}
			else{
				result = "This command is not supported yet. Check again!";
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = "Error! Something is wrong: id = " + id;
		}
		resp.getWriter().write(result);
	}
	
    //NOTICE NOTICE NOTICE
	//GAE supports gzip natively, so code here for reference only
	// Choose GZIP if the header includes "gzip"
	/*PrintWriter out = null;

	String encodings = req.getHeader("Accept-Encoding");
    //Test code
    if (encodings == null) encodings = "gzip, deflate, sdch";
    
    if (encodings != null && encodings.indexOf("gzip") != -1) {
      // Go with GZIP
      resp.setHeader("Content-Encoding", "gzip");
      out = new PrintWriter(new GZIPOutputStream(resp.getOutputStream()),false);
      result = "Trying ZIP! " + result;
    }
    else {
      // No compression
      out = resp.getWriter();
      result = "No Encoding! " + encodings + result;
    }
    resp.setHeader("Vary", "Accept-Encoding");
    resp.setHeader("Born2Go", "Welcome");
    out.write(result);*/
	
	/**
	 * 
	 * @param id
	 * 	Long value, id of Path
	 * @return
	 * 	Page html show the Path or Error
	 */
	private String getPathContentByID(Long id){
		String html = "";
		Path path = dataService.findPart(id);
		if (path != null){
			html = "<h2>" + path.getTitle() + "</h2><br>";
			
			html = path.getDescription();
		}
		else{
			html = "<h2>" + id + "</h2><br> This post is not exist. That's all we know :(";
		}
		return html;
	}
	/**
	 * 
	 * @param id
	 * 	Long value, id of Trip
	 * @return
	 * 	Page json show the Trip or Error (if status is false)
	 */
	private String getTripByID(Long id){
		String json = "";
		Trip trip = dataService.findTrip(id);
		if (trip != null){
			Gson gson = new Gson();
			json = gson.toJson(trip);
		} else{
			json = "{\"status\": false,\"error\": \"Trip ID: " + id + "doesn't exist\"}";
		}
		return json;
	}
	
	private String getAllPostByTripID(Long id){
		String json = "";
		String html = "";
		Trip trip = dataService.findTrip(id);
		if (trip != null){
			List<Long> ids = trip.getDestination();
			List<Path> paths = dataService.listOfPath(ids);
			List<String> posts = new ArrayList<String>();
			
			Gson gson = new Gson();
			
			for(Path path: paths){
				if (path != null){
					html = "<h2>" + path.getTitle() + "</h2><br>";
					
					html = path.getDescription();
				}
				else{
					html = "<h2>" + id + "</h2><br> This post is not exist. That's all we know :(";
				}
				posts.add(html);
			}
			json = gson.toJson(posts);
		} else{
			json = "{\"status\": false,\"error\": \"Trip ID: " + id + "doesn't exist\"}";
		}
		return json;
	}

	private String getUploadUrl(boolean mode){
		if (mode == false)
			return blobstoreService.createUploadUrl("/api");
		String html = "<html>";
		html += "<head>";
		html += "<title>File Uploading Form</title>";
		html += "</head>";
		html += "<body>";
		html += "<h3>File Upload:</h3>";
		html += "Select a file to upload: <br />";
		html += "<form action=\"" + blobstoreService.createUploadUrl("/api") +
				"\" method=\"post\" enctype=\"multipart/form-data\">";
		html += "<input type=\"file\" name=\"file\" size=\"50\" />";
		html += "<br />";
		
		html += "<input type=\"text\" name=\"trip_id\" value=\"123456789\">";
		html += "<br>";
		html += "<input type=\"text\" name=\"post_id\" value=\"0\">";
		html += "<br>";
		html += "Status to update:<br>";
		html += "<input type=\"text\" name=\"post_content\">";
		html += "<br>";
		html += "Access Token:<br>";
		html += "<input type=\"text\" name=\"access_token\">";
		
		html += "<input type=\"submit\" value=\"Upload File\" />";
		html += "</form>";
		html += "</body>";
		html += "</html>";
	
		return html;
	}
	
	private String getSetting(){
		String result = "";
		result = "{\"version\":\"10\",\"minversion\":\"7\",\"advrate\":\"0\"}";//Test setting only
		return result;
	}
}
