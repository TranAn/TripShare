package com.born2go.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.born2go.client.TripShare;
import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class ViewFullPath extends Composite {

	private static ViewFullPathUiBinder uiBinder = GWT
			.create(ViewFullPathUiBinder.class);
	
	@UiField
	HTMLPanel htmlImages;
	@UiField
	FlexTable flextImages;

	interface ViewFullPathUiBinder extends UiBinder<Widget, ViewFullPath> {
	}

	List<String> urls;

	List<Image> listImages;

	public ViewFullPath() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void getPath(Long pathId) {
		TripShare.dataService.findPart(pathId, new AsyncCallback<Path>() {
			
			@Override
			public void onSuccess(Path result) {
				TripShare.dataService.listPicture(result.getGallery(), new AsyncCallback<List<Picture>>() {
					
					@Override
					public void onSuccess(List<Picture> result) {
						if (result != null && result.size() > 0) {
							addImages(result);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("!: Đã có lỗi xảy ra, vui lòng tải lại trang.");
					}
				});
			}
			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("!: Đã có lỗi xảy ra, vui lòng tải lại trang.");
			}
		});
	}

	public void addImages(List<Picture> listPicture) {
		flextImages.clear();
		if (listPicture.size() > 0) {
			int totalRow = 0;
			int size = listPicture.size();

			if (size % 4 == 0) {
				totalRow = size / 4;
			} else {
				totalRow = (size / 4) + 1;
			}
			urls = new ArrayList<String>();
			listImages = new ArrayList<Image>();
			for (int i = 0; i < totalRow; i++) {

				if (size > 0) {
					for (int j = 0; j < 4; j++) {

						if (size > 0) {
							Picture p = listPicture.get(size - 1);
							urls.add(p.getServeUrl());
							Image image = new Image(p.getServeUrl());
							image.setStyleName("imageView");
							flextImages.setWidget(i, j, image);
							listImages.add(image);

						}

						size = size - 1;
					}
				}
			}
			for (final Image image : listImages) {
				final String photosUri = getString(urls);
				image.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						int index = urls.indexOf(image.getUrl());
						openGallery(photosUri, index);
					}
				});
			}
		}
	}

	private String getString(List<String> urls) {
		StringBuilder builder = new StringBuilder();
		for (String string : urls) {
			builder.append(string);
			builder.append(";");
		}
		return builder.toString();
	}

	public static native void openGallery(String photosUri, int index) /*-{
		var viewer = new $wnd.PhotoViewer();
		var URIs = photosUri.split(";");
		for(var i = 0; i < URIs.length-1; i++) {
			viewer.add(URIs[i]);
		}
		viewer.show(index);
	}-*/;
	
}
