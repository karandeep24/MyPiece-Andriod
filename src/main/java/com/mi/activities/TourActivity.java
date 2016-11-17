package com.mi.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.adapters.TourAdapter;
import com.mi.asytask.DashBoardAsyncTask;
import com.mi.common.Constant;
import com.mi.location.GPSTracker;
import com.mi.test.mypiece.R;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

/**
 * Created by karandsingh on 16-09-02.
 */
public class TourActivity extends FragmentActivity implements View.OnClickListener{

    private int Key;
    private int PageKey;
    String Parent, topic;
    private Context context;
    private Intent intent;
    public TextView tour_btn, enLanguageTxt, frLanguageTxt;
    String mode;
    LinearLayout close_layout, enLanguage, frLanguage;
    static boolean scrollFlag = true;
    boolean chkGPS = false;
    SharedPreferences locationPrefs;
    ProgressDialog progressDialog;
    boolean askGPS_Req = true;
    FragmentManager fm;
    LinearLayout one_dot, two_dot, three_dot, four_dot;
    int keyFragment;
    SharedPreferences langPreferences;
    String sLang;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.tour_activity);

        overridePendingTransition(R.anim.bottom_in, R.anim.static_anim);

        one_dot = (LinearLayout) findViewById(R.id.one_dot);
        two_dot = (LinearLayout) findViewById(R.id.two_dot);
        three_dot = (LinearLayout) findViewById(R.id.three_dot);
        four_dot = (LinearLayout) findViewById(R.id.four_dot);
        tour_btn = (TextView) findViewById(R.id.tour_btn);
        enLanguageTxt = (TextView) findViewById(R.id.enLanguageTxt);
        frLanguageTxt = (TextView) findViewById(R.id.frLanguageTxt);

        close_layout = (LinearLayout) findViewById(R.id.close_layout);
        close_layout.setOnClickListener(this);

        enLanguage = (LinearLayout) findViewById(R.id.enLanguage);
        frLanguage = (LinearLayout) findViewById(R.id.frLanguage);

        enLanguage.setOnClickListener(this);
        frLanguage.setOnClickListener(this);


        Intent intent = getIntent();
        mode = intent.getStringExtra("MODE");

        context =this;

        if(mode.equals("SPLASH"))
        {
            tour_btn.setVisibility(View.INVISIBLE);
        }
        else if( mode.equals("TOUR"))
        {
            tour_btn.setVisibility(View.VISIBLE);
        }

        langPreferences = this.getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        sLang = langPreferences.getString("Language", "en");

        if (sLang.equalsIgnoreCase("en")) {
            enLanguage.setBackgroundResource(R.drawable.custom_close_fill);
            enLanguageTxt.setTextColor(Color.WHITE);

            frLanguage.setBackgroundResource(R.drawable.custom_close_primary);
            frLanguageTxt.setTextColor(Color.parseColor("#0A6B91"));

        } else if (sLang.equalsIgnoreCase("fr")) {
            frLanguage.setBackgroundResource(R.drawable.custom_close_fill);
            frLanguageTxt.setTextColor(Color.WHITE);

            enLanguage.setBackgroundResource(R.drawable.custom_close_primary);
            enLanguageTxt.setTextColor(Color.parseColor("#0A6B91"));
        }

        tour_btn.setText(getString(R.string.Act_Tour_Close));

        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        fm = getSupportFragmentManager();

        TourAdapter pagerAdapter = new TourAdapter(fm, Key, context);

        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(Key);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if(position == 2 && scrollFlag && mode.equals("SPLASH")) {
                    scrollFlag = false;
                    init();
                    System.out.println(":::::::::::::::view scrolled::::::::::::::::" + position);
                }

            }

            @Override
            public void onPageSelected(int position) {

                System.out.println(":::::::::::::::view change::::::::::::::::" + position);
                keyFragment = position;
                switch (position) {
                    case 0:
                        one_dot.setBackgroundResource(R.drawable.custom_close);
                        two_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        three_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        four_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        break;

                    case 1:
                        one_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        two_dot.setBackgroundResource(R.drawable.custom_close);
                        three_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        four_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        break;

                    case 2:
                        one_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        two_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        three_dot.setBackgroundResource(R.drawable.custom_close);
                        four_dot.setBackgroundResource(R.drawable.custom_close_ring);

                        break;

                    case 3:
                        one_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        two_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        three_dot.setBackgroundResource(R.drawable.custom_close_ring);
                        four_dot.setBackgroundResource(R.drawable.custom_close);

                        if(mode.equals("SPLASH")) {

                            tour_btn.setText("Ok");
                            tour_btn.setVisibility(View.VISIBLE);
                        }
                        break;

                    default:
                        break;
                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void init() {
        if(isPermission()){
//            Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
//            startActivity(intent);
//            finish();
            askGPS_Req = false;
            checkForLocation();

        }else {
            requestlocationPermission();
        }
    }

    private boolean isPermission() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;
        }
        //If permission is not granted returning false
        return false;
    }

    private void requestlocationPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
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

    private void checkForLocation()
    {
        try {
            GPSTracker gpsTracker = new GPSTracker(TourActivity.this);
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
                    if (latitude_location.equals("0.0")) {
                        latitude_location = Constant.DEFAULT_LAT;
                    }

                    editor_loc.putString("LATITUDE", latitude_location);
                    editor_loc.commit();

                    String longitude_location = String.valueOf(longitude);
                    if (longitude_location.equals("0.0")) {
                        longitude_location = Constant.DEFAULT_LONG;
                    }
                    editor_loc.putString("LONGITUDE", longitude_location);
                    editor_loc.commit();
                }
                else
                {
                    setDefaultLoc();
                }

            } else {

                if(askGPS_Req) {

                    askGPS_Req = false;
                    showSettingsAlert();
                }
                else
                {
                    setDefaultLoc();


                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(TourActivity.this, getString(R.string.Error_Encounter), Toast.LENGTH_SHORT).show();

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

    public void showSettingsAlert() {

        try {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    context);

            alertDialog.setTitle(context.getString(R.string.gps_title));
            alertDialog.setMessage(context.getString(R.string.gps_subtitle));
            alertDialog.setCancelable(false);

            alertDialog.setPositiveButton(context.getString(R.string.gps_setting_btn),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            chkGPS = true;

                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        }
                    });

            alertDialog.setNegativeButton(context.getString(R.string.gps_cancel_btn),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();

                            setDefaultLoc();
                        }
                    });


            // Showing Alert Message
            alertDialog.show();
        }
        catch (Exception ex)
        {
            Toast.makeText(TourActivity.this, "Application Encountered Some problem !!!", Toast.LENGTH_SHORT).show();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v == close_layout)
        {

            if(mode.equals("SPLASH")) {

                Intent intent = new Intent(TourActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent(TourActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
//                overridePendingTransition(R.anim.static_anim, R.anim.bottom_out);
            }
        }
        else if(v == enLanguage)
        {
            enLanguage.setBackgroundResource(R.drawable.custom_close_fill);
            enLanguageTxt.setTextColor(Color.WHITE);

            frLanguage.setBackgroundResource(R.drawable.custom_close_primary);
            frLanguageTxt.setTextColor(Color.parseColor("#0A6B91"));

            changeLang("en");
            updateview();
        }
        else if(v == frLanguage)
        {
            frLanguage.setBackgroundResource(R.drawable.custom_close_fill);
            frLanguageTxt.setTextColor(Color.WHITE);

            enLanguage.setBackgroundResource(R.drawable.custom_close_primary);
            enLanguageTxt.setTextColor(Color.parseColor("#0A6B91"));

            changeLang("fr");
            updateview();
        }
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }


    public void updateview()
    {

        tour_btn.setText(getString(R.string.Act_Tour_Close));

        if(keyFragment == 0) {

            Fragment fragment1 = fm.getFragments().get(0);
            TextView txt = (TextView) fragment1.getView().findViewById(R.id.txt_tourOne);

            txt.setText(getString(R.string.Tour_One));

            Fragment fragment2 = fm.getFragments().get(1);
            TextView txt_fragment2 = (TextView) fragment2.getView().findViewById(R.id.txt_tourTwoA);
            txt_fragment2.setText(getString(R.string.Tour_Two_A));

            TextView txt_fragment2_1 = (TextView) fragment2.getView().findViewById(R.id.txt_tourTwoB);
            txt_fragment2_1.setText(getString(R.string.Tour_Two_B));
        }
        else if(keyFragment == 1)
        {
            Fragment fragment1 = fm.getFragments().get(0);
            TextView txt = (TextView) fragment1.getView().findViewById(R.id.txt_tourOne);

            txt.setText(getString(R.string.Tour_One));

            Fragment fragment2 = fm.getFragments().get(1);
            TextView txt_fragment2 = (TextView) fragment2.getView().findViewById(R.id.txt_tourTwoA);
            txt_fragment2.setText(getString(R.string.Tour_Two_A));

            TextView txt_fragment2_1 = (TextView) fragment2.getView().findViewById(R.id.txt_tourTwoB);
            txt_fragment2_1.setText(getString(R.string.Tour_Two_B));

            Fragment fragment3 = fm.getFragments().get(2);
            TextView txt_fragment3 = (TextView) fragment3.getView().findViewById(R.id.txt_tourThreeA);
            txt_fragment3.setText(getString(R.string.Tour_Three_A));

            TextView txt_fragment3_1 = (TextView) fragment3.getView().findViewById(R.id.txt_tourThreeB);
            txt_fragment3_1.setText(getString(R.string.Tour_Three_B));
        }

        else if(keyFragment == 2)
        {
            Fragment fragment2 = fm.getFragments().get(1);
            TextView txt_fragment2 = (TextView) fragment2.getView().findViewById(R.id.txt_tourTwoA);
            txt_fragment2.setText(getString(R.string.Tour_Two_A));

            TextView txt_fragment2_1 = (TextView) fragment2.getView().findViewById(R.id.txt_tourTwoB);
            txt_fragment2_1.setText(getString(R.string.Tour_Two_B));

            Fragment fragment3 = fm.getFragments().get(2);
            TextView txt_fragment3 = (TextView) fragment3.getView().findViewById(R.id.txt_tourThreeA);
            txt_fragment3.setText(getString(R.string.Tour_Three_A));

            TextView txt_fragment3_1 = (TextView) fragment3.getView().findViewById(R.id.txt_tourThreeB);
            txt_fragment3_1.setText(getString(R.string.Tour_Three_B));

            Fragment fragment4 = fm.getFragments().get(3);
            TextView txt_fragment4 = (TextView) fragment4.getView().findViewById(R.id.txt_tourFourA);
            txt_fragment4.setText(getString(R.string.Tour_Four_A));

            TextView txt_fragment4_1 = (TextView) fragment4.getView().findViewById(R.id.txt_tourFourB);
            txt_fragment4_1.setText(getString(R.string.Tour_Four_B));
        }
        else if(keyFragment == 3)
        {
            Fragment fragment3 = fm.getFragments().get(2);
            TextView txt_fragment3 = (TextView) fragment3.getView().findViewById(R.id.txt_tourThreeA);
            txt_fragment3.setText(getString(R.string.Tour_Three_A));

            TextView txt_fragment3_1 = (TextView) fragment3.getView().findViewById(R.id.txt_tourThreeB);
            txt_fragment3_1.setText(getString(R.string.Tour_Three_B));

            Fragment fragment4 = fm.getFragments().get(3);
            TextView txt_fragment4 = (TextView) fragment4.getView().findViewById(R.id.txt_tourFourA);
            txt_fragment4.setText(getString(R.string.Tour_Four_A));

            TextView txt_fragment4_1 = (TextView) fragment4.getView().findViewById(R.id.txt_tourFourB);
            txt_fragment4_1.setText(getString(R.string.Tour_Four_B));
        }




    }
}