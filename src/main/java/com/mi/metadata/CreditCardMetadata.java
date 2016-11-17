package com.mi.metadata;

/**
 * Created by karandsingh on 16-09-12.
 */
public class CreditCardMetadata {

    public String iUserCardId;
    public String cardId;
    public String cardName;
    public String name;
    public String brand;
    public String country;
    public String exp_month;
    public String exp_year;
    public String last4;

    public String getiUserCardId() {
        return iUserCardId;
    }

    public void setiUserCardId(String iUserCardId) {
        this.iUserCardId = iUserCardId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getExp_month() {
        return exp_month;
    }

    public void setExp_month(String exp_month) {
        this.exp_month = exp_month;
    }

    public String getExp_year() {
        return exp_year;
    }

    public void setExp_year(String exp_year) {
        this.exp_year = exp_year;
    }

    public String getLast4() {
        return last4;
    }

    public void setLast4(String last4) {
        this.last4 = last4;
    }
}
