package com.mi.location;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class Location_Speed_Services extends Service {//implements GPSCallback {
//	public  GPSManager gpsManager = null;
//	public  double getLatitude ;
//	public  double getLongitude;
	public  static int i = 0;
	
	public void onCreate(Bundle savedInstanceState) {

	}

	/*@Override
	public void onGPSUpdate(Location location) {
		getLatitude=location.getLatitude();
		getLongitude=location.getLongitude();
		
	*//*	if(i > 1)
		{
			Intent i1 = new Intent("Update");
			i1.putExtra("type", "NO");
			sendBroadcast(i1);
		}*//*
		
	}*/

	@Override
	public void onDestroy() {
		/*gpsManager.stopListening();
		gpsManager.setGPSCallback(null);

		gpsManager = null;*/

		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		/*gpsManager = new GPSManager();

		gpsManager.startListening(getApplicationContext());
		gpsManager.setGPSCallback(this);
*/
		
		return startId;
	}

}