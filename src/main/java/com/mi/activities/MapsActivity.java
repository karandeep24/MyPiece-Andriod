package com.mi.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mi.asytask.DashBoardAsyncTask;
import com.mi.common.Constant;
import com.mi.location.GPSTracker;
import com.mi.test.mypiece.R;

public class MapsActivity extends AppCompatActivity implements
        GoogleMap.OnInfoWindowClickListener,OnMapReadyCallback {

    Marker[] markers;
    Intent intent;
    Double DEFAULT_LAT = 45.4957;
    Double DEFAULT_LONG = -73.5782;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    final int RQS_GooglePlayServices = 1;
    GoogleMap myMap;

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this, DealActivity.class);
        intent.putExtra("DealPosition", (Integer.parseInt(marker.getTitle())));
        startActivity(intent);

    }


    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoContents(final Marker marker) {
            LinearLayout infowindowll = (LinearLayout)myContentsView.findViewById(R.id.infowindowll);
            infowindowll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
            tvTitle.setText(DashBoardAsyncTask.dashboardVector.get(Integer.parseInt(marker.getTitle())).getdPrice());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(DashBoardAsyncTask.dashboardVector.get(Integer.parseInt(marker.getTitle())).getvRestaurantName());

            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
           // return;
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }

        if(isPermission())
        {
            myMap.setMyLocationEnabled(true);

        }
        //myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(true);
        myMap.getUiSettings().setRotateGesturesEnabled(true);
        myMap.getUiSettings().setScrollGesturesEnabled(true);
        myMap.getUiSettings().setTiltGesturesEnabled(true);
        myMap.getUiSettings().setZoomGesturesEnabled(true);
        //or myMap.getUiSettings().setAllGesturesEnabled(true);
        myMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        myMap.setOnInfoWindowClickListener(this);
        myMap.setTrafficEnabled(true);
//        myMap.setOnMapLongClickListener(this);
        markers = new Marker[DashBoardAsyncTask.dashboardVector.size()];
        for (int i = 0; i < DashBoardAsyncTask.dashboardVector.size(); i++) {
            String key = DashBoardAsyncTask.dashboardVector.get(i).getiCampaignId();
            String lat_map = DashBoardAsyncTask.dashboardVector.get(i).getvLatitude();
            String long_map = DashBoardAsyncTask.dashboardVector.get(i).getvLongitude();
            LatLng resturant_latlong = new LatLng(Double.parseDouble(lat_map), Double.parseDouble(long_map));
            Marker resturant = myMap.addMarker(new MarkerOptions()
                    .position(resturant_latlong)
                    .title(i + ""));
            markers[i]= resturant;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();
//        int padding = 15; // offset from edges of the map in pixels
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//        myMap.animateCamera(cu);

        myMap.moveCamera( CameraUpdateFactory.newLatLngZoom(new LatLng(DEFAULT_LAT , DEFAULT_LONG) , 15.0f));



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == 1){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                checkForLocation();
                Toast.makeText(this, getString(R.string.Toast_Location_Granted),Toast.LENGTH_LONG).show();
            }else{

                setDefaultLoc();

                Toast.makeText(this, getString(R.string.Toast_Location_Denied),Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isPermission() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }


    public void setDefaultLoc()
    {
        SharedPreferences shared_loc = getSharedPreferences("Location", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor_loc = shared_loc.edit();

        String latitude_location = String.valueOf(Constant.DEFAULT_LAT);
        editor_loc.putString("LATITUDE", latitude_location);
        editor_loc.commit();

        String longitude_location = String.valueOf(Constant.DEFAULT_LONG);
        editor_loc.putString("LONGITUDE", longitude_location);
        editor_loc.commit();

    }

    private void checkForLocation()
    {
        try {
            GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);
            if (gpsTracker.canGetLocation()) {
//                MyPreferences.setPref(SplashScreenActivity.this, MyPreferences.LAST_LATITUDE, "" + gpsTracker.getLatitude());
//                MyPreferences.setPref(SplashScreenActivity.this, MyPreferences.LAST_LONGITUDE, "" + gpsTracker.getLongitude());

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                SharedPreferences shared_loc = getSharedPreferences("Location", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_loc = shared_loc.edit();

                Double distance = distance(Double.parseDouble(Constant.DEFAULT_LAT), latitude,
                        Double.parseDouble(Constant.DEFAULT_LONG), longitude);

                if(distance < 50000)
                {
                    String latitude_location = String.valueOf(latitude);
                    if(latitude_location.equals("0.0"))
                    {
                        latitude_location = Constant.DEFAULT_LAT;
                    }

                    editor_loc.putString("LATITUDE", latitude_location);
                    editor_loc.commit();

                    String longitude_location = String.valueOf(longitude);
                    if(longitude_location.equals("0.0"))
                    {
                        longitude_location = Constant.DEFAULT_LONG;
                    }
                    editor_loc.putString("LONGITUDE", longitude_location);
                    editor_loc.commit();
                }
                else
                {
                    setDefaultLoc();
                }

            }
        }
        catch (Exception e)
        {

        }
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2)
    {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = 0;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

}
