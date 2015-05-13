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
	
	Integer verNum = 1;
	
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
		Path realPost = null;
		
		//We get trip_id, post_id, post_content first
		String tripStr = req.getParameter("trip_id");
		String postStr = req.getParameter("post_id");
		String titleStr= req.getParameter("title");
		String verStr = req.getParameter("v");
		if (tripStr == null) tripStr = "0";
		if (postStr == null) postStr = "0";
		if (verStr	== null) verStr = "1";
		
		Long tripID, postID;
		try {
			tripID = Long.parseLong(tripStr);
			postID = Long.parseLong(postStr);
			verNum = Integer.parseInt(verStr);
		} catch (NumberFormatException e) {
			deleteBlobKeysOnError(blobKeys, resp);
			return;
		}
		
		if (tripID == 0){
			deleteBlobKeysOnError(blobKeys, resp);
			return;
		}
		
		if (postID != 0){//Get current post content if we are updating
			realPost = dataService.findPart(postID);
			if (realPost != null){
				postHtml = realPost.getDescription();
			}
		}
		
		postHtml += req.getParameter("post_content") + "<br>";
		
		if(blobKeys != null) {
			BlobInfoFactory blobInfoFac = new BlobInfoFactory();
			for(BlobKey key: blobKeys) {
				// get file name on blob info
				BlobInfo blobInfo = blobInfoFac.loadBlobInfo(key);
				long size = blobInfo.getSize();
				if(size > 0){
					String filename = blobInfo.getFilename();
					String encodedFilename = URLEncoder.encode(filename, "utf-8");
			    	encodedFilename.replaceAll("\\+", "%20");
			    	// set fileupload info
			    	Picture file = new Picture();
		    		file.setOnTrip(tripID);
		    		file.setOnPath(postID);
			    	file.setKey(key.getKeyString());
			    	String servingUrl = imagesService.getServingUrl(ServingUrlOptions.Builder.withBlobKey(key)); 
			    	file.setServeUrl(servingUrl);
			    	
			    	/* Sample CKEditor image tag
			    	<div style="text-align:center">
			    	<figure class="image" style="display:inline-block"><img alt="" height="339" src="http://lh3.googleusercontent.com/fDBkW3tMY4k0Hxg-lHSDhYJ21ti3lrn4hAp9nUG8NWeWA2eM9onDt5XSApr8te06wgrGlp-gpvmVhUqQmnwVSw=s1600" width="605" />
			    	<figcaption>Đợi mồi ...</figcaption>
			    	</figure>
			    	</div>*/
			    	//postHtml += "<img alt=\"\" src=\"" + servingUrl + "\" /> <br>";
			    	postHtml += "<div style=\"text-align:center\"> <figure class=\"image\" style=\"display:inline-block\"><img src=\"";
			    	postHtml += servingUrl;
			    	postHtml += "\" /> <figcaption>" + filename + "</figcaption></figure></div>";
			    			
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
		
		//It's time to create new post if nessesary
		String accessToken = req.getParameter("access_token");
		Path insertedPost;
		if (realPost == null){
			realPost = new Path();
			realPost.setDescription(postHtml);
			realPost.setTitle(titleStr);
			insertedPost = dataService.insertPart(realPost, tripID, accessToken);
		}
		else {
			realPost.setTitle(titleStr);
			insertedPost = dataService.updatePart(realPost);
		}
		
		resp.setContentType("application/json; charset=utf-8");

		if (insertedPost != null){
			String content = getPathContentByID(insertedPost.getId());
			result = createJSONResult("ok", "No error", content);
		} else {
			result = createJSONResult("error", "Can't insert new post. It's supposed trip_id is invalid.", postHtml);
			//We have to clean up photos
			if(blobKeys != null) {
				blobstoreService.delete((BlobKey[])blobKeys.toArray());
			}
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
		String verStr = req.getParameter("v");
		if (verStr == null) verStr = "1";
		
		try {
			verNum = Integer.parseInt(verStr);
			
			if (action.compareToIgnoreCase("getPath") == 0){
				result = getPathContentByID(Long.parseLong(id));
				resp.setContentType("text/html; charset=utf-8");
			}
			else if (action.compareToIgnoreCase("getPost") == 0){
				result = getPostContentByID(Long.parseLong(id));
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
			result = "Error! Something is wrong: id = " + id + ", version = " + verStr;
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
	
	private String getPostContentByID(Long id){
		String jsonRet = "";
		Path post = dataService.findPart(id);
		if (post != null){
			Gson gson = new Gson();
			jsonRet = gson.toJson(post);
		}
		else{
			jsonRet = createJSONResult("false", "Post ID: " + id + "doesn't exist", "");
		}
		return jsonRet;
	}
	
	/**
	 * 
	 * @param id
	 * 	Long value, id of Trip
	 * @return
	 * 	Page json show the Trip or Error (if status is false)
	 */

	private String getTripByID(Long id){
		switch (verNum){
		case 2: 
			return getTripByIDv2(id);
		default:
			return getTripByIDv1(id);
		}
		
	}
	private String getTripByIDv1(Long id){
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
	
	private String getTripByIDv2(Long id){ /* Not a line changed */
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
			
			if (verNum == 2){
				json = gson.toJson(paths);
			}
			else {
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
			}
			
		} else{
			json = createJSONResult("false", "Trip ID: " + id + "doesn't exist","");
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
		html += "Trip ID: ";
		html += "<input type=\"text\" name=\"trip_id\" value=\"0\">";
		html += "<br>";
		html += "Post ID: ";
		html += "<input type=\"text\" name=\"post_id\" value=\"0\">";
		html += "<br>";
		html += "Title: ";
		html += "<input type=\"text\" name=\"title\" value=\"0\">";
		html += "<br>";
		html += "Post content: ";
		html += "<input type=\"text\" name=\"post_content\">";
		html += "<br>";
		html += "Access Token:";
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
