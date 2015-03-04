package com.itpro.tripshare.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.itpro.tripshare.shared.User;

public interface UserServiceAsync {

	void insertUser(User user, AsyncCallback<User> callback);

	void findUser(Long idUser, AsyncCallback<User> callback);

	void updateUser(User user, AsyncCallback<User> callback);

	void removeUser(Long idUser, AsyncCallback<Void> callback);
}
