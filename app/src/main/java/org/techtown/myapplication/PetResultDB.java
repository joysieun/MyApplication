package org.techtown.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

//데이터베이스 만들기
public class PetResultDB extends SQLiteOpenHelper {

    private Context context;
    public static final String DATABASE_NAME ="petresult.db";
    public static final int DATABASE_VERSION =2;

    public static final String TABLE_NAME = "petanduser_info";
    public static final String COLUMN_ID ="_id";
    public static final String COLUMN_USER_ID = "u_id";//사용자 아이디
    public static final String COLUMN_PASSWORD = "u_pwd"; //사용자 비번
    public static final String COLUMN_NAME ="u_name"; //사용자 이름
    public static final String COLUMN_PET_NAME = "p_name"; //반려동물 이름
    public static final String COLUMN_SKIN_IMAGE = "skin_image"; //피부이미지
    public static final String COLUMN_RESULT = "p_result"; //진단결과
    public static final String COLUMN_TIME = "today_date"; //날짜

    //DB생성해주는 메소드
    public PetResultDB(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }


    //테이블 생성(데베 테이블)
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                COLUMN_ID
                +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " VARCHAR(225), " +
                COLUMN_PASSWORD + " INTEGER, " +
                COLUMN_NAME + " TEXT," +COLUMN_PET_NAME + " TEXT,"+
                COLUMN_RESULT + " TEXT," + COLUMN_TIME + " TEXT,"+
                COLUMN_SKIN_IMAGE+" BLOP);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
    //데이터 모두 가져오기
    public Cursor readAllData(){
        String query = "SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =null;
        if(db!=null){
            cursor = db.rawQuery(query,null);

        }
        return cursor;
    }

    public void addResult(String petname, byte[] skin_photo, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PET_NAME, petname);
        cv.put(COLUMN_SKIN_IMAGE, skin_photo);
        cv.put(COLUMN_TIME, time);

        long result = db.insert(TABLE_NAME,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();

        }

    }

    public void addMember(String userid, String userpwd, String username ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, userid);
        cv.put(COLUMN_PASSWORD,userpwd);
        cv.put(COLUMN_NAME,username);

        long result = db.insert(TABLE_NAME,null,cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(context, "Successfully", Toast.LENGTH_SHORT).show();

        }

    }

    public void delete(String time){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM petanduser_info where today_date='"+ time+"';");
        db.close();
    }

    public Cursor getUserList(){
        SQLiteDatabase DB = getReadableDatabase();
        String result="";
        Cursor cursor = DB.rawQuery("SELECT p_name, skin_image, today_date FROM petanduser_info ORDER BY p_id ",null);
        return cursor;
    }


}
