package com.mcgoldricksolutions.hackathon2014.adoptablock;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Singleton, Holder of batch trash tracking data.
 * 
 * @author dirtbag
 *
 */
public class TrashTracking {

	private JSONObject root;	
	private JSONArray points;
	private JSONObject realtime;
	
	public static TrashTracking newInstance() {
		return new TrashTracking();
	}
	
	private TrashTracking() {
		root = new JSONObject();
		points = new JSONArray();
		root.put("points", points);
		
		realtime = new JSONObject();
	}
	
	public void setUsername(String username) {		
		root.put("username", username);
		root.put("hashID",Hashing.Hash(username));
		realtime.put("username", username);
		realtime.put("hashID",Hashing.Hash(username));
	}

	public void addPoint(double lat, double lon, String epoch) {
		
		JSONObject point = new JSONObject();
		point.put("lat", new Double(lat));
		point.put("long", new Double(lon));
		point.put("epoch", epoch);
		points.add(point);
		
		setRealtime(lat, lon, epoch); // keep realtime up to date
	}

	public void setCategory(String category) {
		root.put("userCategory", category);
	}
	public void setCommment(String comment) {
		root.put("comments", comment);
	}
	public void setBuckets(float buckets) {
		root.put("buckets", buckets);
	}

	public String getBatch() {		
		return root.toString();
	}
	
	private void setRealtime(double lat, double lon, String epoch) {
		realtime.put("lat", new Double(lat));
		realtime.put("long", new Double(lon));
		realtime.put("epoch", epoch);
	}
	
	public String getRealtime() {
		return realtime.toString();
	}
}
