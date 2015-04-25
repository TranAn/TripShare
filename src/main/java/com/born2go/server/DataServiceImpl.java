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

import com.born2go.client.rpc.DataService;
import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.born2go.shared.Poster;
import com.born2go.shared.Trip;
import com.born2go.shared.User;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class DataServiceImpl extends RemoteServiceServlet implements
		DataService {

	private Trip exportTrip = null;

	@Override
	public Trip insertTrip(Trip trip, String accessToken) {
		try {
			if(accessToken != null) {
				Poster poster = getPoster(accessToken);
				trip.setPoster(poster);
			}
			trip.setCreateDate(new Date());
			Key<Trip> key = ofy().save().entity(trip).now();
			exportTrip = ofy().load().key(key).now();
			//save trip to user
			User user = ofy().load().type(User.class).id(exportTrip.getPoster().getUserID().toString()).now();
			if(user != null) {
				user.getMyTrips().add(exportTrip.getId());
				ofy().save().entity(user);
			}
			return exportTrip;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Trip findTrip(Long idTrip) {
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
	public Trip updateTrip(Trip trip) {
		Trip oldTrip = findTrip(trip.getId());
		if (oldTrip != null) {
			Key<Trip> key = ofy().save().entity(trip).now();
			exportTrip = ofy().load().key(key).now();
		} else
			exportTrip = null;
		return exportTrip;
	}

	@Override
	public void removeTrip(Long idTrip) {
		Trip oldTrip = findTrip(idTrip);
		if (oldTrip != null)
			ofy().delete().entity(oldTrip);
	}

	/**
	 * impls for part
	 */
	private Path exportPart = null;

	@Override
	public Path insertPart(Path part, Long tripId, String accessToken) {
		Trip trip = ofy().load().type(Trip.class).id(tripId).now();
		if(trip != null) {
			try {
				if(accessToken != null) {
					Poster poster = getPoster(accessToken);
					part.setPoster(poster);
				}
				part.setTripId(tripId);
				Key<Path> key = ofy().save().entity(part).now();
				exportPart = ofy().load().key(key).now();
				//--
				trip.getDestination().add(exportPart.getId());
				ofy().save().entity(trip);
				return exportPart;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		else
			return null;
	}

	@Override
	public Path findPart(Long idPart) {
		Path oldData = ofy().load().type(Path.class).id(idPart).now();
		return oldData;
	}

	@Override
	public List<Path> listOfPath(List<Long> idsPath) {
		Map<Long, Path> mapPaths = ofy().load().type(Path.class).ids(idsPath);
		List<Path> result = new ArrayList<Path>(mapPaths.values());
		return result;
	}

	@Override
	public Path updatePart(Path part) {
		Path oldData = findPart(part.getId());
		if (oldData != null) {
			Key<Path> key = ofy().save().entity(part).now();
			exportPart = ofy().load().key(key).now();
		} else
			exportPart = null;

		return exportPart;
	}

	@Override
	public void removePart(Long idPart) {
		Path oldData = findPart(idPart);
		if (oldData != null)
			ofy().delete().entity(oldData);

	}

	private User exportUser = null;

	Poster getPoster(String accessToken) throws Exception{
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
		poster.setUserName(myObj.getString("first_name")+ " "+ myObj.getString("last_name"));
		
		return poster;
	}

	@Override
	public String getLongLiveToken(String accessToken) throws Exception{
		String url = "https://graph.facebook.com/oauth/access_token?"  
				+ "grant_type=fb_exchange_token&"
				+ "client_id=1636504239911870&"
				+ "client_secret=ce5b4aede58568aba92aafb894b91c49&"
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
	public void removeUser(String idUser) {
		User oldData = findUser(idUser);
		ofy().delete().entity(oldData);
	}

	private BlobstoreService blobStoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	@Override
	public String getUploadUrl() {
		return blobStoreService.createUploadUrl("/photo_upload");
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
	public List<Picture> listPicture(List<Long> idsPicture) {
		Map<Long, Picture> mapPaths = ofy().load().type(Picture.class).ids(idsPicture);
		List<Picture> result = new ArrayList<Picture>(mapPaths.values());
		return result;
	}

	@Override
	public void deletePicture(Long idPicture) {
//		Picture p = findPicture(idPicture);
//		BlobKey blobKey = new BlobKey(p.getKey());
//		blobstoreService.delete(blobKey);
//		ofy().delete().entity(p);
	}
	
}
