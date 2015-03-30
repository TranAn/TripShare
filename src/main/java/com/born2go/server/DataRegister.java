package com.born2go.server;

import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.born2go.shared.Trip;
import com.born2go.shared.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.ObjectifyService;

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
