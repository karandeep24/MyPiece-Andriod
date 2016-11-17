package com.mi.common;

/**
 * Created by karandsingh on 16-08-30.
 */
public class Constant {
//    public static String LOCAL_GET = "http://40.76.89.156/api/";
//    public static String LOCAL_POST = "http://40.76.89.156/api/";



    public static String LOCAL_GET = "http://52.232.129.95/api/";
    public static String LOCAL_POST = "http://52.232.129.95/api/";

    public static String GET_ALL_DEALS = LOCAL_GET + "get_all_deals?"; //latitude=45.4705408&longitude=-73.580427&radius=10";
    public static String LOGIN_URL = LOCAL_POST + "login_user_email?";
    public static String LOGIN_GOOGLE_URL = LOCAL_POST + "login_user_google?";
    public static String LOGIN_FACEBOOK_URL = LOCAL_POST + "login_user_facebook?";
    public static String CREATE_CARD = LOCAL_POST + "create_credit_card?";
    public static String CHECKOUT = LOCAL_POST + "checkout?";
    public static String MY_PURCHASE = LOCAL_POST + "get_all_redeemable_deals?";
    public static String REDEEM = LOCAL_POST + "redeem_deal?";
    public static String SIGNUP = LOCAL_POST + "add_user_email?";
    public static String DEL_CARD = LOCAL_POST + "delete_credit_card?";
    public static String ADD_VIEW = LOCAL_POST + "add_view_count?";
    public static String DISMISS_DEAL = LOCAL_GET + "dismiss_deal?";

    public static String SEND_EMAIL = LOCAL_GET + "send_forgot_password_email?";
    public static String VERIFY_PWD = LOCAL_POST + "verify_password_reset_code?";

    public static String DEFAULT_LAT = "45.4957";
    public static String DEFAULT_LONG ="-73.5782";


/*
    Karans-MacBook-Pro:~ karandsingh$ keytool -exportcert -keystore /Users/karandsingh/AndroidStudioProjects/MyPiece.jks -list -v
    Enter keystore password:

    Keystore type: JKS
    Keystore provider: SUN

    Your keystore contains 1 entry

    Alias name: android
    Creation date: 11-Sep-2016
    Entry type: PrivateKeyEntry
    Certificate chain length: 1
    Certificate[1]:
    Owner: CN=Karan
    Issuer: CN=Karan
    Serial number: 57d4f0e5
    Valid from: Sun Sep 11 01:51:33 EDT 2016 until: Thu Sep 05 01:51:33 EDT 2041
    Certificate fingerprints:
    MD5:  97:C0:7F:70:0B:9F:89:FD:BF:FD:F7:1A:9C:6C:89:88
    SHA1: 2C:A8:D7:AB:02:81:0E:90:66:C9:18:26:A1:F8:FA:B9:54:F5:C6:A4
    SHA256: 45:F8:66:68:30:06:E6:0B:14:36:62:6A:55:1E:95:9F:A0:AE:2A:1C:CC:D3:41:2D:9D:24:AE:51:C2:79:18:E0
    Signature algorithm name: SHA1withRSA
    Version: 3

*/
}
