package com.born2go.client.export;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.born2go.client.widgets.PhotoUpload;
import com.born2go.client.widgets.ViewFullPath;

@ExportPackage("GWTExport")
@Export
public class GwtCallApi implements Exportable {

	public void uploadPhotoCall() {
		PhotoUpload photoUpload = new PhotoUpload();
		photoUpload.uploadFor(ViewFullPath.trip_Id, ViewFullPath.path_Id);
		photoUpload.center();
		photoUpload.handlerUploadEvent();
	}

}
