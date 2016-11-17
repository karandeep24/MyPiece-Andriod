package com.mi.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.widget.LoginButton;
import com.mi.asytask.LoginAsyncTask;
import com.mi.common.Constant;
import com.mi.test.mypiece.R;
import com.mi.activities.SignUpActivity;
import com.mi.utility.DbHelper;
import com.mi.webapi.ConnectionDetector;
import java.util.Locale;

public class Login_activity extends Activity
{

	AutoCompleteTextView autoCompleteTextView;
	Button btn_login;
	EditText password_edit; // email_edit
	Intent intent;
	Typeface face, face_bold;
	TextView forgot_password;
	ProgressDialog progressdialog;
    Context context;
    LinearLayout button_forgot_pwd;
	RelativeLayout relative_forgot;
	String email_login, password_login;
	SharedPreferences sharepreferences;
	SharedPreferences.Editor editor;

//	private static LoginButton loginBtn;

	LinearLayout login_main_layout;
	DbHelper dbHelper;
    ProgressDialog progressDialog;
	public static Login_activity instance = null;
	LinearLayout checkboxLay;
	CheckBox checkbox;
	String sLang;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			// TODO Auto-generated method stub
			// fb_new

			instance = this;

//			Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

			String langPref = "Language";
			SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
			sLang = prefs.getString(langPref, "");

			Locale myLocale = new Locale(sLang);
			android.content.res.Configuration config = new android.content.res.Configuration();
			config.locale = myLocale;
			getBaseContext().getResources().updateConfiguration(config,
					getBaseContext().getResources().getDisplayMetrics());

			setContentView(R.layout.login_page);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);

            context = getApplicationContext();

			login_main_layout = (LinearLayout) findViewById(R.id.login_main_layout);
			autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.email_edit_auto);
			btn_login = (Button) findViewById(R.id.btn_login);
			button_forgot_pwd = (LinearLayout) findViewById(R.id.linear_forgot_pwd);
			password_edit = (EditText) findViewById(R.id.password_edit);
			forgot_password = (TextView) findViewById(R.id.forgot_password);
			checkboxLay = (LinearLayout) findViewById(R.id.checkboxLay);
			checkbox = (CheckBox) findViewById(R.id.checkbox);
			relative_forgot = (RelativeLayout) findViewById(R.id.relative_forgot);

			face = Typeface.createFromAsset(getAssets(), "fonts/Raleway-Bold.ttf");
			face_bold = Typeface.createFromAsset(getAssets(),
					"fonts/Raleway-Bold.ttf");
			password_edit.setTypeface(face);
			btn_login.setTypeface(face);
			autoCompleteTextView.setTypeface(face);

			password_edit.setHint(getString(R.string.signup_pwd));
			forgot_password.setText(getString(R.string.forgot_pwd));

			ConnectionDetector cd = new ConnectionDetector(Login_activity.this);

			final Boolean isInternetPresent = cd.isConnectingToInternet(); // true or
																		// false
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}


			login_main_layout.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(
							password_edit.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);

					return false;
				}
			});


			checkbox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

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
			});

			relative_forgot.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(Login_activity.this, ForgotPassword.class);
					startActivity(intent);
				}
			});

			btn_login.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(isInternetPresent) {
						String dataString;
						email_login = autoCompleteTextView.getText().toString();
						password_login = password_edit.getText().toString();
						// auto=autoCompleteTextView.getText().toString();
						// arraylist.add(arraylist.size(),auto);
						// Methods.syso("@@@@@@@@@@@@@@@@@" + arraylist);
						// Store arraylist in sharedpreference

						try {
							if (getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								if (imm.isActive()) {

									imm.hideSoftInputFromWindow(getCurrentFocus()
											.getWindowToken(), 0);

								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

						if (email_login.length() > 0 && password_login.length() > 0) {

							dataString = getStringLogin(email_login, password_login);

							String URL = Constant.LOGIN_URL;
							progressDialog = ProgressDialog.show(Login_activity.this, "", "Please wait.....");

							LoginAsyncTask asyncTask = new LoginAsyncTask(context, URL, dataString, progressDialog, email_login, sLang);
							asyncTask.execute();

						} else {
							Toast.makeText(getApplicationContext(),
									getString(R.string.Toast_Login_enter_full_detail),
									Toast.LENGTH_SHORT).show();
						}
					}
					else
					{
						Toast.makeText(getApplicationContext(), getString(R.string.No_Connection),
								Toast.LENGTH_SHORT).show();
					}

				}
			});

			dbHelper = new DbHelper(context);
			dbHelper.Create_table();

		} catch (Exception e) {
			e.printStackTrace();
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

	private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
		Account[] accounts = AccountManager.get(context).getAccountsByType(
				"com.google");
		String[] addresses = new String[accounts.length];
		for (int i = 0; i < accounts.length; i++) {
			addresses[i] = accounts[i].name;
		}
		return new ArrayAdapter<String>(context,
				android.R.layout.simple_dropdown_item_1line, addresses);
	}

	// fb_new

//	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
//		@Override
//		public void call(Session session, SessionState state,
//				Exception exception) {
//			if (state.isOpened()) {
//
//				Log.d("FacebookSampleActivity", "Facebook session opened");
//			} else if (state.isClosed()) {
//
//				Log.d("FacebookSampleActivity", "Facebook session closed");
//			}
//		}
//	};


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}
}
