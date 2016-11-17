package com.mi.parsers;

import com.mi.metadata.CreditCardMetadata;
import com.mi.metadata.LoginResponseMetadata;
import com.mi.metadata.SuccessMetadata;
import com.mi.utility.ParserUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by karandsingh on 16-09-14.
 */
public class SuccessParser {

    private ParserUtility parserUtility = new ParserUtility();

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

    public SuccessMetadata parsePaymentVector(String url, String data)
    {
        SuccessMetadata successMetadata = new SuccessMetadata();
        try
        {
            JSONObject objJSON = getJsonArrayFromURL(url, data);

            String status = objJSON.getString("status");
            successMetadata.setStatus(status);

            String message = objJSON.getString("message");
            successMetadata.setMessage(message);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return successMetadata;

    }

    public SuccessMetadata parsePaymentVector(String url)
    {
        SuccessMetadata successMetadata = new SuccessMetadata();
        try
        {
            JSONObject objJSON = getJsonArrayFromURL(url);

            String status = objJSON.getString("status");
            successMetadata.setStatus(status);

            String message = objJSON.getString("message");
            successMetadata.setMessage(message);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return successMetadata;

    }

    public JSONObject getJsonArrayFromURL(String url) {

        String result = "";
        JSONObject jsonObject = null;
        try
        {
            result = parserUtility.getJsonArrayFromURL(url);
            jsonObject = new JSONObject(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;

    }
}

