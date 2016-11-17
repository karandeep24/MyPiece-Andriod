package com.mi.asytask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.mi.activities.Dashboard;
import com.mi.activities.DealActivity;
import com.mi.activities.Login_activity;
import com.mi.activities.MyAccount;
import com.mi.activities.My_PurchaseActivity;
import com.mi.activities.RegisterActivity;
import com.mi.activities.SignUpActivity;
import com.mi.activities.SplashScreenActivity;
import com.mi.metadata.DashboardMetadata;
import com.mi.test.mypiece.R;
import com.mi.parsers.DashboardParsers;
import java.util.Vector;

public class DashBoardAsyncTask extends AsyncTask<String, Integer, Void> {

    private ProgressDialog progressDialog;
    private Context context;
    public static Vector<DashboardMetadata> dashboardVector = new Vector<DashboardMetadata>();
    Boolean excpBool = false;
    String url, route;

    public DashBoardAsyncTask(Context context, String url, ProgressDialog progressDialog, String route) {

        this.context = context;
        this.progressDialog = progressDialog;
        this.url = url;
        this.route = route;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        try {

            if (this.progressDialog.isShowing()) {

            }
        }
        catch (Exception ex)
        {
            System.out.println("::::::::Exception::::::::::::::");
        }
    }

    @Override
    protected Void doInBackground(String... arg) {

        try {

            DashboardParsers parser = new DashboardParsers();
            dashboardVector = parser.parseLeaveVector(url);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            excpBool = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        try {

            if (!excpBool) {

                if (route.equals("REFRESH")) {
                    this.progressDialog.dismiss();
                    Dashboard.setUpList();

                    SharedPreferences sharepreferences = context.getSharedPreferences("tax", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharepreferences.edit();
                    try {
                        editor.putString("gst", dashboardVector.get(0).getdGST());
                        editor.putString("qst", dashboardVector.get(0).getdQST());

                        editor.commit();

                    } catch (Exception ex) {

                    }

                    Dashboard.swipeRefreshLayout.setRefreshing(false);
                } else if (route.equals("RESUME")) {
                    this.progressDialog.dismiss();

                    SharedPreferences sharepreferences = context.getSharedPreferences("tax", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharepreferences.edit();
                    try {
                        editor.putString("gst", dashboardVector.get(0).getdGST());
                        editor.putString("qst", dashboardVector.get(0).getdQST());

                        editor.commit();

                    } catch (Exception ex) {

                    }
                    Dashboard.setUpList();
                } else if(route.equals("NOTIFICATION_DEAL"))
                {
                    this.progressDialog.dismiss();

                    SharedPreferences sharepreferences = context.getSharedPreferences("tax", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharepreferences.edit();
                    try {
                        editor.putString("gst", dashboardVector.get(0).getdGST());
                        editor.putString("qst", dashboardVector.get(0).getdQST());

                        editor.commit();

                    } catch (Exception ex) {

                    }

                    Intent mainIntent = new Intent(context, DealActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mainIntent.putExtra("NOTIFICATION_ROUTE", "NOTIFICATION_DEAL");
                    mainIntent.putExtra("NOTIFICATION_DEAL_ID", SplashScreenActivity.notification_deal_value);
                    context.startActivity(mainIntent);

                    if (SplashScreenActivity.instance == null) {
                    } else {
                        SplashScreenActivity.instance.finish();
                    }

                }
                else {
                    try {
                        this.progressDialog.dismiss();

                        SharedPreferences sharepreferences = context.getSharedPreferences("tax", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharepreferences.edit();
                        try {
                            editor.putString("gst", dashboardVector.get(0).getdGST());
                            editor.putString("qst", dashboardVector.get(0).getdQST());

                            editor.commit();

                        } catch (Exception ex) {

                        }

                        Intent mainIntent = new Intent(context, Dashboard.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(mainIntent);

                        if (Login_activity.instance == null) {
                        } else {
                            Login_activity.instance.finish();
                        }

                        if (RegisterActivity.instance == null) {
                        } else {
                            RegisterActivity.instance.finish();
                        }

                        if (SplashScreenActivity.instance == null) {
                        } else {
                            SplashScreenActivity.instance.finish();
                        }

                        if (MyAccount.instance == null) {
                        } else {
                            MyAccount.instance.finish();
                        }

                        if (My_PurchaseActivity.instance == null) {
                        } else {
                            My_PurchaseActivity.instance.finish();
                        }

                        if (SignUpActivity.instance == null) {
                        } else {
                            SignUpActivity.instance.finish();
                        }

                        if (DealActivity.instance == null) {
                        } else {
                            DealActivity.instance.finish();
                        }

                    } catch (Exception ex)
                    {
                        if (progressDialog.isShowing())
                        {
                            progressDialog.dismiss();
                        }
                        Toast.makeText(context, context.getString(R.string.Error_Encounter), Toast.LENGTH_SHORT).show();
                        ex.printStackTrace();
                    }
                }
            } else {
                // Error Message
            }
        }
        catch (Exception e) {

            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            Toast.makeText(context, context.getString(R.string.Error_Encounter), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
