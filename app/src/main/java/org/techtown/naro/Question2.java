package org.techtown.naro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Question2 extends AppCompatActivity {

    Integer getscore;
    Button yes;
    Button no;
    Integer count;
    byte[] img;

    String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question2);

        getscore = getIntent().getIntExtra("score",0);
        yes = findViewById(R.id.yes2);
        no = findViewById(R.id.no2);
        count = getscore;
        img = getIntent().getByteArrayExtra("img1");
        result = getIntent().getStringExtra("result1");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Question3.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count += 10;
                intent.putExtra("score2",count);
                intent.putExtra("img2",img);
                intent.putExtra("result2",result);
                getApplicationContext().startActivity(intent);

            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Question3.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count += 0;
                intent.putExtra("score2",count);
                intent.putExtra("img2",img);
                intent.putExtra("result2",result);
                getApplicationContext().startActivity(intent);
            }
        });

    }
}
