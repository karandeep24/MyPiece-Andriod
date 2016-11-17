package com.mi.parsers;

import java.io.InputStream;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mi.metadata.AddOnsMetadata;
import com.mi.metadata.DashboardMetadata;
import com.mi.utility.ParserUtility;

public class DashboardParsers {

    private ParserUtility parserUtility = new ParserUtility();
    private Vector<DashboardMetadata> vectordashboardMetadata = new Vector<DashboardMetadata>();
    DashboardMetadata dashboardMetadata;


    public JSONArray getJsonArrayFromURL(String url, String data) {

        InputStream inputStream = null;
        String result = "";
        JSONArray jsonArray = null;
        JSONObject jsonObject = null;
        try
        {
            result = parserUtility.getURL(url);
            jsonArray = new JSONArray(result.trim());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;

    }

    public Vector<DashboardMetadata> parseLeaveVector(String url) {

        try {
            String data = "";
            AddOnsMetadata addOnsMetadata;

            String result = getJSONString(url);

            JSONArray outerJSON = new JSONArray(result.trim());

            if (outerJSON != null && outerJSON.length() > 0) {
                for (int j = 0; j < outerJSON.length(); j++)
                {


                    dashboardMetadata = new DashboardMetadata();
                    JSONObject jsonObject = outerJSON.getJSONObject(j);

                    try {

                        String status = jsonObject.getString("status");
                        dashboardMetadata.setStatus(status);

                        String message = jsonObject.getString("message");
                        dashboardMetadata.setMessage(message);

                        String message_fr = jsonObject.getString("message_fr");
                        dashboardMetadata.setMessage_fr(message_fr);

                        String next_active_day = jsonObject.getString("next_active_day");
                        dashboardMetadata.setNext_active_day(next_active_day);

                        String next_active_time = jsonObject.getString("next_active_time");
                        dashboardMetadata.setNext_active_time(next_active_time);
                    }
                    catch(Exception ex)
                    {

                    }

                    try
                    {
                        if (j == 0) {
                            String dGST = jsonObject.getString("dGST");
                            dashboardMetadata.setdGST(dGST);

                            String dQST = jsonObject.getString("dQST");
                            dashboardMetadata.setdQST(dQST);
                        }

                        String vCampaignOption = jsonObject.getString("vCampaignOption");
                        dashboardMetadata.setvCampaignOption(vCampaignOption);

                        String iCampaignId = jsonObject.getString("iCampaignId");
                        dashboardMetadata.setiCampaignId(iCampaignId);

                        String vRestaurantName = jsonObject.getString("vRestaurantName");
                        dashboardMetadata.setvRestaurantName(vRestaurantName);

                        String vImages = jsonObject.getString("vImages");
                        dashboardMetadata.setvImages(vImages);

                        String vLatitude = jsonObject.getString("vLatitude");
                        dashboardMetadata.setvLatitude(vLatitude);

                        String vLongitude = jsonObject.getString("vLongitude");
                        dashboardMetadata.setvLongitude(vLongitude);

                        String dPrice = jsonObject.getString("dPrice");
                        dashboardMetadata.setdPrice(dPrice);

                        String dOriginPrice = jsonObject.getString("dOriginPrice");
                        dashboardMetadata.setdOriginPrice(dOriginPrice);

                        String distance = jsonObject.getString("distance");
                        dashboardMetadata.setDistance(distance);

                        String vAddress = jsonObject.getString("vAddress");
                        dashboardMetadata.setvAddress(vAddress);

                        String vCity = jsonObject.getString("vCity");
                        dashboardMetadata.setvCity(vCity);

                        String vZip = jsonObject.getString("vZip");
                        dashboardMetadata.setvZip(vZip);

                        String dStartDate = jsonObject.getString("dStartDate");
                        dashboardMetadata.setdStartDate(dStartDate);

                        String dEndDate = jsonObject.getString("dEndDate");
                        dashboardMetadata.setdEndDate(dEndDate);

                        String vStartTime = jsonObject.getString("vStartTime");
                        dashboardMetadata.setvStartTime(vStartTime);

                        String vEndTime = jsonObject.getString("vEndTime");
                        dashboardMetadata.setvEndTime(vEndTime);

                        String vValid = jsonObject.getString("vValid");
                        dashboardMetadata.setvValid(vValid);

                        String vFinePrint = jsonObject.getString("vFinePrint");
                        dashboardMetadata.setvFinePrint(vFinePrint);

                        JSONArray jsonArray = jsonObject.getJSONArray("Add-ons");

                        if (jsonArray != null && jsonArray.length() > 0) {
                            Vector<AddOnsMetadata> addOnsMetadataVector = new Vector<AddOnsMetadata>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jObject = jsonArray.getJSONObject(i);
                                    addOnsMetadata = new AddOnsMetadata();

                                    String iAddOnId = jObject.getString("iAddOnId");
                                    addOnsMetadata.setiAddOnId(iAddOnId);

                                    String dAddOnPrice = jObject.getString("dAddOnPrice");
                                    addOnsMetadata.setdAddOnPrice(dAddOnPrice);

                                    String vAddOnName = jObject.getString("vAddOnName");
                                    addOnsMetadata.setvAddOnName(vAddOnName);

                                    String vAddOnDesc = jObject.getString("vAddOnDesc");
                                    addOnsMetadata.setvAddOnDesc(vAddOnDesc);

                                    addOnsMetadataVector.add(addOnsMetadata);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            dashboardMetadata.setAddOns(addOnsMetadataVector);
                        }
                    }
                    catch (Exception ex)
                    {

                    }

                    vectordashboardMetadata.add(dashboardMetadata);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return vectordashboardMetadata;

    }

    public String getJSONString(String url)
    {
        String result = "";
        try
        {
            result = parserUtility.getURL(url);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

