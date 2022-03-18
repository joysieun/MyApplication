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

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    //사용자 기록 time
    String time="";
    //DB
    PetResultDB helper;
    ArrayList<PetResult> info = new ArrayList<PetResult>();
    AlertDialog.Builder builder;



    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View infoView = inflater.inflate(R.layout.user_item, parent,false);


        return new ViewHolder(infoView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        PetResult inforamtion = info.get(position);
        holder.setInfo(inforamtion);

        holder.cardView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Context context = view.getContext();

                helper = new PetResultDB(context.getApplicationContext());

                //선택한 기록 날짜 담기
                time = info.get(holder.getAdapterPosition()).getTime();
                builder = new AlertDialog.Builder(context);
                builder.setTitle("삭제");
                builder.setMessage("["+time+"]"+"해당 날짜의 결과를 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener(){


                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteItem(holder.getAdapterPosition());
                        notifyDataSetChanged();
                        helper.delete(time);
                    }

                });
                builder.setNegativeButton("아니오",
                        new DialogInterface.OnClickListener(){

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
        return 0;
    }
    public void addItem(PetResult pr){
        info.add(pr);
    }

    public void deleteItem(int position){
        info.remove(position);
    }

    public void removeAllItem(){
        info.clear();
    }



    class ViewHolder extends RecyclerView.ViewHolder{


        TextView tpetname;
        TextView ttime;
        ImageView imgskin;
        CardView cardView;

        public ViewHolder(View infoView){
            super(infoView);

            tpetname = infoView.findViewById(R.id.pet_name);
            ttime = infoView.findViewById(R.id.today_date);
            imgskin = infoView.findViewById(R.id.skin_user);
            cardView = infoView.findViewById(R.id.card);
        }
        public void setInfo(PetResult pr){
            tpetname.setText(pr.getPetname());
            ttime.setText(pr.getTime());
            Bitmap bt = BitmapFactory.decodeByteArray(pr.getPet_image(),0,pr.getPet_image().length);
            imgskin.setImageBitmap(bt);
        }

    }
}
