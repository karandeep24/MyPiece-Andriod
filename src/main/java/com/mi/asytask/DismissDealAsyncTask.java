package com.mi.asytask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mi.activities.AddCreditCard;
import com.mi.activities.MyAccount;
import com.mi.activities.My_PurchaseActivity;
import com.mi.activities.PaymentScreen;
import com.mi.common.Constant;
import com.mi.metadata.CreditCardMetadata;
import com.mi.metadata.LoginResponseMetadata;
import com.mi.metadata.SuccessMetadata;
import com.mi.test.mypiece.R;
import com.mi.parsers.CreateCardParser;
import com.mi.parsers.SuccessParser;
import com.mi.utility.DbHelper;

import java.util.Vector;

/**
 * Created by karandsingh on 16-09-13.
 */
public class DismissDealAsyncTask extends AsyncTask {

    ProgressDialog progressDialog;
    Context context;
    String url;
    SuccessMetadata successMetadata = new SuccessMetadata();
    String sUserID, sLang;

    public DismissDealAsyncTask(Context context, String url, ProgressDialog progressDialog, String sUserId, String sLang) {

        this.context = context;
        this.progressDialog = progressDialog;
        this.url = url;
        this.sLang = sLang;
        this.sUserID = sUserId;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

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
    protected Object doInBackground(Object[] params) {

        SuccessParser parser = new SuccessParser();
        successMetadata = parser.parsePaymentVector(url);

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        try
        {
            if (successMetadata != null) {
                if(successMetadata.getStatus().equals("success")) {

                    Toast.makeText(context, successMetadata.getMessage(), Toast.LENGTH_SHORT).show();

                    String URL = Constant.MY_PURCHASE + "UserId=" + sUserID + "&Lang=" + sLang;
                    MyPurchasesAsyncTask asyncTask = new MyPurchasesAsyncTask(context, URL, progressDialog);
                    asyncTask.execute();





                }
                else
                {
                    if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(context, successMetadata.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            if (progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            Toast.makeText(context, context.getString(R.string.Error_Encounter), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
