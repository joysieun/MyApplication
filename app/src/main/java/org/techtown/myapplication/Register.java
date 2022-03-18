package org.techtown.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Register extends AppCompatActivity {

    PetResultDB openhelper;
    SQLiteDatabase db;
    EditText editname,editid, editpwd;
    Button joinbtn;

    SharedPreferences shard;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        openhelper = new PetResultDB(this);
        try{db = openhelper.getWritableDatabase();}
        catch(SQLiteException e){
            db = openhelper.getWritableDatabase();
        }

        editid = findViewById(R.id.editid2);
        editpwd = findViewById(R.id.editpassword2);
        editname = findViewById(R.id.editname);
        joinbtn = findViewById(R.id.register2);
        joinbtn.setOnClickListener(listener);


    }
    View.OnClickListener listener = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.register2:
                    String name = editname.getText().toString();
                    String id = editid.getText().toString();
                    String pwd = editpwd.getText().toString();

                    String sql = "select * from petanduser_info where u_id = '"+id+"'";

                    Cursor cursor = db.rawQuery(sql,null);
                    if(cursor.getCount() ==1){
                        //해당 아이디가 이미 있음
                        Toast.makeText(Register.this, "이미존재하는 아이디입니다.",Toast.LENGTH_SHORT).show();
                        editid.setText(null);
                        editpwd.setText(null);
                    }
                    else{
                        String sql2 = "insert into petanduser_info(u_id, u_pwd, u_name) values('"+id+"','"+pwd+"','"+name+"')";
                        db.execSQL(sql2);

                        Toast.makeText(Register.this, "회원가입을 축하합니다",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                    }
                    cursor.close();
                    break;
            }
        }
    };
}
