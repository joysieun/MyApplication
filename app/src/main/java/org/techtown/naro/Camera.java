package org.techtown.naro;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Camera extends AppCompatActivity {

    final private static String TAG = "see"; //c
    private static final int PICK_FROM_ALBUM = 2; //앨범에서 사진 가져오기
    private static final int PICK_FROM_CAMERA = 1; //카메라에서 사진 가져오기
    private static final int CROP_PICTURE = 3; //가져온 사진 자르기

    public static final int imageSize = 299;
    Uri photoURI;
    Matrix matrix;
    Bitmap getbitmap;
    Uri cropURI;
    File croppedFileName;
    private String camera_result_class = "result";

    private ClassifyImage cls;

    //사용자에게 권한 받기 위한 변수들
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    //권한 동의 여부 질문 후 콜백함수에 쓰일 함수
    private static final int MULTIPLE_PERMISSIONS = 101;


    Button btncamera;
    Button btnresult;
    ImageView imageView;
    Button btnreset;
    Bitmap bitmap;
    Bitmap bitmap2;
    Button btnselect;

    EditText editpetname;
    EditText edituname;
    String pet;
    String user;
    Toolbar toolbar;
    String type;
    EditText edittext_result_class;

    private FirebaseAuth firebaseAuth;
    String mCurrentPhotoPath;

    // select from camera
    Uri selectedImageUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);

        imageView = findViewById(R.id.skin_image);
        btncamera = findViewById(R.id.camera);
        btnreset = findViewById(R.id.reset);
        btnselect = findViewById(R.id.select);
        btnresult = findViewById(R.id.btn_result);
        edittext_result_class = findViewById(R.id.edittext_result_class);

        // create ClassifyImage class
        cls = new ClassifyImage(this);
        try {
            cls.init();
        } catch (IOException e) {
            e.printStackTrace();
        }


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        String email = googleuser.getEmail();
        //툴바 커스텀 한거 넣기
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_home_24);

        //권한 요청, 안드로이드 버전 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "권한설정완료");

            } else {
                Log.d(TAG, "권한설정 요청");
                ActivityCompat.requestPermissions(Camera.this, new String[]
                        {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            }
        }
        //카메라 버튼
//        btncamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                switch (view.getId()) { //c
//                    case R.id.camera:
//                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(i, PICK_FROM_CAMERA);
//                        break;
//                }
//            }
//        });
        btncamera.setOnClickListener(v -> getImageFromCamera());

        //이미지 초기화
        btnreset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(null);

            }
        });
        //이미지 고르는 버튼 이벤트
//        btnselect.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
//                startActivityForResult(intent, PICK_FROM_ALBUM);
//            }
//        });
        btnselect.setOnClickListener(v -> getImageFromGallery());

        //다음페이지로 넘어가는 이벤트
        btnresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //다음 액티비터로 데이터 전달
                byte[] data = imageViewToByte(imageView);
                user = email;
                type = "check";

                if (camera_result_class == "normal") {
                    Intent intent = new Intent(getApplicationContext(), NormalPage.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("skinimagenormal", data);
                    intent.putExtra("normal_class", camera_result_class);
                    getApplicationContext().startActivity(intent);

                } else {
                    Intent intent = new Intent(getApplicationContext(), ResultPage.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("skinimage", data);
                    intent.putExtra("result_class", camera_result_class);
                    getApplicationContext().startActivity(intent);
                }


            }
        });

    }

    //권한요청 콜백함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "ONREQUESTPERMISSIONRESULT");
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "permission");
        } else {
            //권한요청 동의하지 않을 경우
            showNoPermission();
        }
    }

    //동의하지 않을때 함수
    private void showNoPermission() {
        Toast.makeText(this, "권한 요청에 동의 후 사용가능 합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();

    }

    private void getImageFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    private void getImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Toast.makeText(getApplication(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == PICK_FROM_ALBUM) {
            if (data == null) {
                return;
            }
            Uri selectedImage = data.getData();
//            Bitmap bitmap = null;

            try {
                if (Build.VERSION.SDK_INT >= 29) {
                    photoURI = data.getData();
                    cropImage();

                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                            selectedImage);
                }
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to read Image", ioe);
            }
        } else if (requestCode == PICK_FROM_CAMERA) {
            try {
                if (Build.VERSION.SDK_INT >= 29) {

                    bitmap2 = (Bitmap) data.getExtras().get("data");
//
//                    matrix = new Matrix();
//                    matrix.setScale(1.5f,3f); //넓이를 1.5배,높이를 1.5배 확대
//                    getbitmap = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), matrix, true);
//                    bitmap2.recycle();
                    photoURI = getImageUri(this, bitmap2);
                    cropImage();
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(),
                            selectedImageUri);
                }
            } catch (IOException ioe) {
                Log.e(TAG, "Failed to read Image", ioe);
            }
        } else if (requestCode == CROP_PICTURE) {

            photoURI = data.getData();
            ContentResolver resolver = getContentResolver();

            InputStream inputStream = null;
            try {
                inputStream = resolver.openInputStream(photoURI);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeStream(inputStream);

            if (bitmap != null) {

                Map<String, Float> output = cls.classify(bitmap);
                Pair<String, Float> max_class = cls.argmax(output);

                output.remove(max_class.first, max_class.second);
                Pair<String, Float> second_class = cls.argmax(output);

                output.remove(second_class.first, second_class.second);
                Pair<String, Float> third_class = cls.argmax(output);

                output.remove(third_class.first, third_class.second);
                Pair<String, Float> last_class = cls.argmax(output);

                String resultStr = String.format(Locale.ENGLISH,
                        "%s이 %.2f%%,\n%s이 %.2f%%로 의심됩니다.",
                        max_class.first, max_class.second * 100,
                        second_class.first, second_class.second * 100);
                camera_result_class = resultStr;

                imageView.setImageBitmap(bitmap);

//                Pair<String, Float> output = cimg.classify(bitmap);
//                String resultStr = String.format(Locale.ENGLISH,
//                        "class : %s, prob : %.2f%%",
//                        output.first, output.second * 100);
//
//                imageView.setImageBitmap(bitmap);
//                camera_result_class = output.first;
                // 결과 class: output.first;
            }
        }


    }



//    private void beginCrop(Uri source){
//        Uri destination = Uri.fromFile(new File(getCacheDir(),"cropped"));
//        Crop.of(source,destination).asSquare().start(this);
//    }




    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath(); //언제 사용하는지 보기
        return image;
    }

    //카메라촬영 호출 메소드
    private void dispatchTackPictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//사진찍기 위한 설정
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Toast.makeText(Camera.this, "이미지 처리 오류입니다! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this, "org.techtown.naro.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PICK_FROM_CAMERA);
            }
        }


    }


    public void cropImage() {


        //원본
        Intent i = new Intent("com.android.camera.action.CROP");
        i.setDataAndType(photoURI, "image/*");  //파일 연결
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(i, 0);
        grantUriPermission(list.get(0).activityInfo.packageName, photoURI,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        int size = list.size();
        if (size == 0) {
            Toast.makeText(this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, "용량이 큰 사진의 경우 시간이 오래 걸릴 수 있습니다.", Toast.LENGTH_SHORT).show();
            i.putExtra("outputX", 200);
            i.putExtra("outputY", 200);
            i.putExtra("aspectX", 1);
            i.putExtra("aspectY", 1);
            i.putExtra("scale", true);
            i.putExtra("crop", true);
            startActivityForResult(i, CROP_PICTURE);
        }
    }
//    public void onSelectImageClick(View view){
//        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
//    }





    //toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch(menuItem.getItemId()){
            case android.R.id.home:
                Intent back = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(back);
                return true;

            default:
                ((GoogleLogin)GoogleLogin.mContext).signOut();
                Intent in = new Intent(getApplicationContext(),GoogleLogin.class);
                startActivity(in);
                return true;
        }

    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, imageFileName, null);
        return Uri.parse(path);
    }

    //진단 버튼 누르면 나오는 dialog창 // 옮겨야 하느니라
    void showDialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(Camera.this)
                .setTitle("진단이 완료되었습니다.")
                .setMessage("내 정보> 마이페이지 : 결과를 확인하실 수 있습니다.")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();


                        Intent intent = new Intent(getApplicationContext(), ResultPage.class);
                        startActivity(intent);


                    }
                });
        AlertDialog alertDialog = msgBuilder.create();
        alertDialog.show();

    }
    //데베에 저장할 때 byte로 저장하기 위한 메소드
    public static byte[] imageViewToByte(ImageView image){

        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }




}