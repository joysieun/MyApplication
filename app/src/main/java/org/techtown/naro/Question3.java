package org.techtown.naro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Question3 extends AppCompatActivity {

    Integer getscore;
    Button yes;
    Button no;
    Integer count;
    byte[] img;

    String result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question3);

        getscore = getIntent().getIntExtra("score2",0);
        yes = findViewById(R.id.yes3);
        no = findViewById(R.id.no3);
        count = getscore;
        img = getIntent().getByteArrayExtra("img2");
        result = getIntent().getStringExtra("result2");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Question4.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count += 10;
                intent.putExtra("score3",count);
                intent.putExtra("img3",img);
                intent.putExtra("result3",result);
                getApplicationContext().startActivity(intent);
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Question4.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                count += 0;
                intent.putExtra("score3",count);
                intent.putExtra("img3",img);
                intent.putExtra("result3",result);
                getApplicationContext().startActivity(intent);
            }
        });
    }


}
