package org.techtown.naro;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HospitalDB extends SQLiteOpenHelper {

    public HospitalDB(Context context){
        super(context,"hospital.db",null,2);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public Cursor getUserList(){
        SQLiteDatabase DB = getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT name, place, onoff FROM HOSPITAL ORDER BY name",null);
        return cursor;
    }
}

