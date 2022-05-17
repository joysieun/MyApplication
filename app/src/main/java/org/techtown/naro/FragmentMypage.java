package org.techtown.naro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class FragmentMypage extends Fragment {

    RecyclerView recyclerView;
    private UserAdapter adapter;
    ArrayList<Result> resultList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;
    TextView tv;
    String[] realuserid;
    String email;
    Button plusmemo;
    Button pluscare;

    public FragmentMypage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView = rootView.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);
        getUserList();
        pluscare = rootView.findViewById(R.id.pluscare);
        pluscare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.choose, null,false);
                builder.setView(view1);

                final Button vacci = (Button) view1.findViewById(R.id.btn_vacci);

                final Button care = (Button) view1.findViewById(R.id.btn_care);
                final ImageView close = (ImageView)view1.findViewById(R.id.btn_closechoose);

                vacci.setText("예방접종");
                care.setText("헬스케어");

                final AlertDialog dialog = builder.create();

                vacci.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent i = new Intent(getActivity().getApplicationContext(),Vacci.class);
                        startActivity(i);

                    }
                });
                care.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent i = new Intent(getActivity().getApplicationContext(),Care.class);
                        startActivity(i);


                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }


        });
        


        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        email = googleuser.getDisplayName();
        realuserid = email.split("@");



        return rootView;

    }

    private void getUserList(){
        println("<<<getUserList()>>>");
        adapter.removeAllItem();
        final ResultDB resultDB = new ResultDB(getActivity().getApplicationContext(), "Result.db",null,2);
        Cursor cursor = resultDB.getUserList();
        int count=0;
        while(cursor.moveToNext()){
            Result result = new Result();
            result.setUserid(cursor.getString(0));
            result.setCardtype(cursor.getString(1));
            result.setSkinresult(cursor.getString(2));
            result.setTime(cursor.getString(4));
            result.setSkinresult_more(cursor.getString(3));
            result.setPet_image(cursor.getBlob(5));
            adapter.addItem(result);
            count++;
        }

        adapter.notifyDataSetChanged();
        println("회원수:: "+ adapter.getItemCount());
    }
    public void println(String msg){
        Log.d("mypage",msg);
    }
    }


