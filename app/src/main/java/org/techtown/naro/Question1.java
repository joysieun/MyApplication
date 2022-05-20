package org.techtown.naro;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Question1 extends AppCompatActivity {

    Button yes;
    Button no;
    Integer count=0;
    byte[] img;
    String result;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            // Apply activity transition
//            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//            Slide slide = new Slide();
//            slide.setDuration(1000);
//            getWindow().setEnterTransition(slide);
//            getWindow().setExitTransition(slide);
//
//        } else {
//            // Swap without transition
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.question1);

        yes = findViewById(R.id.yes1);
        no = findViewById(R.id.no1);
        img = getIntent().getByteArrayExtra("img");
        result = getIntent().getStringExtra("result");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Question2.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count += 10;
                intent.putExtra("score",count);
                intent.putExtra("img1",img);
                intent.putExtra("result1",result);
                getApplicationContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Question1.this).toBundle());
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Question2.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count += 0;
                intent.putExtra("score",count);
                intent.putExtra("img1",img);
                intent.putExtra("result1",result);
                getApplicationContext().startActivity(intent);
            }
        });
    }
}
