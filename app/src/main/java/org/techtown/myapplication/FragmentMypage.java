package org.techtown.myapplication;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        adapter.removeAllItem();
        final PetResultDB petResultDB = new PetResultDB(getActivity());
        Cursor cursor = petResultDB.getUserList();
        int count = 0;
        while(cursor.moveToNext()){
            PetResult pr = new PetResult();
            pr.setPet_image(cursor.getBlob(4));
            pr.setPetname(cursor.getString(3));
            pr.setTime(cursor.getString(6));
            adapter.addItem(pr);
            count++;
        }
        adapter.notifyDataSetChanged();

    }
    }

