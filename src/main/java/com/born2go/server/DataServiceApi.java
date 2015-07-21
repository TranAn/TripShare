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

import com.born2go.shared.Path;
import com.born2go.shared.Trip;
import com.born2go.shared.User;
import com.born2go.shared.embedclass.Poster;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.Key;

/** An endpoint class we are exposing */
@Api(name = "dataServiceApi",
	 title = "Trip Share dataservice endpoints",
     version = "v1",
     namespace = @ApiNamespace(ownerDomain = "server.born2go.com",
                                ownerName = "server.born2go.com",
                                packagePath = ""))
public class DataServiceApi {
	
	DataServiceImpl dataServiceImpl = new DataServiceImpl();
	
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

	/**
	 * Insert a trip
	 * @param accessToken
	 * @param trip
	 * @return inserted trip
	 */
    @ApiMethod(name = "insertTrip", httpMethod = HttpMethod.POST)
    public Trip insertTrip(@Named("accessToken") String accessToken, Trip trip) {
    	Trip exportTrip = null;
		if(accessToken != null) {
			Poster poster = getPoster(accessToken);
			if(poster != null) {
				trip.setPoster(poster);
				trip.setCreateDate(new Date());
				Key<Trip> key = ofy().save().entity(trip).now();
				exportTrip = ofy().load().key(key).now();
				//save trip to user
				User user = ofy().load().type(User.class).id(exportTrip.getPoster().getUserID().toString()).now();
				if(user != null) {
					user.getMyTrips().add(exportTrip.getId());
					ofy().save().entity(user);
				}
				if(!trip.getCompanion().isEmpty()) {
					for(Poster p: trip.getCompanion()) {
						User u = ofy().load().type(User.class).id(p.getUserID().toString()).now();
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
    
    /**
     * Find trip by id
     * @param tripId
     * @return trip match the id
     */
    @ApiMethod(name = "findTrip", httpMethod = HttpMethod.GET)
    public Trip findTrip(@Named("tripId") Long tripId) {
//		Trip exportTrip;
//		exportTrip = ofy().load().type(Trip.class).id(tripId).now();
//		return exportTrip;
    	return dataServiceImpl.findTrip(tripId);
	}
    
    /**
     * Get all trip
     * @param tripIds
     * @return list all of trip
     */
    @ApiMethod(name = "listTrip", httpMethod = HttpMethod.GET)
    public List<Trip> listTrip(@Named("tripIds") List<Long> tripIds) {
		Map<Long, Trip> mapTrips = ofy().load().type(Trip.class).ids(tripIds);
		List<Trip> result = new ArrayList<Trip>(mapTrips.values());
		return result;
	}
    
    /**
     * Update a trip
     * @param accessToken
     * @param trip
     * @return updated trip
     */
    @ApiMethod(name = "updateTrip", httpMethod = HttpMethod.PUT)
    public Trip updateTrip(@Named("accessToken") String accessToken, Trip trip) {
		Trip exportTrip = null;
		Trip oldTrip = findTrip(trip.getId());
		Poster poster = getPoster(accessToken);
		if (oldTrip != null && poster != null && poster.getUserID().equals(oldTrip.getPoster().getUserID())) {
			// Update theme
			if(trip.getTheme() != null) {
				if(!trip.getTheme().equals(oldTrip.getTheme())) {
					oldTrip.setTheme(trip.getTheme());
					ofy().save().entity(oldTrip).now();
					return oldTrip;
				}
			}
			// Update companion---
			for(Poster p: oldTrip.getCompanion()) {
				if(!trip.getCompanion().contains(p)) {
					User user = ofy().load().type(User.class).id(p.getUserID().toString()).now();
					if(user != null) {
						user.getMyTrips().remove(trip.getId());
						ofy().save().entity(user);
					}
				}
			}
			for(Poster p: trip.getCompanion()) {
				if(!oldTrip.getCompanion().contains(p)) {
					User user = ofy().load().type(User.class).id(p.getUserID().toString()).now();
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
		return exportTrip;
	}
    
    /**
     * Delete a trip
     * @param accessToken
     * @param tripId
     */
    @ApiMethod(name = "removeTrip", httpMethod = HttpMethod.DELETE)
    public void removeTrip(@Named("accessToken") String accessToken, @Named("tripId") Long tripId) {
		Trip oldTrip = findTrip(tripId);
		Poster poster = getPoster(accessToken);
		if (oldTrip != null && poster != null && poster.getUserID().equals(oldTrip.getPoster().getUserID())) {
			//Delete the link trip in the owner and companions
			User u = ofy().load().type(User.class).id(oldTrip.getPoster().getUserID()).now();
			if(u != null) {
				u.getMyTrips().remove(oldTrip.getId());
				ofy().save().entity(u);
			}
			for(Poster p: oldTrip.getCompanion()) {
				User us = ofy().load().type(User.class).id(p.getUserID()).now();
				if(us != null) {
					us.getMyTrips().remove(oldTrip.getId());
					ofy().save().entity(us);
				}
			}
			//Delete Trip
			ofy().delete().entity(oldTrip);
		}
	}
    
    /**
     * Insert a path (post)
     * @param accessToken
     * @param tripId
     * @param path
     * @return inserted path
     */
    @ApiMethod(name = "insertPath", httpMethod = HttpMethod.POST)
    public Path insertPath(@Named("accessToken") String accessToken, @Named("tripId") Long tripId, Path path) {
		Path exportPath = null;
		Trip trip = ofy().load().type(Trip.class).id(tripId).now();
		if(trip != null && accessToken != null) {
			Poster poster = getPoster(accessToken);
			if(poster != null) {
				path.setPoster(poster);
				path.setTripId(tripId);
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
    
    /**
     * Find path (post) by id
     * @param pathId
     * @return path match the id
     */
    @ApiMethod(name = "findPath", httpMethod = HttpMethod.GET)
    public Path findPath(@Named("pathId") Long pathId) {
		Path exportPath;
		exportPath = ofy().load().type(Path.class).id(pathId).now();
		return exportPath;
	}
    
    /**
     * Get all path (post)
     * @param pathIds
     * @return list all of path
     */
    @ApiMethod(name = "listPath", httpMethod = HttpMethod.GET)
    public List<Path> listPath(@Named("pathIds") List<Long> pathIds) {
		Map<Long, Path> mapPaths = ofy().load().type(Path.class).ids(pathIds);
		List<Path> result = new ArrayList<Path>(mapPaths.values());
		return result;
	}
    
    /**
     * Update a path (post)
     * @param accessToken
     * @param path
     * @return updated path
     */
    @ApiMethod(name = "updatePath", httpMethod = HttpMethod.PUT)
    public Path updatePath(@Named("accessToken") String accessToken, Path path) {
		Path exportPath = null;
		Path oldData = findPath(path.getId());
		Poster poster = getPoster(accessToken);
		if (oldData != null && poster != null && poster.getUserID().equals(oldData.getPoster().getUserID())) {
			if(!oldData.getDescription().equals(path.getDescription())) {
				String preshortHtml = getPlainText(path.getDescription());
				path.setShortDescription(Jsoup.parse(preshortHtml).text());
			}
			Key<Path> key = ofy().save().entity(path).now();
			exportPath = ofy().load().key(key).now();
		} 
		return exportPath;
	}
    
    /**
     * Remove a path (post)
     * @param accessToken
     * @param pathId
     */
    @ApiMethod(name = "removePath", httpMethod = HttpMethod.DELETE)
    public void removePath(@Named("accessToken") String accessToken, @Named("pathId") Long pathId) {
		Path oldData = findPath(pathId);
		Poster poster = getPoster(accessToken);
		if (oldData != null && poster != null && poster.getUserID().equals(oldData.getPoster().getUserID())) {
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
    
    /**
     * Insert new user
     * @param user
     */
    @ApiMethod(name = "insertUser", httpMethod = HttpMethod.POST)
    public void insertUser(User user) {
		User existUser = ofy().load().type(User.class).id(user.getId()).now();
		if(existUser == null) {
			user.setJoinDate(new Date());
			ofy().save().entity(user);
		}
	}
    
    /**
     * Find a user
     * @param userId
     * @return user match the id
     */
    @ApiMethod(name = "findUser", httpMethod = HttpMethod.GET)
    public User findUser(@Named("userId") String userId) {
		if(userId == null || userId.isEmpty()) {
			return null;
		}
		else {
			User oldData = ofy().load().type(User.class).id(userId).now();
			return oldData;
		}
	}
    
}
