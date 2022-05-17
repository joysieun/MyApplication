package org.techtown.naro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MemoPlus extends AppCompatActivity {
    Button button1;
    Button button2;
    EditText editText;

    String memo;
    FirebaseAuth firebaseAuth;
    String email;
    String time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_plus);

        button1 = findViewById(R.id.btn_memoplus);
        button2 = findViewById(R.id.btn_close);
        editText = findViewById(R.id.et_memo);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        email = googleuser.getEmail();

        time = getIntent().getStringExtra("gettime");

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memo = editText.getText().toString();
                MemoDB memodb = new MemoDB(getApplicationContext(), "Memo.db", null, 2);
                memodb.insertmemodata(email, memo, time);
                Intent i = new Intent(getApplicationContext(),MypageResult.class);
                startActivity(i);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),MypageResult.class);
                startActivity(i);
            }
        });





    }
}
