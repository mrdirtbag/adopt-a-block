package com.mcgoldricksolutions.hackathon2014.adoptablock;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * Send realtime GPS info in format:
 * 	{
 	    "username": "mike",
 	    "hashID": "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
	    "lat": 34.412939,
	    "long": -119.8594952,
	    "epoch": 1403756400000
	}
 *
 * @author dirtbag
 *
 */
public class CommandRealtime extends Command {

	private static final String LOGGER_TAG = "CommandRealtime";	

	/**
	 * URL to POST trash pickup realtime data.
	 */
	protected static String REALTIME_TRASHPICKUP_COMPLETE_URL = "http://adopt-a-block.herokuapp.com/realtime";

	/**
	 * json attribute
	 */
	protected String mJSON;

	/**
	 * Constructor 
	 *
	 * @param context
	 * @param json
	 */
	public CommandRealtime(Context context, String json) {
		super(context, LOGGER_TAG);
		mJSON = json;
	}

	@Override
	public void execute() {
		Log.e(this.LOGGER_TAG, "Start Realtime update");
		try {
			HttpHelper.requestUrl(REALTIME_TRASHPICKUP_COMPLETE_URL, mJSON);
		} catch (HttpHelperRequestException e) {
			Log.e(LOGGER_TAG, "Failed on Realtime update.");
			e.printStackTrace();
		}
		Log.e(LOGGER_TAG, "End Realtime update");

	}
	public String getJSON() {
		JSONObject obj = new JSONObject();
		obj.put("username", "Patrick");
		obj.put("hashID", "dlkfajdflkajdflajdflkajdflakdfa");
		obj.put("lat", new Double(34.412939));
		obj.put("long", new Double(-119.859315));
		obj.put("epoch", "120375690000");

		return obj.toString();
	}
}
