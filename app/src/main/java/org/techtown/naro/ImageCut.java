package org.techtown.naro;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ImageCut extends AppCompatActivity {

    Button btnresult;
    ImageView imageView;
    private FirebaseAuth firebaseAuth;
    String user;
    String type;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_cut);

        btnresult = findViewById(R.id.btn_result);
        imageView = findViewById(R.id.cut_image);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        String email = googleuser.getEmail();


        btnresult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] data = imageViewToByte(imageView);
                user = email;
                type = "check";
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String getTime = dateFormat.format(currentTime);
                ResultDB resultdb = new ResultDB(getApplicationContext(), "Result.db", null, 2);
                resultdb.insertdata(user, type,  null, getTime, data);
                showDialog();

            }
        });

    }

    public static byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((GlideBitmapDrawable)image.getDrawable().getCurrent()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    void showDialog(){
        AlertDialog.Builder msgBuilder = new AlertDialog.Builder(ImageCut.this)
                .setTitle("진단이 완료되었습니다.")
                .setMessage("내 정보> 마이페이지 : 결과를 확인하실 수 있습니다.")
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
