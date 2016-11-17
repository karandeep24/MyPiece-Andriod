package com.mi.asytask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mi.activities.AddCreditCard;
import com.mi.activities.MyAccount;
import com.mi.common.Constant;
import com.mi.metadata.CreditCardMetadata;
import com.mi.metadata.LoginResponseMetadata;
import com.mi.metadata.SuccessMetadata;
import com.mi.test.mypiece.R;
import com.mi.parsers.LoginParser;
import com.mi.parsers.SuccessParser;
import com.mi.utility.DbHelper;
import com.mi.webapi.Mypiece_Api;

import java.util.Vector;

/**
 * Created by karandsingh on 16-08-31.
 */
public class DeleteCardAsyncTask extends AsyncTask<String, Integer, Void> {

    ProgressDialog progressDialog;
    Context context;
    String url;
    String dataString, userCardID;
    SuccessMetadata successMetadata;
    SharedPreferences sharepreferences;
    SharedPreferences.Editor editor;

    public DeleteCardAsyncTask(Context context, String url, String dataString, ProgressDialog progressDialog, String userCardID) {

        this.context = context;
        this.progressDialog = progressDialog;
        this.url = url;
        this.dataString = dataString;
        this.userCardID = userCardID;
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
        try
        {
            SuccessParser parser = new SuccessParser();
            successMetadata = parser.parsePaymentVector(url, dataString);

        }
        catch(Exception e)
        {
            e.printStackTrace();

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);

        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        } else {
        }
        if (Mypiece_Api.isOnline == false)
        {
            Toast.makeText(context, context.getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT).show();

        } else
        {
            try
            {
                if (successMetadata != null) {
                    if(successMetadata.getStatus().equals("success")) {

                        MyAccount.chkCardDeleted = true;

                        DbHelper dbHelper = new DbHelper(context);
                        dbHelper.deleteCard(userCardID);

                        Toast.makeText(context, context.getString(R.string.Toast_Add_CardDeleted), Toast.LENGTH_SHORT).show();

                        if (AddCreditCard.instance == null)
                        {}else{AddCreditCard.instance.finish();}

                    }else
                    {
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


}
