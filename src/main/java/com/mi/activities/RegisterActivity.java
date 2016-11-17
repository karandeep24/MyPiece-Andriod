package com.mi.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
//import com.facebook.Request;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.UiLifecycleHelper;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
//import com.facebook.model.GraphUser;
//import com.facebook.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mi.asytask.LoginAsyncTask;
import com.mi.common.Constant;
import com.mi.test.mypiece.R;
import com.mi.webapi.ConnectionDetector;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by karandsingh on 16-09-22.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    public static RegisterActivity instance = null;
    Button already_hv_acc_btn, take_a_tour_btn, signup_btn;
    CheckBox chkAndroid;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;
    LinearLayout sign_in_button, sign_fb_button;
    private ProgressDialog mProgressDialog, progressDialog;
    Context context;
    private ProgressDialog pd;
    public static final String USER_MAP = "userHashmap";
    public static final String FRIEND_LIST = "friendList";

    public static final String USER_ID = "userId";
    public static final String NAME = "name";
    public static final String USER_NAME = "userName";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String BIRTHDAY = "birthday";
    public static final String GENDER = "gender";
    public static final String EMAIL_ID = "emailId";
    public static final String IMAGE_URL = "imageUrl";
    private HashMap<String, String> userHashmap;
    public LinearLayout termsCondition;

//    private UiLifecycleHelper uiHelper;
//    LoginButton loginButton;
    CallbackManager callbackManager;
    SharedPreferences loginPopPreferences, langPreferences;
    String login_pop, sLang;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.register);

        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        instance = this;
        context = this;
        getKeyHash();
        initialize();

        loginPopPreferences = this.getSharedPreferences("LOGIN_POP_FIRST", MODE_PRIVATE);
        langPreferences = this.getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        sLang = langPreferences.getString("Language", "");


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

//        loginButton = (LoginButton) findViewById(R.id.login_button);
//        loginButton.setReadPermissions("email");

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        try {
                            System.out.println("onSuccess" + loginResult.getAccessToken());
                            Toast.makeText(getApplicationContext(), getString(R.string.Toast_Facebook_Granted),
                                    Toast.LENGTH_SHORT).show();
                            GraphRequest request = GraphRequest.newMeRequest(
                                    loginResult.getAccessToken(),
                                    new GraphRequest.GraphJSONObjectCallback() {
                                        @Override
                                        public void onCompleted(JSONObject object, GraphResponse response) {
                                            Log.v("LoginActivity", response.toString());

                                            try {
                                                // Application code
//                                                String email = object.getString("email");
                                                String id = object.getString("id");

                                                String dataString = getFBStringLogin(id);

                                                String URL = Constant.LOGIN_FACEBOOK_URL;
                                                progressDialog = ProgressDialog.show(RegisterActivity.this, "", "Please wait.....");

                                                LoginAsyncTask asyncTask = new LoginAsyncTask(context, URL, dataString, progressDialog, "", sLang);
                                                asyncTask.execute();
                                            }
                                            catch (Exception ex)
                                            {

                                            }
                                        }
                                    });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender,birthday");
                            request.setParameters(parameters);
                            request.executeAsync();

                        }
                        catch (Exception ex)
                        {

                        }

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
    }

    public void initialize()
    {
        already_hv_acc_btn = (Button) findViewById(R.id.already_hv_acc_btn);
        take_a_tour_btn = (Button) findViewById(R.id.take_a_tour_btn);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        sign_in_button = (LinearLayout) findViewById(R.id.sign_in_button);
        sign_fb_button = (LinearLayout) findViewById(R.id.sign_fb_button);
        termsCondition = (LinearLayout) findViewById(R.id.termsCondition);

        chkAndroid = (CheckBox) findViewById(R.id.chkAndroid);;

        already_hv_acc_btn.setOnClickListener(this);
        take_a_tour_btn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
        sign_in_button.setOnClickListener(this);
        sign_fb_button.setOnClickListener(this);
        termsCondition.setOnClickListener(this);
    }

    public void popup() {
        try {

            SharedPreferences sharepreferences = context.getSharedPreferences("LOGIN_POP_FIRST", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharepreferences.edit();

            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_log_in);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);

            LinearLayout btnOk = (LinearLayout) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub


                    editor.putString("IS_LOGIN_POP", "TRUE");
                    editor.commit();

                    dialog.dismiss();

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

        if(v == already_hv_acc_btn)
        {
            login_pop = loginPopPreferences.getString("IS_LOGIN_POP", "");
            if(login_pop.equals("TRUE")) {

                if (chkAndroid.isChecked()) {

                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.Toast_Select_CheckBox),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(RegisterActivity.this, Login_activity.class);
                startActivity(intent);
            }
            else
            {
                popup();
            }
        }
        else if(v == take_a_tour_btn)
        {
            Intent intent = new Intent(RegisterActivity.this, TourActivity.class);
            intent.putExtra("MODE","REGISTER");
            startActivity(intent);
            finish();
        }
        else if(v == signup_btn)
        {

            Intent intent = new Intent(RegisterActivity.this, SignUpActivity.class);
            startActivity(intent);

        }
        else if(v == sign_in_button)
        {
            ConnectionDetector cd = new ConnectionDetector(RegisterActivity.this);
            final Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent) {
                signIn();
            }
            else
            {
                Toast.makeText(getApplicationContext(), getString(R.string.No_Connection),
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if(v == sign_fb_button)
        {
            ConnectionDetector cd = new ConnectionDetector(RegisterActivity.this);
            final Boolean isInternetPresent = cd.isConnectingToInternet();

            if(isInternetPresent) {

                LoginManager.getInstance().logInWithReadPermissions(RegisterActivity.this, Arrays.asList("public_profile", "email"));
            }
            else
            {
                Toast.makeText(getApplicationContext(), getString(R.string.No_Connection),
                        Toast.LENGTH_SHORT).show();
            }
        }
        else if(v == termsCondition)
        {
            Intent intent = new Intent(RegisterActivity.this, TermsCondition.class);
            startActivity(intent);
        }

     }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else
        {
            System.out.println("::::::::::::onActivityResult::::::::::::");

            System.out.println("::::::::::::ResultCode::::::::::::" + resultCode);

            callbackManager.onActivityResult(requestCode, resultCode, data);


//            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);

        }
    }



//    @Override
//    public void onStart() {
//        super.onStart();
//
//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            System.out.println( "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
//        }
//    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please Wait...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        System.out.println( "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            SharedPreferences sharepreferences = context.getSharedPreferences("LOGINTYPE", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharepreferences.edit();

            editor.putString("LOGIN", "GOOGLE");
            editor.commit();

            String personName = acct.getDisplayName();
            //String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            String dataString = getStringLogin(email);

            String URL = Constant.LOGIN_GOOGLE_URL;
            progressDialog = ProgressDialog.show(RegisterActivity.this, "", "Please wait.....");

            LoginAsyncTask asyncTask = new LoginAsyncTask(context, URL, dataString, progressDialog, email, sLang);
            asyncTask.execute();


        } else {
            // Signed out, show unauthenticated UI.
        }
    }

    public String getStringLogin(String email_login)
    {
        String Return="{\n\t";
        Return = Return + "\"GoogleEmail\":"      + "\""+ email_login       +"\"";
        Return =  Return + "\n}";

        return Return;
    }

    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Keyhash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }



    private void getFacebookUserInfo() {
//        Session.openActiveSession(this, true, new Session.StatusCallback() {
//
//            @Override
//            public void call(Session session, SessionState state,
//                             Exception exception) {
//
//                if (session.isOpened()) {
//                    boolean isPermissionAvailable = false;
//                    for (int i = 0; i < session.getPermissions().size(); i++) {
//                        if (session.getPermissions().get(i).contains("email")) {
//                            pd = ProgressDialog.show(RegisterActivity.this, "", "Please wait.....");
//                            isPermissionAvailable = true;
//
//                            Request.newMeRequest(session,
//                                    new Request.GraphUserCallback() {
//
//                                        @Override
//                                        public void onCompleted(
//                                                final GraphUser user,
//                                                Response response) {
//
//                                            if (user != null) {
//                                                getUserInfoFromFacebook(user, pd);
//
//                                                System.out.println("::::::::::::Facebook Logged IN::::::::::::");
//                                            }
//                                        }
//                                    }).executeAsync();
//                        }
//                    }
//                    if (!isPermissionAvailable) {
//                        System.out.println("::::::::::::isPermissionAvailable FALSE::::::::::::");
//
//                        getPermissionFromFacebook();
//                    }
//                }
//            }
//        });
    }

    private void getPermissionFromFacebook() {

        System.out.println("::::::::::::Facebook Permission Method::::::::::::");


        String[] permissions = { "email" };
//        Session.getActiveSession().requestNewReadPermissions(
//                new Session.NewPermissionsRequest(RegisterActivity.this, Arrays
//                        .asList(permissions)));

    }

    public String getFBStringLogin(String id)
    {
        String Return="{\n\t";
        Return = Return + "\"FacebookId\":"      + "\""+ id       +"\"";
        Return =  Return + "\n}";

        return Return;
    }

//    private void getUserInfoFromFacebook(final GraphUser user, ProgressDialog pd) {
//        userHashmap = new HashMap<String, String>();
//
//        try {
//
//            String name = user.getInnerJSONObject().get("name").toString();
//            String id = user.getInnerJSONObject().get("id").toString();
////            String email = user.asMap().get("email").toString();
//
////            userHashmap.put(USER_ID, user.getId());
////            userHashmap.put(USER_NAME, user.getUsername());
////            userHashmap.put(FIRST_NAME, user.getFirstName());
////            userHashmap.put(LAST_NAME, user.getLastName());
////            userHashmap.put(BIRTHDAY, user.getBirthday());
////            userHashmap.put(GENDER, (String) user.getProperty("gender"));
////            userHashmap.put(EMAIL_ID, user.asMap().get("email").toString());
//
//            String dataString = getFBStringLogin(id);
//
//            String URL = Constant.LOGIN_FACEBOOK_URL;
//            //progressDialog = ProgressDialog.show(RegisterActivity.this, "", "Please wait.....");
//
//            LoginAsyncTask asyncTask = new LoginAsyncTask(context, URL, dataString, pd, "");
//            asyncTask.execute();
//
//        }catch(Exception ex)
//        {
//
//        }
//
//    }
}
