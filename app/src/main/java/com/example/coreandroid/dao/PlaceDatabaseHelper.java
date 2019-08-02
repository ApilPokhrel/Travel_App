package com.example.coreandroid.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.coreandroid.entity.LocationModel;
import com.example.coreandroid.entity.PlaceModel;
import com.example.coreandroid.entity.ProfileModel;

import java.util.ArrayList;
import java.util.List;

public class PlaceDatabaseHelper extends SQLiteOpenHelper {


    public final static String DATABASE_NAME = "travel";
    public final static String TABLE_NAME = "place";
    public final static String COL1 = "id";
    public final static String COL2 = "_id";
    public final static String COL3 = "title";
    public final static String COL4 = "title";
    public final static String COL5 = "title";
    public final static String COL6 = "lat";
    public final static String COL7 = "lng";
    public final static String COL8 = "address";
    public final static String COL9 = "description";
    public final static String COL10 = "tag";
    public final static String COL11 = "profiles";



    public PlaceDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT, _id TEXT, title TEXT, contact TEXT, average REAL, description TEXT, address TEXT, tag TEXT, profiles TEXT, lat REAL, lng REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }


    public boolean insertData(String _id, String title, String contact, float average, double lat, double lng, String address, String description, String tag, String profiles){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL2, _id);
        cv.put(COL3, title);
        cv.put(COL4, contact);
        cv.put(COL5, average);
        cv.put(COL6, lat);
        cv.put(COL7, lng);
        cv.put(COL8, address);
        cv.put(COL9, description);
        cv.put(COL10, tag);
        cv.put(COL11, profiles);
        Cursor cursor = getBy_id(_id);
        if(cursor.getCount() > 0){
            return false;
        }
        long result = db.insert(TABLE_NAME, null, cv);
        if(result == -1) return false;
        else return true;
    }

    public Cursor getBy_id(String _id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE _id='"+_id+"'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE id='"+Integer.toString(id)+"'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    public boolean update(String _id, String title, double lat, double lng, String address, String description, String tag, String profiles) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL3, title);
        cv.put(COL4, lat);
        cv.put(COL5, lng);
        cv.put(COL6, address);
        cv.put(COL7, description);
        cv.put(COL8, tag);
        cv.put(COL9, profiles);
        db.update(TABLE_NAME, cv, "_id = ? ", new String[] { _id } );
        return true;
    }

    public Integer deleteBy_id(String _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "_id = ? ",
                new String[] { _id });
    }

    public List<PlaceModel> getAll(){
        List<PlaceModel> myList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            String _id = cursor.getString(cursor.getColumnIndex(COL2));
            String title = cursor.getString(cursor.getColumnIndex(COL3));
            String contact = cursor.getString(cursor.getColumnIndex(COL4));
            float average = cursor.getFloat(cursor.getColumnIndex(COL5));
            double lat  = cursor.getDouble(cursor.getColumnIndex(COL6));
            double lng = cursor.getDouble(cursor.getColumnIndex(COL7));
            String address = cursor.getString(cursor.getColumnIndex(COL8));
            String description = cursor.getString(cursor.getColumnIndex(COL9));
            String tag = cursor.getString(cursor.getColumnIndex(COL10));
            String profile = cursor.getString(cursor.getColumnIndex(COL11));
            String[] profiles = profile.split(",");
            System.out.println("===========profiels is-=============="+profile);
            ArrayList<ProfileModel> profileModels = new ArrayList<>();
            for(String p: profiles){
                ProfileModel profileModel = new ProfileModel("image", p);
                profileModels.add(profileModel);
            }

            ArrayList<Double> coordinates = new ArrayList<>();
            coordinates.add(lng);
            coordinates.add(lat);

            LocationModel locationModel = new LocationModel(address, coordinates);
            System.out.println("profileslength =-----------------------"+profileModels.size());
            PlaceModel data = new PlaceModel(_id, title,contact, average, description, locationModel, profileModels, tag, null);
            myList.add(data);
        }
        return myList;
    }


}
