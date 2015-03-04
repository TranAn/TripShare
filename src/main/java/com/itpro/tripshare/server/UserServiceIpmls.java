package com.itpro.tripshare.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.itpro.tripshare.client.rpc.UserService;
import com.itpro.tripshare.shared.User;
import static com.googlecode.objectify.ObjectifyService.ofy;

@SuppressWarnings("serial")
public class UserServiceIpmls extends RemoteServiceServlet implements
		UserService {
	private User exportUser = null;

	@Override
	public User insertUser(User user) {
		Key<User> key = ofy().save().entity(user).now();
		exportUser = ofy().load().key(key).now();
		return exportUser;
	}

	@Override
	public User findUser(Long idUser) {
		User oldData = ofy().load().type(User.class).id(idUser).now();
		return oldData;
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
	public void removeUser(Long idUser) {
		User oldData = findUser(idUser);
		ofy().delete().entity(oldData);

	}

}
