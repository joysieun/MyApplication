package org.techtown.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    EditText idText;
    EditText pwdText;

    String pwd;
    String id;

    Button btnlogin;
    Button btnregister;
    PetResultDB helper;
    SQLiteDatabase db;

    String sql;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        helper = new PetResultDB(Login.this);

        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException e) {
            db = helper.getWritableDatabase();

        }


        idText = (EditText) findViewById(R.id.editid);
        pwdText = (EditText) findViewById(R.id.editpassword);
        btnlogin = (Button) findViewById(R.id.login_ok);
        btnregister = (Button) findViewById(R.id.want_register);

        btnlogin.setOnClickListener(listener);
        btnregister.setOnClickListener(listener);
    }


    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                case R.id.want_register:
                    Intent intent = new Intent(Login.this, Register.class);
                    startActivity(intent);
                    finish();
                    break;

                case R.id.login_ok:
                    id = idText.getText().toString();
                    pwd = pwdText.getText().toString();

                    if (id.isEmpty() || pwd.isEmpty()) {
                        Toast.makeText(Login.this, "아이디 또는 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                        idText.setText(null);
                        pwdText.setText(null);

                    }

                    sql = "select * from petanduser where u_id = '" + id + "' and u_pwd = '" + pwd + "'";
                    Cursor cursor = db.rawQuery(sql, null);
                    while (cursor.moveToNext()) {
                        String no = cursor.getString(0);
                        String rest_id = cursor.getString(1);
                        Log.d("select", "no:" + no + "\nrest_id:" + rest_id);

                    }
                    if (cursor.getCount() == 1) {
                        Toast.makeText(Login.this, id + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Login.this, MainActivity.class);
                        i.putExtra("u_id", id);
                        i.putExtra("u_pwd", pwd);
                        startActivity(i);
                        finish();
                    } else {
                        //없으면 아무것도 안가져옴 count==0
                        Toast.makeText(Login.this, "아이디 또는 비밀번호가 틀립니다.", Toast.LENGTH_SHORT).show();
                        idText.setText(null);
                        pwdText.setText(null);


                        cursor.close();
                        break;


                    }
            }
        }
    };


}






