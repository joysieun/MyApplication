package org.techtown.naro;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Chatting extends Activity {
    private ArrayList<Chat> chatList;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    private EditText et_chat;
    private Button bt_send;
    private DatabaseReference chatref;

    Button bt_chattingout;

    String time;
    String current_time;
    String current_time2;
    String name;

    FirebaseAuth firebaseAuth;
    int count;

    ChatAdapter chatAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatting_frame);

        //현재시간
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        time = dateFormat.format(currentTime);

        //사용자 이름
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        String email = googleuser.getEmail();
        name = googleuser.getDisplayName();

        initData();

        recyclerView = findViewById(R.id.recycleview_chatting);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(chatList);
        recyclerView.setAdapter(chatAdapter);



        //버튼과 메세지 내용
        bt_send = findViewById(R.id.btn_msgsend);
        et_chat = findViewById(R.id.et_msg);

        //챗봇나가기
        bt_chattingout=findViewById(R.id.bt_chattingout);



        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = et_chat.getText().toString();

                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                current_time = dateFormat.format(currentTime);

                et_chat.setText(null);
                chatList.add(new Chat(name, msg, current_time, Chatcode.ViewType.RIGHT_CONTENT));
                chatAdapter.notifyDataSetChanged();
                onResume();
                naro_answer();
            }

        });

        bt_chattingout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    private void initData(){
        chatList = new ArrayList<>();
        chatList.add(new Chat("naro-bot","안녕하세요.                      무엇을 도와드릴까요?",time,Chatcode.ViewType.LEFT_CONTENT));
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    public void naro_answer(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        current_time2 = dateFormat.format(currentTime);
        chatList.add(new Chat("naro-bot","동물병원에 방문하세요",current_time2,Chatcode.ViewType.LEFT_CONTENT));

        chatAdapter.notifyDataSetChanged();
    }
}
