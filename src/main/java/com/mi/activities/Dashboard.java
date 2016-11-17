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
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mi.adapters.DashboardListAdapter;
import com.mi.asytask.DashBoardAsyncTask;
import com.mi.asytask.MyPurchasesAsyncTask;
import com.mi.common.Constant;
import com.mi.test.mypiece.R;
import com.mi.utility.DbHelper;
import com.mi.webapi.ConnectionDetector;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements View.OnClickListener,
        NavigationView.OnNavigationItemSelectedListener, SwipeRefreshLayout.OnRefreshListener,
        GoogleApiClient.OnConnectionFailedListener
{
    public static DashboardListAdapter adapter_rv;
    Intent intent;
    public static RecyclerView recyclerView;
    public static List<String> albumList;
    public static Dashboard instance = null;
    public static SwipeRefreshLayout swipeRefreshLayout;
    static LinearLayout noDealLay;
    public static Context context;
    SharedPreferences userPreferences, langPreferences;
    String sUserID, sLang;
    ProgressDialog progressDialog;
    static boolean dashChkResume = false;
    Intent dashIntent;
    private GoogleApiClient mGoogleApiClient;
    static TextView nextDealDay, nextDealDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        instance = this;
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
        navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list);

        noDealLay = (LinearLayout) findViewById(R.id.noDealLay);
        nextDealDate = (TextView) findViewById(R.id.nextDealDate);
        nextDealDay = (TextView) findViewById(R.id.nextDealDay);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        userPreferences = context.getSharedPreferences("User_id", Context.MODE_PRIVATE);

        sUserID = userPreferences.getString("user_id_value", "");
        langPreferences = this.getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        sLang = langPreferences.getString("Language", "");


        if(DashBoardAsyncTask.dashboardVector.size() != 0) {

                setUpList();

        }
    }

    private void init() {
        if(isPermission()){
            Intent intent = new Intent(Dashboard.this, MapsActivity.class);
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

                //Displaying a toast
                Toast.makeText(this, getString(R.string.Toast_Location_Granted),Toast.LENGTH_LONG).show();
            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(this, getString(R.string.Toast_Location_Denied),Toast.LENGTH_LONG).show();
            }


        }
    }

    public static void setUpList() {

        if(DashBoardAsyncTask.dashboardVector.get(0).getStatus() != null)
        {
            if(DashBoardAsyncTask.dashboardVector.get(0).getStatus().equals("failure"))
            {
                Toast.makeText(context, context.getString(R.string.Toast_No_Deals), Toast.LENGTH_SHORT).show();
                noDealLay.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                nextDealDate.setText(DashBoardAsyncTask.dashboardVector.get(0).getNext_active_time());
                nextDealDay.setText(DashBoardAsyncTask.dashboardVector.get(0).getNext_active_day());

            }
        }
        else{

            noDealLay.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            albumList = new ArrayList<>();
            adapter_rv = new DashboardListAdapter(context, albumList);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter_rv);
        }
    }

    @Override
    public void finish() {
        super.finish();

        instance = null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        boolean isNetwork = new ConnectionDetector(getApplicationContext()).isConnectingToInternet();

        if (isNetwork) {


            if (id == R.id.nav_home) {
                // Handle the camera action
            } else if (id == R.id.nav_purchases) {

                progressDialog = ProgressDialog.show(Dashboard.this, "", "Please wait.....");
                String URL = Constant.MY_PURCHASE + "UserId=" + sUserID + "&Lang=" + sLang;
                MyPurchasesAsyncTask asyncTask = new MyPurchasesAsyncTask(context, URL, progressDialog);
                asyncTask.execute();


            } else if (id == R.id.nav_account) {
                intent = new Intent(Dashboard.this, MyAccount.class);
                startActivity(intent);

            } else if (id == R.id.nav_info) {

                Intent intent = new Intent(Dashboard.this, GetinTouchActivity.class);
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

                    Intent intent = new Intent(Dashboard.this, RegisterActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
        else {
            Toast.makeText(Dashboard.this, getString(R.string.No_Connection), Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                        Intent intent = new Intent(Dashboard.this, RegisterActivity.class);
                        startActivity(intent);
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

                if(DashBoardAsyncTask.dashboardVector.get(0).getStatus() != null) {
                    if (DashBoardAsyncTask.dashboardVector.get(0).getStatus().equals("failure")) {
                        Toast.makeText(context, getString(R.string.Toast_No_Deals), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Intent intent = new Intent(Dashboard.this, MapsActivity.class);
                    startActivity(intent);
                    break;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {

        ConnectionDetector cd = new ConnectionDetector(Dashboard.this);
        final Boolean isInternetPresent = cd.isConnectingToInternet();

        if(isInternetPresent) {

            SharedPreferences locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

            String latitude = locationPrefs.getString("LATITUDE", "");
            String longitude = locationPrefs.getString("LONGITUDE", "");

            progressDialog = ProgressDialog.show(Dashboard.this, "", "Please wait.....");

            String URL = Constant.GET_ALL_DEALS + "lang=" + sLang + "&latitude=" + latitude + "&longitude=" + longitude + "&radius=50";
            DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "REFRESH");
            asyncTask.execute();
        }
        else
        {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getApplicationContext(), getString(R.string.No_Connection),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(dashChkResume)
        {
            dashChkResume = false;

            System.out.println("::::::::::::::onResume::::::::::::::");
            SharedPreferences locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

            String latitude = locationPrefs.getString("LATITUDE", "");
            String longitude = locationPrefs.getString("LONGITUDE", "");

            progressDialog = ProgressDialog.show(Dashboard.this, "", "Please wait.....");

            String URL = Constant.GET_ALL_DEALS + "lang=" + sLang + "&latitude=" + latitude + "&longitude="+ longitude + "&radius=50";
            DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "REFRESH");
            asyncTask.execute();

        }


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}