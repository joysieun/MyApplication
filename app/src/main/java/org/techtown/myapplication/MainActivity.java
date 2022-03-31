package org.techtown.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    Button btn_diagnose;
    Button btn_info;
    Button btn_doginfo;
    Toolbar toolbar;
    String email;
    DogDB dogDB;
    SQLiteDatabase DB;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_home_24);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        email = googleuser.getEmail();


        dogDB = new DogDB(MainActivity.this,"Dog.db",null,2);
        DB = dogDB.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT userid FROM User_dog WHERE  userid = '" +email+ "'ORDER BY userid",null);



        btn_diagnose = findViewById(R.id.btn_diagnose);
        btn_diagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Camera.class);
                startActivity(intent); //액티비티띄우기
            }
        });
        btn_info = findViewById(R.id.btn_info);
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutInfo.class);
                startActivity(intent);
            }
        });

        btn_doginfo = findViewById(R.id.btn_doginfo);
        btn_doginfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cursor.getCount() != 0 ){
                    Toast.makeText(MainActivity.this, "이미등록",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), DogInfo.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case android.R.id.home:
                Intent back = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(back);
                return true;


            default:
                ((GoogleLogin)GoogleLogin.mContext).signOut();
                Intent in = new Intent(getApplicationContext(),GoogleLogin.class);
                startActivity(in);
                return true;
        }

    }

}