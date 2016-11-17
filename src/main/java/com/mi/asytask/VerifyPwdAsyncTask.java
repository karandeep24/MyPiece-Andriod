package com.mi.asytask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.mi.activities.ForgotPassword;
import com.mi.metadata.SuccessMetadata;
import com.mi.test.mypiece.R;
import com.mi.parsers.SuccessParser;
import com.mi.webapi.Mypiece_Api;

/**
 * Created by karandsingh on 16-08-31.
 */
public class VerifyPwdAsyncTask extends AsyncTask<String, Integer, Void> {

    ProgressDialog progressDialog;
    Context context;
    String url;
    String dataString;
    SuccessMetadata successMetadata;
    SharedPreferences sharepreferences;
    SharedPreferences.Editor editor;

    public VerifyPwdAsyncTask(Context context, String url, String dataString, ProgressDialog progressDialog) {

        this.context = context;
        this.progressDialog = progressDialog;
        this.url = url;
        this.dataString = dataString;
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
        } else
        {}

        if (Mypiece_Api.isOnline == false)
        {
            Toast.makeText(context, context.getString(R.string.No_Connection),
                    Toast.LENGTH_SHORT).show();

        } else
        {
            try
            {
                if (successMetadata != null) {
                    if(successMetadata.getStatus().equals("success")) {

                        Toast.makeText(context, successMetadata.getMessage(), Toast.LENGTH_SHORT).show();

                        if(ForgotPassword.instance==null)
                        {}else{ForgotPassword.instance.finish();}

                    }else
                    {
                        Toast.makeText(context, successMetadata.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
