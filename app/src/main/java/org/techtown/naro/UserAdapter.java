package org.techtown.naro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    //사용자 기록 time
    String time="";
    String result="";
    String type;
    //DB
    ResultDB helper;
    MemoDB helper2;
    ArrayList<Result> info = new ArrayList<Result>();
    AlertDialog.Builder builder;
    Result information;
    byte[] img;






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

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int pos = holder.getAdapterPosition();
                Context context = view.getContext();
                time = info.get(pos).getTime();
                type = info.get(pos).getCardtype();
                builder = new AlertDialog.Builder(context);
                builder.setTitle("Health Care 삭제");
                builder.setMessage("["+time.split("\\s+")[0]+"]"+" "+type+" 삭제하시겠습니까?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        helper = new ResultDB(context.getApplicationContext(),"Result.db",null,2);
                        deleteItem(pos);
                        notifyDataSetChanged();
                        helper.deletetime(time);
                        helper2 = new MemoDB(context.getApplicationContext(),"Memo.db",null,2);
                        helper2.deleteall(time);
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();


            }
        });



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder.getAdapterPosition();
                Context context = view.getContext();
                helper = new ResultDB(context.getApplicationContext(),"Result.db",null,2);
                //클릭한 카드뷰의 시간 정보 담기
                time = info.get(pos).getTime().toString();
                result = info.get(pos).getSkinresult_more();
                img = info.get(pos).getPet_image();
                type = info.get(pos).getCardtype();

                if(type == "check") {
                    Intent intent = new Intent(context.getApplicationContext(), MypageCheck.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("mypage_result", result);
                    intent.putExtra("mypage_img", img);
                    intent.putExtra("timeset", time);
                    context.getApplicationContext().startActivity(intent);

                }else{
                    Intent i = new Intent(context.getApplicationContext(), MypageResult.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("mypage_result", result);
                    i.putExtra("mypage_img", img);
                    i.putExtra("timeset", time);
                    context.getApplicationContext().startActivity(i);

                }



//                //다이얼로그박스 호출
//                builder = new AlertDialog.Builder(context);
//                builder.setTitle("진단결과 삭제");
//                //이런식으로 나중에 클릭하면 크게 보기 하자.
//                builder.setMessage(info.get(pos).getSkinresult_more());
//                builder.setPositiveButton("예",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                int post = holder.getAdapterPosition();
//                                deleteItem(post);
//                                notifyDataSetChanged();
//                                helper.deletetime(time);
//
//                            }
//                        });
//                builder.setNegativeButton("아니요",
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.cancel();
//                            }
//                        });
//                builder.show();
//
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
        private TextView ttile;
        private TextView more;
        private ImageView deletebtn;

        public ViewHolder(View infoView){
            super(infoView);
            ttype = infoView.findViewById(R.id.type);
            ttime = infoView.findViewById(R.id.today_date);
            imgskin = infoView.findViewById(R.id.skin_user);
            cardView = infoView.findViewById(R.id.card);
            deletebtn = infoView.findViewById(R.id.btn_delete);
            ttile = infoView.findViewById(R.id.pet_result);
        }
        public void setInfo(Result pr){
            ttype.setText(pr.getCardtype());
            ttime.setText(pr.getTime().split("\\s+")[0]);
            Bitmap bt = BitmapFactory.decodeByteArray(pr.getPet_image(),0,pr.getPet_image().length);
            imgskin.setImageBitmap(bt);
            ttile.setText(pr.getSkinresult());
        }

    }
}

