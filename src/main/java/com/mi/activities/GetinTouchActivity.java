package com.mi.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mi.test.mypiece.R;

/**
 * Created by karandsingh on 16-09-30.
 */
public class GetinTouchActivity extends AppCompatActivity implements View.OnClickListener {

    TextView sendEmail, call_support;
    Context context;
    LinearLayout fb_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.get_in_touch);

        context = this;

        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sendEmail = (TextView) findViewById(R.id.sendEmail);
        sendEmail.setOnClickListener(this);


        call_support = (TextView) findViewById(R.id.call_support);
        call_support.setOnClickListener(this);

        fb_btn = (LinearLayout) findViewById(R.id.fb_btn);
        fb_btn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if(v == sendEmail)
        {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("text/html");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                    new String[] { "support@mypiece.co" });
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                    "SUPPORT : ");
            emailIntent.setType("vnd.android.cursor.dir/email");
            startActivity(Intent.createChooser(emailIntent, "Email:"));
        }
        else if( v == call_support)
        {
            String url = "tel:+5148230286";
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            context.startActivity(intent);
        }
        else if( v == fb_btn)
        {
            try {
                context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/470669099744729")));

//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MyPieceApp")));


            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/MyPieceApp")));
            }

        }

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
