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

public class ResultPage extends AppCompatActivity {

    Button question;
    ImageView skinimage;
    TextView check;

    String getcheck;
    byte[] getimg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resultpage);

        question = (Button) findViewById(R.id.btn_Qstart);
        skinimage = findViewById(R.id.img_resultpage);
        check = findViewById(R.id.tv_resultpage);

        getcheck = getIntent().getStringExtra("result_class");
        getimg  = getIntent().getByteArrayExtra("skinimage");

        Bitmap bitmap = BitmapFactory.decodeByteArray(getimg,0,getimg.length);
        skinimage.setImageBitmap(bitmap);
        check.setText(getcheck);

        question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Question1.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("img",getimg);
                intent.putExtra("result",getcheck);
                getApplicationContext().startActivity(intent);

            }
        });


    }
}
