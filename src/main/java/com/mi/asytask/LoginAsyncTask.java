package com.mi.asytask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.mi.common.Constant;
import com.mi.metadata.CreditCardMetadata;
import com.mi.metadata.LoginResponseMetadata;
import com.mi.test.mypiece.R;
import com.mi.parsers.LoginParser;
import com.mi.utility.DbHelper;
import com.mi.webapi.Mypiece_Api;
import java.util.Vector;
import com.mi.utility.DbHelper;

/**
 * Created by karandsingh on 16-08-31.
 */
public class LoginAsyncTask extends AsyncTask<String, Integer, Void> {

    ProgressDialog progressDialog;
    Context context;
    String url;
    String dataString, emailID;
    LoginResponseMetadata loginResponseMetadata = new LoginResponseMetadata();
    SharedPreferences sharepreferences, locationPrefs;
    SharedPreferences.Editor editor;
    CreditCardMetadata creditCardMetadata;
    String sLang;

    public LoginAsyncTask(Context context, String url, String dataString, ProgressDialog progressDialog, String emailID, String sLang) {

        this.context = context;
        this.progressDialog = progressDialog;
        this.url = url;
        this.dataString = dataString;
        this.emailID = emailID;
        this.sLang = sLang;
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
            LoginParser parser = new LoginParser();
            loginResponseMetadata = parser.parseLoginVector(url, dataString);

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
            //progressDialog.dismiss();
        } else {
        }
        if (Mypiece_Api.isOnline == false)
        {
            Toast.makeText(context, context.getString(R.string.no_internet_connection),
                    Toast.LENGTH_SHORT).show();

        } else
        {
            sharepreferences = context.getSharedPreferences("User_id", Context.MODE_PRIVATE);
            editor = sharepreferences.edit();
            try
            {
                if (loginResponseMetadata != null) {
                    if(loginResponseMetadata.getStatus().equals("success")) {
                        editor.putString("user_id_value", loginResponseMetadata.getUserId());
                        editor.putString("isLogIn", "LOGIN");
                        editor.putString("EMAIL", emailID);

                        try {
                            if (loginResponseMetadata.getEmail() == null) {
                            } else {
                                editor.putString("EMAIL", loginResponseMetadata.getEmail());
                            }
                        }catch (Exception ex)
                        {

                        }

                        editor.commit();


                        Vector<CreditCardMetadata> creditCardMetadataVector = new Vector<CreditCardMetadata>();
                        creditCardMetadataVector = loginResponseMetadata.getCreditCardMetadata();

                        DbHelper dbHelper = new DbHelper(context);
                        dbHelper.Create_table();

                        if(creditCardMetadataVector != null) {


                            for (int i = 0; i < creditCardMetadataVector.size(); i++) {

                                creditCardMetadata = creditCardMetadataVector.get(i);

                                dbHelper.add(
                                        creditCardMetadata.getiUserCardId(),
                                        creditCardMetadata.getCardId(),
                                        creditCardMetadata.getCardName(),
                                        creditCardMetadata.getName(),
                                        creditCardMetadata.getBrand(),
                                        creditCardMetadata.getCountry(),
                                        creditCardMetadata.getExp_month(),
                                        creditCardMetadata.getExp_year(),
                                        creditCardMetadata.getLast4()
                                );
                                dbHelper.close();
                            }
                        }

                        locationPrefs = context.getSharedPreferences("Location", Context.MODE_PRIVATE);

                        String latitude = locationPrefs.getString("LATITUDE", "");
                        String longitude = locationPrefs.getString("LONGITUDE", "");

                        String URL = Constant.GET_ALL_DEALS + "lang=" + sLang + "&latitude=" + latitude + "&longitude="+ longitude + "&radius=50";
                        DashBoardAsyncTask asyncTask = new DashBoardAsyncTask(context, URL, progressDialog, "LOGIN");
                        asyncTask.execute();


                    }else
                    {
                        if (progressDialog.isShowing())
                        {
                            progressDialog.dismiss();
                        }

                        if(sLang.equals("en")) {
                            Toast.makeText(context, loginResponseMetadata.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else if(sLang.equals("fr"))
                        {
                            Toast.makeText(context, loginResponseMetadata.getMessage_fr(), Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(context, loginResponseMetadata.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                progressDialog.dismiss();

                Toast.makeText(context, context.getString(R.string.Error_Encounter),
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


}
