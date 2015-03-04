package com.itpro.tripshare.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.itpro.tripshare.shared.Picture;

@RemoteServiceRelativePath("blobservice")
public interface PictureService extends RemoteService {
	public String getURLUpload();

	public void deletePicture(Long idPicture);

	public Picture findPicture(Long idPicture);

	public void insertPicture(Picture picture);

}
