package com.born2go.client.widgets;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import com.google.web.bindery.autobean.shared.AutoBeanUtils;

public class Create_HandlerJsonListFriends {
	
	MyFactory factory;

	public interface Friend {
		String getId();
		String getName();
		void setId(String id);
		void setName(String name);
	}

	public interface ListFriends {
	    void setListFriends(List<Friend> friends);
	    List<Friend> getListFriends();
	}

	// Declare the factory type
	interface MyFactory extends AutoBeanFactory {
		AutoBean<Friend> friend();
		AutoBean<ListFriends> list_friends();
	}

	public Create_HandlerJsonListFriends() {
		// Instantiate the factory
		factory = GWT.create(MyFactory.class);
		// In non-GWT code, use AutoBeanFactorySource.create(MyFactory.class);
	}
	
	Friend makeFriend() {
	    // Construct the AutoBean
	    AutoBean<Friend> friend = factory.friend();
	    // Return the Person interface shim
	    return friend.as();
	}
	
	ListFriends makeListFriends() {
	    // Construct the AutoBean
	    AutoBean<ListFriends> listfriends = factory.list_friends();
	    // Return the Person interface shim
	    return listfriends.as();
	}
	
	String serializeToJson(ListFriends list_friends) {
	    // Retrieve the AutoBean controller
	    AutoBean<ListFriends> bean = AutoBeanUtils.getAutoBean(list_friends);
	    return AutoBeanCodex.encode(bean).getPayload();
	}

	ListFriends deserializeFromJson(String jsonListFriends) {
		AutoBean<ListFriends> bean = AutoBeanCodex.decode(factory, ListFriends.class, jsonListFriends);
		return bean.as();
	}
	
}
