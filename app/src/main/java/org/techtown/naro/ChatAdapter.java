package org.techtown.naro;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Chat chatclass;
    ChatDB helper;

    String userid;
    String memo;
    String time;
    AlertDialog.Builder builder;


    ArrayList<Chat> chatinfo = null;

    public ChatAdapter(ArrayList<Chat> chatting) {
        chatinfo = chatting;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (viewType == Chatcode.ViewType.LEFT_CONTENT) {
            view = inflater.inflate(R.layout.chatbot_items, parent, false);
            return new LeftViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.chatuser_items, parent, false);
            return new RightViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LeftViewHolder) {
            ((LeftViewHolder) holder).chatit_name.setText(chatinfo.get(position).getId());
            ((LeftViewHolder) holder).chatit_time.setText(chatinfo.get(position).getTime());
            ((LeftViewHolder) holder).chatit_msg.setText(chatinfo.get(position).getContents());

        }
        else{
            ((RightViewHolder)holder).userit_name.setText(chatinfo.get(position).getId());
            ((RightViewHolder)holder).userit_time.setText(chatinfo.get(position).getTime());
            ((RightViewHolder)holder).userit_msg.setText(chatinfo.get(position).getContents());
        }
    }


    @Override
    public int getItemCount() {
        return chatinfo.size();
    }

    public void deleteItem(int position) {
        chatinfo.remove(position);
    }

    public void addItem(Chat m) {
        chatinfo.add(m);
    }

    public void removeAllItem() {
        chatinfo.clear();
    }
    public int getItemViewType(int position){
        return chatinfo.get(position).getViewType();
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder{
        TextView chatit_name;
        TextView chatit_time;
        TextView chatit_msg;

        public LeftViewHolder(View itemView){
            super(itemView);
            chatit_msg = (TextView) itemView.findViewById(R.id.chatit_msg);
            chatit_name = (TextView) itemView.findViewById(R.id.chatit_name);
            chatit_time = (TextView) itemView.findViewById(R.id.chatit_time);


        }

    }
    public class RightViewHolder extends RecyclerView.ViewHolder {
        TextView userit_name;
        TextView userit_time;
        TextView userit_msg;

        public RightViewHolder(View itemView) {
            super(itemView);
            userit_msg = (TextView) itemView.findViewById(R.id.userit_msg);
            userit_name = (TextView) itemView.findViewById(R.id.userit_name);
            userit_time = (TextView) itemView.findViewById(R.id.userit_time);


        }

    }
}
