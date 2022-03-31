package org.techtown.myapplication;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.GoogleMap;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;

import org.w3c.dom.Text;

import java.util.Locale;

public class AboutInfo extends AppCompatActivity  {

    FragmentInfo fragmentInfo;
    FragmentHos fragmentHos;
    FragmentMypage fragmentMypage;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    Toolbar toolbar;
    DogDB dogDB;
    FirebaseAuth firebaseAuth;
    String email;
    SQLiteDatabase DB;
    ImageView infopage_img;
    TextView tv_petname, tv_petsex,tv_petage,tv_petbirth,tv_owner;
    String name;


    private NaverMap naverMap;
    private FusedLocationSource locationSource;
    private static final int LOCATION_PERMISSION_REQUEST_CODE =1000;
    private static final String[] PERMISSIONS={
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_about);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_home_24);

        infopage_img = findViewById(R.id.infopage_img);
        tv_petage = findViewById(R.id.tv_petage);
        tv_petbirth=findViewById(R.id.tv_petbirth);
        tv_petname = findViewById(R.id.tv_petname);
        tv_petsex = findViewById(R.id.tv_petsex);
        tv_owner = findViewById(R.id.tv_owner);


        fragmentInfo = new FragmentInfo();
        fragmentHos = new FragmentHos();
        fragmentMypage = new FragmentMypage();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.containers,fragmentInfo).commit();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu_navi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        email = googleuser.getEmail();

        firebaseAuth = FirebaseAuth.getInstance();

        name = googleuser.getDisplayName();


        dogDB = new DogDB(AboutInfo.this,"Dog.db",null,2);
        DB = dogDB.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT petname, petage, petsex, petbirth, petface FROM User_dog WHERE  userid = '" +email+ "'ORDER BY userid",null);
        Dog dog = new Dog();
        cursor.moveToFirst();
        dog.setPetname(cursor.getString(0));
        dog.setPetage(cursor.getString(1));
        dog.setPetsex(cursor.getString(2));
        dog.setPetbirth(cursor.getString(3));
        dog.setPetface(cursor.getBlob(4));
        tv_petname.setText(dog.getPetname());
        tv_petage.setText(dog.getPetage());
        tv_petsex.setText(dog.getPetsex()+"살");
        tv_petbirth.setText(dog.getPetbirth().split("-")[0]+"월"+dog.getPetbirth().split("-")[1]+"일");
        tv_owner.setText(name);
        Bitmap bt = BitmapFactory.decodeByteArray(dog.getPetface(),0,dog.getPetface().length);
        infopage_img.setImageBitmap(bt);






    }
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch(item.getItemId()){
                case R.id.menu_disease:
                    transaction.replace(R.id.containers,fragmentInfo).commitAllowingStateLoss();
                    break;


                case R.id.menu_hospital:
                    transaction.replace(R.id.containers,fragmentHos).commitAllowingStateLoss();
                    break;


                case R.id.mypage:
                    transaction.replace(R.id.containers,fragmentMypage).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
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
