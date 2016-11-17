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
import com.mi.activities.PaymentScreen;
import com.mi.metadata.CreditCardMetadata;
import com.mi.metadata.LoginResponseMetadata;
import com.mi.metadata.SuccessMetadata;
import com.mi.parsers.CreateCardParser;
import com.mi.parsers.SuccessParser;
import com.mi.utility.DbHelper;

import java.util.Vector;

/**
 * Created by karandsingh on 16-09-13.
 */
public class AddDealViewAsyncTask extends AsyncTask {

    Context context;
    String url;
    String dataString;
    SuccessMetadata successMetadata;

    public AddDealViewAsyncTask(Context context, String url, String dataString) {

        this.context = context;
        this.url = url;
        this.dataString = dataString;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Object doInBackground(Object[] params) {

        SuccessParser parser = new SuccessParser();
        successMetadata = parser.parsePaymentVector(url, dataString);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }
}
