package com.born2go.client.widgets;

import java.util.ArrayList;
import java.util.List;

import com.born2go.client.widgets.Create_HandlerJsonListFriends.Friend;
import com.born2go.shared.Poster;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

public class CompanionPickTable extends DialogBox {

	private static FriendsTableUiBinder uiBinder = GWT
			.create(FriendsTableUiBinder.class);

	interface FriendsTableUiBinder extends UiBinder<Widget, CompanionPickTable> {
	}
	
	@UiField(provided = true)
	CellTable<Friend> talbeFriends = new CellTable<Friend>();
	@UiField HTMLPanel htmlFooter;
	
	private ListDataProvider<Friend> dataProviderTable = new ListDataProvider<Friend>();
	private List<Friend> listfriends = new ArrayList<Friend>();
	private List<Poster> listSelected = new ArrayList<Poster>();
	
	public interface Listener {
		void onSaveClick(List<Poster> friends);
	}
	
	private Listener listener;
	
	public void setListener(Listener listener) {
		this.listener = listener;
	}

	public CompanionPickTable() {
		setWidget(uiBinder.createAndBindUi(this));
		this.setStyleName("LoginDialog-Obj1");
		setGlassEnabled(true);
		setAutoHideEnabled(true);
		
		initTable();
	}
	
	void initTable() {
		talbeFriends.setAutoHeaderRefreshDisabled(true);
		Label lb = new Label("You have no friend use this app.");
		lb.getElement().setAttribute("style", "margin-top: 25px");
		talbeFriends.setEmptyTableWidget(lb);
		talbeFriends.setPageSize(1000);
		
		Column<Friend, String> clImage = new Column<Friend, String>(
				new ImageCell()) {
			@Override
			public String getValue(Friend object) {
				return "https://graph.facebook.com/"+ object.getId()+ "/picture?width=40&height=40";
			}
		};
		talbeFriends.addColumn(clImage);
		talbeFriends.setColumnWidth(clImage, "50px");
		
		TextColumn<Friend> clName = new TextColumn<Friend>() {
			@Override
			public String getValue(Friend object) {
				return object.getName();
			}
		};
		talbeFriends.addColumn(clName);
		
		final MultiSelectionModel<Friend> selectionModel = new MultiSelectionModel<Friend>();
		final Column<Friend, Boolean> clCheck = new Column<Friend, Boolean>(
				new CheckboxCell(false, true)) {
			@Override
			public Boolean getValue(Friend object) {
				Poster p = new Poster();
				p.setUserID(Long.valueOf(object.getId()));
				if (listSelected.contains(p))
					return true;
				else
					return false;
			}
		};
//		clCheck.setFieldUpdater(new FieldUpdater<Friend, Boolean>() {
//			@Override
//			public void update(int index, Friend object, Boolean value) {
//				selectionModel.setSelected(object, value);
//				String id = object.getId();
//				if (value == false) {
//					Poster p = new Poster();
//					p.setUserID(Long.valueOf(id));
//					listSelected.remove(p);
//				} else {
//					Poster p = new Poster();
//					p.setUserID(Long.valueOf(id));
//					p.setUserName(object.getName());
//					listSelected.add(p);
//				}
//			}
//		});
		talbeFriends.addColumn(clCheck);
		talbeFriends.setColumnWidth(clCheck, "20px");
		
		talbeFriends.addCellPreviewHandler(new Handler<Friend>() {
			@Override
			public void onCellPreview(CellPreviewEvent<Friend> event) {
				if (BrowserEvents.CLICK
						.equals(event.getNativeEvent().getType())) {
					if (event.getColumn() != 0) {
						String id = event.getValue().getId();
						Poster p = new Poster();
						p.setUserID(Long.valueOf(id));
						p.setUserName(event.getValue().getName());
						if (listSelected.contains(p))
							listSelected.remove(p);
						else
							listSelected.add(p);
						talbeFriends.redraw();
					}
				}
			}
		});
		
		dataProviderTable.addDataDisplay(talbeFriends);
		listfriends = dataProviderTable.getList();
	}
	
	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent event) {
		super.onPreviewNativeEvent(event);
		switch (event.getTypeInt()) {
		case Event.ONKEYDOWN:
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
				hide();
			}
			break;
		}
	}
	
	public void setListFriend(List<Friend> listfriends, List<Poster> selectedFriends) {
		this.listfriends.clear();
		this.listfriends.addAll(listfriends);
		this.listSelected.clear();
		this.listSelected.addAll(selectedFriends);
		if(listfriends.isEmpty()) {
			htmlFooter.setVisible(false);
			setAutoHideEnabled(true);
		}
		else {
			htmlFooter.setVisible(true);
			setAutoHideEnabled(false);
		}
	}
	
	@UiHandler("btnSave")
	void onBtnSaveClick(ClickEvent event) {
		if(listener != null)
			listener.onSaveClick(listSelected);
		hide();
	}
	
	@UiHandler("btnCancel")
	void onBtnCancelClick(ClickEvent event) {
		hide();
	}

}
