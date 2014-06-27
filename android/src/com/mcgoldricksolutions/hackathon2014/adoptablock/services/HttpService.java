package com.mcgoldricksolutions.hackathon2014.adoptablock.services;

import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mcgoldricksolutions.hackathon2014.adoptablock.HttpHelper;
import com.mcgoldricksolutions.hackathon2014.adoptablock.HttpHelperRequestException;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Service to make HTTP calls in the background.
 * 
 * @author dirtbag
 *
 */
public class HttpService extends Service {

	/**
	 * Log Tag
	 */
	private static final String LOG_TAG = "HttpService";
	
	/**
	 * URL to POST trash pickup finished data.
	 */
	protected static String POST_TRASHPICKUP_COMPLETE_URL = "http://adopt-a-block.herokuapp.com/ivCollection";

    Thread mFetcherThread;

    /*
     * Error status
     */
    protected String mErrorMessage = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(LOG_TAG, "Received start id " + startId + ": " + intent);
        
        if (mFetcherThread != null && mFetcherThread.isAlive()) {
                Log.d(LOG_TAG, "Killing existing fetch thread, so we can start a new one");
                mFetcherThread.interrupt();
        }
        
        mFetcherThread = new Fetcher();
        mFetcherThread.start();

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
	    // WE have no reason to exist!
        return START_STICKY;
    	
    }

    @Override
    public void onDestroy() {
    	        
        if (mFetcherThread.isAlive()) {
                mFetcherThread.interrupt();
        }

        Log.d(LOG_TAG, "Stopped Service");
    }



    
	/**
	 * Don't allow binding.
	 */
    @Override
    public IBinder onBind(Intent intent) {
            return null;
    }
        
    /**
     * Start separate thread to do lookup of site data.  
     */
    public class Fetcher extends Thread {

        public Fetcher() {
        }

        public void run() {
			try {
				HttpHelper.requestUrl(POST_TRASHPICKUP_COMPLETE_URL, getJSON());
			} catch (HttpHelperRequestException e) {

				Log.e("Main", "Error on Post: " + getJSON());
				e.printStackTrace();
			}
 	
        }

        private void showError(Exception ex) {
                Log.e(LOG_TAG, "Error updating site", ex);
        }
        
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
		point2.put("lat", new Double(34.412939));
		point2.put("long", new Double(-119.859315));
		point2.put("epoch", "120375690000");

		JSONObject point3 = new JSONObject();
		point3.put("lat", new Double(34.412939));
		point3.put("long", new Double(-119.859315));
		point3.put("epoch", "120375690000");

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

