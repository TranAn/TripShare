package com.itpro.tripshare.client.rpc;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.itpro.tripshare.shared.User;

@RemoteServiceRelativePath("service-user")
public interface UserService {
	public User insertUser(User user);

	public User findUser(Long idUser);

	public User updateUser(User user);

	public void removeUser(Long idUser);
}
