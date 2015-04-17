package com.born2go.client.widgets;

import com.axeiya.gwtckeditor.client.CKConfig;
import com.axeiya.gwtckeditor.client.CKConfig.TOOLBAR_OPTIONS;
import com.axeiya.gwtckeditor.client.CKEditor;
import com.axeiya.gwtckeditor.client.Toolbar;
import com.axeiya.gwtckeditor.client.ToolbarLine;
import com.born2go.client.TripShare;
import com.born2go.shared.Path;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class DestinationPage_EditForm {
	
	HTMLPanel container;
	CKEditor editor;
	Label btnSave = new Label();
	Label btnCancel = new Label();
	
	public DestinationPage_EditForm() {
		container = new HTMLPanel("");
		editor = new CKEditor(getCKConfig());
		container.add(editor);
		//---
		btnSave.setStyleName("PhotoUpload-Obj2");
		btnSave.getElement().setInnerHTML("<i style='margin-right:5px;' class='fa fa-floppy-o fa-lg'></i>Save");
		btnCancel.setStyleName("PhotoUpload-Obj2");
		btnCancel.getElement().setInnerHTML("<i style='margin-right:5px;' class='fa fa-ban fa-lg'></i>Cancel");
		//--- Event handler
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
				Path viewPath = ViewFullPath.path;
				viewPath.setDescription(editor.getData());
				TripShare.dataService.updatePart(viewPath, new AsyncCallback<Path>() {
					@Override
					public void onSuccess(Path result) {
						TripShare.loadBox.hide();
						ViewFullPath.path = result;
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
	
	public void addEditor() {
		//remove content
		DOM.getElementById("pathDescription").setInnerHTML("");
		DOM.getElementById("pathEditTool").setInnerHTML("");
		//add editor
		RootPanel.get("pathDescription").add(container);
		editor.setData(ViewFullPath.path.getDescription());
		Window.scrollTo(0, container.getElement().getAbsoluteTop()-50);
		//add edit tool
		RootPanel.get("pathEditTool").add(btnSave);
		RootPanel.get("pathEditTool").add(btnCancel);
	}
	
	public void openEdit() {
		//remove content
		DOM.getElementById("pathDescription").setInnerHTML("");
		DOM.getElementById("pathEditTool").setInnerHTML("");
		//add editor
		HTMLPanel newContainer = new HTMLPanel("");
		RootPanel.get("pathDescription").add(newContainer);
		final CKEditor editor = new CKEditor(getCKConfig());
		newContainer.add(editor);
		editor.setData(ViewFullPath.path.getDescription());
		Window.scrollTo(0, container.getElement().getAbsoluteTop()-50);
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
				Path viewPath = ViewFullPath.path;
				viewPath.setDescription(editor.getData());
				TripShare.dataService.updatePart(viewPath, new AsyncCallback<Path>() {
					@Override
					public void onSuccess(Path result) {
						TripShare.loadBox.hide();
						ViewFullPath.path = result;
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
	
	public void cancelEdit() {
		//remove content
		DOM.getElementById("pathDescription").setInnerHTML("");
		DOM.getElementById("pathEditTool").setInnerHTML("");
		//add static content
		HTMLPanel newContainer = new HTMLPanel("");
		RootPanel.get("pathDescription").add(newContainer);
		newContainer.getElement().setInnerHTML(ViewFullPath.path.getDescription());
		Window.scrollTo(0, newContainer.getElement().getAbsoluteTop()-50);
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
	
}
