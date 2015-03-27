package com.itpro.tripshare.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;
import com.itpro.tripshare.shared.Path;
import com.itpro.tripshare.shared.Picture;
import com.itpro.tripshare.shared.Trip;
import com.itpro.tripshare.shared.User;

@SuppressWarnings("serial")
public class DataRegister extends RemoteServiceServlet {
	
	public DataRegister() {
		super();

		ObjectifyService.register(Trip.class);
		ObjectifyService.register(Picture.class);
		ObjectifyService.register(User.class);
		ObjectifyService.register(Path.class);

	}

}
