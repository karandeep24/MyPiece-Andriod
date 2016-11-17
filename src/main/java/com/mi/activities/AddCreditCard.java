package com.mi.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.mi.asytask.AddCreditCardAsyncTask;
import com.mi.asytask.DeleteCardAsyncTask;
import com.mi.asytask.LoginAsyncTask;
import com.mi.common.Constant;
import com.mi.metadata.CreditCardMetadata;
import com.mi.test.mypiece.R;
import com.mi.utility.DbHelper;
import com.mi.webapi.ConnectionDetector;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.stripe.exception.CardException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by karandsingh on 16-09-13.
 */
public class AddCreditCard extends AppCompatActivity implements View.OnClickListener{

    EditText cardName, cardNumber, cardCVV, cardMM, cardYY, PostalCode;
    EditText cardHolderName, cardAddress, cardAddress2, City, cardEmail;
    ArrayList<String> countries = new ArrayList<String>();
    HashMap<Integer, String> countyry_map = new HashMap<Integer, String>();
    LinearLayout btnSave, redBtn, greenBtn, btnDelete;
    Context context;
    String sCardName, sCardNumber, sCardCVV, sCardMM, sCardYY, sPostalCode;
    String sCardHolderName, sCardAddress, sCity, sCardEmail, sUserId;
    boolean isValidated =false;
    static String tokenId;
    ProgressDialog progressDialog;
    public static AddCreditCard instance = null;
    SharedPreferences sharepreferences;
    SharedPreferences.Editor editor;
    Intent intent;
    String cardId, mode, userCardID, route;
    CreditCardMetadata creditCardMetadata = new CreditCardMetadata();
    HashMap<String, CreditCardMetadata> creditMap;
    DbHelper dbHelper;
    SharedPreferences userPreferences;
    boolean isEmail = false;
    HashMap<String, String> frenchMsg = new HashMap<String, String>();
    String sLang;
    SharedPreferences langPreferences;
    public static boolean saveDisable = false;
    private static final char space = ' ';
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);

        setContentView(R.layout.add_credit_card);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        context = this;
        instance = this;

        langPreferences = this.getSharedPreferences("CommonPrefs", MODE_PRIVATE);
        sLang = langPreferences.getString("Language", "en");

        intent = getIntent();
        mode = intent.getStringExtra("MODE");
        route = intent.getStringExtra("ROUTE");

        sharepreferences = context.getSharedPreferences("User_id", Context.MODE_PRIVATE);
        sUserId = sharepreferences.getString("user_id_value", "");

        initialize();

        if(mode.equals("EDITDEL")) {

            isEditable(false);

            cardId = intent.getStringExtra("CARDID");
            dbHelper = new DbHelper(this);

            creditMap = dbHelper.getcardRecords();
            creditCardMetadata = creditMap.get(cardId);

            greenBtn.setVisibility(View.GONE);
            redBtn.setVisibility(View.VISIBLE);

            fill_form();
        }
        else
        {
            isEditable(true);

            greenBtn.setVisibility(View.VISIBLE);
            redBtn.setVisibility(View.GONE);

            String sCreditEmail;

            userPreferences = context.getSharedPreferences("User_id", Context.MODE_PRIVATE);

            if(userPreferences.getString("EMAIL", "").equals(""))
            {
                sCreditEmail = "";
                isEmail = false;
            }else
            {
                sCreditEmail = userPreferences.getString("EMAIL", "");
                isEmail = true;
            }

            cardEmail.setText(sCreditEmail);
            if(isEmail)
            {
                cardEmail.setEnabled(false);
            }
            else
            {
                cardEmail.setEnabled(true);
            }
        }

        frenchMsg.put("incorrect_number", "Le numéro de la carte est incorrecte.");
        frenchMsg.put("invalid_number", "Le numéro de la carte n'est pas un numéro de carte de crédit valide.");
        frenchMsg.put("invalid_expiry_month", "Le mois d'expiration n'est pas valid.");
        frenchMsg.put("invalid_expiry_year", "L'année d'expiration n'est pas valid.");
        frenchMsg.put("invalid_cvc", "Le code de sécurité n'est pas valid.");
        frenchMsg.put("expired_card", "La carte est expirée");
        frenchMsg.put("incorrect_cvc", "Le code de sécurité est incorrect.");
        frenchMsg.put("incorrect_zip", "Le code postal n'est pas valid.");
        frenchMsg.put("card_declined", "La carte a été refusée.");
        frenchMsg.put("missing", "Il n'y a pas de carte sur un client qui est en cours de charge.");
        frenchMsg.put("processing_error", "Une erreur est survenue lors du traitement de la carte.");

        cardNumber.addTextChangedListener(new TextWatcher() {
            private static final char space = ' ';

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove all spacing char
                int pos = 0;
                while (true) {
                    if (pos >= s.length()) break;
                    if (space == s.charAt(pos) && (((pos + 1) % 5) != 0 || pos + 1 == s.length())) {
                        s.delete(pos, pos + 1);
                    } else {
                        pos++;
                    }
                }

                // Insert char where needed.
                pos = 4;
                while (true) {
                    if (pos >= s.length()) break;
                    final char c = s.charAt(pos);
                    // Only if its a digit where there should be a space we insert a space
                    if ("0123456789".indexOf(c) >= 0) {
                        s.insert(pos, "" + space);
                    }
                    pos += 5;
                }
            }
        });
    }

    public void fill_form()
    {
        cardName.setText(creditCardMetadata.getCardName());
        cardHolderName.setText(creditCardMetadata.getName());

        String cardNum = creditCardMetadata.getLast4();
        cardNum = "XXXX XXXX XXXX " + cardNum;

        cardNumber.setText(cardNum);
        cardMM.setText(creditCardMetadata.getExp_month());
        cardYY.setText(creditCardMetadata.getExp_year());

        cardCVV.setText("XXX");
        cardAddress.setVisibility(View.INVISIBLE);
        City.setVisibility(View.INVISIBLE);
        PostalCode.setVisibility(View.INVISIBLE);
        cardEmail.setVisibility(View.INVISIBLE);
        cardAddress2.setVisibility(View.INVISIBLE);

    }

    public void initialize()
    {
        cardName = (EditText) findViewById(R.id.cardName);
        cardNumber = (EditText) findViewById(R.id.cardNumber);
        cardCVV = (EditText) findViewById(R.id.cardCVV);
        cardMM = (EditText) findViewById(R.id.cardMM);
        cardYY = (EditText) findViewById(R.id.cardYY);
        cardHolderName = (EditText) findViewById(R.id.cardHolderName);
        cardAddress = (EditText) findViewById(R.id.cardAddress);
        cardAddress2 = (EditText) findViewById(R.id.cardAddress2);
        City = (EditText) findViewById(R.id.City);
        PostalCode = (EditText) findViewById(R.id.PostalCode);
        cardEmail = (EditText) findViewById(R.id.cardEmail);

        btnSave = (LinearLayout) findViewById(R.id.btnSave);
        btnDelete = (LinearLayout) findViewById(R.id.btnDelete);
        redBtn = (LinearLayout) findViewById(R.id.redBtn);
        greenBtn = (LinearLayout) findViewById(R.id.greenBtn);

        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        redBtn.setOnClickListener(this);
        greenBtn.setOnClickListener(this);

        countyry_map.put(1, "Canada");
        countyry_map.put(2, "USA");
    }

    public void getCardRecords()
    {
        sCardName = cardName.getText().toString();
        sCardNumber = cardNumber.getText().toString();
        sCardCVV = cardCVV.getText().toString();
        sCardMM = cardMM.getText().toString();
        sCardYY = cardYY.getText().toString();
        sCardHolderName = cardHolderName.getText().toString();
        sCardAddress = cardAddress.getText().toString();
        sCity = City.getText().toString();
        sCardEmail = cardEmail.getText().toString();
        sPostalCode = PostalCode.getText().toString();

        sCardNumber.replaceAll(" ", "");
    }


    public String purchase ()
    {
        try {

            boolean isNetwork = new ConnectionDetector(getApplicationContext()).isConnectingToInternet();

            if (isNetwork) {

                tokenId = "";

                Card card = new Card(sCardNumber, Integer.parseInt(sCardMM), Integer.parseInt(sCardYY), sCardCVV);
                card.setName(sCardHolderName);
                card.setAddressLine1(sCardAddress);
                card.setAddressCity(sCity);
                card.setAddressZip(sPostalCode);

                Stripe stripe = new Stripe("pk_test_AnYAqCPUVBxbzP2usDbGuxGK");
                stripe.createToken(
                        card,
                        new TokenCallback() {
                            public void onSuccess(Token token) {
                                // Send token to your server
                                System.out.println("::::::::::::::::::TOKEN ::::" + token.getId());
                                tokenId = token.getId();
                                sendToken();
                            }

                            public void onError(Exception error) {
                                // Show localized error message

                                saveDisable = false;

                                if(sLang.equals("en")) {

                                    Toast.makeText(context, error.getMessage(),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                                else if(sLang.equals("fr"))
                                {

                                    CardException cardException = (CardException) error;
                                    Toast.makeText(context, frenchMsg.get(cardException.getCode()),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        }
                );
            }
            else {
                Toast.makeText(AddCreditCard.this, getString(R.string.No_Connection), Toast.LENGTH_SHORT).show();

            }
        }
        catch (Exception ex)
        {
            tokenId = "";
            ex.printStackTrace();
        }

        return tokenId;
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stubs
        super.onBackPressed();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public boolean validate()
    {
        boolean check = false;

        if(sCardName.length() == 0)
        {
            Toast.makeText(context, getString(R.string.Toast_Add_Card_Name), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        if( check && sCardNumber.length() < 16)
        {
            Toast.makeText(context, getString(R.string.Toast_Add_Card_Number), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        if( check && cardCVV.length() != 3 )
        {
            Toast.makeText(context, getString(R.string.Toast_Add_Card_CVC), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        if( check && cardMM.length() == 0 )
        {
            Toast.makeText(context, getString(R.string.Toast_Add_Card_Month), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        if( check && cardYY.length() == 0 )
        {
            Toast.makeText(context, getString(R.string.Toast_Add_Card_Year), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        if( check && cardHolderName.length() == 0 )
        {
            Toast.makeText(context, getString(R.string.Toast_Add_Card_HolderName), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        if( check && cardAddress.length() == 0 )
        {
            Toast.makeText(context, getString(R.string.Toast_Add_Card_Address), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        if( check && City.length() == 0 )
        {
            Toast.makeText(context, getString(R.string.Toast_Add_Card_City), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        if( check && sPostalCode.length() == 0 )
        {
            Toast.makeText(context, getString(R.string.Toast_Add_Card_Code), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        if( check && sCardEmail.length() == 0 )
        {
            Toast.makeText(context, getString(R.string.Toast_Add_Card_Email), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        if( check && !validate(sCardEmail) )
        {
            Toast.makeText(context, getString(R.string.Toast_WrongEmail), Toast.LENGTH_SHORT).show();
            check = false;
            return check;
        }
        else
        {
            check = true;
        }

        return check;
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public String getStringCreateCard(String userId, String token, String card_Name, String email)
    {
        String Return = "";

        if(isEmail) {
            Return = "{\n\t";
            Return = Return + "\"UserId\":" + "\"" + userId + "\",";
            Return = Return + "\n\t";
            Return = Return + "\"Token\":" + "\"" + token + "\",";
            Return = Return + "\n\t";
            Return = Return + "\"Card_Name\":" + "\"" + card_Name + "\"";
            Return = Return + "\n}";
        }
        else
        {
            Return = "{\n\t";
            Return = Return + "\"UserId\":" + "\"" + userId + "\",";
            Return = Return + "\n\t";
            Return = Return + "\"Token\":" + "\"" + token + "\",";
            Return = Return + "\n\t";
            Return = Return + "\"Card_Name\":" + "\"" + card_Name + "\",";
            Return = Return + "\n\t";
            Return = Return + "\"Email\":" + "\"" + email + "\"";
            Return = Return + "\n}";
        }

        return Return;
    }

    public void sendToken()
    {
        System.out.println("::::::::::::::::::in TOKEN ::::" + tokenId);

        boolean isNetwork = new ConnectionDetector(getApplicationContext()).isConnectingToInternet();

        if (isNetwork) {

            if (tokenId.length() > 0) {

                String email = cardEmail.getText().toString();

                String dataString = getStringCreateCard(sUserId, tokenId, sCardName, email);



                System.out.println("::::::::::::::::::in dataString ::::" + dataString);

                String URL = Constant.CREATE_CARD;
                progressDialog = ProgressDialog.show(AddCreditCard.this, "", "Please wait.....");

                AddCreditCardAsyncTask asyncTask = new AddCreditCardAsyncTask(context, URL, dataString, progressDialog, sCardEmail, route);
                asyncTask.execute();
            }
        }
        else
        {
            saveDisable = true;
            Toast.makeText(AddCreditCard.this, getString(R.string.No_Connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

        if(v == btnSave)
        {
            try
            {
                if (mode.equals("ADD")) {


                    getCardRecords();
                    isValidated = validate();

                    if (isValidated) {

                        if(!saveDisable) {

                            System.out.println("::::::::::::::::::ADD ADD ADD ::::");

                            saveDisable = true;
                            purchase();
                        }
                    }
                }

            }catch (Exception ex)
            {
                System.out.println("::::::::::::::::::Exception ::::" + ex.toString());
            }
        }
        else if(v == btnDelete)
        {
            if(mode.equals("EDITDEL"))
            {
                userCardID = creditCardMetadata.getiUserCardId();

                boolean isNetwork = new ConnectionDetector(getApplicationContext()).isConnectingToInternet();

                if (isNetwork) {

                    String delDataString = getDelCardDatastring(sUserId, userCardID);
                    String URL = Constant.DEL_CARD;

                    progressDialog = ProgressDialog.show(AddCreditCard.this, "", "Please wait.....");

                    DeleteCardAsyncTask asyncTask = new DeleteCardAsyncTask(context, URL, delDataString, progressDialog, userCardID);
                    asyncTask.execute();
                }else
                {
                    Toast.makeText(AddCreditCard.this, getString(R.string.No_Connection), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }


    public String getDelCardDatastring(String sUserId, String userCardID)
    {
        String Return="{\n\t";
        Return = Return + "\"UserId\":"      + "\""+ sUserId       +"\",";
        Return=  Return + "\n\t";
        Return = Return + "\"UserCardId\":"       + "\""+ userCardID        +"\"";
        Return = Return + "\n}";

        return Return;
    }

    public void isEditable(boolean flag)
    {
        cardName.setEnabled(flag);
        cardNumber.setEnabled(flag);
        cardCVV.setEnabled(flag);
        cardMM.setEnabled(flag);
        cardYY.setEnabled(flag);
        cardHolderName.setEnabled(flag);
        cardAddress.setEnabled(flag);
        City.setEnabled(flag);
        cardEmail.setEnabled(flag);
        PostalCode.setEnabled(flag);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish(); // close this activity and return to preview activity (if there is any)
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
