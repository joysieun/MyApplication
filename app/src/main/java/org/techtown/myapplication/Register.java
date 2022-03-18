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

                    startActivity(new Intent(Register.this, Login.class));
                    break;
            }
        }
    };
}
