package com.mi.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mi.asytask.ForgetPwdAsyncTask;
import com.mi.asytask.VerifyPwdAsyncTask;
import com.mi.common.Constant;
import com.mi.test.mypiece.R;
import com.mi.webapi.ConnectionDetector;

/**
 * Created by karandsingh on 16-10-25.
 */
public class ForgotPassword extends Activity implements View.OnClickListener{

    public static LinearLayout verifyScreen, recoverScreen;
    Button btn_sendMail, btn_reset;
    EditText email_edit, new_pwd, verification_code;
    String email;
    ProgressDialog progressDialog;
    Context context;
    Boolean isInternetPresent;
    public static ForgotPassword instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        setContentView(R.layout.forgot_pwd);

        context = this;
        instance = this;

        verifyScreen = (LinearLayout) findViewById(R.id.verifyScreen);
        recoverScreen = (LinearLayout) findViewById(R.id.recoverScreen);
        email_edit = (EditText) findViewById(R.id.email_edit);
        new_pwd = (EditText) findViewById(R.id.new_pwd);
        verification_code = (EditText) findViewById(R.id.verification_code);

        btn_sendMail = (Button) findViewById(R.id.btn_sendMail);
        btn_reset = (Button) findViewById(R.id.btn_reset);

        recoverScreen.setVisibility(View.VISIBLE);
        verifyScreen.setVisibility(View.GONE);

        btn_sendMail.setOnClickListener(this);
        btn_reset.setOnClickListener(this);

        ConnectionDetector cd = new ConnectionDetector(ForgotPassword.this);

        isInternetPresent = cd.isConnectingToInternet();

    }

    @Override
    public void onClick(View v) {

        if(v == btn_sendMail)
        {

            if(isInternetPresent) {

                email = email_edit.getText().toString();

                if(!email.equals("")) {

                    String URL = Constant.SEND_EMAIL + "Email=" + email + "&Lang=en";

                    progressDialog = ProgressDialog.show(ForgotPassword.this, "", "Please wait.....");

                    ForgetPwdAsyncTask asyncTask = new ForgetPwdAsyncTask(context, URL, progressDialog);
                    asyncTask.execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.Valid_Email),
                            Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.No_Connection),
                        Toast.LENGTH_SHORT).show();
            }
        }
        else  if(v == btn_reset)
        {
            String pwd = new_pwd.getText().toString();
            String code = verification_code.getText().toString();

            if(pwd.equals("") || code.equals(""))
            {
                Toast.makeText(getApplicationContext(),
                        getString(R.string.Complete_Information),
                        Toast.LENGTH_SHORT).show();
            }
            else {
                String dataString = getStringLogin(email, pwd, code);
                String URL = Constant.VERIFY_PWD;

                progressDialog = ProgressDialog.show(ForgotPassword.this, "", "Please wait.....");

                VerifyPwdAsyncTask asyncTask = new VerifyPwdAsyncTask(context, URL, dataString, progressDialog);
                asyncTask.execute();

            }
        }
    }

    public String getStringLogin(String email_login, String password_login, String authToken)
    {
        String Return="{\n\t";
        Return = Return + "\"Email\":"      + "\""+ email_login       +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"Password\":"   + "\""+ password_login    +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"AuthToken\":"  + "\""+ authToken         +"\"";
        Return =  Return + "\n}";

        return Return;
    }
}
