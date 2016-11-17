package com.mi.metadata;


import java.util.Vector;

public class DashboardMetadata {

    String vCampaignOption;
    String iCampaignId;
    String vRestaurantName;
    String vImages;
    String vLatitude;
    String vLongitude;
    String dPrice;
    String dOriginPrice;
    String distance;
    String dStartDate;
    String dEndDate;
    String vStartTime;
    String vEndTime;
    String vValid;
    String vAddress;
    String vCity;
    String vZip;
    String vFinePrint;
    String dGST;
    String dQST;

    String status;
    String message;
    String message_fr;
    String next_active_day;
    String next_active_time;

    Vector<AddOnsMetadata> addOns;

    public String getvCampaignOption() {
        return vCampaignOption;
    }

    public void setvCampaignOption(String vCampaignOption) {
        this.vCampaignOption = vCampaignOption;
    }

    public String getiCampaignId() {
        return iCampaignId;
    }

    public void setiCampaignId(String iCampaignId) {
        this.iCampaignId = iCampaignId;
    }

    public String getvRestaurantName() {
        return vRestaurantName;
    }

    public void setvRestaurantName(String vRestaurantName) {
        this.vRestaurantName = vRestaurantName;
    }

    public String getvImages() {
        return vImages;
    }

    public void setvImages(String vImages) {
        this.vImages = vImages;
    }

    public String getvLatitude() {
        return vLatitude;
    }

    public void setvLatitude(String vLatitude) {
        this.vLatitude = vLatitude;
    }

    public String getvLongitude() {
        return vLongitude;
    }

    public void setvLongitude(String vLongitude) {
        this.vLongitude = vLongitude;
    }

    public String getdPrice() {
        return dPrice;
    }

    public void setdPrice(String dPrice) {
        this.dPrice = dPrice;
    }

    public String getdOriginPrice() {
        return dOriginPrice;
    }

    public void setdOriginPrice(String dOriginPrice) {
        this.dOriginPrice = dOriginPrice;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getdStartDate() {
        return dStartDate;
    }

    public void setdStartDate(String dStartDate) {
        this.dStartDate = dStartDate;
    }

    public String getdEndDate() {
        return dEndDate;
    }

    public void setdEndDate(String dEndDate) {
        this.dEndDate = dEndDate;
    }

    public String getvStartTime() {
        return vStartTime;
    }

    public void setvStartTime(String vStartTime) {
        this.vStartTime = vStartTime;
    }

    public String getvEndTime() {
        return vEndTime;
    }

    public void setvEndTime(String vEndTime) {
        this.vEndTime = vEndTime;
    }

    public String getvValid() {
        return vValid;
    }

    public void setvValid(String vValid) {
        this.vValid = vValid;
    }

    public String getvAddress() {
        return vAddress;
    }

    public void setvAddress(String vAddress) {
        this.vAddress = vAddress;
    }

    public String getvCity() {
        return vCity;
    }

    public void setvCity(String vCity) {
        this.vCity = vCity;
    }

    public String getvZip() {
        return vZip;
    }

    public void setvZip(String vZip) {
        this.vZip = vZip;
    }

    public Vector<AddOnsMetadata> getAddOns() {
        return addOns;
    }

    public String getvFinePrint() {
        return vFinePrint;
    }

    public void setvFinePrint(String vFinePrint) {
        this.vFinePrint = vFinePrint;
    }

    public void setAddOns(Vector<AddOnsMetadata> addOns) {
        this.addOns = addOns;
    }

    public String getdGST() {
        return dGST;
    }

    public void setdGST(String dGST) {
        this.dGST = dGST;
    }

    public String getdQST() {
        return dQST;
    }

    public void setdQST(String dQST) {
        this.dQST = dQST;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_fr() {
        return message_fr;
    }

    public void setMessage_fr(String message_fr) {
        this.message_fr = message_fr;
    }

    public String getNext_active_day() {
        return next_active_day;
    }

    public void setNext_active_day(String next_active_day) {
        this.next_active_day = next_active_day;
    }

    public String getNext_active_time() {
        return next_active_time;
    }

    public void setNext_active_time(String next_active_time) {
        this.next_active_time = next_active_time;
    }
}
