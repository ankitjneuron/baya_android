package com.baya.Helper;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;



public class GPSTracker extends Service implements LocationListener {

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude = 0.0; // latitude
	double longitude = 0.0; // longitude
	private SharedPreferences preferences;

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 5000;// 1000 * 60 * 1; // 1
															// minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v("LogView", "onCreate");
		getLocation();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v("LogView", "onStartCommand");
		return super.onStartCommand(intent, flags, startId);

	}

	public Location getLocation() {
		try {
			Log.v("LogView", "getLocation");

			locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

			this.canGetLocation = true;

			if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				Log.v("LogView", "Network Enabled...");

				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

				if (locationManager != null) {
					Log.v("LogView", "Network locationManager is not null...");
					location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						Log.v("LogView", "Network location is not null...");
						latitude = location.getLatitude();
						longitude = location.getLongitude();
						Log.v("LogView", "NET Lat :" + latitude);
						Log.v("LogView", "NET Long :" + longitude);
						// onLocationChanged(location);
					}
				}
			}

			// if GPS Enabled get lat/long using GPS Services
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Log.v("LogView", "GPS Enabled...");

				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

				if (locationManager != null) {
					Log.v("LogView", "GPS locationManager is not null...");
					location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location != null) {
						Log.v("LogView", "GPS location is not null...");
						latitude = location.getLatitude();
						longitude = location.getLongitude();

						Log.v("LogView", "GPS Lat :" + latitude);
						Log.v("LogView", "GPS Long :" + longitude);
						// onLocationChanged(location);
					}

				} else {
					Log.v("LogView", "location is null...");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.v("LogView", "in onLocationChanged...");

		 latitude = location.getLatitude();
		 longitude = location.getLongitude();
		Constants.latitude = latitude;
		Constants.logitude = longitude;
		System.out.println("vvvvvvvvvvvvv" + latitude+"  "+longitude);


	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
