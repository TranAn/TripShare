package com.born2go.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.axeiya.gwtckeditor.client.CKConfig;
import com.axeiya.gwtckeditor.client.CKConfig.TOOLBAR_OPTIONS;
import com.axeiya.gwtckeditor.client.CKEditor;
import com.axeiya.gwtckeditor.client.Toolbar;
import com.axeiya.gwtckeditor.client.ToolbarLine;
import com.born2go.client.TripShare;
import com.born2go.shared.Path;
import com.born2go.shared.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Destination_EditToolBar extends Composite {

	private static ViewFullPathUiBinder uiBinder = GWT
			.create(ViewFullPathUiBinder.class);
	
	@UiField
	HTMLPanel htmlImages;
	@UiField
	FlexTable flextImages;

	interface ViewFullPathUiBinder extends UiBinder<Widget, Destination_EditToolBar> {
	}

	List<String> urls;
	
	private Path path;
	private boolean isPoster = false;

	public Destination_EditToolBar() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public void checkPermission() {
		if(TripShare.user_id != null && path != null) {
			if(path.getPoster().getUserID().toString().equals(TripShare.user_id)) {
				addEditor();
				isPoster = true;
			}
		}
	}
	
	public CKConfig getCKConfig() {
		// Creates a new config, with FULL preset toolbar as default
		CKConfig ckf = new CKConfig();
		
		// Setting background color
		ckf.setUiColor("#f6f7f8");
		
		// Setting size
		ckf.setWidth("");
		ckf.setHeight("250px");

		// Creating personalized toolbar
		ToolbarLine line1 = new ToolbarLine();
		line1.add(TOOLBAR_OPTIONS.Source);
		line1.add(TOOLBAR_OPTIONS._);
		line1.add(TOOLBAR_OPTIONS.Undo);
		line1.add(TOOLBAR_OPTIONS.Redo);

		// Creates the toolbar
		Toolbar t = new Toolbar();
		t.add(line1);
		t.addSeparator();

		// Define the second line
		TOOLBAR_OPTIONS[] t2 = { TOOLBAR_OPTIONS.Bold, TOOLBAR_OPTIONS.Italic,
				TOOLBAR_OPTIONS.Underline, TOOLBAR_OPTIONS._,
				TOOLBAR_OPTIONS.RemoveFormat, TOOLBAR_OPTIONS._ };
		ToolbarLine line2 = new ToolbarLine();
		line2.addAll(t2);
		t.add(line2);
		t.addSeparator();

		// Define the third line
		TOOLBAR_OPTIONS[] t3 = { TOOLBAR_OPTIONS.Find, TOOLBAR_OPTIONS.Replace, };
		ToolbarLine line3 = new ToolbarLine();
		line3.addAll(t3);
		t.add(line3);
		t.addSeparator();

		// Define the second line
		TOOLBAR_OPTIONS[] t4 = {
		TOOLBAR_OPTIONS.JustifyLeft, TOOLBAR_OPTIONS.JustifyCenter,
				TOOLBAR_OPTIONS.JustifyRight, TOOLBAR_OPTIONS.JustifyBlock };
		
		ToolbarLine line4 = new ToolbarLine();
		line4.addAll(t4);
		t.add(line4);
		t.addSeparator();

		ToolbarLine line5 = new ToolbarLine();
		line5.add(TOOLBAR_OPTIONS.Link);
		line5.add(TOOLBAR_OPTIONS.Unlink);
		t.add(line5);
		t.addSeparator();

		ToolbarLine line6 = new ToolbarLine();
		line6.add(TOOLBAR_OPTIONS.Image);
		line6.add(TOOLBAR_OPTIONS.Smiley);
		t.add(line6);
		t.addSeparator();

		ToolbarLine line7 = new ToolbarLine();
		line7.add(TOOLBAR_OPTIONS.Styles);
		t.add(line7);

		ToolbarLine line8 = new ToolbarLine();
		line8.add(TOOLBAR_OPTIONS.Format);
		t.add(line8);
		t.addSeparator();

		ToolbarLine line9 = new ToolbarLine();
		line9.add(TOOLBAR_OPTIONS.Font);
		t.add(line9);
		t.addSeparator();

		ToolbarLine line10 = new ToolbarLine();
		line10.add(TOOLBAR_OPTIONS.FontSize);
		t.add(line10);
		t.addSeparator();

		ToolbarLine line11 = new ToolbarLine();
		line11.add(TOOLBAR_OPTIONS.TextColor);
//		line11.add(TOOLBAR_OPTIONS.SpecialChar);
		line11.add(TOOLBAR_OPTIONS.Maximize);
		t.add(line11);
		t.addSeparator();
		
		// Set the toolbar to the config (replace the FULL preset toolbar)
		ckf.setToolbar(t);
		return ckf;
	}
	
	void addEditor() {
		Label btnEdit = new Label();
		btnEdit.setStyleName("PhotoUpload-Obj2");
		btnEdit.getElement().setInnerHTML("<i style='margin-right:5px;' class='fa fa-pencil fa-lg'></i>Edit");
		Label btnUpload = new Label();
		btnUpload.setStyleName("PhotoUpload-Obj2");
		btnUpload.getElement().setInnerHTML("<i style='margin-right:5px;' class='fa fa-camera fa-lg'></i>Upload Photos");
		RootPanel.get("pathEditTool").add(btnEdit);
		RootPanel.get("pathUploadTool").add(btnUpload);
		//event handler
		btnEdit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				openEdit();
			}
		});
		btnUpload.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				PhotoUpload photoUpload = new PhotoUpload();
				photoUpload.uploadFor(path.getTripId(), path.getId());
				photoUpload.center();
				photoUpload.handlerUploadEvent();
			}
		});
	}
	
	void openEdit() {
		//remove content
		DOM.getElementById("pathDescription").setInnerHTML("");
		DOM.getElementById("pathEditTool").setInnerHTML("");
		DOM.getElementById("pathTitle").setInnerHTML("");
		//add editor
		final TextBox txbPathTitle = new TextBox();
		txbPathTitle.setText(path.getTitle());
		txbPathTitle.setStyleName("font_4");
		txbPathTitle.getElement().setAttribute("style", "width:99%; text-indent: 5px;");
		RootPanel.get("pathTitle").add(txbPathTitle);
		HTMLPanel newContainer = new HTMLPanel("");
		RootPanel.get("pathDescription").add(newContainer);
		final CKEditor editor = new CKEditor(getCKConfig());
		newContainer.add(editor);
		editor.setData(path.getDescription());
		Window.scrollTo(0, 85);
		//add edit tool
		Label btnSave = new Label();
		Label btnCancel = new Label();
		btnSave.setStyleName("PhotoUpload-Obj2");
		btnSave.getElement().setInnerHTML("<i style='margin-right:5px;' class='fa fa-floppy-o fa-lg'></i>Save");
		btnCancel.setStyleName("PhotoUpload-Obj2");
		btnCancel.getElement().setInnerHTML("<i style='margin-right:5px;' class='fa fa-ban fa-lg'></i>Cancel");
		RootPanel.get("pathEditTool").add(btnSave);
		RootPanel.get("pathEditTool").add(btnCancel);
		//event handler
		btnCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cancelEdit();
			}
		});
		btnSave.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				TripShare.loadBox.center();
				Path viewPath = path;
				viewPath.setTitle(txbPathTitle.getText());
				viewPath.setDescription(editor.getData());
				TripShare.dataService.updatePart(viewPath, new AsyncCallback<Path>() {
					@Override
					public void onSuccess(Path result) {
						TripShare.loadBox.hide();
						path = result;
						cancelEdit();
					}
					@Override
					public void onFailure(Throwable caught) {
						TripShare.loadBox.hide();
						Window.alert("!: Đã có lỗi xảy ra, vui lòng tải lại trang.");
					}
				});
			}
		});
	}
	
	void cancelEdit() {
		//remove content
		DOM.getElementById("pathDescription").setInnerHTML("");
		DOM.getElementById("pathEditTool").setInnerHTML("");
		DOM.getElementById("pathTitle").setInnerHTML("");
		//add static content
		Label title = new Label();
		title.setText(path.getTitle());
		title.setStyleName("font_4");
		RootPanel.get("pathTitle").add(title);
		HTMLPanel newContainer = new HTMLPanel("");
		RootPanel.get("pathDescription").add(newContainer);
		newContainer.getElement().setInnerHTML(path.getDescription());
		Window.scrollTo(0, 85);
		Label btnEdit = new Label();
		btnEdit.setStyleName("PhotoUpload-Obj2");
		btnEdit.getElement().setInnerHTML("<i style='margin-right:5px;' class='fa fa-pencil fa-lg'></i>Edit");
		RootPanel.get("pathEditTool").add(btnEdit);
		//event handler
		btnEdit.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				openEdit();
			}
		});
	}
	
	public void getPath(Long pathId) {
		TripShare.dataService.findPart(pathId, new AsyncCallback<Path>() {
			@Override
			public void onSuccess(Path result) {
				path = result;
				checkPermission();
				TripShare.dataService.listPicture(result.getGallery(), new AsyncCallback<List<Picture>>() {
					@Override
					public void onSuccess(List<Picture> result) {
						if (result != null && result.size() > 0) {
							addImages(result);
						}
					}				
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(TripShare.ERROR_MESSAGE);
					}
				});
			}			
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(TripShare.ERROR_MESSAGE);
			}
		});
	}

	public void addImages(final List<Picture> listPicture) {
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
			final List<Image> listImages = new ArrayList<Image>();
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
				image.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {					
						if(isPoster) {
							final Destination_PhotoOption option = new Destination_PhotoOption();
							option.center();
							option.addStyleName("fadeIn");
							option.setListener(new Destination_PhotoOption.Listener() {
								@Override
								public void onViewClick() {
									option.hide();
									int index = urls.indexOf(image.getUrl());
									String photosUri = getString(urls);
									openGallery(photosUri, index);
								}
								
								@Override
								public void onSetFeaturedPhotoClick() {
									option.startLoading();
									final int index = listPicture.size() - 1 - urls.indexOf(image.getUrl());
									path.setFeaturedPhoto(index);
									TripShare.dataService.updatePart(path, new AsyncCallback<Path>() {
										@Override
										public void onSuccess(Path result) {
											option.hide();
											option.endLoading();
											DOM.getElementById("destination_featured_photo").setAttribute("src", listPicture.get(index).getServeUrl());
										}
										
										@Override
										public void onFailure(Throwable caught) {
											option.hide();
											option.endLoading();
											Window.alert(TripShare.ERROR_MESSAGE);
										}
									});
								}
								
								@Override
								public void onDeleteClick() {
									option.startLoading();
									final int index = listPicture.size() - 1 - urls.indexOf(image.getUrl());
									TripShare.dataService.deletePicture(path.getGallery().get(index), new AsyncCallback<Void>() {								
										@Override
										public void onSuccess(Void result) {
											option.hide();
											option.endLoading();
											listPicture.remove(index);
											listImages.remove(urls.indexOf(image.getUrl()));
											urls.remove(image.getUrl());
											path.getGallery().remove(index);
											flextImages.clear();
											int imageIndex = 0;
											int row = 0;
											int col = 0;
											while(imageIndex < listImages.size()) {
												if(col > 3) {
													row++;
													col = 0;
												}
												flextImages.setWidget(row, col, listImages.get(imageIndex));
												imageIndex++;
												col++;
											}
										}
										
										@Override
										public void onFailure(Throwable caught) {
											option.hide();
											option.endLoading();
											Window.alert(TripShare.ERROR_MESSAGE);
										}
									});
								}
							});
						}
						else {
							int index = urls.indexOf(image.getUrl());
							String photosUri = getString(urls);
							openGallery(photosUri, index);
						}
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
