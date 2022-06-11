package org.techtown.naro;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Vacci extends AppCompatActivity {

    String[] items = {"1차","2차","3차","4차","5차","6차","7차"};
    String vacci;
    EditText hospital;
    ImageView imageView;
    Button plus;
    Button close;
    byte[] img;
    Uri photoURI;
    String getTime;

    private final int GET_GALLERY_IMAGE =200;
    private final int CROP_PICTURE =100;


    FirebaseAuth firebaseAuth;
    String type;
    String user;

    String hos;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plus_vaccination);

        hospital = findViewById(R.id.vaccihospital);
        imageView = findViewById(R.id.imgvacci);
        plus = findViewById(R.id.btn_plusvacci);
        close = findViewById(R.id.btn_closevacci);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        String email = googleuser.getEmail();
        user = email;
        type = "vacci";

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vacci = items[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });



        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img = imageViewToByte(imageView);
                hos = hospital.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                getTime = dateFormat.format(currentTime);
                ResultDB resultdb = new ResultDB(getApplicationContext(), "Result.db", null, 2);
                resultdb.insertdata(user, type, vacci+" 예방접종 완료", hos ,getTime,img);

                finish();


            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            switch (requestCode){
                case GET_GALLERY_IMAGE: {
                    if (requestCode == GET_GALLERY_IMAGE && data != null && data.getData() != null) {
                        photoURI = data.getData();
                        cropImage();

                    }
                }
                case CROP_PICTURE:{
                    Bitmap bitmap;
                    try{
                        photoURI = data.getData();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                            ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),photoURI);
                            bitmap = ImageDecoder.decodeBitmap(source);
                            imageView.setImageBitmap(bitmap);
                            Toast.makeText(this, "이미지추가 성공", Toast.LENGTH_SHORT).show();

                        }
                        else {

                            bitmap = MediaStore.Images.Media.getBitmap(Vacci.this.getContentResolver(), photoURI);
                            Bitmap thumbImage = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);
                            ByteArrayOutputStream bs = new ByteArrayOutputStream();
                            thumbImage.compress(Bitmap.CompressFormat.JPEG, 100, bs);
                            imageView.setImageBitmap(thumbImage);
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] imageViewToByte(ImageView image){

        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void cropImage(){

        this.grantUriPermission("com.android.camera",photoURI,Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //원본
        Intent i = new Intent("com.android.camera.action.CROP");
        i.setDataAndType(photoURI,"image/*");  //파일 연결
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(i,0);
        grantUriPermission(list.get(0).activityInfo.packageName,photoURI,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if(size == 0){
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        }else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            i.putExtra("outputX", 200);
            i.putExtra("outputY", 200);
            i.putExtra("aspectX", 1);
            i.putExtra("aspectY", 1);
            i.putExtra("crop", true);
            i.putExtra("scale",true);
            startActivityForResult(i, CROP_PICTURE);
        }
    }
}
