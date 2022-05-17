package org.techtown.naro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
                getApplicationContext().startActivity(intent);
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
