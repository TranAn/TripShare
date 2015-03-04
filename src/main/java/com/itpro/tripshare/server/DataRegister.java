package com.itpro.tripshare.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.itpro.tripshare.shared.Journey;
import com.itpro.tripshare.shared.Journey.Point;
import com.itpro.tripshare.shared.Locate;
import com.itpro.tripshare.shared.Part;
import com.itpro.tripshare.shared.Picture;
import com.itpro.tripshare.shared.Trip;
import com.itpro.tripshare.shared.User;
import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class DataRegister extends RemoteServiceServlet {
	public DataRegister() {
		super();

		ObjectifyService.register(Trip.class);
		ObjectifyService.register(Picture.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(Part.class);

		Locate locate1 = new Locate();
		locate1.setAddressName("Ha Noi");
		locate1.setLatitude(10.0);
		locate1.setLongitude(20.0);

		Locate locate2 = new Locate();
		locate2.setAddressName("Ha Noi");
		locate2.setLatitude(10.0);
		locate2.setLongitude(20.0);

		List<Locate> listLocal = new ArrayList<Locate>();
		listLocal.add(locate2);
		listLocal.add(locate1);
		Journey journey = new Journey();
		journey.setLocate(listLocal);
		Point p1 = new Point(10.0, 20.0);
		Point p2 = new Point(20.0, 30.0);
		List<Point> listP = new ArrayList<Point>();
		listP.add(p1);
		listP.add(p2);

		Map<String, List<Point>> map = new HashMap<>();
		map.put("list point", listP);
		journey.setDirections(map);

		Trip trip = new Trip();
		trip.setCreateDate(new Date());
		trip.setDescription("Ha noi - Hai Phong");
		trip.setJourney(journey);
		ofy().save().entity(trip);

	}

}
