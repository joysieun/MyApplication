package org.techtown.naro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MemoDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Memo.db";
    public static final String TABLE_NAME = "User_memo";


    public static final String COLUMN_ID ="_id";
    public static final String COLUMN_USER_ID = "userid";//사용자 아이디
    public static final String COLUMN_MEMO = "memo"; //메모내용
    public static final String COLUMN_TIME = "time"; //마이페이지케어시간

    FirebaseAuth firebaseAuth;


    public MemoDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+ TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userid TEXT, memo TEXT, time TEXT );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertmemodata(String userid, String memo, String time){
        SQLiteDatabase db3 = getWritableDatabase();
        ContentValues cv3 = new ContentValues();
        cv3.put(COLUMN_USER_ID, userid);
        cv3.put(COLUMN_MEMO, memo);
        cv3.put(COLUMN_TIME, time);

        long result = db3.insert(TABLE_NAME,null,cv3);

        if(result == -1){
            return false;

        }
        else{
            return true;

        }

    }
    public void deletedata(String memo){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM User_memo WHERE memo='" + memo + "'; ");
    }

    public void deleteall(String time){
        SQLiteDatabase DB = this.getReadableDatabase();
        DB.execSQL("DELETE FROM User_memo WHERE time='"+time+"';");
    }

    public Cursor getUserList(String pagetime){

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        String email = googleuser.getEmail();

        SQLiteDatabase DB = getReadableDatabase();
        String result="";
        Cursor cursor = DB.rawQuery("SELECT userid, memo, time FROM User_memo WHERE userid = '" +email+ "' and time = '"+ pagetime +"' ORDER BY userid" ,null);
        return cursor;
    }

}
