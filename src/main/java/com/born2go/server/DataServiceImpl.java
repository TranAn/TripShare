package com.born2go.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import com.born2go.client.rpc.DataService;
import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.born2go.shared.Trip;
import com.born2go.shared.User;
import com.born2go.shared.embedclass.Poster;
import com.born2go.shared.embedclass.ServerTransform;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {

	private BlobstoreService blobStoreService = BlobstoreServiceFactory.getBlobstoreService();

	//----- Java function -----
	
	private Poster getPoster(String accessToken) {
		try {
			String url = "https://graph.facebook.com/me?access_token=" + accessToken;
			 
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		
			// optional default is GET
			con.setRequestMethod("GET");
		
			//add request header
			con.setRequestProperty("User-Agent", "Mozilla/5.0");
		
			/*int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);*/
		
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
		
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		
			//print result
			JSONObject myObj = new JSONObject(response.toString());
			Poster poster = new Poster();
			poster.setUserID(myObj.getLong("id"));
			poster.setUserName(myObj.getString("name"));
			
			return poster;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
		
	private String getPlainText(String strSrc) {
	    String resultStr = strSrc;

	    // Ignore the <p> tag if it is in very start of the text
//	    if(strSrc.indexOf("<p>") == 0)
//	        resultStr = strSrc.substring(3);

	    // Replace <p> with two newlines
//	    resultStr = resultStr.replaceAll("<p[^>]*>(&nbsp;)?", "br2nbr2n");
	    // Replace <br /> with one newline
//	    resultStr = resultStr.replaceAll("(?i)<br[^>]*>", "br2n");
	    // Remove img caption
	    resultStr = resultStr.replaceAll("<figcaption>.*</figcaption>", "");
	    
	    // Remove exceed linebreak
//	    resultStr = resultStr.replaceAll("(br2nbr2n(</p>)?\\s*\\r*\\n*)+", "br2nbr2n");
	    
	    // Remove white space char code
	    resultStr = resultStr.replaceAll("&nbsp;", " ");

	    return resultStr;
	}

	//----- Exchange token -----
	
	@Override
	public String getLongLiveToken(String accessToken) throws Exception{
		String url = "https://graph.facebook.com/oauth/access_token?"  
				+ "grant_type=fb_exchange_token&"
				+ "client_id=386540048195283&"
				+ "client_secret=e46d1a5f49dfa88cce1e3396526d8cd6&"
				+ "fb_exchange_token="+ accessToken;
		 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	
		// optional default is GET
		con.setRequestMethod("GET");
	
		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
	
		/*int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);*/
	
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
	
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String token[] = response.toString().replaceAll("access_token=", "").split("&");
		/*System.out.println("long live token="+ token[0]);*/
		return token[0];
	}

	//----- Impl for Trip -----
	
	@Override
	public Trip insertTrip(Trip trip, String accessToken) {
		Trip exportTrip = null;
		if(accessToken != null) {
			Poster poster = getPoster(accessToken);
			if(poster != null) {
				trip.setPoster(new ServerTransform().posterToString(poster));
				trip.setCreateDate(new Date());
				Key<Trip> key = ofy().save().entity(trip).now();
				exportTrip = ofy().load().key(key).now();
				//save trip to user
				User user = ofy().load().type(User.class).id(new ServerTransform().stringToPoster(exportTrip.getPoster()).getUserID().toString()).now();
				if(user != null) {
					user.getMyTrips().add(exportTrip.getId());
					ofy().save().entity(user);
				}
				if(!trip.getCompanion().isEmpty()) {
					for(String p: trip.getCompanion()) {
						User u = ofy().load().type(User.class).id(new ServerTransform().stringToPoster(p).getUserID().toString()).now();
						if(u != null) {
							u.getMyTrips().add(exportTrip.getId());
							ofy().save().entity(u);
						}
					}
				}
			}
		}
		return exportTrip;
	}

	@Override
	public Trip findTrip(Long idTrip) {
		Trip exportTrip;
		exportTrip = ofy().load().type(Trip.class).id(idTrip).now();
		return exportTrip;
	}

	@Override
	public List<Trip> listOfTrip(List<Long> idsTrip) {
		Map<Long, Trip> mapTrips = ofy().load().type(Trip.class).ids(idsTrip);
		List<Trip> result = new ArrayList<Trip>(mapTrips.values());
		return result;
	}

	@Override
	public Trip updateTrip(Trip trip, String accessToken) {
		Trip exportTrip = null;
		Trip oldTrip = findTrip(trip.getId());
		Poster poster = getPoster(accessToken);
		if (oldTrip != null && poster != null) {
			if (poster.getUserID().equals(new ServerTransform().stringToPoster(oldTrip.getPoster()).getUserID())) {
				// Update theme
				if(!(trip.getTheme() == oldTrip.getTheme())) {
					oldTrip.setTheme(trip.getTheme());
					ofy().save().entity(oldTrip).now();
					return oldTrip;
				}
				// Update companion---
				for(String p: oldTrip.getCompanion()) {
					if(!trip.getCompanion().contains(p)) {
						User user = ofy().load().type(User.class).id(new ServerTransform().stringToPoster(p).getUserID().toString()).now();
						if(user != null) {
							user.getMyTrips().remove(trip.getId());
							ofy().save().entity(user);
						}
					}
				}
				for(String p: trip.getCompanion()) {
					if(!oldTrip.getCompanion().contains(p)) {
						User user = ofy().load().type(User.class).id(new ServerTransform().stringToPoster(p).getUserID().toString()).now();
						if(user != null) {
							user.getMyTrips().add(trip.getId());
							ofy().save().entity(user);
						}
					}
				}
				// Update trip
				Key<Trip> key = ofy().save().entity(trip).now();
				exportTrip = ofy().load().key(key).now();
			} 
		}
		return exportTrip;
	}

	@Override
	public void removeTrip(Long idTrip, String accessToken) {
		Trip oldTrip = findTrip(idTrip);
		Poster poster = getPoster(accessToken);
		if (oldTrip != null && poster != null && poster.getUserID().equals(new ServerTransform().stringToPoster(oldTrip.getPoster()).getUserID())) {
			//Delete the link trip in the owner and companions
			User u = ofy().load().type(User.class).id(new ServerTransform().stringToPoster(oldTrip.getPoster()).getUserID()).now();
			if(u != null) {
				u.getMyTrips().remove(oldTrip.getId());
				ofy().save().entity(u);
			}
			for(String p: oldTrip.getCompanion()) {
				User us = ofy().load().type(User.class).id(new ServerTransform().stringToPoster(p).getUserID()).now();
				if(us != null) {
					us.getMyTrips().remove(oldTrip.getId());
					ofy().save().entity(us);
				}
			}
			//Delete Trip
			ofy().delete().entity(oldTrip);
		}
	}

	//----- Impls for Path -----
	
	@Override
	public Path insertPath(Path path, Long idTrip, String accessToken) {
		Path exportPath = null;
		Trip trip = ofy().load().type(Trip.class).id(idTrip).now();
		if(trip != null && accessToken != null) {
			Poster poster = getPoster(accessToken);
			if(poster != null) {
				path.setPoster(new ServerTransform().posterToString(poster));
				path.setTripId(idTrip);
				String preshortHtml = getPlainText(path.getDescription());
				path.setShortDescription(Jsoup.parse(preshortHtml).text());
				Key<Path> key = ofy().save().entity(path).now();
				exportPath = ofy().load().key(key).now();
				//--
				trip.getDestination().add(exportPath.getId());
				ofy().save().entity(trip);
			}
		}
		return exportPath;
	}

	@Override
	public Path findPath(Long idPath) {
		Path exportPath;
		exportPath = ofy().load().type(Path.class).id(idPath).now();
		return exportPath;
	}

	@Override
	public List<Path> listOfPath(List<Long> idsPath) {
		Map<Long, Path> mapPaths = ofy().load().type(Path.class).ids(idsPath);
		List<Path> result = new ArrayList<Path>(mapPaths.values());
		return result;
	}

	@Override
	public Path updatePath(Path path, String accessToken) {
		Path exportPath = null;
		Path oldData = findPath(path.getId());
		Poster poster = getPoster(accessToken);
		if ((oldData != null && poster != null) && 
				poster.getUserID().equals(new ServerTransform().stringToPoster(oldData.getPoster()).getUserID())) {
			if(!oldData.getDescription().equals(path.getDescription())) {
				String preshortHtml = getPlainText(path.getDescription());
				path.setShortDescription(Jsoup.parse(preshortHtml).text());
			}
			Key<Path> key = ofy().save().entity(path).now();
			exportPath = ofy().load().key(key).now();
		} 
		return exportPath;
	}

	@Override
	public void removePath(Long idPath, String accessToken) {
		Path oldData = findPath(idPath);
		Poster poster = getPoster(accessToken);
		if ((oldData != null && poster != null) 
				&& poster.getUserID().equals(new ServerTransform().stringToPoster(oldData.getPoster()).getUserID())) {
			//Delete the link part on the trip
			Trip trip = ofy().load().type(Trip.class).id(oldData.getTripId()).now();
			if(trip != null) {
				trip.getDestination().remove(oldData.getId());
				ofy().save().entity(trip);
			}
			//Delete Part
			ofy().delete().entity(oldData);
		}
	}
	
	//----- Impls for User -----
	
	@Override
	public void insertUser(User user) {
		User existUser = ofy().load().type(User.class).id(user.getId()).now();
		if(existUser == null) {
			user.setJoinDate(new Date());
			ofy().save().entity(user);
		}
	}

	@Override
	public User findUser(String idUser) {
		if(idUser == null || idUser.isEmpty()) {
			return null;
		}
		else {
			User oldData = ofy().load().type(User.class).id(idUser).now();
			return oldData;
		}
	}

	@Override
	public List<User> listOfUser(List<String> idsUser) {
		Map<String, User> mapUsers = ofy().load().type(User.class).ids(idsUser);
		List<User> result = new ArrayList<User>(mapUsers.values());
		return result;
	}

//	@Override
//	public User updateUser(User user) {
//		User exportUser ;
//		User oldData = findUser(user.getId());
//		if (oldData != null) {
//			Key<User> key = ofy().save().entity(user).now();
//			exportUser = ofy().load().key(key).now();
//		} else
//			exportUser = null;
//		return exportUser;
//	}

//	@Override
//	public void removeUser(String idUser) {
//		User oldData = findUser(idUser);
//		ofy().delete().entity(oldData);
//	}
	
	//----- Impls for Picture -----

	@Override
	public String getUploadUrl(Long pathId, Long idTrip, String accessToken) {
		Trip oldTrip = null;
		Path oldPath = null;
		if(idTrip != null)
			oldTrip = findTrip(idTrip);
		if(pathId != null)
			oldPath = findPath(pathId);
		Poster poster = getPoster(accessToken);
		if((oldTrip != null && poster != null) && poster.getUserID().equals(new ServerTransform().stringToPoster(oldTrip.getPoster()).getUserID())
				|| (oldPath != null && poster != null) && poster.getUserID().equals(new ServerTransform().stringToPoster(oldPath.getPoster()).getUserID())) 
			return blobStoreService.createUploadUrl("/photo_upload");
		else
			return "#";
	}

//	@Override
//	public void insertPicture(Picture picture) {
//		ofy().save().entity(picture);
//	}

	@Override
	public Picture findPicture(Long idPicture) {
		Picture p = ofy().load().type(Picture.class).id(idPicture).now();
		return p;
	}

	@Override
	public List<Picture> listPicture(List<Long> idsPicture) {
		Map<Long, Picture> mapPaths = ofy().load().type(Picture.class).ids(idsPicture);
		List<Picture> result = new ArrayList<Picture>(mapPaths.values());
		return result;
	}

	@Override
	public void deletePicture(Long idPicture, String accessToken) {
		Picture p = findPicture(idPicture);
		Trip oldTrip = null;
		if(p.getOnTrip() != null)
			oldTrip = findTrip(p.getOnTrip());
		Path oldPath = null;
		if(p.getOnPath() != null)
			oldPath = findPath(p.getOnPath());
		Poster poster = getPoster(accessToken);
		if(p != null) {
			if((oldTrip != null && poster != null) && poster.getUserID().equals(new ServerTransform().stringToPoster(oldTrip.getPoster()).getUserID())
					|| (oldPath != null && poster != null) && poster.getUserID().equals(new ServerTransform().stringToPoster(oldPath.getPoster()).getUserID())) {
				//Delete blob
				BlobKey blobKey = new BlobKey(p.getKey());
				blobStoreService.delete(blobKey);
				//Delete link on Trip of on Path
				if(p.getOnPath() == null) {
					Trip trip = ofy().load().type(Trip.class).id(p.getOnTrip()).now();
					if(trip != null) {
						trip.getGallery().remove(p.getId());
						ofy().save().entity(trip);
					}
				}
				else {
					Path path = ofy().load().type(Path.class).id(p.getOnPath()).now();
					if(path != null) {
						path.getGallery().remove(p.getId());
						ofy().save().entity(path);
					}
				}
				//Delete picture
				ofy().delete().entity(p);
			}
		}
	}
	
}
