package org.techtown.naro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Question5 extends AppCompatActivity {

    Integer getscore;
    Button yes;
    Button no;
    Integer count;
    byte[] img;
    String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question5);

        getscore = getIntent().getIntExtra("score4",0);
        yes = findViewById(R.id.yes5);
        no = findViewById(R.id.no5);
        count = getscore;
        img = getIntent().getByteArrayExtra("img4");
        result = getIntent().getStringExtra("result4");


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),QuestionResult.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count += 2;
                intent.putExtra("score5",count);
                intent.putExtra("img5",img);
                intent.putExtra("result5",result);
                getApplicationContext().startActivity(intent);

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),QuestionResult.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count += 0;
                intent.putExtra("score5",count);
                intent.putExtra("img5",img);
                intent.putExtra("result5",result);
                getApplicationContext().startActivity(intent);
            }
        });


    }
}
