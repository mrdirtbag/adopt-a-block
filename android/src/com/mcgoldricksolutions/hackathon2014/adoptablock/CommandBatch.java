package com.mcgoldricksolutions.hackathon2014.adoptablock;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Batch data to server in format: 
 * 
 * content-type:  ‘application/json’
{
    "username": "mike",
    "hashID": "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
    "points": [{
        "lat": 34.412939,
        "long": -119.859315,
        "epoch": 1403756390000
    }, {
        "lat": 34.412939,
        "long": -119.8594062,
        "epoch": 1403756395000
    }, {
        "lat": 34.412939,
        "long": -119.8594952,
        "epoch": 1403756400000
    }],
    "userCategory": "CSW",
    "comments": "I had too much fun",
    "buckets": 1.25
}

 * @author dirtbag
 *
 */
public class CommandBatch extends Command {

	private static final String LOGGER_TAG = "CommandBatch";	

	/**
	 * URL to POST trash pickup finished data.
	 */
	protected static String BATCH_TRASHPICKUP_COMPLETE_URL = "http://adopt-a-block.herokuapp.com/ivCollection";

	/**
	 * json attribute
	 */
	protected String mJSON;
	
	/**
	 * Constructor
	 * @param context
	 * @param name
	 * @param json
	 */
	public CommandBatch(Context context, String json) {
		super(context, LOGGER_TAG);
		mJSON = json;
	}

	/**
	 * Standard Command overwrite function
	 */
	@Override
	public void execute() {
		Log.e(this.LOGGER_TAG, "Start HttpHelper");
		try {
			HttpHelper.requestUrl(BATCH_TRASHPICKUP_COMPLETE_URL, mJSON);
		} catch (HttpHelperRequestException e) {
			Log.e(LOGGER_TAG, "Failed on Batch update.");
			e.printStackTrace();
		}
		Log.e(LOGGER_TAG, "End HttpHelper");
	}
	public String getJSON() {
		JSONObject obj = new JSONObject();
		obj.put("username", "Patrick");
		obj.put("hashID", "dlkfajdflkajdflajdflkajdflakdfa");
		JSONArray points = new JSONArray();

		JSONObject point1 = new JSONObject();
		point1.put("lat", new Double(34.412939));
		point1.put("long", new Double(-119.859315));
		point1.put("epoch", "120375690000");

		JSONObject point2 = new JSONObject();
		point2.put("lat", new Double(34.500000));
		point2.put("long", new Double(-119.959365));
		point2.put("epoch", "120375790000");

		JSONObject point3 = new JSONObject();
		point3.put("lat", new Double(34.600000));
		point3.put("long", new Double(-119.859400));
		point3.put("epoch", "120375890000");

		points.add(point1);
		points.add(point2);
		points.add(point3);
		obj.put("points", points);

		obj.put("userCategory", "CSW");
		obj.put("comments", "I am still having fun!");
		obj.put("buckets", 1.25);

		return obj.toString();
	}
}
