package org.techtown.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    //사용자 기록 time
    String time="";
    //DB
    ResultDB helper;
    ArrayList<Result> info = new ArrayList<Result>();
    AlertDialog.Builder builder;
    Result information;


    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View infoView = inflater.inflate(R.layout.user_item, parent,false);

        return new ViewHolder(infoView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, final int position) {

        information = info.get(holder.getAdapterPosition());
        holder.setInfo(information);



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Context context = view.getContext();
                helper = new ResultDB(context.getApplicationContext(),"Result.db",null,2);
                //클릭한 카드뷰의 시간 정보 담기
                time = info.get(pos).getTime().toString();
                //다이얼로그박스 호출
                builder = new AlertDialog.Builder(context);
                builder.setTitle("진단결과 삭제");
                builder.setMessage("["+time+"]해당 날짜 진단결과를 삭제하시겠습니까?");
                builder.setPositiveButton("예",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int post = holder.getAdapterPosition();
                                deleteItem(post);
                                notifyDataSetChanged();
                                helper.deletetime(time);

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
        return info.size();
    }

    public void addItem(Result pr){
        info.add(pr);
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        info.remove(position);
    }

    public void removeAllItem(){
        info.clear();

    }

    class ViewHolder extends RecyclerView.ViewHolder{


        private TextView ttype;
        private TextView ttime;
        private ImageView imgskin;
        private CardView cardView;

        public ViewHolder(View infoView){
            super(infoView);
            ttype = infoView.findViewById(R.id.type);
            ttime = infoView.findViewById(R.id.today_date);
            imgskin = infoView.findViewById(R.id.skin_user);
            cardView = infoView.findViewById(R.id.card);
        }
        public void setInfo(Result pr){
            ttype.setText(pr.getCardtype());
            ttime.setText(pr.getTime());
            Bitmap bt = BitmapFactory.decodeByteArray(pr.getPet_image(),0,pr.getPet_image().length);
            imgskin.setImageBitmap(bt);
        }

    }
}

