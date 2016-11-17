package com.mi.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mi.asytask.DismissDealAsyncTask;
import com.mi.common.Constant;
import com.mi.metadata.AddOnsMetadata;
import com.mi.metadata.MyPurchaseMetadata;
import com.mi.test.mypiece.R;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by karandsingh on 16-09-16.
 */
public class RedeemFragment extends Fragment implements View.OnClickListener{

    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
    static Vector<AddOnsMetadata> addOnsMetadataVector;
    static String AddOn1, AddOn1Qty, AddOn2, AddOn2Qty, AddOn3, AddOn3Qty;
    static int size;
    TextView txt_timer;
    MyCount_alert counter_alert;
    LinearLayout btnRedeem, redeemLay, dismissLay, expiredLay;
    static int dealId;
    static Context appContext;
    static HashMap<String, String> dealPosMap = new HashMap<String, String>();
    SharedPreferences userPreferences, langPreferences;
    String sUserID, sLang;

    public static final RedeemFragment newInstance(MyPurchaseMetadata myPurchaseMetadata, Context context, int pos)
    {

        RedeemFragment f = new RedeemFragment();
        Bundle bdl = new Bundle(1);

        dealId = pos;
        appContext = context;

        bdl.putString("RESTRO_NAME", myPurchaseMetadata.getvName());
        bdl.putString("RESTRO_ADDR", myPurchaseMetadata.getvAddress());
        bdl.putString("MAIN_QTY", myPurchaseMetadata.getiQuantity());
        bdl.putString("MAIN_NAME", myPurchaseMetadata.getvCampaignOption());
        bdl.putString("TOTAL_PRICE", myPurchaseMetadata.getdTotalIncome());
        bdl.putString("END_TIME", myPurchaseMetadata.getvEndTimeFormat());
        bdl.putString("PURCHASE_TIME", myPurchaseMetadata.getDtCreationDate());
        bdl.putString("IS_REDEEMABLE", myPurchaseMetadata.getIsRedeemable());
        bdl.putString("END_TIME_DEAL", myPurchaseMetadata.getvEndTime());
        bdl.putString("START_TIME", myPurchaseMetadata.getvStartTime());
        bdl.putString("V_VALID", myPurchaseMetadata.getvValid());
        bdl.putString("V_VALID_FR", myPurchaseMetadata.getvValid_fr());
        bdl.putString("ESTATUS", myPurchaseMetadata.geteStatus());
        bdl.putString("EXPIRY_TIME", myPurchaseMetadata.getdEndDate());


        addOnsMetadataVector = myPurchaseMetadata.getAddOns();

        if(addOnsMetadataVector != null) {

            size = addOnsMetadataVector.size();

            switch (size) {
                case 1:
                    getAddOn1(addOnsMetadataVector.get(0));
                    bdl.putString("ADD_ON1", AddOn1);
                    bdl.putString("ADD_ON1QTY", AddOn1Qty);

                    bdl.putString("ADD_ON2", "");
                    bdl.putString("ADD_ON2QTY", "0");

                    bdl.putString("ADD_ON3", "");
                    bdl.putString("ADD_ON3QTY", "0");
                    break;

                case 2:
                    getAddOn1(addOnsMetadataVector.get(0));
                    getAddOn2(addOnsMetadataVector.get(1));

                    bdl.putString("ADD_ON1", AddOn1);
                    bdl.putString("ADD_ON1QTY", AddOn1Qty);

                    bdl.putString("ADD_ON2", AddOn2);
                    bdl.putString("ADD_ON2QTY", AddOn2Qty);

                    bdl.putString("ADD_ON3", "");
                    bdl.putString("ADD_ON3QTY", "0");
                    break;

                case 3:
                    getAddOn1(addOnsMetadataVector.get(0));
                    getAddOn2(addOnsMetadataVector.get(1));
                    getAddOn3(addOnsMetadataVector.get(2));

                    bdl.putString("ADD_ON1", AddOn1);
                    bdl.putString("ADD_ON1QTY", AddOn1Qty);

                    bdl.putString("ADD_ON2", AddOn2);
                    bdl.putString("ADD_ON2QTY", AddOn2Qty);

                    bdl.putString("ADD_ON3", AddOn3);
                    bdl.putString("ADD_ON3QTY", AddOn3Qty);

                    break;
            }
        }
        else
        {
            bdl.putString("ADD_ON1", "");
            bdl.putString("ADD_ON1QTY", "0");

            bdl.putString("ADD_ON2", "");
            bdl.putString("ADD_ON2QTY", "0");

            bdl.putString("ADD_ON3", "");
            bdl.putString("ADD_ON3QTY", "0");
        }

        bdl.putString("MAIN_NAME", myPurchaseMetadata.getvCampaignOption());
        f.setArguments(bdl);

        return f;

    }

    public static void getAddOn1(AddOnsMetadata addOnsMetadata)
    {
        AddOn1 = addOnsMetadata.getvAddOnName();
        AddOn1Qty = addOnsMetadata.getiQuantity();
    }

    public static void getAddOn2(AddOnsMetadata addOnsMetadata)
    {
        AddOn2 = addOnsMetadata.getvAddOnName();
        AddOn2Qty = addOnsMetadata.getiQuantity();
    }

    public static void getAddOn3(AddOnsMetadata addOnsMetadata)
    {
        AddOn3 = addOnsMetadata.getvAddOnName();
        AddOn3Qty = addOnsMetadata.getiQuantity();
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.my_fragment, container, false);

        userPreferences = appContext.getSharedPreferences("User_id", Context.MODE_PRIVATE);

        sUserID = userPreferences.getString("user_id_value", "");

        langPreferences = appContext.getSharedPreferences("CommonPrefs", Context.MODE_PRIVATE);
        sLang = langPreferences.getString("Language", "en");


        String IS_REDEEMABLE = getArguments().getString("IS_REDEEMABLE");
        String ESTATUS = getArguments().getString("ESTATUS");
        String EXPIRY_TIME = getArguments().getString("EXPIRY_TIME");

        TextView txt_RestroName = (TextView) v.findViewById(R.id.txt_RestroName);
        TextView txt_Address = (TextView) v.findViewById(R.id.txt_Address);
        TextView txt_mainQty = (TextView) v.findViewById(R.id.txt_mainQty);
        TextView txt_main = (TextView) v.findViewById(R.id.txt_main);
        TextView txt_amount = (TextView) v.findViewById(R.id.txt_amount);
        TextView txt_Total = (TextView) v.findViewById(R.id.txt_Total);
        TextView startTime = (TextView) v.findViewById(R.id.startTime);
        TextView endTime = (TextView) v.findViewById(R.id.endTime);

        TextView txt_addOnOneQty = (TextView) v.findViewById(R.id.txt_addOnOneQty);
        TextView txt_addOnOne = (TextView) v.findViewById(R.id.txt_addOnOne);
        TextView txt_addOnTwoQty = (TextView) v.findViewById(R.id.txt_addOnTwoQty);
        TextView txt_addOnTwo = (TextView) v.findViewById(R.id.txt_addOnTwo);
        TextView txt_addOnThreeQty = (TextView) v.findViewById(R.id.txt_addOnThreeQty);
        TextView txt_addOnThree = (TextView) v.findViewById(R.id.txt_addOnThree);
        TextView txt_purchasedDate = (TextView) v.findViewById(R.id.txt_purchasedDate);
        TextView txt_purchasedDateTxt = (TextView) v.findViewById(R.id.txt_purchasedDateTxt);
        TextView days = (TextView) v.findViewById(R.id.days);


        FrameLayout mainFrame = (FrameLayout) v.findViewById(R.id.mainFrame);

        LinearLayout total_layout = (LinearLayout) v.findViewById(R.id.total_layout);


        txt_timer = (TextView) v.findViewById(R.id.txt_timer);
        redeemLay = (LinearLayout) v.findViewById(R.id.redeemLay);
        dismissLay = (LinearLayout) v.findViewById(R.id.dismissLay);
        expiredLay = (LinearLayout) v.findViewById(R.id.expiredLay);

        LinearLayout isRedeemable = (LinearLayout) v.findViewById(R.id.isRedeemable);
        btnRedeem = (LinearLayout) v.findViewById(R.id.btnRedeem);

        btnRedeem.setOnClickListener(this);
        dismissLay.setOnClickListener(this);

        String RESTRO_NAME = getArguments().getString("RESTRO_NAME");
        String RESTRO_ADDR = getArguments().getString("RESTRO_ADDR");
        String MAIN_QTY = getArguments().getString("MAIN_QTY");
        String MAIN_NAME = getArguments().getString("MAIN_NAME");
        String TOTAL_PRICE = getArguments().getString("TOTAL_PRICE");
        String END_TIME = getArguments().getString("END_TIME");
        String PURCHASE_TIME = getArguments().getString("PURCHASE_TIME");
        String END_TIME_DEAL = getArguments().getString("END_TIME_DEAL");
        String START_TIME = getArguments().getString("START_TIME");
        String AddOn1 = getArguments().getString("ADD_ON1");
        String AddOn2 = getArguments().getString("ADD_ON2");
        String AddOn3 = getArguments().getString("ADD_ON3");
        String AddOn1Qty = getArguments().getString("ADD_ON1QTY");
        String AddOn2Qty = getArguments().getString("ADD_ON2QTY");
        String AddOn3Qty = getArguments().getString("ADD_ON3QTY");
        String V_VALID = getArguments().getString("V_VALID");
        String V_VALID_FR = getArguments().getString("V_VALID_FR");

        txt_RestroName.setText(RESTRO_NAME);
        txt_Address.setText(RESTRO_ADDR);
        txt_main.setText(MAIN_NAME);
        txt_mainQty.setText(MAIN_QTY);
        txt_amount.setText(TOTAL_PRICE);
        txt_purchasedDate.setText(PURCHASE_TIME);

        if(Integer.parseInt(AddOn1Qty) > 0) {
            txt_addOnOne.setVisibility(View.VISIBLE);
            txt_addOnOneQty.setVisibility(View.VISIBLE);

            txt_addOnOne.setText(AddOn1);
            txt_addOnOneQty.setText(AddOn1Qty);
        }
        else
        {
            txt_addOnOne.setVisibility(View.GONE);
            txt_addOnOneQty.setVisibility(View.GONE);
        }

        if(Integer.parseInt(AddOn2Qty) > 0) {

            txt_addOnTwo.setVisibility(View.VISIBLE);
            txt_addOnTwoQty.setVisibility(View.VISIBLE);

            txt_addOnTwo.setText(AddOn2);
            txt_addOnTwoQty.setText(AddOn2Qty);
        }
        else {
            txt_addOnTwo.setVisibility(View.GONE);
            txt_addOnTwoQty.setVisibility(View.GONE);
        }

        if(Integer.parseInt(AddOn3Qty) > 0)
        {
            txt_addOnThree.setVisibility(View.VISIBLE);
            txt_addOnThreeQty.setVisibility(View.VISIBLE);

            txt_addOnThree.setText(AddOn3);
            txt_addOnThreeQty.setText(AddOn3Qty);
        }
        else {
            txt_addOnThree.setVisibility(View.GONE);
            txt_addOnThreeQty.setVisibility(View.GONE);
        }

        if(ESTATUS.equals("Active")) {

            if (IS_REDEEMABLE.equals("true")) {

                txt_timer.setVisibility(View.VISIBLE);
                isRedeemable.setVisibility(View.GONE);
                timerDisplay(END_TIME);

                total_layout.setVisibility(View.VISIBLE);

                days.setVisibility(View.GONE);
                btnRedeem.setEnabled(true);
                redeemLay.setVisibility(View.VISIBLE);
                expiredLay.setVisibility(View.GONE);
                dismissLay.setVisibility(View.GONE);

                txt_purchasedDate.setVisibility(View.VISIBLE);
                txt_purchasedDateTxt.setVisibility(View.VISIBLE);


                txt_RestroName.setTextColor(Color.parseColor("#0A6B91"));
                txt_Address.setTextColor(Color.parseColor("#0A6B91"));
                txt_mainQty.setTextColor(Color.parseColor("#0A6B91"));
                txt_main.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnOneQty.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnOne.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnTwoQty.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnTwo.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnThreeQty.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnThree.setTextColor(Color.parseColor("#0A6B91"));
                txt_Total.setTextColor(Color.parseColor("#0A6B91"));
                txt_amount.setTextColor(Color.parseColor("#0A6B91"));
                txt_purchasedDate.setTextColor(Color.parseColor("#0A6B91"));
                txt_purchasedDateTxt.setTextColor(Color.parseColor("#0A6B91"));

                mainFrame.setBackgroundColor(Color.parseColor("#FFFFFF"));

            } else {
                txt_timer.setVisibility(View.GONE);
                isRedeemable.setVisibility(View.VISIBLE);
                btnRedeem.setEnabled(false);
                redeemLay.setVisibility(View.GONE);
                expiredLay.setVisibility(View.GONE);
                dismissLay.setVisibility(View.GONE);

                total_layout.setVisibility(View.GONE);

                txt_purchasedDate.setVisibility(View.VISIBLE);
                txt_purchasedDateTxt.setVisibility(View.VISIBLE);

                days.setVisibility(View.VISIBLE);


                if(sLang.equals("en"))
                {
                    days.setText(V_VALID);
                }
                else if(sLang.equals("fr"))
                {
                    days.setText(V_VALID_FR);
                }
                else
                {
                    days.setText(V_VALID);
                }


                startTime.setText(START_TIME);
                endTime.setText(END_TIME_DEAL);

                txt_RestroName.setTextColor(Color.parseColor("#FFFFFF"));
                txt_Address.setTextColor(Color.parseColor("#FFFFFF"));
                txt_mainQty.setTextColor(Color.parseColor("#FFFFFF"));
                txt_main.setTextColor(Color.parseColor("#FFFFFF"));
                txt_addOnOneQty.setTextColor(Color.parseColor("#FFFFFF"));
                txt_addOnOne.setTextColor(Color.parseColor("#FFFFFF"));
                txt_addOnTwoQty.setTextColor(Color.parseColor("#FFFFFF"));
                txt_addOnTwo.setTextColor(Color.parseColor("#FFFFFF"));
                txt_addOnThreeQty.setTextColor(Color.parseColor("#FFFFFF"));
                txt_addOnThree.setTextColor(Color.parseColor("#FFFFFF"));
                txt_Total.setTextColor(Color.parseColor("#FFFFFF"));
                txt_amount.setTextColor(Color.parseColor("#FFFFFF"));
                txt_purchasedDate.setTextColor(Color.parseColor("#FFFFFF"));
                txt_purchasedDateTxt.setTextColor(Color.parseColor("#FFFFFF"));

                mainFrame.setBackgroundColor(Color.parseColor("#0A6B91"));

            }
        }
        else
        {
            long difference = getDifference(EXPIRY_TIME);

            if(difference > 0)
            {
                txt_timer.setVisibility(View.VISIBLE);
                isRedeemable.setVisibility(View.GONE);
                exp_timerDisplay(difference);

                total_layout.setVisibility(View.VISIBLE);

                days.setVisibility(View.GONE);
                btnRedeem.setEnabled(true);
                redeemLay.setVisibility(View.VISIBLE);
                expiredLay.setVisibility(View.GONE);
                dismissLay.setVisibility(View.GONE);

                txt_purchasedDate.setVisibility(View.VISIBLE);
                txt_purchasedDateTxt.setVisibility(View.VISIBLE);

                txt_RestroName.setTextColor(Color.parseColor("#0A6B91"));
                txt_Address.setTextColor(Color.parseColor("#0A6B91"));
                txt_mainQty.setTextColor(Color.parseColor("#0A6B91"));
                txt_main.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnOneQty.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnOne.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnTwoQty.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnTwo.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnThreeQty.setTextColor(Color.parseColor("#0A6B91"));
                txt_addOnThree.setTextColor(Color.parseColor("#0A6B91"));
                txt_Total.setTextColor(Color.parseColor("#0A6B91"));
                txt_amount.setTextColor(Color.parseColor("#0A6B91"));
                txt_purchasedDate.setTextColor(Color.parseColor("#0A6B91"));
                txt_purchasedDateTxt.setTextColor(Color.parseColor("#0A6B91"));


                mainFrame.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            else
            {
                txt_timer.setVisibility(View.GONE);
                isRedeemable.setVisibility(View.GONE);
                total_layout.setVisibility(View.GONE);

                days.setVisibility(View.GONE);
                btnRedeem.setEnabled(false);
                redeemLay.setVisibility(View.GONE);

                expiredLay.setVisibility(View.VISIBLE);
                dismissLay.setVisibility(View.VISIBLE);

                txt_purchasedDate.setVisibility(View.GONE);
                txt_purchasedDateTxt.setVisibility(View.GONE);

                mainFrame.setBackgroundColor(Color.parseColor("#aaaaaa"));
            }


        }
        return v;

    }

    public void timerDisplay(String endTime) {
        try {

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date currDate = new Date();
            Date endDate = dateFormat.parse(endTime);
            Date startDate = dateFormat.parse(dateFormat.format(currDate));

            long difference = endDate.getTime() - startDate.getTime();

            counter_alert = new MyCount_alert(difference, 1000);
            counter_alert.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public long getDifference(String endTime)
    {
        long difference = 0;

        try
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currDate = new Date();
            Date endDate = dateFormat.parse(endTime);
            Date startDate = dateFormat.parse(dateFormat.format(currDate));

            difference = endDate.getTime() - startDate.getTime();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return difference;
    }

    public void exp_timerDisplay(long difference) {
        try {
            counter_alert = new MyCount_alert(difference, 1000);
            counter_alert.start();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public String formatTime(long millis) {

        String output = "";
        long seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours = hours;

        String secondsD = String.valueOf(seconds);
        String minutesD = String.valueOf(minutes);
        String hoursD = String.valueOf(hours);

        if (seconds < 10)
            secondsD = "0" + seconds;
        if (minutes < 10)
            minutesD = "0" + minutes;

        if (hours < 10)
            hoursD = "0" + hours;

        output = hoursD + ":" + minutesD + ":" + secondsD;

        return output;
    }

    @Override
    public void onClick(View v) {

        if(v == btnRedeem)
        {
            try {
                Intent intent = new Intent(appContext, RedeemDeal.class);
                intent.putExtra("DEALID", My_PurchaseActivity.dealId);
                startActivity(intent);
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }
        else  if(v == dismissLay)
        {
            try {
                String URL = Constant.DISMISS_DEAL + "myDealId=" + My_PurchaseActivity.campaignID;
                ProgressDialog progressDialog = ProgressDialog.show(appContext, "", "Please wait.....");

                DismissDealAsyncTask dismissDealAsyncTask = new DismissDealAsyncTask(appContext, URL, progressDialog, sUserID, sLang);
                dismissDealAsyncTask.execute();
            }catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }
    }

    public class MyCount_alert extends CountDownTimer {
        Context mContext;

        public MyCount_alert(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            txt_timer.setText(formatTime(millisUntilFinished));

        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
        }
    }

}

