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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class QuestionResult extends AppCompatActivity {

    Integer getscore;
    TextView allcount;
    ImageView imageView;
    Button button;

    private FirebaseAuth firebaseAuth;

    String user;
    String type;
    String result;

    byte[] img;

    String level1 ="\n집에서 충분히 케어 가능합니다.\n";
    String level2 = "\n초기단계입니다.\n 집에서 케어 가능하고 증상이 지속되면 동물병원을 방문하세요.\n";
    String level3 = "\n증상이 약간 진행된 상태입니다.\n 동물병원 방문을 권유합니다.\n";
    String level4 = "\n심각한 단계로 보입니다.\n 즉시 동물병원을 방문하세요.\n";
    String allergy = "\n알러지성 피부병일 수 있으므로 관련 치료를 받았던 동물병원을 방문하세요.\n";
    String mold = "\n곰팡이성 피부병은 사람에게 옮길수 있으므로 즉시 동물병원을 방문하세요.\n";
    String sb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_result);


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        String email = googleuser.getEmail();
        user = email;
        type = "check";

        getscore = getIntent().getIntExtra("score5",0);
        allcount = findViewById(R.id.tv_Qresult);
        imageView = findViewById(R.id.img_Qresult);
        button = findViewById(R.id.btn_Qhospital);
        img = getIntent().getByteArrayExtra("img5");
        result= getIntent().getStringExtra("result5");

        Bitmap bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
        imageView.setImageBitmap(bitmap);



        if(0<=getscore && getscore<10){
            int number;
            number = getscore % 10;

            switch(number){
                case 0:
                    sb = level1;
                    allcount.setText(sb);
                    break;
                case 1:
                    sb = level1+allergy;
                    allcount.setText(sb);
                    break;
                case 2:
                    sb = level1 +mold;
                    allcount.setText(sb);
                    break;
                case 3:
                    sb = level1 + allergy + mold;
                    allcount.setText(sb);
                    break;

            }
        }
        else if(10<=getscore && getscore<20){
            int number;
            number = getscore % 10;
            switch(number){
                case 0:
                    sb = level2;
                    allcount.setText(sb);
                    break;
                case 1:
                    sb = level2 + allergy;
                    allcount.setText(sb);
                    break;
                case 2:
                    sb = level2 + mold;
                    allcount.setText(sb);
                    break;
                case 3:
                    sb = level2 + allergy +mold;
                    allcount.setText(sb);
                    break;

            }
        }

        else if(20<=getscore && getscore<30){
            int number;
            number = getscore % 10;

            switch(number){
                case 0:
                    sb = level3;
                    allcount.setText(sb);
                    break;
                case 1:
                    sb =level3+"\n"+ allergy;
                    allcount.setText(sb);
                    break;
                case 2:
                    sb = level3+"\n"+ mold;
                    allcount.setText(sb);
                    break;
                case 3:
                    sb = level3 + "\n"+mold+ "\n"+allergy;
                    allcount.setText(sb);
                    break;


            }
        }

        else if(30<=getscore && getscore<40){
            int number;
            number = getscore % 10;

            switch(number){
                case 0:
                    sb = level4;
                    allcount.setText(sb);
                    break;
                case 1:
                    sb = level4 +"\n"+ allergy;
                    allcount.setText(sb);
                    break;
                case 2:
                    sb = level4 +"\n"+mold;
                    allcount.setText(sb);
                    break;
                case 3:
                    sb = level4 + "\n"+allergy +"\n"+mold;
                    allcount.setText(sb);
                    break;

            }
        }
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String getTime = dateFormat.format(currentTime);
        ResultDB resultdb = new ResultDB(getApplicationContext(), "Result.db", null, 2);
        resultdb.insertdata(user, type, result, sb ,getTime, img);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });

    }
}
