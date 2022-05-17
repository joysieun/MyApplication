package org.techtown.naro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MemoFragment extends Fragment {

    RecyclerView recyclerView;
    private MemoAdapter adapter;
    String thattime;
    Button button;

    TextView memo;

    public MemoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_memo,container,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView = rootview.findViewById(R.id.recycleView_memo);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),1));
        button = rootview.findViewById(R.id.btn_memo);

        Bundle bundle =getArguments();
        if(bundle !=null){
            thattime =bundle.getString("key");
        }





        adapter = new MemoAdapter();
        recyclerView.setAdapter(adapter);
        memo = rootview.findViewById(R.id.tv_memo);
        getUserList(thattime);
        return rootview;
    }

    private void getUserList(String time){
        adapter.removeAllItem();
        final MemoDB dBhelper = new MemoDB(getActivity().getApplicationContext(),"Memo.db",null,2);
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