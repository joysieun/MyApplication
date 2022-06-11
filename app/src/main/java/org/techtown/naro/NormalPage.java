package org.techtown.naro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NormalPage extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Button button;

    String getcheck;
    byte[] getimg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.normal_page);

        imageView = findViewById(R.id.img_normalpage);
        textView = findViewById(R.id.tv_normalpage);
        button = findViewById(R.id.btn_hospital);

        getcheck = getIntent().getStringExtra("normal_class");
        getimg  = getIntent().getByteArrayExtra("skinimagenormal");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });


    }
}
