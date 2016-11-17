package com.mi.parsers;

import com.mi.metadata.AddOnsMetadata;
import com.mi.metadata.DashboardMetadata;
import com.mi.metadata.MyPurchaseMetadata;
import com.mi.utility.ParserUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Vector;

/**
 * Created by karandsingh on 16-09-15.
 */
public class MyPurchaseParser {

    private ParserUtility parserUtility = new ParserUtility();
    private Vector<MyPurchaseMetadata> myPurchaseMetadataVector= new Vector<MyPurchaseMetadata>();
    MyPurchaseMetadata myPurchaseMetadata;


    public JSONArray getJsonArrayFromURL(String url) {

        InputStream inputStream = null;
        String result = "";
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        try {
            result = parserUtility.getURL(url);
            jsonArray = new JSONArray(result.trim());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;

    }

    public Vector<MyPurchaseMetadata> parsePurchaseVector(String url) {

        try {
            String data = "";
            AddOnsMetadata addOnsMetadata;


            JSONArray outerJSON= getJsonArrayFromURL(url);

            if (outerJSON != null && outerJSON.length() > 0) {
                for (int j = 0; j < outerJSON.length(); j++)
                {


                    myPurchaseMetadata = new MyPurchaseMetadata();
                    JSONObject jsonObject = outerJSON.getJSONObject(j);

                    String iMyDealId = jsonObject.getString("iMyDealId");
                    myPurchaseMetadata.setiMyDealId(iMyDealId);

                    String iCampaignId = jsonObject.getString("iCampaignId");
                    myPurchaseMetadata.setiCampaignId(iCampaignId);

                    String vName = jsonObject.getString("vName");
                    myPurchaseMetadata.setvName(vName);

                    String vAddress = jsonObject.getString("vAddress");
                    myPurchaseMetadata.setvAddress(vAddress);

                    String vCity = jsonObject.getString("vCity");
                    myPurchaseMetadata.setvCity(vCity);

                    String vZip = jsonObject.getString("vZip");
                    myPurchaseMetadata.setvZip(vZip);

                    String vStartTime = jsonObject.getString("vStartTime");
                    myPurchaseMetadata.setvStartTime(vStartTime);

                    String vEndTime = jsonObject.getString("vEndTime");
                    myPurchaseMetadata.setvEndTime(vEndTime);

                    String vEndTimeFormat = jsonObject.getString("vEndTimeFormat");
                    myPurchaseMetadata.setvEndTimeFormat(vEndTimeFormat);

                    String vValid = jsonObject.getString("vValid");
                    myPurchaseMetadata.setvValid(vValid);

                    String vValid_fr = jsonObject.getString("vValid_fr");
                    myPurchaseMetadata.setvValid_fr(vValid_fr);

                    String vCampaignOption = jsonObject.getString("vCampaignOption");
                    myPurchaseMetadata.setvCampaignOption(vCampaignOption);

                    String iQuantity = jsonObject.getString("iQuantity");
                    myPurchaseMetadata.setiQuantity(iQuantity);

                    String dTotalIncome = jsonObject.getString("dTotalIncome");
                    myPurchaseMetadata.setdTotalIncome(dTotalIncome);

                    String isRedeemable = jsonObject.getString("isRedeemable");
                    myPurchaseMetadata.setIsRedeemable(isRedeemable);

                    String dtCreationDate = jsonObject.getString("dtCreationDate");
                    myPurchaseMetadata.setDtCreationDate(dtCreationDate);

                    String dPrice = jsonObject.getString("dPrice");
                    myPurchaseMetadata.setdPrice(dPrice);

                    String dEndDate = jsonObject.getString("dEndDate");
                    myPurchaseMetadata.setdEndDate(dEndDate);

                    String eStatus = jsonObject.getString("eStatus");
                    myPurchaseMetadata.seteStatus(eStatus);

                    String vFinePrint = jsonObject.getString("vFinePrint");
                    myPurchaseMetadata.setvFinePrint(vFinePrint);

                    String dGST = jsonObject.getString("dGST");
                    myPurchaseMetadata.setGst(dGST);

                    String dQST = jsonObject.getString("dQST");
                    myPurchaseMetadata.setQst(dQST);

                    JSONArray jsonArray = jsonObject.getJSONArray("Add-ons");

                    if (jsonArray != null && jsonArray.length() > 0) {
                        Vector<AddOnsMetadata> addOnsMetadataVector = new Vector<AddOnsMetadata>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jObject = jsonArray.getJSONObject(i);
                                addOnsMetadata = new AddOnsMetadata();

                                String vAddOnName = jObject.getString("vAddOnName");
                                addOnsMetadata.setvAddOnName(vAddOnName);

                                String iQuantityA = jObject.getString("iQuantity");
                                addOnsMetadata.setiQuantity(iQuantityA);

                                String dAddOnPrice = jObject.getString("dAddOnPrice");
                                addOnsMetadata.setdAddOnPrice(dAddOnPrice);

                                addOnsMetadataVector.add(addOnsMetadata);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        myPurchaseMetadata.setAddOns(addOnsMetadataVector);
                    }
                    myPurchaseMetadataVector.add(myPurchaseMetadata);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return myPurchaseMetadataVector;

    }


}