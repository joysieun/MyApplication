package org.techtown.naro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Care extends AppCompatActivity {
    private final int GET_GALLERY_IMAGE =200;
    EditText carehospital;
    EditText carecontent;
    ImageView img_care;
    Button carebutton;
    Button caresuspend;
    FirebaseAuth firebaseAuth;
    String email;

    String user;
    String title;
    String content;
    String type;
    String carecontext;
    String caretitle;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.care_activity);

        carehospital = (EditText) findViewById(R.id.carehospital);
        carecontent = (EditText) findViewById(R.id.carecontent);
        img_care = (ImageView) findViewById(R.id.imgcare);
        carebutton = (Button) findViewById(R.id.btn_care);
        caresuspend = (Button)findViewById(R.id.btn_suspend2);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        email = googleuser.getEmail();

        img_care.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*"); //c
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        carebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] data = imageViewToByte(img_care);
                user = email;
                type = "care";
                carecontext = carehospital.getText().toString();
                caretitle = carehospital.getText().toString();

                if(carecontext.length() == 0 || caretitle.length() == 0){
                    Toast.makeText(Care.this, "작성을 완성해주세요",Toast.LENGTH_SHORT).show();
                }
                else {
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String getTime = dateFormat.format(currentTime);
                    ResultDB resultdb = new ResultDB(getApplicationContext(), "Result.db", null, 2);
                    resultdb.insertdata(user, type, caretitle, getTime, data);
                    Intent i = new Intent(Care.this, MainActivity.class);
                    startActivity(i);
                }
            }});

        caresuspend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Care.this, MainActivity.class);
                startActivity(i);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            switch (requestCode){
                case GET_GALLERY_IMAGE:
                    if(requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data!=null && data.getData() !=null){
                        Uri selectedImageUri = data.getData();
                        Glide.with(this).load(selectedImageUri).into(img_care);

                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((GlideBitmapDrawable)image.getDrawable().getCurrent()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}


