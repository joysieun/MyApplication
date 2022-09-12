package org.techtown.naro;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;

public class DogInfodetail extends AppCompatActivity {
    EditText updatename, updateowner, updateage,updatesex, updatebirth;
    ImageView updateImageView;
    String name, owner,age,sex,birth;
    byte[] image;
    String email;
    FirebaseAuth firebaseAuth;
    DogDB dogDB;
    SQLiteDatabase DB;
    Bitmap bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        updatename = findViewById(R.id.up_name);
        updateage = findViewById(R.id.up_age);
        updatesex = findViewById(R.id.up_sex);
        updatebirth = findViewById(R.id.up_birth);
        updateImageView = findViewById(R.id.up_face);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        email = googleuser.getEmail();
        dogDB = new DogDB(DogInfodetail.this,"Dog.db",null,2);

        getAndSetIntentData();

        Button update = findViewById(R.id.btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updatename.getText().toString();
                String age = updateage.getText().toString();
                String sex = updatesex.getText().toString();
                String birth =updatebirth.getText().toString();
                byte[] data = imageViewToByte(updateImageView);

                DogDB dog = new DogDB(DogInfodetail.this,"Dog.db",null,2);
                dog.updateData(email,name,age,sex,birth,data);
                Intent i = new Intent(DogInfodetail.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
    public void getAndSetIntentData(){
        DB = dogDB.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT petname, petage, petsex, petbirth, petface FROM User_dog WHERE  userid = '" +email+ "'ORDER BY userid",null);
        Dog dog = new Dog();
        cursor.moveToFirst();
        dog.setPetname(cursor.getString(0));
        dog.setPetage(cursor.getString(1));
        dog.setPetsex(cursor.getString(2));
        dog.setPetbirth(cursor.getString(3));
        dog.setPetface(cursor.getBlob(4));

        updatename.setText(dog.getPetname());
        updateage.setText(dog.getPetage());
        updatebirth.setText(dog.getPetbirth());
        updatesex.setText(dog.getPetsex());
        bt = BitmapFactory.decodeByteArray(dog.getPetface(), 0, dog.getPetface().length);
        updateImageView.setImageBitmap(bt);

    }

    public static byte[] imageViewToByte(ImageView image){

        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
