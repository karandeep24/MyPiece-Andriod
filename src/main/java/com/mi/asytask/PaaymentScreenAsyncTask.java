package com.mi.asytask;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mi.activities.DealActivity;
import com.mi.activities.PaymentScreen;
import com.mi.activities.ProceedCheckout;
import com.mi.metadata.SuccessMetadata;
import com.mi.test.mypiece.R;
import com.mi.parsers.SuccessParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by karandsingh on 16-09-14.
 */
public class PaaymentScreenAsyncTask extends AsyncTask {

    Context context;
    String dataString, url;
    ProgressDialog progressDialog;
    SuccessMetadata successMetadata;
    String endTime;
    MyCount_alert counter_alert;
    TextView textView1_hour;

    public PaaymentScreenAsyncTask(Context context, String url, String dataString, ProgressDialog progressDialog,
                                   String endTime)
    {
        this.context = context;
        this.dataString = dataString;
        this.url = url;
        this.progressDialog = progressDialog;
        this.endTime = endTime;
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

        SuccessParser parsers = new SuccessParser();
        successMetadata = parsers.parsePaymentVector(url, dataString);

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
            if(successMetadata != null) {
                if (successMetadata.getStatus().equals("success")) {
                    popup();
                }
                else if(successMetadata.getMessage() != null)
                {
                    Toast.makeText(context, successMetadata.getMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, successMetadata.getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void popup() {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.payment_popup);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            // dialog.setTitle(dialogHeader);

            LinearLayout btnOk = (LinearLayout) dialog.findViewById(R.id.btnOk);
            textView1_hour = (TextView) dialog.findViewById(R.id.timer);

            timerDisplay(endTime);

            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    PaymentScreen.instance.finish();

                    if(ProceedCheckout.instance != null)
                        ProceedCheckout.instance.finish();

                    if(DealActivity.instance != null)
                        DealActivity.instance.finish();

                }
            });

            dialog.show();
        } catch (Exception ex) {

            Toast.makeText(context, context.getString(R.string.Error_Encounter),
                    Toast.LENGTH_LONG).show();
        }
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

    public class MyCount_alert extends CountDownTimer {
        Context mContext;

        public MyCount_alert(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onTick(long millisUntilFinished) {
            textView1_hour.setText(formatTime(millisUntilFinished));

        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
        }
    }

}
