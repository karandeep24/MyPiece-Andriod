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
import com.mi.common.Constant;
import com.mi.metadata.CreditCardMetadata;
import com.mi.metadata.LoginResponseMetadata;
import com.mi.test.mypiece.R;
import com.mi.parsers.CreateCardParser;
import com.mi.utility.DbHelper;

import java.util.Vector;

/**
 * Created by karandsingh on 16-09-13.
 */
public class AddCreditCardAsyncTask extends AsyncTask {

    ProgressDialog progressDialog;
    Context context;
    String url;
    String dataString;
    LoginResponseMetadata loginResponseMetadata = new LoginResponseMetadata();
    CreditCardMetadata creditCardMetadata = new CreditCardMetadata();
    String emailID, Route;

    public AddCreditCardAsyncTask(Context context, String url, String dataString, ProgressDialog progressDialog,
                                  String emailID, String Route) {

        this.context = context;
        this.progressDialog = progressDialog;
        this.url = url;
        this.dataString = dataString;
        this.emailID = emailID;
        this.Route = Route;
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

        CreateCardParser parser = new CreateCardParser();
        loginResponseMetadata = parser.parseLoginVector(url, dataString);

        System.out.println("::::::::::::loginResponseMetadata:::::::::::::::::::" + loginResponseMetadata.getStatus());

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (progressDialog.isShowing())
        {
            progressDialog.dismiss();
        } else {
        }

        try
        {
            if (loginResponseMetadata != null) {
                if(loginResponseMetadata.getStatus().equals("success")) {

                    AddCreditCard.saveDisable = false;

                    SharedPreferences sharepreferences = context.getSharedPreferences("User_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharepreferences.edit();

                    editor.putString("EMAIL", emailID);
                    editor.commit();

                    Vector<CreditCardMetadata> creditCardMetadataVector = new Vector<CreditCardMetadata>();
                    creditCardMetadataVector = loginResponseMetadata.getCreditCardMetadata();

                    DbHelper dbHelper = new DbHelper(context);

                    for(int i=0; i<creditCardMetadataVector.size(); i++) {

                        creditCardMetadata = creditCardMetadataVector.get(i);

                        dbHelper.Create_table();

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

                    Toast.makeText(context, context.getString(R.string.Toast_Add_CardAdded), Toast.LENGTH_SHORT).show();
                    if(AddCreditCard.instance == null)
                    {}else{AddCreditCard.instance.finish();}

                    if(MyAccount.instance == null)
                    {}else{MyAccount.instance.finish();}

                    if(Route.equals("ACCOUNT")) {
                        Intent intent = new Intent(context, MyAccount.class);
                        context.startActivity(intent);
                    }
                    else if(Route.equals("PAYMENT"))
                    {
                        PaymentScreen.fill_spinner();
                    }

                }
                else
                {
                    if (progressDialog.isShowing())
                    {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(context, loginResponseMetadata.getMessage(), Toast.LENGTH_SHORT).show();
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
