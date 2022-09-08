package org.techtown.naro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChatDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Chat.db";
    public static final String TABLE_NAME = "User_chat";


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_ID = "userid";//사용자 아이디
    public static final String COLUMN_CHAT = "contents"; //채팅 내용
    public static final String COLUMN_TIME = "time"; //채팅시간

    FirebaseAuth firebaseAuth;
    ChatAdapter adapter;


    public ChatDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, userid TEXT, memo TEXT, time TEXT );");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertchatdata(String userid, String contents, String time) {
        SQLiteDatabase db4 = getWritableDatabase();
        ContentValues cv4 = new ContentValues();
        cv4.put(COLUMN_USER_ID, userid);
        cv4.put(COLUMN_CHAT, contents);
        cv4.put(COLUMN_TIME, time);

        long result = db4.insert(TABLE_NAME, null, cv4);



        if (result == -1) {
            return false;

        } else {

            return true;

        }

    }


    public void delete_contents(String userid) {
        SQLiteDatabase DB = this.getReadableDatabase();
        DB.execSQL("DELETE FROM User_chat WHERE userid='" + userid + "';");

    }


    public Cursor getChatList() {

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        String email = googleuser.getEmail();

        SQLiteDatabase DB = getReadableDatabase();
        String result = "";
        Cursor cursor = DB.rawQuery("SELECT userid, contents, time FROM User_chat WHERE userid = '" + email+"';", null);

        return cursor;
    }

}
