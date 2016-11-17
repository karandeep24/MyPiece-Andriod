package com.mi.parsers;

import com.mi.metadata.CreditCardMetadata;
import com.mi.metadata.LoginResponseMetadata;
import com.mi.utility.ParserUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by karandsingh on 16-08-31.
 */
public class SplashParser {

    private ParserUtility parserUtility = new ParserUtility();
    CreditCardMetadata creditCardMetadata;


    public JSONObject getJsonArrayFromURL(String url, String data) {

        String result = "";
        JSONObject jsonObject = null;
        try
        {
            result = parserUtility.performPostonData(url, data);
            jsonObject = new JSONObject(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    public LoginResponseMetadata parseLoginVector(String url, String data)
    {
        LoginResponseMetadata loginResponseMetadata = new LoginResponseMetadata();

        try
        {
            JSONObject objJSON = getJsonArrayFromURL(url, data);

            String status = objJSON.getString("status");
            loginResponseMetadata.setStatus(status);

            String message = objJSON.getString("message");
            loginResponseMetadata.setMessage(message);

            String userId = objJSON.getString("user");
            loginResponseMetadata.setUserId(userId);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return loginResponseMetadata;

    }
}
