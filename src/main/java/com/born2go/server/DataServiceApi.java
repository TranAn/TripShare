package com.born2go.server;

import java.util.List;

import com.born2go.shared.Path;
import com.born2go.shared.Trip;
import com.born2go.shared.User;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

/** An endpoint class we are exposing */
@Api(name = "dataServiceApi",
	 title = "Trip Share dataservice endpoints",
     version = "v1",
     namespace = @ApiNamespace(ownerDomain = "server.born2go.com",
                                ownerName = "server.born2go.com",
                                packagePath = ""))
public class DataServiceApi {
	
	DataServiceImpl dataServiceImpl = new DataServiceImpl();

	/**
	 * Insert a trip
	 * @param accessToken
	 * @param trip
	 * @return inserted trip
	 */
    @ApiMethod(name = "insertTrip", httpMethod = HttpMethod.POST)
    public Trip insertTrip(@Named("accessToken") String accessToken, Trip trip) {
    	return dataServiceImpl.insertTrip(trip, accessToken);	
    }
    
    /**
     * Find trip by id
     * @param tripId
     * @return trip match the id
     */
    @ApiMethod(name = "findTrip", httpMethod = HttpMethod.GET)
    public Trip findTrip(@Named("tripId") Long tripId) {
    	return dataServiceImpl.findTrip(tripId);
	}
    
    /**
     * Get all trip
     * @param tripIds
     * @return list all of trip
     */
    @ApiMethod(name = "listTrip", httpMethod = HttpMethod.GET)
    public List<Trip> listTrip(@Named("tripIds") List<Long> tripIds) {
    	return dataServiceImpl.listOfTrip(tripIds);
	}
    
    /**
     * Update a trip
     * @param accessToken
     * @param trip
     * @return updated trip
     */
    @ApiMethod(name = "updateTrip", httpMethod = HttpMethod.PUT)
    public Trip updateTrip(@Named("accessToken") String accessToken, Trip trip) {
    	return dataServiceImpl.updateTrip(trip, accessToken);
	}
    
    /**
     * Delete a trip
     * @param accessToken
     * @param tripId
     */
    @ApiMethod(name = "removeTrip", httpMethod = HttpMethod.DELETE)
    public void removeTrip(@Named("accessToken") String accessToken, @Named("tripId") Long tripId) {
		dataServiceImpl.removeTrip(tripId, accessToken);
	}
    
    /**
     * Insert a path (post)
     * @param accessToken
     * @param tripId
     * @param path
     * @return inserted path
     */
    @ApiMethod(name = "insertPath", httpMethod = HttpMethod.POST)
    public Path insertPath(@Named("accessToken") String accessToken, @Named("trip_id") Long tripId, Path path) {
    	return dataServiceImpl.insertPath(path, tripId, accessToken);
	}
    
    /**
     * Find path (post) by id
     * @param pathId
     * @return path match the id
     */
    @ApiMethod(name = "findPath", httpMethod = HttpMethod.GET)
    public Path findPath(@Named("pathId") Long pathId) {
		return dataServiceImpl.findPath(pathId);
	}
    
    /**
     * Get all path (post)
     * @param pathIds
     * @return list all of path
     */
    @ApiMethod(name = "listPath", httpMethod = HttpMethod.GET)
    public List<Path> listPath(@Named("pathIds") List<Long> pathIds) {
		return dataServiceImpl.listOfPath(pathIds);
	}
    
    /**
     * Update a path (post)
     * @param accessToken
     * @param path
     * @return updated path
     */
    @ApiMethod(name = "updatePath", httpMethod = HttpMethod.PUT)
    public Path updatePath(@Named("accessToken") String accessToken, Path path) {
		return dataServiceImpl.updatePath(path, accessToken);
	}
    
    /**
     * Remove a path (post)
     * @param accessToken
     * @param pathId
     */
    @ApiMethod(name = "removePath", httpMethod = HttpMethod.DELETE)
    public void removePath(@Named("accessToken") String accessToken, @Named("pathId") Long pathId) {
		dataServiceImpl.removePath(pathId, accessToken);
	}
    
    /**
     * Insert new user
     * @param user
     */
    @ApiMethod(name = "insertUser", httpMethod = HttpMethod.POST)
    public void insertUser(User user) {
		dataServiceImpl.insertUser(user);
	}
    
    /**
     * Find a user
     * @param userId
     * @return user match the id
     */
    @ApiMethod(name = "findUser", httpMethod = HttpMethod.GET)
    public User findUser(@Named("userId") String userId) {
		return dataServiceImpl.findUser(userId);
	}
    
}
