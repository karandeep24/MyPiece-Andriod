package com.mi.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.asytask.DashBoardAsyncTask;
import com.mi.metadata.AddOnsMetadata;
import com.mi.metadata.DashboardMetadata;
import com.mi.test.mypiece.R;

import org.w3c.dom.Text;

import java.util.Vector;

/**
 * Created by karandsingh on 16-09-08.
 */
public class ProceedCheckout extends AppCompatActivity implements View.OnClickListener{

    TextView nameDeal, mainCount, addOnOneName, addOnOneCount, addOnTwoName, addOnTwoTextCount, addOnThreeTextCount;
    TextView addOnOneTextPrice, addOnTwoTextPrice, addOnThreeTextPrice, addOnThreeName, restroName, mainTextPrice;
    static TextView textProceedCheckout;
    LinearLayout mainSubtractButton, mainAddButton, addOnOneSubtractButton, addOnOneAddButton;
    LinearLayout addOnTwoSubtractButton, addOnTwoAddButton, addOnThreeSubtractButton, addOnThreeAddButton, btnProceed;
    RelativeLayout addOnOne, addOnTwo, addOnThree;
    Intent dealIntent;
    int dealPosition;
    DashboardMetadata dashboardMetadata;
    Vector<AddOnsMetadata> addOnsMetadata;
    static int iMainCount, iAddOnOneCount, iAddOnTwoCount, iAddOnThreeCount;
    public static ProceedCheckout instance;
    SharedPreferences checkoutPreferences;
    Context context;
    String checkOutPop;
    boolean checkPopFlag = false;
    boolean isCheckPopFlag= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        setContentView(R.layout.deal_checkout);

        instance = this;
        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        dealIntent = getIntent();
        dealPosition = dealIntent.getIntExtra("DealPosition", 0);

        iMainCount = 1;
        iAddOnOneCount = 0;
        iAddOnTwoCount = 0;
        iAddOnThreeCount = 0;


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

            addDetails(dashboardMetadata);
        }
        restroName.setText(dashboardMetadata.getvRestaurantName());
        mainTextPrice.setText(dashboardMetadata.getdPrice());


        checkoutPreferences = this.getSharedPreferences("CHECKOUT_POP", MODE_PRIVATE);
        checkOutPop = checkoutPreferences.getString("CHECK_POP", "");

    }

    public void initialize()
    {
        nameDeal = (TextView) findViewById(R.id.nameDeal);
        mainCount = (TextView) findViewById(R.id.mainCount);
        addOnOneName =  (TextView) findViewById(R.id.addOnOneName);
        addOnOneCount =  (TextView) findViewById(R.id.addOnOneCount);
        addOnTwoName =  (TextView) findViewById(R.id.addOnTwoName);
        addOnTwoTextCount =  (TextView) findViewById(R.id.addOnTwoTextCount);
        addOnThreeTextCount =  (TextView) findViewById(R.id.addOnThreeTextCount);
        addOnOneTextPrice = (TextView) findViewById(R.id.addOnOneTextPrice);
        addOnTwoTextPrice = (TextView) findViewById(R.id.addOnTwoTextPrice);
        addOnThreeTextPrice = (TextView) findViewById(R.id.addOnThreeTextPrice);
        addOnThreeName = (TextView) findViewById(R.id.addOnThreeName);
        restroName = (TextView) findViewById(R.id.toolbar_title);
        mainTextPrice = (TextView) findViewById(R.id.mainTextPrice);
        textProceedCheckout = (TextView) findViewById(R.id.textProceedCheckout);

        addOnOne = (RelativeLayout) findViewById(R.id.addOnOne);
        addOnTwo = (RelativeLayout) findViewById(R.id.addOnTwo);
        addOnThree = (RelativeLayout) findViewById(R.id.addOnThree);

        mainSubtractButton = (LinearLayout) findViewById(R.id.mainSubtractButton);
        mainAddButton = (LinearLayout) findViewById(R.id.mainAddButton);
        addOnOneAddButton = (LinearLayout) findViewById(R.id.addOnOneAddButton);
        addOnOneSubtractButton = (LinearLayout) findViewById(R.id.addOnOneSubtractButton);
        addOnTwoSubtractButton = (LinearLayout) findViewById(R.id.addOnTwoSubtractButton);
        addOnTwoAddButton = (LinearLayout) findViewById(R.id.addOnTwoAddButton);
        addOnThreeSubtractButton = (LinearLayout) findViewById(R.id.addOnThreeSubtractButton);
        addOnThreeAddButton = (LinearLayout) findViewById(R.id.addOnThreeAddButton);
        btnProceed = (LinearLayout) findViewById(R.id.btnProceed);

        mainSubtractButton.setOnClickListener(this);
        mainAddButton.setOnClickListener(this);
        addOnOneAddButton.setOnClickListener(this);
        addOnOneSubtractButton.setOnClickListener(this);
        addOnTwoSubtractButton.setOnClickListener(this);
        addOnTwoAddButton.setOnClickListener(this);
        addOnThreeAddButton.setOnClickListener(this);
        addOnThreeSubtractButton.setOnClickListener(this);
        btnProceed.setOnClickListener(this);

    }

    public void addDetails(DashboardMetadata dashboardMetadata)
    {
        try {
            nameDeal.setText(dashboardMetadata.getvCampaignOption());
            mainCount.setText(String.valueOf(iMainCount));

            int size = addOnsMetadata.size();

            switch (size) {
                case 1:
                    addOnOneDetails(addOnsMetadata.get(0));
                    addOnOne.setVisibility(View.VISIBLE);
                    addOnTwo.setVisibility(View.INVISIBLE);
                    addOnThree.setVisibility(View.INVISIBLE);

                    break;

                case 2:
                    addOnOneDetails(addOnsMetadata.get(0));
                    addOnTwoDetails(addOnsMetadata.get(1));

                    addOnOne.setVisibility(View.VISIBLE);
                    addOnTwo.setVisibility(View.VISIBLE);
                    addOnThree.setVisibility(View.INVISIBLE);
                    break;

                case 3:
                    addOnOneDetails(addOnsMetadata.get(0));
                    addOnTwoDetails(addOnsMetadata.get(1));
                    addOnThreeDetails(addOnsMetadata.get(2));

                    addOnOne.setVisibility(View.VISIBLE);
                    addOnTwo.setVisibility(View.VISIBLE);
                    addOnThree.setVisibility(View.VISIBLE);
                    break;
            }
        }
        catch(Exception ex)
        {
            System.out.println(":::::::::::::::::::::::::::::::" + ex.toString());
        }
    }

    public void addOnOneDetails(AddOnsMetadata addOnsMetadata)
    {
        addOnOneName.setText(addOnsMetadata.getvAddOnName());
        addOnOneCount.setText(String.valueOf(iAddOnOneCount));
        addOnOneTextPrice.setText("$"+addOnsMetadata.getdAddOnPrice());
    }

    public void addOnTwoDetails(AddOnsMetadata addOnsMetadata)
    {
        addOnTwoName.setText(addOnsMetadata.getvAddOnName());
        addOnTwoTextCount.setText(String.valueOf(iAddOnTwoCount));
        addOnTwoTextPrice.setText("$"+addOnsMetadata.getdAddOnPrice());
    }

    public void addOnThreeDetails(AddOnsMetadata addOnsMetadata)
    {
        addOnThreeName.setText(addOnsMetadata.getvAddOnName());
        addOnThreeTextCount.setText(String.valueOf(iAddOnThreeCount));
        addOnThreeTextPrice.setText("$"+addOnsMetadata.getdAddOnPrice());
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stubs
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
        textProceedCheckout.setVisibility(View.INVISIBLE);
        DealActivity.btnPurchase.setText(getString(R.string.Act_Deal_Desc_Purchase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            textProceedCheckout.setVisibility(View.INVISIBLE);
            DealActivity.btnPurchase.setText(getString(R.string.Act_Deal_Desc_Purchase));
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if(v == mainAddButton)
        {
            iMainCount++;
            mainCount.setText(String.valueOf(iMainCount));
        }
        else if(v == mainSubtractButton)
        {
            if(iMainCount > 1) {
                iMainCount--;
                mainCount.setText(String.valueOf(iMainCount));
            }
        }
        else if(v == addOnOneAddButton)
        {
            iAddOnOneCount++;
            addOnOneCount.setText(String.valueOf(iAddOnOneCount));
        }
        else if(v == addOnOneSubtractButton)
        {
            if(iAddOnOneCount > 0) {
                iAddOnOneCount--;
                addOnOneCount.setText(String.valueOf(iAddOnOneCount));
            }
        }
        else if(v == addOnTwoAddButton)
        {
            iAddOnTwoCount++;
            addOnTwoTextCount.setText(String.valueOf(iAddOnTwoCount));
        }
        else if(v == addOnTwoSubtractButton)
        {
            if(iAddOnTwoCount > 0) {
                iAddOnTwoCount--;
                addOnTwoTextCount.setText(String.valueOf(iAddOnTwoCount));
            }
        }
        else if(v == addOnThreeAddButton)
        {
            iAddOnThreeCount++;
            addOnThreeTextCount.setText(String.valueOf(iAddOnThreeCount));
        }
        else if(v == addOnThreeSubtractButton)
        {
            if(iAddOnThreeCount > 0) {
                iAddOnThreeCount--;
                addOnThreeTextCount.setText(String.valueOf(iAddOnThreeCount));
            }
        }
        else if(v == btnProceed)
        {
            if(!checkOutPop.equals("")) {

                textProceedCheckout.setVisibility(View.INVISIBLE);
                if (PaymentScreen.textPay == null) {

                } else {
                    PaymentScreen.textPay.setVisibility(View.VISIBLE);
                }
                Intent intent = new Intent(ProceedCheckout.this, PaymentScreen.class);
                intent.putExtra("DealPosition", dealPosition);
                startActivity(intent);
            }
            else
            {
                popup();
            }
        }
    }


    public void popup() {
        try {

            SharedPreferences sharepreferences = context.getSharedPreferences("CHECKOUT_POP", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharepreferences.edit();

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_checkout);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            // dialog.setTitle(dialogHeader);

            LinearLayout btnOk = (LinearLayout) dialog.findViewById(R.id.btnOk);
            CheckBox dontShowCheck = (CheckBox) dialog.findViewById(R.id.dontShowCheck);

            dontShowCheck.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //is chkIos checked?
                    if (((CheckBox) v).isChecked()) {
                        checkPopFlag = true;
                    }
                    else {
                        checkPopFlag = false;
                    }
                }
            });


            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();

                    if(checkPopFlag) {
                        editor.putString("CHECK_POP", "FALSE");
                        editor.commit();
                    }
                    else {
                        editor.putString("CHECK_POP", "");
                        editor.commit();

                    }

                    isCheckPopFlag = true;
                    Intent intent = new Intent(ProceedCheckout.this, PaymentScreen.class);
                    intent.putExtra("DealPosition", dealPosition);
                    startActivity(intent);
                }
            });

            dialog.show();
        } catch (Exception ex) {

            Toast.makeText(context, getString(R.string.Error_Encounter),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isCheckPopFlag)
        {
            checkoutPreferences = this.getSharedPreferences("CHECKOUT_POP", MODE_PRIVATE);
            checkOutPop = checkoutPreferences.getString("CHECK_POP", "");
        }
    }
}
