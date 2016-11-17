package com.mi.asytask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mi.activities.DealActivity;
import com.mi.activities.My_PurchaseActivity;
import com.mi.activities.PaymentScreen;
import com.mi.activities.ProceedCheckout;
import com.mi.activities.RedeemDeal;
import com.mi.metadata.MyPurchaseMetadata;
import com.mi.metadata.SuccessMetadata;
import com.mi.test.mypiece.R;
import com.mi.parsers.MyPurchaseParser;
import com.mi.parsers.SuccessParser;

import java.util.Vector;

/**
 * Created by karandsingh on 16-09-15.
 */
public class RedeemDealAsyncTask extends AsyncTask<String, Integer, Void> {

    ProgressDialog progressDialog;
    Context context;
    String url;
    public static SuccessMetadata successMetadata;


    public RedeemDealAsyncTask(Context context, String url, ProgressDialog progressDialog) {

        this.context = context;
        this.progressDialog = progressDialog;
        this.url = url;
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
            successMetadata = parser.parsePaymentVector(url);

        } catch (Exception ex) {

        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        } else {
        }

        if(successMetadata!=null) {
            if (successMetadata.getStatus().equals("success")) {
                popup();
            } else {
                Toast.makeText(context, context.getString(R.string.Toast_Redeem_Failure), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(context, context.getString(R.string.Toast_Redeem_Failure), Toast.LENGTH_SHORT).show();
        }
    }

    public void popup() {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_thanks_redeem);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            LinearLayout btnOk = (LinearLayout) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    if(RedeemDeal.instance != null)
                        RedeemDeal.instance.finish();

                    if(My_PurchaseActivity.instance != null)
                        My_PurchaseActivity.instance.finish();

                }
            });

            dialog.show();
        } catch (Exception ex) {

            Toast.makeText(context, context.getString(R.string.Error_Encounter),
                    Toast.LENGTH_LONG).show();
        }
    }
    }
