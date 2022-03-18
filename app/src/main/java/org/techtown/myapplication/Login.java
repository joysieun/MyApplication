package org.techtown.myapplication;

import android.content.ContentValues;
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
    //SQLiteDatabase db;

    String sql;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

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

                    Intent i = new Intent(Login.this, MainActivity.class);
                    startActivity(i);


            }
        }
    };


}






