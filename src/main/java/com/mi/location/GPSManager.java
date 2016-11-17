
package com.mi.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

public class GPSManager
{
	private static final int gpsMinTime = 500;
	private static final int gpsMinDistance = 100;
	
	private  LocationManager locationManager = null;
	private LocationListener locationListener = null;
	private GPSCallback gpsCallback = null;
	
	public GPSManager()
	{	
		locationListener = new LocationListener()
		{
			@Override
			public void onLocationChanged(final Location location)
			{
				if (gpsCallback != null)
				{
					gpsCallback.onGPSUpdate(location);
					Location_Speed_Services.i=Location_Speed_Services.i++;
				}
			}
			
			@Override
			public void onProviderDisabled(final String provider)
			{
			}
			
			@Override
			public void onProviderEnabled(final String provider)
			{
			}
			
			@Override
			public void onStatusChanged(final String provider, final int status, final Bundle extras)
			{
				
			}
		};
	}
	
	public GPSCallback getGPSCallback()
	{
		return gpsCallback;
	}
	
	public void setGPSCallback(final GPSCallback gpsCallback)
	{
		this.gpsCallback = gpsCallback;
	}
	
	public void startListening(final Context context)
	{
		if (locationManager == null)
		{
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
		
		final Criteria criteria = new Criteria();
		
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setSpeedRequired(true);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		final String bestProvider = locationManager.getBestProvider(criteria, true);
		
		if (bestProvider != null && bestProvider.length() > 0)
		{
			locationManager.requestLocationUpdates(bestProvider, gpsMinTime,
					gpsMinDistance, locationListener);
		}
		else
		{
			final List<String> providers = locationManager.getProviders(true);
			
			for (final String provider : providers)
			{
				locationManager.requestLocationUpdates(provider, gpsMinTime,
						gpsMinDistance, locationListener);
			}
		}
	}
	
	public void stopListening()
	{
		try
		{
			if (locationManager != null && locationListener != null)
			{
				locationManager.removeUpdates(locationListener);
				
			}
			
			locationManager = null;
		}
		catch (final Exception ex)
		{
			
		}
	}
}
