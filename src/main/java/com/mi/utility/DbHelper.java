package com.mi.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mi.metadata.CreditCardMetadata;

import java.util.HashMap;
import java.util.Vector;

/**
 * Created by karandsingh on 16-09-09.
 */
public class DbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MYPIECE";
    private static final String KEY_USERID = "iUserCardId";
    private static final String KEY_CARDID = "cardId";
    private static final String KEY_CARDNAME = "cardName";
    private static final String KEY_NAME = "name";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_EXPIRYMONTH = "exp_month";
    private static final String KEY_EXPIRYYEAR = "exp_year";
    private static final String KEY_LAST4 = "last4";
    private static String TABLE_RECORDS = "CreditCards";

    Cursor cursor;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS TABLE_RECORDS");

        // create fresh books table
        this.onCreate(db);
    }

    public void Create_table() {
        // SQL statement to create book table
        SQLiteDatabase db = this.getWritableDatabase();
        // db.execSQL("DROP TABLE IF EXISTS TABLE_RECORDS");

        String TABLE_RECORDS = "CREATE TABLE IF NOT EXISTS CreditCards ( " +
                "iUserCardId TEXT, " +
                "cardId TEXT, " +
                "cardName TEXT, " +
                "name TEXT, " +
                "brand TEXT, " +
                "country TEXT, " +
                "exp_month TEXT, " +
                "exp_year TEXT, " +
                "last4 TEXT )";

        db.execSQL(TABLE_RECORDS);
        db.close();
    }

    public void add(String iUserCardId, String cardId, String cardName, String name, String brand, String country,
                    String exp_month, String exp_year, String last4)
    {
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_USERID, iUserCardId);
        values.put(KEY_CARDID, cardId);
        values.put(KEY_CARDNAME, cardName);
        values.put(KEY_NAME, name);
        values.put(KEY_BRAND, brand);
        values.put(KEY_COUNTRY, country);
        values.put(KEY_EXPIRYMONTH, exp_month);
        values.put(KEY_EXPIRYYEAR,   exp_year);
        values.put(KEY_LAST4,   last4);

        // 3. insert
        db.insert(TABLE_RECORDS, null, values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public HashMap<String, CreditCardMetadata> getcardRecords()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        HashMap<String, CreditCardMetadata> stringCreditCardMetadataHashMap = new HashMap<String, CreditCardMetadata>();
        Vector<CreditCardMetadata> creditCardMetadataVector = new Vector<CreditCardMetadata>();
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_RECORDS;

        // 2. get reference to writable DB
        cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        CreditCardMetadata record = null;
        if (cursor.moveToFirst()) {
            do {
                record = new CreditCardMetadata();
                record.setiUserCardId(cursor.getString(0));
                record.setCardId(cursor.getString(1));
                record.setCardName(cursor.getString(2));
                record.setName(cursor.getString(3));
                record.setBrand(cursor.getString(4));
                record.setCountry(cursor.getString(5));
                record.setExp_month((cursor.getString(6)));
                record.setExp_year(cursor.getString(7));
                record.setLast4(cursor.getString(8));
                // Add book to books
                stringCreditCardMetadataHashMap.put(record.getCardName(), record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return stringCreditCardMetadataHashMap;
    }

    public void delete()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS CreditCards");
        db.close();

    }

    public void deleteCard(String userCardID)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE from CreditCards Where iUserCardId = " + userCardID);
        db.close();

    }
 }
