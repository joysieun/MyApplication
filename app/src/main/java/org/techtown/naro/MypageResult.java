package org.techtown.naro;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MypageResult extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    Button button;
    EditText editText;
    byte[] img;
    String result;
    String result_time;
    String memo;
    String email;
    Bitmap bitmap;

    Toolbar toolbar;


    RecyclerView recyclerView;
    MemoAdapter adapter;

    MemoFragment memoFragment;
    FirebaseAuth firebaseAuth;

    ResultDB DB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_result);


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        email = googleuser.getEmail();


        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        imageView = findViewById(R.id.img_mypage);
        textView = findViewById(R.id.tv_mypage);
        button = findViewById(R.id.btn_memo);
        editText = findViewById(R.id.et_memo);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView = findViewById(R.id.memo);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),1));



        img = getIntent().getByteArrayExtra("mypage_img");
        result= getIntent().getStringExtra("mypage_result");
        result_time = getIntent().getStringExtra("timeset");

        bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
        imageView.setImageBitmap(bitmap);
        textView.setText(result);

        adapter = new MemoAdapter();
        recyclerView.setAdapter(adapter);
        getUserList(result_time);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MypageResult.this);
                View view1 = LayoutInflater.from(MypageResult.this).inflate(R.layout.memo_plus, null,false);
                builder.setView(view1);

                final Button memoinsert = (Button) view1.findViewById(R.id.btn_memoplus);
                final EditText editText_memo = (EditText) view1.findViewById(R.id.et_memo);
                final Button memoclose = (Button) view1.findViewById(R.id.btn_close);

                memoinsert.setText("추가");
                memoclose.setText("취소");

                final AlertDialog dialog = builder.create();

                memoinsert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String memo = editText_memo.getText().toString();
                        MemoDB memodb = new MemoDB(getApplicationContext(), "Memo.db", null, 2);
                        memodb.insertmemodata(email, memo, result_time);
                        dialog.dismiss();
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                memoclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                ((GoogleLogin) GoogleLogin.mContext).signOut();
                Intent in = new Intent(getApplicationContext(), GoogleLogin.class);
                startActivity(in);
                return true;
        }
    }

    private void getUserList(String time){
        adapter.removeAllItem();
        final MemoDB dBhelper = new MemoDB(getApplicationContext(),"Memo.db",null,2);
        Cursor cursor = dBhelper.getUserList(time);
        int count=0;
        while(cursor.moveToNext()) {
            Memo mm = new Memo();
            mm.setMemo(cursor.getString(1));
            adapter.addItem(mm);
            count++;
        }
        adapter.notifyDataSetChanged();
    }
}
