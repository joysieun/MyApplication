package org.techtown.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class AboutInfo extends AppCompatActivity {

    FragmentInfo fragmentInfo;
    FragmentHos fragmentHos;
    FragmentMypage fragmentMypage;
    TabLayout tabs;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_about);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_home_24);


        fragmentInfo = new FragmentInfo();
        fragmentHos = new FragmentHos();
        fragmentMypage = new FragmentMypage();
        //기본프래그먼트 첫화면설정
        getSupportFragmentManager().beginTransaction().replace(R.id.containers,fragmentInfo).commit();

        tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("강아지피부질병"));
        tabs.addTab(tabs.newTab().setText("우리동네병원"));
        tabs.addTab(tabs.newTab().setText("마이페이지"));
        tabs.setTabTextColors(Color.rgb(0,0,0),Color.rgb(255,0,0));
        //탭 선택시 사용하는 이벤트 리스너
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Fragment selected =null;
                //피부질병탭
                if(position == 0){
                    selected = fragmentInfo;
                }else if(position ==1){
                    selected = fragmentHos;
                }
                else if(position ==2){
                    selected = fragmentMypage;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.containers,selected).commit();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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
