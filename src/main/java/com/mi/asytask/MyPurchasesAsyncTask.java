package com.mi.asytask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.mi.activities.MyAccount;
import com.mi.activities.My_PurchaseActivity;
import com.mi.metadata.MyPurchaseMetadata;
import com.mi.parsers.MyPurchaseParser;

import java.util.Vector;

/**
 * Created by karandsingh on 16-09-15.
 */
public class MyPurchasesAsyncTask  extends AsyncTask<String, Integer, Void> {

    ProgressDialog progressDialog;
    Context context;
    String url;
    public static Vector<MyPurchaseMetadata> myPurchaseMetadata;
    String route;

    public MyPurchasesAsyncTask(Context context, String url, ProgressDialog progressDialog) {

        this.context = context;
        this.progressDialog = progressDialog;
        this.url = url;
//        this.route = route;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        try {
            super.onPreExecute();

            if (this.progressDialog.isShowing()) {

            } else {
                this.progressDialog.show();
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        // TODO Auto-generated method stub
        try {

            MyPurchaseParser parser = new MyPurchaseParser();
            myPurchaseMetadata = parser.parsePurchaseVector(url);

        } catch (Exception ex) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        } else {
        }

        if(My_PurchaseActivity.instance == null)
        {}else{My_PurchaseActivity.instance.finish();}

        Intent mainIntent = new Intent(context, My_PurchaseActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mainIntent);

        if(MyAccount.instance == null)
        {}else{MyAccount.instance.finish();}


    }
}
