package com.mi.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mi.adapters.My_PagerAdapter;
import com.mi.asytask.DashBoardAsyncTask;
import com.mi.asytask.MyPurchasesAsyncTask;
import com.mi.common.Constant;
import com.mi.test.mypiece.R;
import com.mi.utility.DbHelper;
import com.mi.webapi.ConnectionDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karandsingh on 16-09-16.
 */
public class My_PurchaseActivity  extends FragmentActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.OnConnectionFailedListener
{
    Intent intent;
    int size;
    LinearLayout noPagerLay;
    FrameLayout pagerLay;
    TextView total_count, count_pager;
    public static My_PurchaseActivity instance = null;
    public static int dealId, campaignID;
    Context context;
    My_PagerAdapter my_pagerAdapter;
    List<Fragment> fragments;
    private GoogleApiClient mGoogleApiClient;
    String sLang;
    SharedPreferences langPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_purchase_pager);

        instance = this;
        context = this;

        dealId = 0;
        langPreferences = this.getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        sLang = langPreferences.getString("Language", "en");

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
        navigationView.setCheckedItem(R.id.nav_purchases);
        navigationView.setNavigationItemSelectedListener(this);

        pagerLay = (FrameLayout) findViewById(R.id.pagerLay);
        noPagerLay = (LinearLayout) findViewById(R.id.noPagerLay);
        count_pager = (TextView) findViewById(R.id.count_pager);
        total_count = (TextView) findViewById(R.id.total_count);


        //sliderSetup();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        size = MyPurchasesAsyncTask.myPurchaseMetadata.size();



        if(size == 0)
        {
            noPagerLay.setVisibility(View.VISIBLE);
            pagerLay.setVisibility(View.GONE);
        }
        else {
            noPagerLay.setVisibility(View.GONE);
            pagerLay.setVisibility(View.VISIBLE);


            fragments = getFragments();
            my_pagerAdapter = new My_PagerAdapter(getSupportFragmentManager(), fragments);
            ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
            pager.setAdapter(my_pagerAdapter);

            total_count.setText(String.valueOf(size));
            campaignID = Integer.parseInt(MyPurchasesAsyncTask.myPurchaseMetadata.get(0).getiMyDealId());

            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    System.out.println(":::::::::::::::view change::::::::::::::::" + position);
                    dealId = position;
                    campaignID = Integer.parseInt(MyPurchasesAsyncTask.myPurchaseMetadata.get(position).getiMyDealId());

                    count_pager.setText(String.valueOf(position+1));

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }

    private List<Fragment> getFragments(){

        List<Fragment> fList = new ArrayList<Fragment>();

        for(int i = 0; i < size; i++) {

            fList.add(RedeemFragment.newInstance(MyPurchasesAsyncTask.myPurchaseMetadata.get(i), this, i));
        }
        return fList;

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        boolean isNetwork = new ConnectionDetector(getApplicationContext()).isConnectingToInternet();

        if (isNetwork) {

            if (id == R.id.nav_home) {
                drawer.closeDrawer(GravityCompat.START);
                ConnectionDetector cd = new ConnectionDetector(My_PurchaseActivity.this);
                final Boolean isInternetPresent = cd.isConnectingToInternet();

                if(isInternetPresent) {


                    SharedPreferences locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

                    String latitude = locationPrefs.getString("LATITUDE", "");
                    String longitude = locationPrefs.getString("LONGITUDE", "");

                    ProgressDialog progressDialog = ProgressDialog.show(My_PurchaseActivity.this, "", "Please wait.....");

                    String URL = Constant.GET_ALL_DEALS + "lang=" + sLang + "&latitude=" + latitude + "&longitude=" + longitude + "&radius=10";
                    DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "ACCOUNT");
                    asyncTask.execute();

                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.No_Connection),
                            Toast.LENGTH_SHORT).show();
                }
            }
            else if (id == R.id.nav_purchases) {
                drawer.closeDrawer(GravityCompat.START);

            } else if (id == R.id.nav_account) {
                drawer.closeDrawer(GravityCompat.START);
                intent = new Intent(My_PurchaseActivity.this, MyAccount.class);
                startActivity(intent);
                finish();

            } else if (id == R.id.nav_info) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(My_PurchaseActivity.this, GetinTouchActivity.class);
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

                    Intent intent = new Intent(My_PurchaseActivity.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                    if (Dashboard.instance == null) {
                    } else {
                        Dashboard.instance.finish();
                    }
                }


            }
        }
        else
        {

        }
        return false;
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        Intent intent = new Intent(My_PurchaseActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finish();
                        if(Dashboard.instance == null)
                        {}else{Dashboard.instance.finish();}

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
            Intent intent = new Intent(My_PurchaseActivity.this, MapsActivity.class);
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
    public void onBackPressed() {


        ConnectionDetector cd = new ConnectionDetector(My_PurchaseActivity.this);
        final Boolean isInternetPresent = cd.isConnectingToInternet();

        if(isInternetPresent) {

                SharedPreferences locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

                String latitude = locationPrefs.getString("LATITUDE", "");
                String longitude = locationPrefs.getString("LONGITUDE", "");

                ProgressDialog progressDialog = ProgressDialog.show(My_PurchaseActivity.this, "", "Please wait.....");

                String URL = Constant.GET_ALL_DEALS + "lang=" + sLang + "&latitude=" + latitude + "&longitude=" + longitude + "&radius=50";
                DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "ACCOUNT");
                asyncTask.execute();
        }
        else
        {
            Toast.makeText(My_PurchaseActivity.this, getString(R.string.No_Connection), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
