package org.techtown.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Camera extends AppCompatActivity {
    final private static String TAG ="GILBOMI";
    private final int GET_GALLERY_IMAGE =200;
    Button btn;
    Button resultbtn;
    Uri uri;
    ImageView imageView;
    Button btnreset;
    Bitmap bitmap;
    Button btnselect;
    EditText editpetname;
    EditText edituname;
    String pet;
    String user;


    final static int TAKE_PICTURE =1;
    String mCurrentPhotoPath;
    final static int REQUEST_TAKE_PHOTO =1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        imageView = findViewById(R.id.skin_image);
        btn = findViewById(R.id.camera);
        resultbtn = findViewById(R.id.btn_result);
        btnreset = findViewById(R.id.reset);
        btnselect = findViewById(R.id.select);
        editpetname = findViewById(R.id.petname);
        edituname = findViewById(R.id.uname);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한설정완료");

            } else {
                Log.d(TAG, "권한설정 요청");
                ActivityCompat.requestPermissions(Camera.this, new String[]
                        {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.camera:
                        dispatchTackPictureIntent();
                        break;
                }
            }
        });

        resultbtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                byte[] data = imageViewToByte(imageView);
                pet = editpetname.getText().toString();
                user = edituname.getText().toString();

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String getTime = dateFormat.format(currentTime);
                ResultDB petdb = new ResultDB(getApplicationContext(),"Result.db",null,2);
                petdb.insertdata(user,pet,null,getTime, data);
                showDialog();
            }
        });

        btnreset.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(null);

            }
        });

        btnselect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG,"ONREQUESTPERMISSIONRESULT");
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "permission");}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_TAKE_PHOTO: {
                    if (resultCode == RESULT_OK) {
                        File file = new File(mCurrentPhotoPath);
                        Bitmap bitmap;
                        if (Build.VERSION.SDK_INT >= 29) {
                            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), Uri.fromFile(file));
                            try {
                                bitmap = ImageDecoder.decodeBitmap(source);
                                if (bitmap != null) {
                                    Uri gi = getImageUri(this,bitmap);
                                    Glide.with(this).load(gi).into(imageView);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(file));
                                if (bitmap != null) {

                                    Uri gi = getImageUri(this,bitmap);
                                    Glide.with(this).load(gi).into(imageView);

                                }

                            } catch (IOException E) {
                                E.printStackTrace();
                            }

                        }

                    }
                    break;
                }
                case GET_GALLERY_IMAGE:
                    if(requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data!=null && data.getData() !=null){
                        Uri selectedImageUri = data.getData();
                        Glide.with(this).load(selectedImageUri).into(imageView);

                    }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    //카메라 인텐트
    private void dispatchTackPictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;

            try {photoFile = createImageFile();}
            catch(IOException e){}
            if(photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(this,"org.techtown.myapplication.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }


    }
    public static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((GlideBitmapDrawable)image.getDrawable().getCurrent()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    void showDialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(Camera.this)
                .setTitle("Diagnosis has been completed")
                .setMessage("MyInfo > Mypage : you can show your result!!")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);


                    }
                });
        AlertDialog alertDialog = msgBuilder.create();
        alertDialog.show();

    }





}
