package com.example.carofinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";


    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create table users(username TEXT PRIMARY KEY, password TEXT,gold INTEGER,choosen INTEGER, listSkin TEXT)");
        MyDB.execSQL("create table match_history(username_1 TEXT , username_2 TEXT , ketqua TEXT,thoigian TEXT, lichsu TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop table if exists users ");
        MyDB.execSQL("drop table if exists match_history");
    }

    public Boolean insertData(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("gold",0);
        contentValues.put("choosen",0);
        contentValues.put("listSkin","0");


        long result = MyDB.insert("users", null, contentValues);
        if (result == -1) return false;
        else return true;

    }

    public Boolean checkUsername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where username=?", new String[]{username});
        if (cursor.getCount() > 0) {
            return true;
        } else return false;

    }

    public Boolean checkUsernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where username=? and password=?", new String[]{username, password});
        if (cursor.getCount() > 0) {
            return true;
        } else return false;
    }
    public void  updateGold(String userName,int gold)
    {
        SQLiteDatabase MyDB=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",userName);
        contentValues.put("gold",gold);
        MyDB.update("users",contentValues,"username = ?",new String[]{userName});
        MyDB.close();

    }
    public Integer getGold(String username)
    {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where username=?", new String[]{username});
        Integer gold=-1;
        if(cursor!=null && cursor.moveToFirst()) {
            gold = cursor.getInt(2);
            cursor.close();
        }
        MyDB.close();
        return gold;
    }
    public void  updateChoosen(String userName,int choosen)
    {
        SQLiteDatabase MyDB=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",userName);
        contentValues.put("choosen",choosen);
        MyDB.update("users",contentValues,"username = ?",new String[]{userName});
        MyDB.close();

    }
    public Integer getChoosen(String username)
    {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where username=?", new String[]{username});
        Integer choosen=0;
        if(cursor!=null && cursor.moveToFirst()) {
            choosen = cursor.getInt(3);
            cursor.close();
        }
        MyDB.close();
        return choosen;
    }
    public void  updatelistSkin(String userName,int ID)
    {
        String list=this.getListSkin(userName);
        list+=",";
        list+=String.valueOf(ID);

        SQLiteDatabase MyDB=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",userName);

        contentValues.put("listSkin",list);
        MyDB.update("users",contentValues,"username = ?",new String[]{userName});
        MyDB.close();

    }
    public String getListSkin(String username)
    {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where username=?", new String[]{username});
        String listSKin="";
        if(cursor!=null && cursor.moveToFirst()) {
            listSKin = cursor.getString(4);
            cursor.close();
        }
        MyDB.close();
        return listSKin;
    }

    public boolean insertMatch(String username,String username2,String ketqua,String thoigian,String lichsu)
    {
        SQLiteDatabase MyDB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("username_1",username);
        contentValues.put("username_2",username2);
        contentValues.put("ketqua",ketqua);
        contentValues.put("thoigian",thoigian);
        contentValues.put("lichsu",lichsu);

        long result = MyDB.insert("match_history", null, contentValues);
        if (result == -1) return false;
        else return true;
    }
    public ArrayList<ArrayList<String>> getMatchHistory(String username)
    {
        SQLiteDatabase MyDB=this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from match_history where username_1=?", new String[]{username});
        ArrayList<ArrayList<String>> result=new ArrayList<ArrayList<String>>();
        ArrayList<String> username_1=new ArrayList<>();
        ArrayList<String>username_2=new ArrayList<>();
        ArrayList<String>ketqua=new ArrayList<>();
        ArrayList<String>thoigian=new ArrayList<>();
        ArrayList<String>lichsu=new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do{
                username_1.add(cursor.getString(0));
                username_2.add(cursor.getString(1));
                ketqua.add(cursor.getString(2));
                thoigian.add(cursor.getString(3));
                lichsu.add(cursor.getString(4));
            }
            while (cursor.moveToNext());
        }
        result.add(username_1);
        result.add(username_2);
        result.add(ketqua);
        result.add(thoigian);
        result.add(lichsu);
        return result;
    }

}
