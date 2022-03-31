package org.techtown.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Loading extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 3000);

    }
    private class splashhandler implements Runnable{

        @Override
        public void run() {
            startActivity(new Intent(getApplication(),GoogleLogin.class));
            Loading.this.finish();
        }
    }


}
