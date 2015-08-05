package com.born2go.shared.embedclass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.born2go.shared.embedclass.Journey.Point;

public class ServerTransform {
	
	public String journeyToString(Journey j) {
		try {
			JSONObject o = new JSONObject();
			JSONArray jlocates = new JSONArray();
			for(int i = 0; i < j.locates.size(); i++) {
				JSONObject locate = new JSONObject();
				locate.put("addressName", j.locates.get(i).getAddressName());
				locate.put("latitude", new Double(j.locates.get(i).getLatitude()));
				locate.put("longitude", new Double(j.locates.get(i).getLongitude()));
				jlocates.put(i, locate);
			}
			JSONArray jdirections = new JSONArray();
			for(int i = 0; i < j.directions.size(); i++) {
				JSONObject point = new JSONObject();
				point.put("x", new Double(j.directions.get(i).getX()));
				point.put("y", new Double(j.directions.get(i).getY()));
				jdirections.put(i, point);
			}
			o.put("locates", jlocates);
			o.put("directions", jdirections);
			return o.toString();
		} catch(JSONException e) {
			System.out.println(e.getMessage());
			return "";
		}
	}
	
	public Journey stringToJourney(String jsonJourney) {
		try {
			JSONObject v = new JSONObject(jsonJourney);
			Journey j = new Journey();
			JSONArray jlocates = v.getJSONArray("locates");
			for(int i = 0; i < jlocates.length(); i++) {
				Locate locate = new Locate();
				locate.setAddressName(jlocates.getJSONObject(i).getString("addressName"));
				locate.setLatitude(jlocates.getJSONObject(i).getDouble("latitude"));
				locate.setLongitude(jlocates.getJSONObject(i).getDouble("longitude"));
				j.locates.add(locate);
			}
			JSONArray jdirections = v.getJSONArray("directions");
			for(int i = 0; i < jdirections.length(); i++) {
				Point point = new Point();
				point.setX(jdirections.getJSONObject(i).getDouble("x"));
				point.setY(jdirections.getJSONObject(i).getDouble("y"));
				j.directions.add(point);
			}
			return j;
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public String locateToString(Locate l) {
		try {
			JSONObject o = new JSONObject();
			o.put("addressName", l.addressName);
			o.put("latitude", new Double(l.latitude));
			o.put("longitude", new Double(l.longitude));
			return o.toString();
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			return "";
		}
	}
	
	public Locate stringToLocate(String jsonLocate) {
		try {
			JSONObject v = new JSONObject(jsonLocate);
			Locate l = new Locate();
			l.addressName = v.getString("addressName");
			l.latitude = v.getDouble("latitude");
			l.longitude = v.getDouble("longitude");
			return l;
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public String posterToString(Poster p) {
		try {
			JSONObject o = new JSONObject();
			o.put("userID", new Long(p.userID));
			o.put("userName", p.userName);
			o.put("userImg", p.userImg);
			return o.toString();
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			return "";
		}
	}
	
	public Poster stringToPoster(String jsonPoster) {
		try {
			JSONObject v = new JSONObject(jsonPoster);
			Poster p = new Poster();
			p.userID = v.getLong("userID");
			p.userName = v.getString("userName");
			p.userImg = v.getString("userImg");
			return p;
		} catch (JSONException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
