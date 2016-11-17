package com.mi.metadata;

import java.util.Vector;

/**
 * Created by karandsingh on 16-09-15.
 */
public class MyPurchaseMetadata {

    String iMyDealId;
    String iCampaignId;
    String vName;
    String vAddress;
    String vCity;
    String vZip;
    String vStartTime;
    String vEndTime;
    String vValid;
    String vCampaignOption;
    String iQuantity;
    String dTotalIncome;
    String isRedeemable;
    String dtCreationDate;
    String vEndTimeFormat;
    String dPrice;
    String eStatus;
    String dEndDate;
    String vFinePrint;
    Vector<AddOnsMetadata> addOns;
    String qst;
    String gst;
    String vValid_fr;

    public String getiMyDealId() {
        return iMyDealId;
    }

    public void setiMyDealId(String iMyDealId) {
        this.iMyDealId = iMyDealId;
    }

    public String getiCampaignId() {
        return iCampaignId;
    }

    public void setiCampaignId(String iCampaignId) {
        this.iCampaignId = iCampaignId;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
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

    public String getvCampaignOption() {
        return vCampaignOption;
    }

    public void setvCampaignOption(String vCampaignOption) {
        this.vCampaignOption = vCampaignOption;
    }

    public String getiQuantity() {
        return iQuantity;
    }

    public void setiQuantity(String iQuantity) {
        this.iQuantity = iQuantity;
    }

    public String getdTotalIncome() {
        return dTotalIncome;
    }

    public void setdTotalIncome(String dTotalIncome) {
        this.dTotalIncome = dTotalIncome;
    }

    public Vector<AddOnsMetadata> getAddOns() {
        return addOns;
    }

    public void setAddOns(Vector<AddOnsMetadata> addOns) {
        this.addOns = addOns;
    }

    public String getIsRedeemable() {
        return isRedeemable;
    }

    public void setIsRedeemable(String isRedeemable) {
        this.isRedeemable = isRedeemable;
    }

    public String getDtCreationDate() {
        return dtCreationDate;
    }

    public void setDtCreationDate(String dtCreationDate) {
        this.dtCreationDate = dtCreationDate;
    }

    public String getvEndTimeFormat() {
        return vEndTimeFormat;
    }

    public void setvEndTimeFormat(String vEndTimeFormat) {
        this.vEndTimeFormat = vEndTimeFormat;
    }

    public String getdPrice() {
        return dPrice;
    }

    public void setdPrice(String dPrice) {
        this.dPrice = dPrice;
    }

    public String geteStatus() {
        return eStatus;
    }

    public void seteStatus(String eStatus) {
        this.eStatus = eStatus;
    }

    public String getdEndDate() {
        return dEndDate;
    }

    public void setdEndDate(String dEndDate) {
        this.dEndDate = dEndDate;
    }

    public String getvFinePrint() {
        return vFinePrint;
    }

    public void setvFinePrint(String vFinePrint) {
        this.vFinePrint = vFinePrint;
    }

    public String getQst() {
        return qst;
    }

    public void setQst(String qst) {
        this.qst = qst;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getvValid_fr() {
        return vValid_fr;
    }

    public void setvValid_fr(String vValid_fr) {
        this.vValid_fr = vValid_fr;
    }
}
