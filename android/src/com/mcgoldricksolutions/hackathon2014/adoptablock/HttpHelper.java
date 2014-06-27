package com.mcgoldricksolutions.hackathon2014.adoptablock;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Scanner;

import android.os.Build;
import android.util.Log;

/**
 * POST (JSON) to hackathon endpoint for trash cleanup application.
 * 
 * Code mostly from: http://ihofmann.wordpress.com/2013/01/23/android-sending-post-requests-with-parameters/
 * Modified to my preference.
 * 
 * @author dirtbag
 *
 */
public class HttpHelper {

	public static final int TIMEOUT = 60000;
	public static final String METHOD_POST = "POST";
	private static final String LOGGER_TAG = "HttpHelper";	

	public static String requestUrl(String url, String json)
	    throws HttpHelperRequestException {
	    if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
	        Log.i(LOGGER_TAG, "Requesting service: " + url);
	    }
	     
	    disableConnectionReuseIfNecessary();
	 
	    HttpURLConnection connection = null;
	    try {
	        // create connection
	    	connection = createConnection(url);
	    	
            connection.setFixedLengthStreamingMode(
            		json.getBytes().length);
             
            //send the POST out
            PrintWriter out = new PrintWriter(connection.getOutputStream());
            out.print(json);
            out.close();
	         
	        // handle issues
	        int statusCode = connection.getResponseCode();
	        if (statusCode != HttpURLConnection.HTTP_OK) {
	            // throw some exception
	        	Log.e(LOGGER_TAG, "Status Code: " + statusCode);
	        }
         
	        // read response 
            InputStream in =
                new BufferedInputStream(connection.getInputStream());
            return getResponseText(in);
	        	         
	         
	    } catch (SocketTimeoutException e) {
	        // handle timeout
        	Log.e(LOGGER_TAG, "Socket Timedout.");
			throw new HttpHelperRequestException("Socket timed out.");

	    } catch (IOException e) {
	    	// handle IO
        	Log.e(LOGGER_TAG, "IOExeption");
			throw new HttpHelperRequestException("Connection failed, created IOException");
			
	    } finally {
	        if (connection != null) {
	        	connection.disconnect();
	        }
	    }
	}    
	
    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';

    private static HttpURLConnection createConnection(String stringURL) throws HttpHelperRequestException {
		  
		URL url = null;
		HttpURLConnection conn = null;

		try {
			url = new URL(stringURL);
		} catch (MalformedURLException e1) {
			throw new HttpHelperRequestException("url: " + stringURL + " created Malformed Exception.");
		}

		try {
			conn = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			throw new HttpHelperRequestException("url: " + url + " created IOException, upon call to openConnection().");
		}
        conn.setDoOutput(true); // set method to POST        
        try {
			conn.setRequestMethod(METHOD_POST);
		} catch (ProtocolException e) {
			throw new HttpHelperRequestException("HttpURLConnection failed method setRequestMethod with method: " + METHOD_POST);
		}
        conn.setInstanceFollowRedirects(false); // don't rely on native redirection support
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);
        conn.setRequestProperty("Content-Type", "application/json");
        return conn;
    }
    
    
    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK)
                < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }
     
    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        return new Scanner(inStream).useDelimiter("\\A").next();
    }
}
