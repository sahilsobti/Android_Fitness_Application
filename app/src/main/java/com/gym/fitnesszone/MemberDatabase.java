package com.gym.fitnesszone;

/**
 * Created by sahil on 23/06/2015.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MemberDatabase {

    public static final String KEY_ROWID = "Id";
    public static final String KEY_NAME = "Name";
    public static final String KEY_AGE = "Age";
    public static final String KEY_SEX = "Sex";
    public static final String KEY_PHONE = "Phone";
    public static final String KEY_DOB = "Dob";
    public static final String KEY_JOINING = "Joining_date";
    private static final String KEY_PAYMENT = "payment_received";
    private static final String KEY_PAYMENT_FLAG = "payment_flag";
    private static final String KEY_MESSAGE_FLAG = "message_flag";
    private static final String KEY_BIRTHDAY_FLAG = "birthday_flag";
    private static final String DATABASE_NAME = "FitnessDb";
    private static final String DATABASE_TABLE = "FMember";
    private static final int DATABASE_VERSION = 1;
    private Context ourContext= null;
    public ListView l;
    private SQLiteDatabase ourDatabase;
    private DatabaseCreation helper;
    String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_AGE, KEY_SEX, KEY_PHONE, KEY_DOB, KEY_JOINING,KEY_PAYMENT,KEY_PAYMENT_FLAG,KEY_MESSAGE_FLAG,KEY_BIRTHDAY_FLAG};
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
    public MemberDatabase(Context sp) {
        ourContext = sp;
    }

    public MemberDatabase open() throws SQLException {
        helper = new DatabaseCreation(ourContext);
        ourDatabase = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public long createEntry(String pname, int page, String psex, String pph,
                            String dob, String join,String payDate,int flag) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, pname);
        cv.put(KEY_AGE, page);
        cv.put(KEY_SEX, psex);
        cv.put(KEY_PHONE, pph);
        cv.put(KEY_DOB, dob);
        cv.put(KEY_JOINING, join);
        cv.put(KEY_PAYMENT,payDate);
        cv.put(KEY_PAYMENT_FLAG, flag);
        cv.put(KEY_BIRTHDAY_FLAG, 0);
        return ourDatabase.insert(DATABASE_TABLE, null, cv);
    }
    public long modifyEntry(long key,String pname, int page, String psex, String pph,
                            String dob, String join,String payDate,int flag) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_NAME, pname);
        cv.put(KEY_AGE, page);
        cv.put(KEY_SEX, psex);
        cv.put(KEY_PHONE, pph);
        cv.put(KEY_DOB, dob);
        cv.put(KEY_JOINING, join);
        cv.put(KEY_PAYMENT,payDate);
        cv.put(KEY_PAYMENT_FLAG,flag);
        cv.put(KEY_MESSAGE_FLAG,0);
        cv.put(KEY_BIRTHDAY_FLAG, 0);
        return ourDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + "=" + key, null);
    }

    public String[] getNames() throws SQLException {
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns,null,null,null,null,KEY_NAME+" COLLATE NOCASE");
        String[] result = new String[c.getCount()];
        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iSex = c.getColumnIndex(KEY_SEX);
        int counter = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result[counter] = c.getString(iRow) + "&" + c.getString(iName) + "&" + c.getString(iSex) + "\n";
            counter++;
        }
        return result;
    }



    void setPaymentFlag() throws SQLException{
        Calendar cal = Calendar.getInstance();
        String s1 = sdf.format(cal.getTime());
        ContentValues cv = new ContentValues();
        cv.put(KEY_PAYMENT_FLAG,1);
        int i = ourDatabase.update(DATABASE_TABLE,cv,KEY_PAYMENT+"='"+s1.trim()+"' and "+KEY_PAYMENT_FLAG +" = 0",null);
    }

    void resetPaymentFlag(String value) throws SQLException{
        long ll = Long.parseLong(value.trim());
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + " = " + ll, null, null, null, null);
        c.moveToFirst();
        int iJoin = c.getColumnIndex(KEY_PAYMENT);
        String join = c.getString(iJoin);
        try {
            Date d = sdf.parse(join);
            Calendar call = Calendar.getInstance();
            call.setTime(d);
            call.add(call.MONTH,1);
            String sjoin = sdf.format(call.getTime());
            ContentValues cv = new ContentValues();
            cv.put(KEY_PAYMENT_FLAG, 0);
            cv.put(KEY_PAYMENT, sjoin);
            cv.put(KEY_MESSAGE_FLAG,0);
            ourDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + " = " + ll + " AND " + KEY_PAYMENT_FLAG + " =1", null);
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public String[] getalertData() throws SQLException {
        Cursor c = ourDatabase.query(DATABASE_TABLE,columns,KEY_PAYMENT_FLAG + "= 1",null,null,null,KEY_NAME+" COLLATE NOCASE");
        String[] result = new String[c.getCount()];
        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iSex = c.getColumnIndex(KEY_SEX);
        int iPay = c.getColumnIndex(KEY_PAYMENT);
        int counter = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result[counter] = c.getString(iRow) + "&" + c.getString(iName) + "&" + c.getString(iSex)
                    + "&" +c.getString(iPay) +  "\n";
            counter++;
        }
        return result;
    }

    public String[] sendalertData() throws SQLException {
        Cursor c = ourDatabase.query(DATABASE_TABLE,columns,KEY_PAYMENT_FLAG + "= 1" + " AND " + KEY_MESSAGE_FLAG + "=0",null,null,null,null);
        String[] result = new String[c.getCount()];
        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iPhone = c.getColumnIndex(KEY_PHONE);
        int iPay = c.getColumnIndex(KEY_PAYMENT);
        int counter = 0;
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            result[counter] = c.getString(iRow) + "&" +c.getString(iName) + "&" + c.getString(iPhone) +"&" + c.getString(iPay) + "\n";
            counter++;
        }
        return result;
    }
    void resetMessageFlag(String value) throws SQLException{
        long ll = Long.parseLong(value.trim());
        ContentValues cv = new ContentValues();
        cv.put(KEY_MESSAGE_FLAG,1);
        ourDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + " = " + ll, null);
    }



    public int deleteContact(long Rkey) throws SQLException{
        try {
            int r = ourDatabase.delete(DATABASE_TABLE, KEY_ROWID + "=" + Rkey, null);
        }catch(Exception e){
            return 0;
        }
        return 1;
    }

    public String getData(long Rkey) throws SQLException {
        // TODO Auto-generated method stub
        Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID + "=" + Rkey, null, null, null, null);
        String result = "";
        int iName = c.getColumnIndex(KEY_NAME);
        int iAge = c.getColumnIndex(KEY_AGE);
        int iSex = c.getColumnIndex(KEY_SEX);
        int iPhone = c.getColumnIndex(KEY_PHONE);
        int iDob = c.getColumnIndex(KEY_DOB);
        int iJoining = c.getColumnIndex(KEY_JOINING);
        c.moveToFirst();
        result = c.getString(iName) + "&" + c.getString(iAge) + "&" + c.getString(iSex)
                + "&" + c.getString(iPhone) + "&" + c.getString(iDob) + "&" + c.getString(iJoining);
        return result;
    }

    private static class DatabaseCreation extends SQLiteOpenHelper {

        public DatabaseCreation(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE " + DATABASE_TABLE + " ("
                    + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NAME + " TEXT NOT NULL, "
                    + KEY_AGE + " INTEGER NOT NULL, "
                    + KEY_SEX + " TEXT NOT NULL, "
                    + KEY_PHONE + " TEXT NOT NULL, "
                    + KEY_DOB + " TEXT NOT NULL, "
                    + KEY_JOINING + " TEXT NOT NULL, "
                    + KEY_PAYMENT + " TEXT NOT NULL, "
                    + KEY_PAYMENT_FLAG + " INTEGER NOT NULL, "
                    + KEY_MESSAGE_FLAG + " INTEGER DEFAULT 0, "
                    + KEY_BIRTHDAY_FLAG + " INTEGER DEFAULT 0);"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }


    }


}

