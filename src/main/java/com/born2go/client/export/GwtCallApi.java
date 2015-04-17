package com.born2go.client.export;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.born2go.client.widgets.DestinationPage_EditForm;
import com.born2go.client.widgets.PhotoUpload;
import com.born2go.client.widgets.ViewFullPath;

@ExportPackage("GWTExport")
@Export
public class GwtCallApi implements Exportable {

	public void uploadPhotoCall() {
		PhotoUpload photoUpload = new PhotoUpload();
		photoUpload.uploadFor(ViewFullPath.path.getTripId(), ViewFullPath.path.getId());
		photoUpload.center();
		photoUpload.handlerUploadEvent();
	}
	
	public void editFormCall() {
		DestinationPage_EditForm editor = new DestinationPage_EditForm();
		editor.addEditor();
	}

}
