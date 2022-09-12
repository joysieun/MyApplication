package org.techtown.naro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MypageCheck extends AppCompatActivity {
    Button button;
    ImageView imageView;
    TextView textView;

    byte[] img;
    String result;
    String result_time;
    Bitmap bitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_check);

        button = findViewById(R.id.check_hos);
        imageView = findViewById(R.id.img_mycheck);
        textView  = findViewById(R.id.tv_mycheck);

        img = getIntent().getByteArrayExtra("mypage_img");
        result= getIntent().getStringExtra("mypage_result");
        result_time = getIntent().getStringExtra("timeset");
        bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
        imageView.setImageBitmap(bitmap);
        textView.setText(result);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

            }
        });


    }
}
