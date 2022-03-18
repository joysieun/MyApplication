package org.techtown.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Camera extends AppCompatActivity {
    Button button;
    Uri uri;
    ImageView imageView; //db에 저장할 이미지
    Bitmap bitmap;
    Button btncamera;
    Button btnselect;
    Button btnresult;
    Button btnreset;
    EditText editpetname;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        imageView = findViewById(R.id.skin_image);
        btncamera = findViewById(R.id.camera);
        btnselect = findViewById(R.id.select);
        btnresult = findViewById(R.id.btn_result);
        btnreset = findViewById(R.id.reset);
        editpetname = findViewById(R.id.petname);


        //사진카메라로 찍기
        btncamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultPicture.launch(intent);
            }
        });

        //사진이미지에서 가져오기
        btnselect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultSelect.launch(intent);
            }
        });

        //저장버튼
        btnresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] data = imageViewToByte(imageView);
                String registertext = editpetname.getText().toString();

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String getTime = dateFormat.format(currentTime);
                PetResultDB petdb = new PetResultDB(Camera.this);
                petdb.addResult(registertext,data,getTime);
            }
        });

        //사진영역초기화
        btnreset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(null);

            }
        });
    }
        //사진찍기

        ActivityResultLauncher<Intent> activityResultPicture = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            Bundle extras = result.getData().getExtras();
                            bitmap = (Bitmap) extras.get("data");
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                }

        );

        //사진가져오기
        ActivityResultLauncher<Intent> activityResultSelect = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                            try {
                                uri = result.getData().getData();
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                                imageView.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    public static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }




}
