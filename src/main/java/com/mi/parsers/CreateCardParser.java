package com.mi.parsers;

import com.mi.metadata.CreditCardMetadata;
import com.mi.metadata.LoginResponseMetadata;
import com.mi.utility.ParserUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by karandsingh on 16-09-13.
 */
public class CreateCardParser {
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

            JSONArray jsonArray = objJSON.getJSONArray("cards");

            if (jsonArray != null && jsonArray.length() > 0) {
                Vector<CreditCardMetadata> creditCardMetadataVector = new Vector<CreditCardMetadata>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        creditCardMetadata = new CreditCardMetadata();

                        String iUserCardId = jObject.getString("iUserCardId");
                        creditCardMetadata.setiUserCardId(iUserCardId);

                        String cardId = jObject.getString("cardId");
                        creditCardMetadata.setCardId(cardId);

                        String cardName = jObject.getString("cardName");
                        creditCardMetadata.setCardName(cardName);

                        String name = jObject.getString("name");
                        creditCardMetadata.setName(name);

                        String brand = jObject.getString("brand");
                        creditCardMetadata.setBrand(brand);

                        String country = jObject.getString("country");
                        creditCardMetadata.setCountry(country);

                        String exp_month = jObject.getString("exp_month");
                        creditCardMetadata.setExp_month(exp_month);

                        String exp_year = jObject.getString("exp_year");
                        creditCardMetadata.setExp_year(exp_year);

                        String last4 = jObject.getString("last4");
                        creditCardMetadata.setLast4(last4);

                        creditCardMetadataVector.add(creditCardMetadata);

                    } catch (Exception ex) {

                    }
                }

                loginResponseMetadata.setCreditCardMetadata(creditCardMetadataVector);
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return loginResponseMetadata;

    }
}
