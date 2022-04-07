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

public class DogInfo extends AppCompatActivity {

    private final int GET_GALLERY_IMAGE =200;
    EditText dog_name;
    EditText dog_sex;
    EditText dog_age;
    EditText dog_birth;
    ImageView pet_face;
    Button btn_savedoginfo;
    FirebaseAuth firebaseAuth;
    String dname;
    String dage;
    String dsex;
    byte[] data;
    String dbirth;
    ImageView dface;

    String email;
    DogDB dogDB;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dog_info);

        dog_name = (EditText) findViewById(R.id.dog_name);
        dog_sex = (EditText) findViewById(R.id.dog_sex);
        dog_age = (EditText) findViewById(R.id.dog_age);
        dog_birth = (EditText) findViewById(R.id.dog_birth);
        btn_savedoginfo = (Button) findViewById(R.id.btn_savedoginfo);
        pet_face = findViewById(R.id.pet_face);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        email = googleuser.getEmail();

        pet_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*"); //c
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });




        btn_savedoginfo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                data = imageViewToByte(pet_face);
                dname = dog_name.getText().toString();
                dsex = dog_sex.getText().toString();
                dage = dog_age.getText().toString();
                dbirth = dog_birth.getText().toString();
                //여기에 이미지 안들어갔을때오류 발생하는거 고쳐야 됨!!!!
                if(dname.length() == 0 || dsex.length() == 0 || dage.length() == 0 || dbirth.length() == 0){
                    Toast.makeText(DogInfo.this, "작성을 완성해주세요",Toast.LENGTH_SHORT).show();
                }
                else{
                    dogDB = new DogDB(getApplicationContext(), "Dog.db", null, 2);
                    dogDB.insertdogdata(email, dname, dage, dsex, dbirth,data);
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                }


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
                        Glide.with(this).load(selectedImageUri).into(pet_face);

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
