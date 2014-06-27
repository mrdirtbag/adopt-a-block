package com.mcgoldricksolutions.hackathon2014.adoptablock;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View.OnClickListener;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity 
implements OnClickListener, LocationListener {

	public static String LOGGER_TAG = "MainActivity";
	
	public static int UPDATE_INTERVAL = 5;
	
	private LocationManager locationManager;
	private String mProvider;
	private TrashTracking mTrashTracking;
	private int updateCounter = 0;
	
	protected double lat = 0.0;
	protected double lon = 0.0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		StartStopButton btnStartStop = (StartStopButton)this.findViewById(R.id.btnStartStop);
		btnStartStop.setBackgroundResource(R.drawable.start_button);
		btnStartStop.setOnClickListener(this);
		
		Button btnSubmit = (Button) this.findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		
		// Get the location manager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		// Define the criteria how to select the location provider -> use
		// default
		Criteria criteria = new Criteria();
		mProvider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(mProvider);

		// Initialize the location fields
		if (location != null) {
			Log.e(LOGGER_TAG, "Provider " + mProvider + " has been selected.");
			onLocationChanged(location);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v) {
		Log.e(LOGGER_TAG, "Click");

		if (v.getId() == R.id.btnStartStop) {
			Log.e(LOGGER_TAG,"Button");
			StartStopButton btnStartStop = (StartStopButton) this.findViewById(R.id.btnStartStop);
			btnStartStop.started = !btnStartStop.started;
			
			Log.e(LOGGER_TAG, "State: " + btnStartStop.started);

			// Just Started Running
			if(btnStartStop.started){
				btnStartStop.setBackgroundResource(R.drawable.stop_button);
				mTrashTracking = TrashTracking.newInstance();
				EditText tvUser = (EditText) this.findViewById(R.id.tvUser);
				mTrashTracking.setUsername(tvUser.getText().toString());
				locationManager.requestLocationUpdates(mProvider, 400, 1, this);

			} else { // Just Stopped Running
				btnStartStop.setBackgroundResource(R.drawable.start_button);
				locationManager.removeUpdates(this);
				btnStartStop.setVisibility(1);
			}
			
			//Submit Button pressed
		} else if (v.getId() == R.id.btnSubmit) {
			Log.e(LOGGER_TAG,mTrashTracking.toString());
			EditText tvBuckets = (EditText) this.findViewById(R.id.tvBuckets);
			String buckets = tvBuckets.getText().toString();
			if(buckets == null || buckets.length() == 0){
				buckets = "0.0";
			}
			mTrashTracking.setBuckets(Float.parseFloat(buckets));
			Command batch = new CommandBatch(this, mTrashTracking.getBatch());
			Command commands[] = { batch };
			CommandRunner runner = new CommandRunner( commands );
			runner.executeInBackground();
			
			Log.e(LOGGER_TAG, "Clicked Submit Button");
		}		
			
		
	}
	@Override
	public void onLocationChanged(Location location) {
		Log.e(LOGGER_TAG, "DATA POINT");
		lat =  (location.getLatitude());
		lon =  (location.getLongitude());
		TextView tvLat = (TextView) this.findViewById(R.id.lblLat);
		TextView tvLon = (TextView) this.findViewById(R.id.lblLong);
		tvLat.setText(String.valueOf(lat));
		tvLon.setText(String.valueOf(lon));
		if(mTrashTracking != null) {
			mTrashTracking.addPoint(lat, lon, "" + new Date().getTime());

			StartStopButton btnStartStop = (StartStopButton) this.findViewById(R.id.btnStartStop);
			updateCounter++;
			
			if(btnStartStop.started && updateCounter % this.UPDATE_INTERVAL == 0) {
				Command realtime = new CommandRealtime(this,mTrashTracking.getRealtime());
				Command commands[] = { realtime };
				CommandRunner runner = new CommandRunner( commands );
				runner.executeInBackground();
				Log.e(LOGGER_TAG, "ADD DATA POINT: " + updateCounter);

			}
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {
	}

}
