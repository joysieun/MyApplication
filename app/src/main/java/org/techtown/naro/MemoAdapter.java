package org.techtown.naro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class MemoAdapter extends RecyclerView.Adapter<MemoAdapter.ViewHolder> {


    Memo memoclass;
    MemoDB helper;

    String userid;
    String memo;
    String time;
    AlertDialog.Builder builder;


    ArrayList<Memo> memoinfo = new ArrayList<>();

    @NonNull
    @Override
    public MemoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View memoView = inflater.inflate(R.layout.memo_items, parent, false);
        return new ViewHolder(memoView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoAdapter.ViewHolder holder, int position) {
        memoclass = memoinfo.get(holder.getAdapterPosition());
        holder.setInfo(memoclass);

        holder.btn_closememo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Context context = view.getContext();
                helper = new MemoDB(context.getApplicationContext(),"Memo.db",null,2);
                //클릭한 카드뷰의 시간 정보 담기
                memo = memoinfo.get(pos).getMemo();

                //다이얼로그박스 호출
                builder = new AlertDialog.Builder(context);
                builder.setTitle("메모 삭제");
                //이런식으로 나중에 클릭하면 크게 보기 하자.
                builder.setMessage("선택한 메모를 삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int post = holder.getAdapterPosition();
                                deleteItem(post);
                                notifyDataSetChanged();
                                helper.deletedata(memo);

                            }
                        });
                builder.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.show();

            }
        });



    }

    @Override
    public int getItemCount() {
        return memoinfo.size();
    }

    public void deleteItem(int position){
        memoinfo.remove(position);
    }
    public void addItem(Memo m){
        memoinfo.add(m);
    }
    public void removeAllItem(){
        memoinfo.clear();
    }

 //여기서 부터 고치기

    class ViewHolder extends RecyclerView.ViewHolder{
        public View cardView2;
        private ImageView btn_closememo;
        private TextView memocontext;
        private TextView id;
        private TextView time;
        public ViewHolder(View memoView){
            super(memoView);
            btn_closememo = memoView.findViewById(R.id.btn_closememo);
            cardView2 = memoView.findViewById(R.id.cardView2);
            memocontext = memoView.findViewById(R.id.tv_memo);

        }
        public void setInfo(Memo m){
            memocontext.setText(m.getMemo());

        }
    }
}
