
package com.mi.location;

import android.location.Location;

public interface GPSCallback
{
	public abstract void onGPSUpdate(Location location);
}
