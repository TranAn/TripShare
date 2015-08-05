package com.born2go.shared.embedclass;

import com.born2go.shared.embedclass.Journey.Point;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class ClientTransform {
	
	public String journeyToString(Journey j) {
		JSONObject o = new JSONObject();
		JSONArray jlocates = new JSONArray();
		for(int i = 0; i < j.locates.size(); i++) {
			JSONObject locate = new JSONObject();
			locate.put("addressName", new JSONString(j.locates.get(i).getAddressName()));
			locate.put("latitude", new JSONNumber(j.locates.get(i).getLatitude()));
			locate.put("longitude", new JSONNumber(j.locates.get(i).getLongitude()));
			jlocates.set(i, locate);
		}
		JSONArray jdirections = new JSONArray();
		for(int i = 0; i < j.directions.size(); i++) {
			JSONObject point = new JSONObject();
			point.put("x", new JSONNumber(j.directions.get(i).getX()));
			point.put("y", new JSONNumber(j.directions.get(i).getY()));
			jdirections.set(i, point);
		}
		o.put("locates", jlocates);
		o.put("directions", jdirections);
		return o.toString();
	}
	
	public Journey stringToJourney(String jsonJourney) {
		JSONValue v = JSONParser.parseStrict(jsonJourney);
		Journey j = new Journey();
		JSONArray jlocates = v.isObject().get("locates").isArray();
		for(int i = 0; i < jlocates.size(); i++) {
			Locate locate = new Locate();
			locate.setAddressName(jlocates.get(i).isObject().get("addressName").toString());
			locate.setLatitude(jlocates.get(i).isObject().get("latitude").isNumber().doubleValue());
			locate.setLongitude(jlocates.get(i).isObject().get("longitude").isNumber().doubleValue());
			j.locates.add(locate);
		}
		JSONArray jdirections = v.isObject().get("directions").isArray();
		for(int i = 0; i < jdirections.size(); i++) {
			Point point = new Point();
			point.setX(jdirections.get(i).isObject().get("x").isNumber().doubleValue());
			point.setY(jdirections.get(i).isObject().get("y").isNumber().doubleValue());
			j.directions.add(point);
		}
		return j;
	}
	
	public String locateToString(Locate l) {
		JSONObject o = new JSONObject();
		o.put("addressName", new JSONString(l.addressName));
		o.put("latitude", new JSONNumber(l.latitude));
		o.put("longitude", new JSONNumber(l.longitude));
		return o.toString();
	}
	
	public Locate stringToLocate(String jsonLocate) {
		JSONValue v = JSONParser.parseStrict(jsonLocate);
		Locate l = new Locate();
		l.addressName = v.isObject().get("addressName").toString();
		l.latitude = v.isObject().get("latitude").isNumber().doubleValue();
		l.longitude = v.isObject().get("longitude").isNumber().doubleValue();
		return l;
	}
	
	public String posterToString(Poster p) {
		JSONObject o = new JSONObject();
		o.put("userID", new JSONNumber(p.userID));
		o.put("userName", new JSONString(p.userName));
		o.put("userImg", new JSONString(p.userImg));
		return o.toString();
	}
	
	public Poster stringToPoster(String jsonPoster) {
		JSONValue v = JSONParser.parseStrict(jsonPoster);
		Poster p = new Poster();
		p.userID = Long.valueOf(v.isObject().get("userID").toString());
		p.userName = v.isObject().get("userName").toString();
		p.userImg = v.isObject().get("userImg").toString();
		return p;
	}
	
}
