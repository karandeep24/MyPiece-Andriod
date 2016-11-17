package com.mi.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.asytask.DashBoardAsyncTask;
import com.mi.asytask.LoginAsyncTask;
import com.mi.asytask.PaaymentScreenAsyncTask;
import com.mi.common.Constant;
import com.mi.metadata.AddOnsMetadata;
import com.mi.metadata.CreditCardMetadata;
import com.mi.metadata.DashboardMetadata;
import com.mi.test.mypiece.R;

import com.mi.utility.DbHelper;
import com.mi.webapi.ConnectionDetector;
import com.stripe.android.*;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by karandsingh on 16-09-09.
 */
public class PaymentScreen extends AppCompatActivity implements View.OnClickListener {

    int iMainCount, iAddOnOneCount, iAddOnTwoCount, iAddOnThreeCount;
    Intent dealIntent;
    int dealPosition;
    DashboardMetadata dashboardMetadata;
    Vector<AddOnsMetadata> addOnsMetadata;
    static Spinner paymentCard;
    static TextView textPay;
    TextView nameDeal, dealCount, mainDealPrice, nameAddOnOne, addOnOneCount, addOnOnePrice, nameAddOnTwo;
    TextView addOnTwoCount, addOnTwoPrice, nameAddOnThree, addOnThreeCount, addOnThreePrice, restroName;
    TextView nameQST, nameGST, nameTotal, priceQST, priceGST, priceTotal;
    RelativeLayout addOnOne, addOnTwo, addOnThree;
    LinearLayout btnPay;
    static LinearLayout noCard;
    double dMainDealPrice, dAddOnOnePrice, dAddOnTwoPrice, dAddOnThreePrice;
    double dtotal, qsTax, gsTax, qsTaxVal, gsTaxVal;
    SharedPreferences preferences, userPreferences;
    static Context context;
    static DbHelper dbHelper;
    static HashMap<String, CreditCardMetadata> spinnerArray;
    static ArrayList<String> cardsArray = new ArrayList<String>();
    String sUserID, sCampaignId, sUserCardId, sCampaignQty;
    String AddOn1Id, AddOn1Qty, AddOn2Id, AddOn2Qty, AddOn3Id, AddOn3Qty;
    String dataString;
    public static PaymentScreen instance;
    ProgressDialog progressDialog;
    static boolean canPay = false;
    DecimalFormat df = new DecimalFormat("0.00");
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        setContentView(R.layout.deal_pay);

        context = this;
        instance = this;
        canPay = false;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        dfs.setDecimalSeparator('.');
        df.setDecimalFormatSymbols(dfs);


        preferences = context.getSharedPreferences("tax", Context.MODE_PRIVATE);
        userPreferences = context.getSharedPreferences("User_id", Context.MODE_PRIVATE);

        sUserID = userPreferences.getString("user_id_value", "");

        String temp = preferences.getString("qst", "");
        qsTax = Double.parseDouble(temp);
        temp = preferences.getString("gst", "");
        gsTax = Double.parseDouble(temp);

        dealIntent = getIntent();
        dealPosition = dealIntent.getIntExtra("DealPosition", 0);

        dashboardMetadata = DashBoardAsyncTask.dashboardVector.get(dealPosition);
        addOnsMetadata = dashboardMetadata.getAddOns();

        initialize();

        if(addOnsMetadata == null) {

            addOnOne.setVisibility(View.GONE);
            addOnTwo.setVisibility(View.GONE);
            addOnThree.setVisibility(View.GONE);
        }
        else
        {
            addOnOne.setVisibility(View.VISIBLE);
            addOnTwo.setVisibility(View.VISIBLE);
            addOnThree.setVisibility(View.VISIBLE);
        }

        iMainCount = ProceedCheckout.iMainCount;
        iAddOnOneCount = ProceedCheckout.iAddOnOneCount;
        iAddOnTwoCount = ProceedCheckout.iAddOnTwoCount;
        iAddOnThreeCount = ProceedCheckout.iAddOnThreeCount;

        sCampaignId = DashBoardAsyncTask.dashboardVector.get(dealPosition).getiCampaignId();


        displayDetails(dashboardMetadata);

        dbHelper = new DbHelper(context);
        fill_spinner();

        dtotal = dMainDealPrice + dAddOnOnePrice + dAddOnTwoPrice + dAddOnThreePrice;

        calculateTax();

    }

    public void initialize(){

        paymentCard = (Spinner) findViewById(R.id.paymentCard);

        textPay = (TextView) findViewById(R.id.textPay);
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

        addOnOne = (RelativeLayout) findViewById(R.id.layoutAddOnOne);
        addOnTwo = (RelativeLayout) findViewById(R.id.layoutAddOnTwo);
        addOnThree = (RelativeLayout) findViewById(R.id.layoutAddOnThree);

        btnPay = (LinearLayout) findViewById(R.id.btnPay);
        noCard = (LinearLayout) findViewById(R.id.noCard);

        btnPay.setOnClickListener(this);
        noCard.setOnClickListener(this);

    }

    public void displayDetails(DashboardMetadata dashboardMetadata)
    {
        try {
            restroName.setText(dashboardMetadata.getvRestaurantName());
            nameDeal.setText(dashboardMetadata.getvCampaignOption());
            dealCount.setText(String.valueOf(iMainCount));

            String dealPrice = dashboardMetadata.getdPrice();
            dMainDealPrice = Double.parseDouble(dealPrice.replace("$", ""));
            dMainDealPrice = dMainDealPrice * iMainCount;
            String txt_dMainDealPrice = df.format(dMainDealPrice);
            mainDealPrice.setText("$" + txt_dMainDealPrice);


            if(addOnsMetadata != null)
            {
                int size = addOnsMetadata.size();

                switch (size)
                {
                    case 1:
                        addOnOne.setVisibility(View.VISIBLE);
                        addOnTwo.setVisibility(View.GONE);
                        addOnThree.setVisibility(View.GONE);
                        addOnOneDetails(addOnsMetadata.get(0));

                        break;
                    case 2:
                        addOnOne.setVisibility(View.VISIBLE);
                        addOnTwo.setVisibility(View.VISIBLE);
                        addOnThree.setVisibility(View.GONE);
                        addOnOneDetails(addOnsMetadata.get(0));
                        addOnTwoDetails(addOnsMetadata.get(1));

                        break;
                    case 3:
                        addOnOne.setVisibility(View.VISIBLE);
                        addOnTwo.setVisibility(View.VISIBLE);
                        addOnThree.setVisibility(View.VISIBLE);
                        addOnOneDetails(addOnsMetadata.get(0));
                        addOnTwoDetails(addOnsMetadata.get(1));
                        addOnThreeDetails(addOnsMetadata.get(2));

                        break;
                }
            }
        }
        catch (Exception ex)
        {

        }
    }

    public void addOnOneDetails(AddOnsMetadata addOnsMetadata)
    {
        if(iAddOnOneCount > 0) {

            addOnOne.setVisibility(View.VISIBLE);

            nameAddOnOne.setText(addOnsMetadata.getvAddOnName());
            addOnOneCount.setText(String.valueOf(iAddOnOneCount));
            String price = addOnsMetadata.getdAddOnPrice();
            dAddOnOnePrice = Double.parseDouble(price);
            dAddOnOnePrice = dAddOnOnePrice * iAddOnOneCount;
            String txt_dAddOnOnePrice = df.format(dAddOnOnePrice);
            addOnOnePrice.setText("$" + txt_dAddOnOnePrice);
        }
        else
        {
            addOnOne.setVisibility(View.GONE);
        }

    }

    public void addOnTwoDetails(AddOnsMetadata addOnsMetadata)
    {
        if(iAddOnTwoCount > 0) {

            addOnTwo.setVisibility(View.VISIBLE);

            nameAddOnTwo.setText(addOnsMetadata.getvAddOnName());
            addOnTwoCount.setText(String.valueOf(iAddOnTwoCount));
            String price = addOnsMetadata.getdAddOnPrice();
            dAddOnTwoPrice = Double.parseDouble(price);
            dAddOnTwoPrice = dAddOnTwoPrice * iAddOnTwoCount;
            String txt_dAddOnTwoPrice = df.format(dAddOnTwoPrice);
            addOnTwoPrice.setText("$" + txt_dAddOnTwoPrice);
        }
        else
        {
            addOnTwo.setVisibility(View.GONE);

        }

    }

    public void addOnThreeDetails(AddOnsMetadata addOnsMetadata)
    {
        if(iAddOnThreeCount > 0 ) {

            addOnThree.setVisibility(View.VISIBLE);

            nameAddOnThree.setText(addOnsMetadata.getvAddOnName());
            addOnThreeCount.setText(String.valueOf(iAddOnThreeCount));
            String price = addOnsMetadata.getdAddOnPrice();
            dAddOnThreePrice = Double.parseDouble(price);
            dAddOnThreePrice = dAddOnThreePrice * iAddOnThreeCount;
            String txt_dAddOnThreePrice = df.format(dAddOnThreePrice);
            addOnThreePrice.setText("$" + txt_dAddOnThreePrice);
        }
        else
        {
            addOnThree.setVisibility(View.GONE);
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

    public static void fill_spinner()
    {

        try {
            spinnerArray = dbHelper.getcardRecords();
            cardsArray.clear();

            if(spinnerArray.size() > 0) {

                canPay = true;

                paymentCard.setVisibility(View.VISIBLE);
                noCard.setVisibility(View.GONE);

                Iterator it = spinnerArray.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    System.out.println(pair.getKey() + " = " + pair.getValue());
                    cardsArray.add(pair.getKey().toString());
                    it.remove(); // avoids a ConcurrentModificationException
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        context, android.R.layout.simple_spinner_item, cardsArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                paymentCard.setAdapter(adapter);
            }
            else {
                paymentCard.setVisibility(View.GONE);
                noCard.setVisibility(View.VISIBLE);
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stubs
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        textPay.setVisibility(View.INVISIBLE);
        ProceedCheckout.textProceedCheckout.setVisibility(View.VISIBLE);
    }

    public void purchase ()
    {
        try {

            spinnerArray = dbHelper.getcardRecords();

            sCampaignQty = String.valueOf(iMainCount);

            int id = paymentCard.getSelectedItemPosition();
            sUserCardId = spinnerArray.get(cardsArray.get(id)).getiUserCardId();

            if(addOnsMetadata != null) {
                int size = addOnsMetadata.size();

                switch (size) {
                    case 1:
                        AddOn1Id = addOnsMetadata.get(0).getiAddOnId();
                        AddOn1Qty = String.valueOf(iAddOnOneCount);

                        AddOn2Id = "";
                        AddOn2Qty = "";

                        AddOn3Id = "";
                        AddOn3Qty = "";

                        break;

                    case 2:
                        AddOn1Id = addOnsMetadata.get(0).getiAddOnId();
                        AddOn1Qty = String.valueOf(iAddOnOneCount);

                        AddOn2Id = addOnsMetadata.get(1).getiAddOnId();
                        AddOn2Qty = String.valueOf(iAddOnTwoCount);

                        AddOn3Id = "";
                        AddOn3Qty = "";

                        break;

                    case 3:
                        AddOn1Id = addOnsMetadata.get(0).getiAddOnId();
                        AddOn1Qty = String.valueOf(iAddOnOneCount);

                        AddOn2Id = addOnsMetadata.get(1).getiAddOnId();
                        AddOn2Qty = String.valueOf(iAddOnTwoCount);

                        AddOn3Id = addOnsMetadata.get(2).getiAddOnId();
                        AddOn3Qty = String.valueOf(iAddOnThreeCount);

                        break;

                }
            }
            else
            {
                AddOn1Id = "";
                AddOn1Qty = "";

                AddOn2Id = "";
                AddOn2Qty = "";

                AddOn3Id = "";
                AddOn3Qty = "";
            }

            dataString = getDataString();

            String URL = Constant.CHECKOUT;

            boolean isNetwork = new ConnectionDetector(getApplicationContext()).isConnectingToInternet();

            if (isNetwork) {

                progressDialog = ProgressDialog.show(PaymentScreen.this, "", "Please wait.....");

                PaaymentScreenAsyncTask asyncTask = new PaaymentScreenAsyncTask(context, URL, dataString, progressDialog,
                        dashboardMetadata.getvEndTime());
                asyncTask.execute();
            }
            else
            {
                Toast.makeText(PaymentScreen.this, getString(R.string.No_Connection), Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public String getDataString()
    {
        String Return="{\n\t";
        Return = Return + "\"UserId\":"      + "\""+ sUserID       +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"CampaignId\":"       + "\""+ sCampaignId        +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"UserCardId\":"       + "\""+ sUserCardId        +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"CampaignQty\":"       + "\""+ sCampaignQty        +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"AddOn1Id\":"       + "\""+ AddOn1Id        +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"AddOn1Qty\":"       + "\""+ AddOn1Qty        +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"AddOn2Id\":"       + "\""+ AddOn2Id        +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"AddOn2Qty\":"       + "\""+ AddOn2Qty        +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"AddOn3Id\":"       + "\""+ AddOn3Id        +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"AddOn3Qty\":"       + "\""+ AddOn3Qty        +"\"";
        Return = Return + "\n}";

        return Return;
    }

    @Override
    public void onClick(View v) {

        if(v == btnPay)
        {
            if(canPay) {
                purchase();
            }else
            {
                Toast.makeText(this, getString(R.string.Toast_Add_Card),Toast.LENGTH_LONG).show();
            }
        }
        else if(v == noCard)
        {
            Intent intent = new Intent(PaymentScreen.this, AddCreditCard.class);
            intent.putExtra("ROUTE", "PAYMENT");
            intent.putExtra("MODE", "ADD");
            startActivity(intent);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            textPay.setVisibility(View.INVISIBLE);
            ProceedCheckout.textProceedCheckout.setVisibility(View.VISIBLE);
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
