package org.techtown.naro;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.MyViewHolder> {
    private List<Chat> mDataset;
    private String name;

    FirebaseAuth firebaseAuth;


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_name;
        public TextView tv_msg;
        public TextView tv_time;
        public View rootView;

        public LinearLayout linear;



        public MyViewHolder(View v){
            super(v);
            tv_time = v.findViewById(R.id.row_time);//바꾸기
            tv_msg = v.findViewById(R.id.row_msg);
            tv_name = v.findViewById(R.id.row_id);
            rootView =v;

            linear = rootView.findViewById(R.id.linear2);

            v.setClickable(true);
            v.setEnabled(true);
        }
    }

    public ChattingAdapter(List<Chat> myDataset, Chatting context, String name){
        mDataset = myDataset;
        this.name = name ;

    }

    @NonNull
    @Override
    public ChattingAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //firebaseAuth = FirebaseAuth.getInstance();
        //final FirebaseUser googleuser = firebaseAuth.getCurrentUser();
        //email = googleuser.getEmail();

        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat,parent,false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Chat chat = mDataset.get(position);
        holder.tv_name.setText(chat.getId());
        holder.tv_msg.setText(chat.getContents());

        if(getItemCount()%2==0){
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
            holder.tv_name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.linear.setGravity(Gravity.RIGHT);



        }else{
            holder.tv_name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.tv_msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

        }






    }

    @Override
    public int getItemCount() {
        return mDataset==null?0:mDataset.size();
    }
    public Chat getChat(int position){
        return mDataset !=null?mDataset.get(position) :null;
    }

    public void addChat(Chat chat){
        mDataset.add(chat);
        notifyItemInserted(mDataset.size()-1);
    }
}
