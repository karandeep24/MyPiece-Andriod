package com.mi.activities;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import android.app.ProgressDialog;
import com.google.android.gms.gcm.GoogleCloudMessaging;




import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mi.asytask.DashBoardAsyncTask;
import com.mi.asytask.MyPurchasesAsyncTask;
import com.mi.common.Constant;
import com.mi.location.GPSTracker;
import com.mi.test.mypiece.R;
import com.mi.webapi.ConnectionDetector;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by mind on 14/9/15.
 */
public class SplashScreenActivity extends Activity
{
    static int bool=0;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "RegisterActivity";
    String SENDER_ID = "502973706112";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    boolean permissionGranted = false;
    Intent intent;
    SharedPreferences preferences, userPreferences, tourPreferences, langPreferences;
    SharedPreferences.Editor editor;
    Handler handler = new Handler();
    Context context;
    String regid, loginStatus, tourStatus;
    ProgressDialog progressDialog;
    public static SplashScreenActivity instance = null;
    SharedPreferences locationPrefs;
    boolean askGPS_Req = true;
    boolean chkGPS = false;
    String sLang, notification_route;
    public static String notification_deal_value;
    Intent notification_intent = new Intent();
    String sUserID;


    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e)
        {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context)
    {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences("CommonPrefs",
                Context.MODE_PRIVATE);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend()
    {
        // Your implementation here.
    }
    private void storeRegistrationId(Context context, String regId)
    {
        try {
            // This sample app persists the registration ID in shared preferences, but
            // how you store the regID in your app is up to you.


            SharedPreferences pref2s = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());

            SharedPreferences.Editor editor1 = pref2s.edit();
            editor1.putString("gcmid", regId);
            editor1.commit();


            final SharedPreferences prefs = getGcmPreferences(context);
            // int appVersion = getAppVersion(context);
            //Log.i(TAG, "Saving regId on app version " + appVersion);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(PROPERTY_REG_ID, regId);
            //editor.putInt(PROPERTY_APP_VERSION, appVersion);
            editor.commit();

        }
        catch (Exception e)
        {
            Intent mainIntent = new Intent(
                    SplashScreenActivity.this, RegisterActivity.class);
            startActivity(mainIntent);
        }
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
//    private String getRegistrationId(Context context)
//    {
//        try {
//
//
//            final SharedPreferences prefs = getGcmPreferences(context);
//            String registrationId = prefs.getString(PROPERTY_REG_ID, "");
//            if (registrationId.isEmpty()) {
//                Log.i(TAG, "Registration not found.");
//                return "";
//            } else {
//                Log.i(TAG, "Registration found.");
//                return "";
//
//            }
//            // Check if app was updated; if so, it must clear the registration ID
//            // since the existing regID is not guaranteed to work with the new
//            // app version.
//            //int currentVersion = getAppVersion(context);
//            //if (registeredVersion != currentVersion) {
//            //Log.i(TAG, "App version changed.");
//            //return "";
//            //}
//        }
//        catch (Exception e)
//        {
//            return "";
//
//        }
//    }

//    /**
//     * Registers the application with GCM servers asynchronously.
//     * <p>
//     * Stores the registration ID and the app versionCode in the application's
//     * shared preferences.
//     */
//    private void registerInBackground()
//    {
//        try {
//            new AsyncTask<Void, Void, String>() {
//                @Override
//                protected String doInBackground(Void... params) {
//                    String msg = "";
//                    try {
//                        if (gcm == null) {
//                            gcm = GoogleCloudMessaging.getInstance(context);
//                        }
//                        regid = gcm.register(SENDER_ID);
//                        msg = "Device registered, registration ID=" + regid;
//
//
//                        Log.i(TAG, "Reg ID - " + regid);
//
//
//                        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
//
//
//                        // You should send the registration ID to your server over HTTP, so it
//                        // can use GCM/HTTP or CCS to send messages to your app.
//                        sendRegistrationIdToBackend();
//                        storeRegistrationId(context, regid);
//                    } catch (IOException ex)
//                    {
//                        Intent mainIntent = new Intent(
//                                SplashScreenActivity.this,
//                                RegisterActivity.class);
//                        startActivity(mainIntent);
//                        msg = "Error :" + ex.getMessage();
//                    }
//                    return msg;
//                }
//
//                @Override
//                protected void onPostExecute(String msg) {
//                    //mDisplay.append(msg + "\n");
//                }
//            }.execute(null, null, null);
//        }
//        catch (Exception e)
//        {
//            Intent mainIntent = new Intent(
//                    SplashScreenActivity.this,
//                    RegisterActivity.class);
//            startActivity(mainIntent);
//        }
//    }

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private boolean checkPlayServices()
    {
        try {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (apiAvailability.isUserResolvableError(resultCode)) {
                    apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                            .show();
                } else {
                    Log.i(TAG, "This device is not supported.");
                    finish();
                }
                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            return true;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.act_splash_screen);
            context = this;

            instance = this;

            try {
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                }
            }
            catch (Exception e)
            {
                Intent mainIntent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                startActivity(mainIntent);
            }


            tourPreferences = this.getSharedPreferences("TOUR", MODE_PRIVATE);
            preferences = this.getSharedPreferences("cookies", MODE_PRIVATE);
            userPreferences = this.getSharedPreferences("User_id", MODE_PRIVATE);
            langPreferences = this.getSharedPreferences("CommonPrefs", MODE_PRIVATE);

            sUserID = userPreferences.getString("user_id_value", "");
            loginStatus = userPreferences.getString("isLogIn", "");
            tourStatus = tourPreferences.getString("isTourTaken", "");
            sLang = langPreferences.getString("Language", "");

            notification_intent = getIntent();
            notification_route = notification_intent.getStringExtra("NOTIFICATION_ROUTE");
            notification_deal_value = notification_intent.getStringExtra("NOTIFICATION_VALUE");

            System.out.println("notification_route:::::::::::::::" + notification_route);
            System.out.println("notification_deal_value:::::::::::::::" + notification_deal_value);

            if(notification_route == null)
            {
                notification_route = "SPLASH";
            }

            System.out.println("locale" + sLang);

            if (sLang.equalsIgnoreCase("en")) {
                changeLang("en");
            } else if (sLang.equalsIgnoreCase("fr")) {
                changeLang("fr");
            } else {
                changeLang("en");
            }

            String token = FirebaseInstanceId.getInstance().getToken();

            System.out.println("token:::::::::::::::::::" + token );

            permissionGranted = preferences.getBoolean("Permission", false);
            System.out.println("Permission Status: " + permissionGranted);

            boolean isNetwork = new ConnectionDetector(getApplicationContext()).isConnectingToInternet();

            if (isNetwork) {

                handler.postDelayed(new Runnable() {
                    public void run() {

                        if (tourStatus.equals("TAKEN")) {

                            if (loginStatus.equals("LOGIN")) {
                                askGPS_Req = false;
                                checkForLocation();

//                            locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);
//
//                            String latitude = locationPrefs.getString("LATITUDE", "");
//                            String longitude = locationPrefs.getString("LONGITUDE", "");
//
//
//                            String URL = Constant.GET_ALL_DEALS + "latitude=" + latitude + "&longitude="+ longitude + "&radius=10";
//
//                            progressDialog = ProgressDialog.show(SplashScreenActivity.this, "", "Please wait.....");
//                            DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "SPLASH");
//                            asyncTask.execute();

                            } else {
                                init();
                            }
                        } else {

                            editor = tourPreferences.edit();

                            editor.putString("isTourTaken", "TAKEN");
                            editor.commit();

                            Intent intent = new Intent(SplashScreenActivity.this, TourActivity.class);
                            intent.putExtra("MODE", "SPLASH");
                            startActivity(intent);
                            finish();

                        }


                    }
                }, 2 * 2000);

            } else {
                Network_unavailable();
                Toast.makeText(SplashScreenActivity.this,
                        getString(R.string.No_Connection), Toast.LENGTH_SHORT).show();
            }
//            if(permissionGranted)
//            {
//                String URL = Constant.GET_ALL_DEALS;
//                progressDialog = ProgressDialog.show(SplashScreenActivity.this, "",
//                        "Please wait.....");
//
//                DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog);
//                asyncTask.execute();
//            }
//            else
//            {
//                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }


//            int opencount = 0;
//
//            SharedPreferences pref2s = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
//            opencount = pref2s.getInt("opencount", 0);
//
//            final int pass = opencount;
//
//            if (opencount == 4) {
//                bool = 0;
//                //opencount++;
//                SharedPreferences.Editor editor1 = pref2s.edit();
//                editor1.putInt("opencount", opencount);
//                editor1.commit();
//
//                //Log.d("Yoyo",regid);
//                if (checkPlayServices()) {
//                    gcm = GoogleCloudMessaging.getInstance(this);
//                    regid = getRegistrationId(context);
//
//                    if (regid.isEmpty()) {
//                        //registerInBackground();
//                    }
//
//                    Log.d("Yoyo", regid);
//                } else {
//                    Log.i(TAG, "No valid Google Play Services APK found.");
//                }
//
//
//                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        SharedPreferences sharedPreferences =
//                                PreferenceManager.getDefaultSharedPreferences(context);
//                        boolean sentToken = sharedPreferences
//                                .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
//
//                    }
//                };
//
//                //For the change display language
//
//                String locale_check = Locale.getDefault().getLanguage();
//                // System.out.println("locale" + locale_check);
//                if (locale_check.equalsIgnoreCase("en")) {
//                    changeLang("en");
//                } else if (locale_check.equalsIgnoreCase("fr")) {
//                    changeLang("fr");
//                } else {
//                    changeLang("en");
//                }
//
//                if (android.os.Build.VERSION.SDK_INT > 9) {
//                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                            .permitAll().build();
//                    StrictMode.setThreadPolicy(policy);
//                }
//
//                Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
//
//                getSharedPreferences(ApplicationSessionFragmentActivity.TAG,
//                        MODE_WORLD_WRITEABLE).edit().clear().commit();
//
//                try
//                {
//                    //Countly.sharedInstance().init(getApplicationContext(),
//                    // "https://cloud.count.ly",
//                    //"5c20a7014bea1f02264b96aea12713ff17693740");
//                }
//                catch (Exception e)
//                {
//                    Intent mainIntent = new Intent(
//                            SplashScreenActivity.this,
//                            MainActivity.class);
//                    startActivity(mainIntent);
//                    e.printStackTrace();
//                }
//
//
//            }


//            if (opencount == 3) {
//                opencount++;
//                SharedPreferences.Editor editor1 = pref2s.edit();
//                editor1.putInt("opencount", opencount);
//                editor1.commit();
//                bool = 1;
//
//
//                runOnUiThread(new Runnable() {
//
//                    public void run() {
//
//                        if (!isFinishing()) {
//                            final Dialog dialog = new Dialog(SplashScreenActivity.this,
//                                    R.style.ProgressHUD);
//                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog.setContentView(R.layout.custom_alert_box);
//                            TextView  tv_2 = (TextView) dialog.findViewById(R.id.tv_2);
//                            Button btn_yes = (Button) dialog.findViewById(R.id.button101);
//                            Button btn_no = (Button) dialog.findViewById(R.id.button102);
//
//                            tv_2.setText("How is your experience so far with MyPiece?");
//                            btn_yes.setText("Good");
//                            btn_no.setText("Bad");
//
//                            btn_yes.setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    // TODO Auto-generated method stub
//                                    // moveTaskToBack(true);
//                                    runOnUiThread(new Runnable() {
//
//                                        public void run() {
//
//                                            if (!isFinishing()) {
//                                                new AlertDialog.Builder(SplashScreenActivity.this)
//                                                        .setTitle("Rate MyPiece")
//                                                        .setMessage("Please rate the app with 5 stars\n" +
//                                                                "and help fellow Android users \n" +
//                                                                "discover it.  Thanks for the\n" +
//                                                                "support!")
//                                                        .setCancelable(false)
//                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                                            @Override
//                                                            public void onClick(DialogInterface dialog, int which) {
//                                                                Intent i = new Intent(android.content.Intent.ACTION_VIEW);
//                                                                i.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.mi.mypiece"));
//                                                                startActivity(i);
//                                                                finish();
//                                                            }
//
//                                                        })
//                                                        .create().show();
//                                            }
//                                        }
//                                    });
//                                    //LogoutTask.sendActivityFinishBroadcast(MainpageActivity.this);
//
//
//                                    //finish();
//                                }
//                            });
//                            btn_no. setOnClickListener(new View.OnClickListener() {
//
//                                @Override
//                                public void onClick(View v) {
//                                    // TODO Auto-generated method stub
//
//                                    Intent mainIntent = new Intent(
//                                            SplashScreenActivity.this,
//                                            MainActivity.class);
//                                    mainIntent.putExtra("check_intent_src", "check_exist_record_login");
//                                    startActivity(mainIntent);
//                                    finish();
//
//
//                                }
//                            });
//                            // set the custom dialog components - text, image and
//                            // button
//
//                            dialog.show();
//
//                        }
//                    }
//                });
//
//
//                //---------------------------DIALOG BOX for review
//
//
//                //---------------------------
//
//
//            } else
//            {
//                opencount++;
//                SharedPreferences.Editor editor1 = pref2s.edit();
//                editor1.putInt("opencount", opencount);
//                editor1.commit();
//
//
//                //Log.d("Yoyo",regid);
//                if (checkPlayServices()) {
//                    gcm = GoogleCloudMessaging.getInstance(this);
//                    regid = getRegistrationId(context);
//
//                    if (regid.isEmpty()) {
//                        //    registerInBackground();
//                    }
//
//                    Log.d("Yoyo", regid);
//                } else {
//                    Log.i(TAG, "No valid Google Play Services APK found.");
//                }
//
//
//                mRegistrationBroadcastReceiver = new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        SharedPreferences sharedPreferences =
//                                PreferenceManager.getDefaultSharedPreferences(context);
//                        boolean sentToken = sharedPreferences
//                                .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
//
//                    }
//                };
//
//                //For the change display language
//
//                String locale_check = Locale.getDefault().getLanguage();
//                // System.out.println("locale" + locale_check);
//                if (locale_check.equalsIgnoreCase("en")) {
//                    changeLang("en");
//                } else if (locale_check.equalsIgnoreCase("fr")) {
//                    changeLang("fr");
//                } else {
//                    changeLang("en");
//                }
//
//                if (android.os.Build.VERSION.SDK_INT > 9) {
//                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
//                            .permitAll().build();
//                    StrictMode.setThreadPolicy(policy);
//                }
//
//                Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
//
//                getSharedPreferences(ApplicationSessionFragmentActivity.TAG,
//                        MODE_WORLD_WRITEABLE).edit().clear().commit();
//
//                try {
//                    //Countly.sharedInstance().init(getApplicationContext(),
//                    // "https://cloud.count.ly",
//                    //"5c20a7014bea1f02264b96aea12713ff17693740");
//                }
//                catch (Exception e)
//                {
//                    Intent mainIntent = new Intent(
//                            SplashScreenActivity.this,
//                            MainActivity.class);
//                    startActivity(mainIntent);
//                    e.printStackTrace();
//                }
//
//            }
        }
        catch (Exception e)
        {
            Toast.makeText(SplashScreenActivity.this, getString(R.string.Error_Encounter), Toast.LENGTH_SHORT).show();
        }


    }

    private void init()
    {
        if(isPermission())
        {
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

//                Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(this, getString(R.string.Toast_Location_Granted),Toast.LENGTH_LONG).show();
            }else{

                setDefaultLoc();

                Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(this, getString(R.string.Toast_Location_Denied),Toast.LENGTH_LONG).show();
            }
        }
    }


//    public void askForContactPermission(){
//        try {
//
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
//
//                    // Should we show an explanation?
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(SplashScreenActivity.this, Manifest.permission.READ_CONTACTS)) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
//                        builder.setTitle("Contacts access needed");
//                        builder.setPositiveButton(android.R.string.ok, null);
//                        builder.setMessage("please confirm Contacts access");//TODO put real question
//                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                            @TargetApi(Build.VERSION_CODES.M)
//                            @Override
//                            public void onDismiss(DialogInterface dialog) {
//                                requestPermissions(
//                                        new String[]
//                                                {Manifest.permission.READ_CONTACTS}
//                                        , PERMISSIONS_REQUEST_READ_CONTACTS);
//                            }
//                        });
//                        builder.show();
//                        // Show an expanation to the user *asynchronously* -- don't block
//                        // this thread waiting for the user's response! After the user
//                        // sees the explanation, try again to request the permission.
//
//                    } else {
//
//                        // No explanation needed, we can request the permission.
//
//                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_CONTACTS},
//                                PERMISSIONS_REQUEST_READ_CONTACTS);
//
//                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
//                        // app-defined int constant. The callback method gets the
//                        // result of the request.
//                    }
//                } else {
//
//                    System.out.println("::::::::::::::::::in else ::::::::");
//
//                    if(permissionGranted)
//                    {
//                        String URL = Constant.GET_ALL_DEALS;
//                        progressDialog = ProgressDialog.show(SplashScreenActivity.this, "",
//                                "Please wait.....");
//
//                        DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "SPLASH");
//                        asyncTask.execute();
//                    }
//                    else
//                    {
//                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            } else {
//                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS)
//        {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                permissionGranted = true;
//                getEmailAcc();
//                System.out.println("permissionGranted:::::::::::::::" + permissionGranted);
//
//                if(permissionGranted)
//                {
//                    String URL = Constant.GET_ALL_DEALS;
//                    progressDialog = ProgressDialog.show(SplashScreenActivity.this, "",
//                            "Please wait.....");
//
//                    DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "SPLASH");
//                    asyncTask.execute();
//                }
//                else
//                {
//
//                }
//            }
//            else
//            {
//                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }
//
//    }

    public void getEmailAcc()
    {
        String email = "";

        AccountManager accManager = AccountManager.get(context);
        Account acc[] = accManager.getAccounts();
        int accCount = acc.length;
        for (Account account : acc)
        {
            if(account.name.contains("@gmail.com"))
            {
                email = account.name;
            }
        }

        setCookies(email, permissionGranted);

    }

    private void setCookies(String email, Boolean permissionGranted) {

        SharedPreferences preferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("Email", email);
        editor.putBoolean("Permission", permissionGranted);

        editor.commit();
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

    private void checkForLocation()
    {
        try {
            GPSTracker gpsTracker = new GPSTracker(SplashScreenActivity.this);
            if (gpsTracker.canGetLocation()) {
//                MyPreferences.setPref(SplashScreenActivity.this, MyPreferences.LAST_LATITUDE, "" + gpsTracker.getLatitude());
//                MyPreferences.setPref(SplashScreenActivity.this, MyPreferences.LAST_LONGITUDE, "" + gpsTracker.getLongitude());

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                SharedPreferences shared_loc = getSharedPreferences("Location", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_loc = shared_loc.edit();

                Double distance = distance(Double.parseDouble(Constant.DEFAULT_LAT), latitude,
                        Double.parseDouble(Constant.DEFAULT_LONG), longitude);

                if(distance < 50000) {

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


                if (loginStatus.equals("LOGIN")) {

                    if(notification_route.equals("NOTIFICATION_PURCHASE"))
                    {
                        progressDialog = ProgressDialog.show(SplashScreenActivity.this, "", "Please wait.....");
                        String URL = Constant.MY_PURCHASE + "UserId=" + sUserID + "&Lang=" + sLang;
                        MyPurchasesAsyncTask asyncTask = new MyPurchasesAsyncTask(context, URL, progressDialog);
                        asyncTask.execute();
                    }
                    else {

                        locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

                        String lat = locationPrefs.getString("LATITUDE", "");
                        String longi = locationPrefs.getString("LONGITUDE", "");


                        String URL = Constant.GET_ALL_DEALS + "lang=" + sLang + "&latitude=" + lat + "&longitude=" + longi + "&radius=50";

                        progressDialog = ProgressDialog.show(SplashScreenActivity.this, "", "Please wait.....");
                        DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, notification_route);
                        asyncTask.execute();
                    }
                }
                 else {
                    Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }

            } else {

                if(askGPS_Req) {

                    askGPS_Req = false;
                    showSettingsAlert();
                }
                else
                {
                    setDefaultLoc();

                    if (loginStatus.equals("LOGIN")) {

                        if(notification_route.equals("NOTIFICATION_PURCHASE"))
                        {
                            progressDialog = ProgressDialog.show(SplashScreenActivity.this, "", "Please wait.....");
                            String URL = Constant.MY_PURCHASE + "UserId=" + sUserID + "&Lang=" + sLang;
                            MyPurchasesAsyncTask asyncTask = new MyPurchasesAsyncTask(context, URL, progressDialog);
                            asyncTask.execute();
                        }
                        else {
                            locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

                            String lat = locationPrefs.getString("LATITUDE", "");
                            String longi = locationPrefs.getString("LONGITUDE", "");

                            String URL = Constant.GET_ALL_DEALS + "lang=" + sLang + "&latitude=" + lat + "&longitude=" + longi + "&radius=50";

                            progressDialog = ProgressDialog.show(SplashScreenActivity.this, "", "Please wait.....");
                            DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, notification_route);
                            asyncTask.execute();
                        }
                    }
                    else {
                        Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(SplashScreenActivity.this, getString(R.string.Error_Encounter), Toast.LENGTH_SHORT).show();

        }
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

                            Intent intent = new Intent(SplashScreenActivity.this, RegisterActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });


            // Showing Alert Message
            alertDialog.show();
        }
        catch (Exception ex)
        {
            Toast.makeText(SplashScreenActivity.this, "Application Encountered Some problem !!!", Toast.LENGTH_SHORT).show();
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


//    private boolean checkIsLogin()
//    {
//        DatabaseUtils_ds db = null;
//
//        try
//        {
//            Hashtable<String, String> tmp1 = new Hashtable<String, String>();
//
//            db = new DatabaseUtils_ds(this, tmp1, "Mypiece", "Login_user");
//            Cursor c = db.get_all();
//
//            if (c.moveToFirst()) {
//                String User_id = c.getColumnName(c.getColumnIndex("User_id"));
//
//                if (Methods.isValidString(User_id))
//                {
//                    return true;
//                }
//
//            }
//
//            c.close();
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            Intent mainIntent = new Intent(
//                    SplashScreenActivity.this,
//                    RegisterActivity.class);
//            startActivity(mainIntent);
//        }
//
//        if (db != null)
//        {
//            db.close();
//        }
//
//        return false;
//    }

//    private void setSplashTime()
//    {
//        try
//        {
//
//
//            boolean isNetwork = new ConnectionDetector(getApplicationContext()).isConnectingToInternet();
//
//            if (checkIsLogin()) {
//                if (isNetwork) {
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (bool != 1) {
//
//                                String URL = Constant.GET_ALL_DEALS;
//                                progressDialog = ProgressDialog.show(SplashScreenActivity.this, "",
//                                        "Please wait.....");
//
//                                DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog);
//                                asyncTask.execute();
//
////                                Intent mainIntent = new Intent(
////                                        SplashScreenActivity.this,
////                                        Dashboard.class);
////                                mainIntent.putExtra("check_intent_src", "check_exist_record_login");
////                                startActivity(mainIntent);
////                       /* overridePendingTransition(
////                                R.anim.anim_slide_in_left,
////                                R.anim.anim_slide_out_left);*/
////                                finish();
//                            }
//                        }
//                    }, 500);
//                } else {
//                    Toast.makeText(SplashScreenActivity.this, "No Internet Connection Available.", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                if (isNetwork) {
//
//                } else {
//                    Toast.makeText(SplashScreenActivity.this, "No Internet Connection Available.", Toast.LENGTH_SHORT).show();
//                }
//
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (bool != 1) {
//                            Intent mainIntent = new Intent(
//                                    SplashScreenActivity.this,
//                                    RegisterActivity.class);
//                            startActivity(mainIntent);
//                   /* overridePendingTransition(
//                            R.anim.anim_slide_in_left,
//                            R.anim.anim_slide_out_left);*/
//                            finish();
//                        }
//                    }
//                }, 500);
//            }
//        }
//        catch (Exception e)
//        {
//            Intent mainIntent = new Intent(
//                    SplashScreenActivity.this,
//                    RegisterActivity.class);
//            startActivity(mainIntent);
//        }
//    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if(chkGPS) {
            try {
                chkGPS = false;
                checkForLocation();
            } catch (Exception e) {

            }
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

    private void Network_unavailable() {


        AlertDialog.Builder builder = new AlertDialog.Builder(
                SplashScreenActivity.this);
        builder.setMessage(getString(R.string.Pop_No_Network));
        builder.setCancelable(false);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.setPositiveButton(getString(R.string.Retry),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(SplashScreenActivity.this, SplashScreenActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        builder.show();
    }
}
