package com.mi.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mi.asytask.DashBoardAsyncTask;
import com.mi.asytask.MyPurchasesAsyncTask;
import com.mi.common.Constant;
import com.mi.location.GPSTracker;
import com.mi.metadata.CreditCardMetadata;
import com.mi.test.mypiece.R;
import com.mi.utility.DbHelper;
import com.mi.webapi.ConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by MilindVyas on 20-Aug-16.
 */
public class MyAccount extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener
{
    Intent intent;
    LinearLayout enLanguage, frLanguage, addCard, creditInfo, firstCard, secondCard, secondCardLine;
    TextView enLanguageTxt, frLanguageTxt, creditEmail, firstTextCardName, firstTextCardType,
            secondTextCardName, secondTextCardType, toolbar_title;
    ListView creditList;
    public static MyAccount instance = null;
    DbHelper dbHelper;
    HashMap<String, CreditCardMetadata> spinnerArray;
    ArrayList<String> cardsArray = new ArrayList<String>();
    Context context;
    String sCreditEmail;
    ProgressDialog progressDialog;
    String sUserID, sLang;
    SharedPreferences userPreferences;
    CreditCardMetadata creditCardMetadata = new CreditCardMetadata();
    CheckBox loc_check;
    boolean chkResume = false;
    boolean chkBack = true;
    public static boolean chkCardDeleted = false;
    private GoogleApiClient mGoogleApiClient;
    SharedPreferences langPreferences;
    TextView txt_yourPayment, txt_billingEmail, txt_locationService, txt_language;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);

        instance = this;
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_account);
        navigationView.setNavigationItemSelectedListener(this);

        // get menu from navigationView
        menu = navigationView.getMenu();

        userPreferences = context.getSharedPreferences("User_id", Context.MODE_PRIVATE);
        langPreferences = this.getSharedPreferences("CommonPrefs", MODE_PRIVATE);


        sUserID = userPreferences.getString("user_id_value", "");
        sLang = langPreferences.getString("Language", "en");

        initialize();

        System.out.println("locale " + sLang);

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


        if(userPreferences.getString("EMAIL", "").equals(""))
        {
            sCreditEmail = "No email configured !";
        }else
        {
            sCreditEmail = userPreferences.getString("EMAIL", "No email configured !");
        }

        creditEmail.setText(sCreditEmail);

        dbHelper = new DbHelper(this);
        chkCreditDb();

        if(isPermission())
        {
            loc_check.setChecked(true);
        }
        else
        {
            loc_check.setChecked(false);
        }

        //chkBack = false;

    }

    public void initialize()
    {
        enLanguage = (LinearLayout) findViewById(R.id.enLanguage);
        frLanguage = (LinearLayout) findViewById(R.id.frLanguage);
        addCard = (LinearLayout) findViewById(R.id.addCard);
        creditInfo = (LinearLayout) findViewById(R.id.creditInfo);
        firstCard = (LinearLayout) findViewById(R.id.firstCard);
        secondCard = (LinearLayout) findViewById(R.id.secondCard);
        secondCardLine = (LinearLayout) findViewById(R.id.secondCardLine);

        enLanguageTxt = (TextView) findViewById(R.id.enLanguageTxt);
        frLanguageTxt = (TextView) findViewById(R.id.frLanguageTxt);
        creditEmail = (TextView) findViewById(R.id.creditEmail);
        firstTextCardType = (TextView) findViewById(R.id.firstTextCardType);
        firstTextCardName = (TextView) findViewById(R.id.firstTextCardName);
        secondTextCardType = (TextView) findViewById(R.id.secondTextCardType);
        secondTextCardName = (TextView) findViewById(R.id.secondTextCardName);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);

        txt_yourPayment = (TextView) findViewById(R.id.txt_yourPayment);
        txt_billingEmail = (TextView) findViewById(R.id.txt_billingEmail);
        txt_locationService = (TextView) findViewById(R.id.txt_locationService);
        txt_language = (TextView) findViewById(R.id.txt_language);


        loc_check = (CheckBox) findViewById(R.id.loc_check);

        //creditList = (ListView) findViewById(R.id.creditList);

        enLanguage.setOnClickListener(this);
        frLanguage.setOnClickListener(this);
        addCard.setOnClickListener(this);
        firstCard.setOnClickListener(this);
        secondCard.setOnClickListener(this);
        loc_check.setOnClickListener(this);
    }

    public void chkCreditDb()
    {
        fill_list();
    }

    public void fill_list()
    {

        try {
            spinnerArray = dbHelper.getcardRecords();
            cardsArray.clear();

            if(spinnerArray.size() > 0) {


                creditInfo.setVisibility(View.VISIBLE);

                Iterator it = spinnerArray.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    cardsArray.add(pair.getKey().toString());
                    //it.remove(); // avoids a ConcurrentModificationException
                }

                int size = cardsArray.size();

                if(size == 0)
                {
                    firstTextCardName.setText(getString(R.string.AddCard));
                    firstTextCardType.setText("");
                    firstTextCardName.setTextColor(Color.parseColor("#aaaaaa"));
                    firstTextCardType.setTextColor(Color.parseColor("#aaaaaa"));

                    secondCard.setVisibility(View.INVISIBLE);
                    secondCardLine.setVisibility(View.INVISIBLE);

                }else if(size == 1)
                {
                    creditCardMetadata = spinnerArray.get(cardsArray.get(0));

                    firstTextCardName.setText(creditCardMetadata.getCardName());
                    firstTextCardType.setText(creditCardMetadata.getBrand());

                    secondTextCardName.setText(getString(R.string.AddCard));
                    secondTextCardType.setText("");
                    secondTextCardName.setTextColor(Color.parseColor("#aaaaaa"));
                    secondTextCardType.setTextColor(Color.parseColor("#aaaaaa"));




                }else if(size == 2)
                {
                    firstTextCardName.setText(spinnerArray.get(cardsArray.get(0)).getCardName());
                    firstTextCardType.setText(spinnerArray.get(cardsArray.get(0)).getBrand());


                    secondTextCardName.setText(spinnerArray.get(cardsArray.get(1)).getCardName());
                    secondTextCardType.setText(spinnerArray.get(cardsArray.get(1)).getBrand());
                }
                else
                {
                    firstTextCardName.setText(spinnerArray.get(cardsArray.get(0)).getCardName());
                    firstTextCardType.setText(spinnerArray.get(cardsArray.get(0)).getBrand());

                    secondTextCardName.setText(getString(R.string.AddCard));
                    secondTextCardType.setText("");
                    secondTextCardName.setTextColor(Color.parseColor("#aaaaaa"));
                    secondTextCardType.setTextColor(Color.parseColor("#aaaaaa"));
                }

            }
            else
            {
                firstTextCardName.setText(getString(R.string.AddCard));
                firstTextCardType.setText("");
                firstTextCardName.setTextColor(Color.parseColor("#aaaaaa"));
                firstTextCardType.setTextColor(Color.parseColor("#aaaaaa"));

                secondCard.setVisibility(View.INVISIBLE);
                secondCardLine.setVisibility(View.INVISIBLE);
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
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

    private void updateView()
    {
        Resources resources = getResources();

        txt_yourPayment.setText(resources.getString(R.string.creditInfo));
        txt_billingEmail.setText(resources.getString(R.string.creditEmail));
        txt_locationService.setText(resources.getString(R.string.Location));
        txt_language.setText(resources.getString(R.string.creditLanguage));
        toolbar_title.setText(resources.getString(R.string.Nav_Drawer_MyAccount));


        MenuItem nav_Home = menu.findItem(R.id.nav_home);
        nav_Home.setTitle(getString(R.string.Nav_Drawer_Home));

        MenuItem nav_MyPurchase = menu.findItem(R.id.nav_purchases);
        nav_MyPurchase.setTitle(getString(R.string.Nav_Drawer_MyPurchases));

        MenuItem nav_MyAccount = menu.findItem(R.id.nav_account);
        nav_MyAccount.setTitle(getString(R.string.Nav_Drawer_MyAccount));

        MenuItem nav_GetInfo = menu.findItem(R.id.nav_info);
        nav_GetInfo.setTitle(getString(R.string.Nav_Drawer_GetInTouch));

        MenuItem nav_LogOut = menu.findItem(R.id.nav_logout);
        nav_LogOut.setTitle(getString(R.string.Nav_Drawer_LogOut));

        fill_list();

    }

    @Override
    public void onClick(View v) {

        if(v == enLanguage)
        {
            enLanguage.setBackgroundResource(R.drawable.custom_close_fill);
            enLanguageTxt.setTextColor(Color.WHITE);

            frLanguage.setBackgroundResource(R.drawable.custom_close_primary);
            frLanguageTxt.setTextColor(Color.parseColor("#0A6B91"));

            sLang = "en";

            changeLang("en");
            updateView();

        }
        else if(v == frLanguage)
        {
            frLanguage.setBackgroundResource(R.drawable.custom_close_fill);
            frLanguageTxt.setTextColor(Color.WHITE);

            enLanguage.setBackgroundResource(R.drawable.custom_close_primary);
            enLanguageTxt.setTextColor(Color.parseColor("#0A6B91"));

            sLang = "fr";

            changeLang("fr");
            updateView();
        }
        else if( v == firstCard) {
            if (cardsArray.size() == 0)
            {
                Intent intent = new Intent(MyAccount.this, AddCreditCard.class);
                intent.putExtra("MODE", "ADD");
                intent.putExtra("ROUTE", "ACCOUNT");
                startActivity(intent);
            }
            else
            {
                Intent intent = new Intent(MyAccount.this, AddCreditCard.class);
                intent.putExtra("MODE", "EDITDEL");
                intent.putExtra("ROUTE", "ACCOUNT");
                intent.putExtra("CARDID", cardsArray.get(0));
                startActivity(intent);
            }
        }
        else if( v == secondCard) {
            if (cardsArray.size() == 1)
            {
                Intent intent = new Intent(MyAccount.this, AddCreditCard.class);
                intent.putExtra("MODE", "ADD");
                intent.putExtra("ROUTE", "ACCOUNT");
                startActivity(intent);
            }
            else
            {
                // Edit this
                Intent intent = new Intent(MyAccount.this, AddCreditCard.class);
                intent.putExtra("MODE", "EDITDEL");
                intent.putExtra("ROUTE", "ACCOUNT");
                intent.putExtra("CARDID", cardsArray.get(1));
                startActivity(intent);
            }
        }else if(v == loc_check)
        {
            Dashboard.dashChkResume = true;

            if (loc_check.isChecked()) {

                System.out.println("::::::::if::::::::");
                requestlocationPermission();

            } else {

                chkResume = true;
                chkBack = true;
                if(Dashboard.instance == null)
                {}Dashboard.instance.finish();

                System.out.println("::::::::else::::::::");
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);


                //requestlocationPermission();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        boolean isNetwork = new ConnectionDetector(getApplicationContext()).isConnectingToInternet();

        if (isNetwork) {
            if (id == R.id.nav_home) {
                drawer.closeDrawer(GravityCompat.START);

                if (chkBack) {

                    chkBack = true;

                    System.out.println("::::::::::::::onBACK FROM ACC::::::::::::::");
                    SharedPreferences locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

                    String latitude = locationPrefs.getString("LATITUDE", "");
                    String longitude = locationPrefs.getString("LONGITUDE", "");

                    progressDialog = ProgressDialog.show(MyAccount.this, "", "Please wait.....");

                    String URL = Constant.GET_ALL_DEALS + "lang=" + sLang + "&latitude=" + latitude + "&longitude=" + longitude + "&radius=10";
                    DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "ACCOUNT");
                    asyncTask.execute();
                } else {
                    finish();
                }
            } else if (id == R.id.nav_purchases) {
                drawer.closeDrawer(GravityCompat.START);
                progressDialog = ProgressDialog.show(MyAccount.this, "", "Please wait.....");
                String URL = Constant.MY_PURCHASE + "UserId=" + sUserID + "&Lang=" + sLang;
                MyPurchasesAsyncTask asyncTask = new MyPurchasesAsyncTask(context, URL, progressDialog);
                asyncTask.execute();

            } else if (id == R.id.nav_account) {
                drawer.closeDrawer(GravityCompat.START);

            } else if (id == R.id.nav_info) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(MyAccount.this, GetinTouchActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {

                DbHelper dbHelper = new DbHelper(context);
                dbHelper.delete();

                SharedPreferences settings = context.getSharedPreferences("User_id", Context.MODE_PRIVATE);
                settings.edit().clear().commit();

                SharedPreferences settings_checkout = context.getSharedPreferences("CHECKOUT_POP", Context.MODE_PRIVATE);
                settings_checkout.edit().clear().commit();

                SharedPreferences login_prefs = context.getSharedPreferences("LOGINTYPE", Context.MODE_PRIVATE);
                String type = login_prefs.getString("LOGIN", "");

                if (type.equals("GOOGLE")) {
                    signOut();
                } else {

                    Intent intent = new Intent(MyAccount.this, RegisterActivity.class);
                    startActivity(intent);
                    if (Dashboard.instance == null) {
                    } else {
                        Dashboard.instance.finish();
                    }
                    finish();
                }


            }
        }
        else {
            Toast.makeText(MyAccount.this, getString(R.string.No_Connection), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        Intent intent = new Intent(MyAccount.this, RegisterActivity.class);
                        startActivity(intent);
                        if(Dashboard.instance == null)
                        {}else{Dashboard.instance.finish();}
                        finish();

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_map:
                init();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        if(isPermission()){
            Intent intent = new Intent(MyAccount.this, MapsActivity.class);
            startActivity(intent);

        }else {
            requestlocationPermission();
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

                loc_check.setChecked(true);

                checkForLocation();

                Toast.makeText(this, getString(R.string.Toast_Location_Granted),Toast.LENGTH_LONG).show();
            }else{

                loc_check.setChecked(false);

                SharedPreferences shared_loc = getSharedPreferences("Location", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_loc = shared_loc.edit();

                String latitude_location = String.valueOf(Constant.DEFAULT_LAT);
                editor_loc.putString("LATITUDE", latitude_location);
                editor_loc.commit();

                String longitude_location = String.valueOf(Constant.DEFAULT_LONG);
                editor_loc.putString("LONGITUDE", longitude_location);
                editor_loc.commit();

                Toast.makeText(this, getString(R.string.Toast_Location_Denied),Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkForLocation()
    {
        try {
            GPSTracker gpsTracker = new GPSTracker(MyAccount.this);
            if (gpsTracker.canGetLocation()) {
//                MyPreferences.setPref(SplashScreenActivity.this, MyPreferences.LAST_LATITUDE, "" + gpsTracker.getLatitude());
//                MyPreferences.setPref(SplashScreenActivity.this, MyPreferences.LAST_LONGITUDE, "" + gpsTracker.getLongitude());

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                SharedPreferences shared_loc = getSharedPreferences("Location", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor_loc = shared_loc.edit();

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

//                setSplashTime();

            } else {
                DialogInterface.OnCancelListener onCancelListener = new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                };
                gpsTracker.showSettingsAlert(onCancelListener);
            }
        }
        catch (Exception e)
        {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(chkResume) {

            chkResume = false;

            if (isPermission()) {
                loc_check.setChecked(true);
            } else {
                loc_check.setChecked(false);
            }
        }

        if(chkCardDeleted)
        {
            chkCardDeleted = false;

            chkCreditDb();
        }
    }

    @Override
    public void onBackPressed() {


        System.out.println(":::::::: chkBack:::: " + chkBack);

        ConnectionDetector cd = new ConnectionDetector(MyAccount.this);
        final Boolean isInternetPresent = cd.isConnectingToInternet();

        if(isInternetPresent) {

            if (chkBack) {

                chkBack = true;

                System.out.println("::::::::::::::onBACK FROM ACC::::::::::::::");
                SharedPreferences locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

                String latitude = locationPrefs.getString("LATITUDE", "");
                String longitude = locationPrefs.getString("LONGITUDE", "");

                progressDialog = ProgressDialog.show(MyAccount.this, "", "Please wait.....");

                String URL = Constant.GET_ALL_DEALS + "lang=" + sLang + "&latitude=" + latitude + "&longitude=" + longitude + "&radius=50";
                DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "ACCOUNT");
                asyncTask.execute();
            }
        }
        else
        {
            finish();
        }

//        super.onBackPressed();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
