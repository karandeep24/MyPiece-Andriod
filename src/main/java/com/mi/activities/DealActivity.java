package com.mi.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mi.asytask.AddDealViewAsyncTask;
import com.mi.asytask.DashBoardAsyncTask;
import com.mi.common.Constant;
import com.mi.metadata.DashboardMetadata;
import com.mi.test.mypiece.R;
import com.mi.webapi.ConnectionDetector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by karandsingh on 16-09-09.
 */
public class DealActivity extends AppCompatActivity implements View.OnClickListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    static Button btnPurchase;
    Intent dealIntent;
    int dealPosition;
    static DashboardMetadata dashboardMetadata = new DashboardMetadata();
    Context context;
    ProgressDialog progressDialog;
    public static DealActivity instance;
    TextView textview_fineprint, textView1_hour, textview_address, textview_City, textview_Postal, restroName;
    LinearLayout backButton;
    MyCount_alert counter_alert;
    GoogleMap myMap;
    SharedPreferences sharepreferences, langPreferences;
    String sUserId, route, deal_id_notification, sLang;
    boolean chkBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        instance = this;

        setContentView(R.layout.deal_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        initialize();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_image_imageviews);
        mapFragment.getMapAsync(this);

//        face = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
//        face_bold = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");
//        face_proxinova_bold = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");
//        face_new_proxinova_extra_bold = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");
//        face_new_proxinova_regular = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");
//        face_new_proxinova_light = Typeface.createFromAsset(getAssets(),"fonts/Raleway-Bold.ttf");
//
//        textView1_hour.setTypeface(face_new_proxinova_regular);
//        textview_address.setTypeface(face_new_proxinova_regular);
//        textview_City.setTypeface(face_new_proxinova_regular);
//        textview_Postal.setTypeface(face_new_proxinova_regular);
//        textview_fineprint.setTypeface(face_new_proxinova_regular);

        context = getApplicationContext();

        sharepreferences = context.getSharedPreferences("User_id", Context.MODE_PRIVATE);
        sUserId = sharepreferences.getString("user_id_value", "");

        langPreferences = this.getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        sLang = langPreferences.getString("Language", "en");

        dealIntent = getIntent();
        route = dealIntent.getStringExtra("NOTIFICATION_ROUTE");

        if (route == null) {

            chkBack = false;
            dealPosition = dealIntent.getIntExtra("DealPosition", 0);
            dashboardMetadata = DashBoardAsyncTask.dashboardVector.get(dealPosition);

        } else if (route.equals("NOTIFICATION_DEAL"))
        {

            chkBack = true;
            deal_id_notification = dealIntent.getStringExtra("NOTIFICATION_DEAL_ID");
            dashboardMetadata = findSpecificDeal();
        }

        try {

            String dataString = getDataString(dashboardMetadata.getiCampaignId(), sUserId);
            String url = Constant.ADD_VIEW;

            AddDealViewAsyncTask asyncTask = new AddDealViewAsyncTask(context, url, dataString);
            asyncTask.execute();

        }
        catch (Exception ex)
        {

        }

        display(dashboardMetadata);

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnPurchase.setText("");
                if(ProceedCheckout.textProceedCheckout == null)
                {
                }
                else
                {
                    ProceedCheckout.textProceedCheckout.setVisibility(View.VISIBLE);
                }

                Intent intent = new Intent(DealActivity.this, ProceedCheckout.class);
                intent.putExtra("DealPosition", dealPosition);
                startActivity(intent);
            }
        });
    }

    public DashboardMetadata findSpecificDeal()
    {
        DashboardMetadata dashboardMetadata = null;

        for(int i = 0; i < DashBoardAsyncTask.dashboardVector.size() ; i++)
        {
            String id = DashBoardAsyncTask.dashboardVector.get(i).getiCampaignId();
            if(id.equals(deal_id_notification))
            {
                dashboardMetadata = DashBoardAsyncTask.dashboardVector.get(i);
            }
        }

        return dashboardMetadata;
    }

    public String getDataString(String id, String userId)
    {
        String Return = "{\n\t";
        Return = Return + "\"CampaignId\":" + "\"" + id + "\",";
        Return = Return + "\n\t";
        Return = Return + "\"UserId\":" + "\"" + userId + "\"";
        Return = Return + "\n}";

        return Return;
    }

    public void initialize() {
        textview_fineprint = (TextView) findViewById(R.id.textview_fineprint);
        textView1_hour = (TextView) findViewById(R.id.textView1_hour);
        textview_address = (TextView) findViewById(R.id.textview_address);
        textview_City = (TextView) findViewById(R.id.textview_City);
        textview_Postal = (TextView) findViewById(R.id.textview_Postal);
        restroName = (TextView) findViewById(R.id.toolbar_title);

        btnPurchase = (Button) findViewById(R.id.btnpurchase);

    }

    public void display(DashboardMetadata dashboardMetadata) {

        String address = dashboardMetadata.getvAddress();

        textview_fineprint.setText(dashboardMetadata.getvFinePrint());
        textview_address.setText(address);
        textview_City.setText(dashboardMetadata.getvCity());
        textview_Postal.setText(dashboardMetadata.getvZip());
        restroName.setText(dashboardMetadata.getvRestaurantName());

        timerDisplay(dashboardMetadata.getvEndTime());
    }

    public void timerDisplay(String endTime) {
        try {

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date currDate = new Date();
            Date endDate = dateFormat.parse(endTime);
            Date startDate = dateFormat.parse(dateFormat.format(currDate));

            long difference = endDate.getTime() - startDate.getTime();

            counter_alert = new MyCount_alert(difference, 1000);
            counter_alert.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String formatTime(long millis) {

        String output = "";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours;

        String secondsD = String.valueOf(seconds);
        String minutesD = String.valueOf(minutes);
        String hoursD = String.valueOf(hours);

        if (seconds < 10)
            secondsD = "0" + seconds;
        if (minutes < 10)
            minutesD = "0" + minutes;

        if (hours < 10)
            hoursD = "0" + hours;

        output = hoursD + ":" + minutesD + ":" + secondsD;

        return output;
    }

    @Override
    public void onClick(View v) {

        if(v == backButton)
        {
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            finish();
        }
    }

    public class MyCount_alert extends CountDownTimer {
        Context mContext;

        public MyCount_alert(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            textView1_hour.setText(formatTime(millisUntilFinished));

        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
        }
    }

    @Override
    public void onBackPressed() {

        ConnectionDetector cd = new ConnectionDetector(DealActivity.this);
        final Boolean isInternetPresent = cd.isConnectingToInternet();

        if(isInternetPresent) {

            if (chkBack) {

                chkBack = true;

                System.out.println("::::::::::::::onBACK FROM ACC::::::::::::::");
                SharedPreferences locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

                String latitude = locationPrefs.getString("LATITUDE", "");
                String longitude = locationPrefs.getString("LONGITUDE", "");

                progressDialog = ProgressDialog.show(DealActivity.this, "", "Please wait.....");

                String URL = Constant.GET_ALL_DEALS + "lang=" + sLang + "&latitude=" + latitude + "&longitude=" + longitude + "&radius=50";
                DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "ACCOUNT");
                asyncTask.execute();
            }
            else
            {
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

                finish();
            }
        }
        else
        {
            Toast.makeText(DealActivity.this, getString(R.string.No_Connection), Toast.LENGTH_SHORT).show();
        }


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

    String lat_map,long_map;
    LatLng resturant_latlong;
    Marker resturant;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.getUiSettings().setCompassEnabled(true);
        myMap.getUiSettings().setMyLocationButtonEnabled(true);
        myMap.getUiSettings().setRotateGesturesEnabled(true);
        myMap.getUiSettings().setScrollGesturesEnabled(true);
        myMap.getUiSettings().setTiltGesturesEnabled(true);
        myMap.getUiSettings().setZoomGesturesEnabled(true);
        //or myMap.getUiSettings().setAllGesturesEnabled(true);
        myMap.setTrafficEnabled(true);

        lat_map = dashboardMetadata.getvLatitude();
        long_map = dashboardMetadata.getvLongitude();
        resturant_latlong = new LatLng(Double.parseDouble(lat_map), Double.parseDouble(long_map));
        resturant = myMap.addMarker(new MarkerOptions()
                .position(resturant_latlong));

        CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(resturant_latlong,15);

        myMap.animateCamera(cu);

    }
    @Override
    public boolean onMarkerClick(final Marker marker) {
        double latitude = Double.parseDouble(lat_map);
        double longitude = Double.parseDouble(long_map);
        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+myMap.getMyLocation().getLatitude()+","+myMap.getMyLocation().getLatitude()+"&daddr="+latitude+","+longitude;
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(Intent.createChooser(intent, "Select an application"));
//        double latitude = Double.parseDouble(lat_map);
//        double longitude = Double.parseDouble(long_map);
//        String label = dashboardMetadata.getvRestaurantName();
//        String uriBegin = "geo:" + latitude + "," + longitude;
//        String query = latitude + "," + longitude + "(" + label + ")";
//        String encodedQuery = Uri.encode(query);
//        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
//        Uri uri = Uri.parse(uriString);
//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
//        startActivity(intent);
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }
}