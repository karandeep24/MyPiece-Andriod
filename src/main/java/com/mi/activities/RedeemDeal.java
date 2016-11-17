package com.mi.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.asytask.MyPurchasesAsyncTask;
import com.mi.asytask.RedeemDealAsyncTask;
import com.mi.common.Constant;
import com.mi.metadata.AddOnsMetadata;
import com.mi.metadata.MyPurchaseMetadata;
import com.mi.test.mypiece.R;
import com.mi.webapi.ConnectionDetector;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Vector;

/**
 * Created by karandsingh on 16-09-16.
 */
public class RedeemDeal extends AppCompatActivity implements View.OnClickListener{

    Intent intent;
    int dealId;
    TextView nameDeal, dealCount, mainDealPrice, nameAddOnOne, addOnOneCount, addOnOnePrice, nameAddOnTwo;
    TextView addOnTwoCount, addOnTwoPrice, nameAddOnThree, addOnThreeCount, addOnThreePrice, restroName, textview_fineprint;
    TextView nameQST, nameGST, nameTotal, priceQST, priceGST, priceTotal;
    RelativeLayout addOnOne, addOnTwo, addOnThree;
    MyPurchaseMetadata metadata;
    LinearLayout lay_RedeemBtn, lay_RedeemStaff, btnRedeem, noBtn, yesBtn, deal_desc;
    Context context;
    ProgressDialog progressDialog;
    public static RedeemDeal instance = null;
    Vector<AddOnsMetadata> addOnsMetadataVector = new Vector<AddOnsMetadata>();
    double dMainDealPrice, dAddOnOnePrice, dAddOnTwoPrice, dAddOnThreePrice;
    double dtotal, qsTax, gsTax, qsTaxVal, gsTaxVal;
    SharedPreferences preferences;
    DecimalFormat df = new DecimalFormat("0.00");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.bottom_in, R.anim.static_anim);
        setContentView(R.layout.redeem_deal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);

        context = this;
        instance = this;

        intent = getIntent();
        dealId = intent.getIntExtra("DEALID", 0);

        initialize();

//        preferences = context.getSharedPreferences("tax", Context.MODE_PRIVATE);
//        String temp = preferences.getString("qst", "");
//        qsTax = Double.parseDouble(temp);
//        temp = preferences.getString("gst", "");
//        gsTax = Double.parseDouble(temp);


        int size = MyPurchasesAsyncTask.myPurchaseMetadata.size();
        metadata = MyPurchasesAsyncTask.myPurchaseMetadata.get(dealId);

        qsTax = Double.parseDouble(metadata.getQst());
        gsTax = Double.parseDouble(metadata.getGst());

        fill_detail();

        dtotal = dMainDealPrice + dAddOnOnePrice + dAddOnTwoPrice + dAddOnThreePrice;

        calculateTax();
    }

    public void initialize(){

        nameDeal = (TextView) findViewById(R.id.nameDeal);
        dealCount = (TextView) findViewById(R.id.dealCount);
        mainDealPrice = (TextView) findViewById(R.id.mainDealPrice);
        nameAddOnOne = (TextView) findViewById(R.id.nameAddOnOne);
        addOnOneCount = (TextView) findViewById(R.id.addOnOneCount);
        addOnOnePrice = (TextView) findViewById(R.id.addOnOnePrice);
        nameAddOnTwo = (TextView) findViewById(R.id.nameAddOnTwo);
        addOnTwoCount = (TextView) findViewById(R.id.addOnTwoCount);
        addOnTwoPrice = (TextView) findViewById(R.id.addOnTwoPrice);
        nameAddOnThree = (TextView) findViewById(R.id.nameAddOnThree);
        addOnThreeCount = (TextView) findViewById(R.id.addOnThreeCount);
        addOnThreePrice = (TextView) findViewById(R.id.addOnThreePrice);
        nameQST = (TextView) findViewById(R.id.nameQST);
        nameGST = (TextView) findViewById(R.id.nameGST);
        nameTotal = (TextView) findViewById(R.id.nameTotal);
        priceQST = (TextView) findViewById(R.id.priceQST);
        priceGST = (TextView) findViewById(R.id.priceGST);
        priceTotal = (TextView) findViewById(R.id.priceTotal);
        restroName = (TextView) findViewById(R.id.toolbar_title);
        textview_fineprint = (TextView) findViewById(R.id.textview_fineprint);

        lay_RedeemStaff = (LinearLayout) findViewById(R.id.lay_RedeemStaff);
        lay_RedeemBtn = (LinearLayout) findViewById(R.id.lay_RedeemBtn);
        btnRedeem = (LinearLayout) findViewById(R.id.btnRedeem);
        noBtn = (LinearLayout) findViewById(R.id.noBtn);
        yesBtn = (LinearLayout) findViewById(R.id.yesBtn);
        deal_desc = (LinearLayout) findViewById(R.id.deal_desc);

        addOnOne = (RelativeLayout) findViewById(R.id.layoutAddOnOne);
        addOnTwo = (RelativeLayout) findViewById(R.id.layoutAddOnTwo);
        addOnThree = (RelativeLayout) findViewById(R.id.layoutAddOnThree);


        lay_RedeemBtn.setVisibility(View.VISIBLE);
        deal_desc.setVisibility(View.VISIBLE);
        lay_RedeemStaff.setVisibility(View.GONE);

        btnRedeem.setOnClickListener(this);
        noBtn.setOnClickListener(this);
        yesBtn.setOnClickListener(this);

    }

    public void fill_detail()
    {
        int mainQt = 1;


        restroName.setText(metadata.getvName());
        textview_fineprint.setText(metadata.getvFinePrint());

        nameDeal.setText(metadata.getvCampaignOption());
        dealCount.setText(metadata.getiQuantity());

        mainQt = Integer.parseInt(metadata.getiQuantity());
        dMainDealPrice = Double.parseDouble(metadata.getdPrice());

        dMainDealPrice = mainQt * dMainDealPrice;
        String txt_dMainDealPrice = df.format(dMainDealPrice);
        mainDealPrice.setText("$" + txt_dMainDealPrice);

        addOnsMetadataVector = metadata.getAddOns();

        if(addOnsMetadataVector != null) {
            int size = addOnsMetadataVector.size();

            switch (size) {
                case 0:
                    addOnOne.setVisibility(View.GONE);
                    addOnTwo.setVisibility(View.GONE);
                    addOnThree.setVisibility(View.GONE);
                    break;

                case 1:
                    addOnOneDetails(addOnsMetadataVector.get(0));
                    addOnTwo.setVisibility(View.GONE);
                    addOnThree.setVisibility(View.GONE);
                    break;

                case 2:
                    addOnOneDetails(addOnsMetadataVector.get(0));
                    addOnTwoDetails(addOnsMetadataVector.get(1));
                    addOnThree.setVisibility(View.GONE);
                    break;

                case 3:
                    addOnOneDetails(addOnsMetadataVector.get(0));
                    addOnTwoDetails(addOnsMetadataVector.get(1));
                    addOnThreeDetails(addOnsMetadataVector.get(2));
                    break;
            }
        }
        else
        {
            addOnOne.setVisibility(View.GONE);
            addOnTwo.setVisibility(View.GONE);
            addOnThree.setVisibility(View.GONE);
        }
    }

    public void addOnOneDetails(AddOnsMetadata addOnsMetadata)
    {
        int count = 0;

        count = Integer.parseInt(addOnsMetadata.getiQuantity());

        if(count == 0)
        {
            addOnOne.setVisibility(View.GONE);
        }
        else
        {
            addOnOne.setVisibility(View.VISIBLE);
            nameAddOnOne.setText(addOnsMetadata.getvAddOnName());
            addOnOneCount.setText(String.valueOf(count));
            String price = addOnsMetadata.getdAddOnPrice();
            dAddOnOnePrice = Double.parseDouble(price);
            dAddOnOnePrice = dAddOnOnePrice * count;
            String txt_dAddOnOnePrice = df.format(dAddOnOnePrice);
            addOnOnePrice.setText("$" + txt_dAddOnOnePrice);

        }
    }

    public void addOnTwoDetails(AddOnsMetadata addOnsMetadata)
    {
        int count = 0;

        count = Integer.parseInt(addOnsMetadata.getiQuantity());

        if(count == 0)
        {
            addOnTwo.setVisibility(View.GONE);
        }
        else
        {
            addOnTwo.setVisibility(View.VISIBLE);
            nameAddOnTwo.setText(addOnsMetadata.getvAddOnName());
            addOnTwoCount.setText(String.valueOf(count));
            String price = addOnsMetadata.getdAddOnPrice();
            dAddOnTwoPrice = Double.parseDouble(price);
            dAddOnTwoPrice = dAddOnTwoPrice * count;
            String txt_dAddOnTwoPrice = df.format(dAddOnTwoPrice);
            addOnTwoPrice.setText("$" + txt_dAddOnTwoPrice);

        }
    }

    public void addOnThreeDetails(AddOnsMetadata addOnsMetadata)
    {
        int count = 0;

        count = Integer.parseInt(addOnsMetadata.getiQuantity());

        if(count == 0)
        {
            addOnThree.setVisibility(View.GONE);
        }
        else
        {
            addOnThree.setVisibility(View.VISIBLE);
            nameAddOnThree.setText(addOnsMetadata.getvAddOnName());
            addOnThreeCount.setText(String.valueOf(count));
            String price = addOnsMetadata.getdAddOnPrice();
            dAddOnThreePrice = Double.parseDouble(price);
            dAddOnThreePrice = dAddOnThreePrice * count;
            String txt_dAddOnTwoPrice = df.format(dAddOnTwoPrice);
            addOnThreePrice.setText("$" + txt_dAddOnTwoPrice);

        }
    }

    public void popup() {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_warning);
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);

            // dialog.setTitle(dialogHeader);

            LinearLayout btnContinue = (LinearLayout) dialog.findViewById(R.id.btnContinue);

            btnContinue.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();

                    lay_RedeemBtn.setVisibility(View.GONE);
                    deal_desc.setVisibility(View.GONE);
                    lay_RedeemStaff.setVisibility(View.VISIBLE);


                }
            });

            dialog.show();
        } catch (Exception ex) {

            Toast.makeText(context, getString(R.string.Error_Encounter),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {

        if(v == btnRedeem)
        {
            popup();
        }
        else if(v == noBtn)
        {
            lay_RedeemBtn.setVisibility(View.VISIBLE);
            deal_desc.setVisibility(View.VISIBLE);
            lay_RedeemStaff.setVisibility(View.GONE);
        }
        else if(v == yesBtn)
        {
            ConnectionDetector cd = new ConnectionDetector(RedeemDeal.this);
            final Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent) {

                String URL = Constant.REDEEM + "myDealId=" + metadata.getiMyDealId();
                progressDialog = ProgressDialog.show(RedeemDeal.this, "", "Please wait.....");

                RedeemDealAsyncTask asyncTask = new RedeemDealAsyncTask(context, URL, progressDialog);
                asyncTask.execute();
            }
            else
            {
                Toast.makeText(getApplicationContext(), getString(R.string.No_Connection),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void calculateTax()
    {
        qsTaxVal = (qsTax*dtotal)/100;
        gsTaxVal = (gsTax*dtotal)/100;

        String sQST = df.format(qsTaxVal);
        String sGST = df.format(gsTaxVal);

        priceGST.setText( "$" + sGST);
        priceQST.setText( "$" + sQST);

        dtotal = dtotal + qsTaxVal + gsTaxVal;

        String sTotal = df.format(dtotal);
        priceTotal.setText("$" + sTotal);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
