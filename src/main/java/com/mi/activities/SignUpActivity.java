package com.mi.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mi.asytask.LoginAsyncTask;
import com.mi.asytask.SplashAsyncTask;
import com.mi.common.Constant;
import com.mi.test.mypiece.R;
import com.mi.webapi.ConnectionDetector;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by karandsingh on 16-09-23.
 */
public class SignUpActivity extends Activity implements View.OnClickListener {

    EditText password_edit, email_edit;
    LinearLayout checkboxLay, sign_up_button;
    CheckBox checkbox;
    int ctr = 0;
    ProgressDialog progressDialog;
    Context context;
    String email_login, password_login;
    public static SignUpActivity instance = null;
    String sLang;
    SharedPreferences langPreferences;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        setContentView(R.layout.sign_up);

        context = this;
        instance = this;

        langPreferences = this.getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        sLang = langPreferences.getString("Language", "en");

        initialize();
    }

    public void initialize()
    {
        password_edit = (EditText) findViewById(R.id.password_edit);
        email_edit = (EditText) findViewById(R.id.email_edit);

        checkboxLay = (LinearLayout) findViewById(R.id.checkboxLay);
        sign_up_button = (LinearLayout) findViewById(R.id.sign_up_button);
        checkbox = (CheckBox) findViewById(R.id.checkbox);

        checkboxLay.setOnClickListener(this);
        checkbox.setOnClickListener(this);
        sign_up_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v == checkbox)
        {
            if(checkbox.isChecked())
            {
                password_edit.setTransformationMethod(null);
                checkbox.setChecked(true);
            }
            else
            {
                password_edit.setTransformationMethod(new PasswordTransformationMethod());
                checkbox.setChecked(false);
            }
        }
        else if(v == sign_up_button) {

            ConnectionDetector cd = new ConnectionDetector(SignUpActivity.this);
            final Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent) {

                email_login = email_edit.getText().toString();
                password_login = password_edit.getText().toString();

                if(validate(email_login)) {

                    if (email_login.equals("") || password_login.equals("")) {

                        Toast.makeText(getApplicationContext(),
                                getString(R.string.Toast_Login_enter_full_detail),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        String dataString = getStringLogin(email_login, password_login);
                        String URL = Constant.SIGNUP;

                        progressDialog = ProgressDialog.show(SignUpActivity.this, "", "Please wait.....");

                        SplashAsyncTask asyncTask = new SplashAsyncTask(context, URL, dataString, progressDialog, email_login, sLang);
                        asyncTask.execute();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.Toast_WrongEmail),
                            Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), getString(R.string.No_Connection),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getStringLogin(String email_login, String password_login)
    {
        String Return="{\n\t";
        Return = Return + "\"Email\":"      + "\""+ email_login       +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"Password\":"      + "\""+ password_login       +"\"";
        Return =  Return + "\n}";

        return Return;
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
