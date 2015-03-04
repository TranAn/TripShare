package com.itpro.tripshare.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.itpro.tripshare.shared.Picture;

public interface PictureServiceAsync {
	void getURLUpload(AsyncCallback<String> callback);

	void deletePicture(Long idPicture, AsyncCallback<Void> callback);

	void findPicture(Long idPicture, AsyncCallback<Picture> callback);

	void insertPicture(Picture picture, AsyncCallback<Void> callback);
}
