package org.techtown.myapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class FragmentMypage extends Fragment {

    RecyclerView recyclerView;
    private UserAdapter adapter;
    ArrayList<Result> resultList = new ArrayList<>();

    TextView tv;

    public FragmentMypage() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_mypage, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView = rootView.findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);
        getUserList();
        //tv =rootView.findViewById(R.id.onusername);
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
            result.setPetname(cursor.getString(1));
            result.setSkinresult(cursor.getString(2));
            result.setTime(cursor.getString(3));
            result.setPet_image(cursor.getBlob(4));
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

