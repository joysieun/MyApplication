package org.techtown.naro;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResultDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Results.db";
    public static final String TABLE_NAME = "Result_Info";

    public static final String COLUMN_ID ="_id";

    public static final String COLUMN_USER_ID = "userid";//사용자 아이디
    public static final String COLUMN_TYPE = "cardtype"; // 메모인지, 피부체크인지, 케어인지
    public static final String COLUMN_SKIN_IMAGE = "skin_image"; //피부이미지
    public static final String COLUMN_RESULT = "p_result"; //진단결과
    public static final String COLUMN_TIME = "ctime"; //날짜

    FirebaseAuth firebaseAuth;
    //ResultDB 생성자로 관리할 DB 이름과 버전 정보를 받음

    public ResultDB(Context context,String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);

    }
    //테이블 생성
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+ TABLE_NAME+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,  userid TEXT, cardtype TEXT, p_result TEXT, ctime TEXT, skin_image BLOB );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    //결과에 대한 데이터 등록
    public boolean insertdata( String userid,String cardtype, String skinresult, String ctime, byte[] pet_image){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_USER_ID, userid);
        cv.put(COLUMN_TYPE, cardtype);
        cv.put(COLUMN_RESULT, skinresult);
        cv.put(COLUMN_TIME, ctime);
        cv.put(COLUMN_SKIN_IMAGE, pet_image);


        long result = db.insert(TABLE_NAME,"",cv);
        if(result == -1){
            return false;

        }
        else{
            return true;

        }

    }

    //데이터 다 보여주기
    public Cursor getAllData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor res = DB.rawQuery("select*from "+TABLE_NAME, null);
        return res;
    }
    //해당 유저의 행 삭제
    public void deletedata(String userid ){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Result_Info WHERE userid='" + userid + "'; ");
    }
    public void deletetime(String time){
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("DELETE FROM Result_Info WHERE ctime='" + time + "'; ");
        DB.close();
    }
    public Cursor getUserList(){
        //구글 로그인 사용자의 이메일 가져오기
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        String email = googleuser.getEmail();

        SQLiteDatabase DB = getReadableDatabase();
        String result = "";
        Cursor cursor = DB.rawQuery("SELECT userid, cardtype, p_result, ctime, skin_image FROM Result_Info WHERE userid = '" +email+ "'ORDER BY userid",null);
        return cursor;
    }


}
