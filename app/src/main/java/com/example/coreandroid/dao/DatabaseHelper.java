package com.example.coreandroid.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {

    public final static String DATABASE_NAME = "user.auth";
    public final static String TABLE_NAME = "token";
    public final static String COL_1 = "id";
    public final static String COL_2 = "_id";
    public final static String COL_3 = "token";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT, _id TEXT, token TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String _id, String token){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_2, _id);
        cv.put(COL_3, token);
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1) return false;
        else return true;
    }

    public Cursor getByToken(String token){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE token='"+token+"'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update(Integer id, String _id, String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", _id);
        contentValues.put("token", token);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteByToken(String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "token = ? ",
                new String[] { token });
    }

    public ArrayList<String> getAll(){
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false){
            array_list.add(cursor.getString(cursor.getColumnIndex(COL_3)));
            cursor.moveToNext();
        }
        return array_list;
    }

    public String checkToken(){
        List<String> tokens = this.getAll();
        if(tokens != null && tokens.size() > 0){
            return tokens.get(tokens.size()-1);
        }else{
            return null;
        }

    }



}
