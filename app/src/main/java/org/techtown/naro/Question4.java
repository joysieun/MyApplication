package org.techtown.naro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Question4 extends AppCompatActivity {

    Integer getscore;
    Button yes;
    Button no;
    Integer count;
    byte[] img;

    String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question4);

        getscore = getIntent().getIntExtra("score3",0);
        yes = findViewById(R.id.yes4);
        no = findViewById(R.id.no4);
        count = getscore;
        img = getIntent().getByteArrayExtra("img3");
        result = getIntent().getStringExtra("result3");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Question5.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count += 1;
                intent.putExtra("score4",count);
                intent.putExtra("img4",img);
                intent.putExtra("result4",result);
                getApplicationContext().startActivity(intent);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Question5.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count += 0;
                intent.putExtra("score4",count);
                intent.putExtra("img4",img);
                intent.putExtra("result4",result);
                getApplicationContext().startActivity(intent);
            }
        });


    }
}
