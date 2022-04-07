package org.techtown.naro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HosAdapter extends RecyclerView.Adapter<HosAdapter.ViewHolder> {

    Hospital hospital;
    ArrayList<Hospital> info = new ArrayList<>();

    @NonNull
    @Override
    public HosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View hosView = inflater.inflate(R.layout.hos_item, parent, false);
        return new ViewHolder(hosView);
    }

    @Override
    public void onBindViewHolder(@NonNull HosAdapter.ViewHolder holder, int position) {
        hospital = info.get(holder.getAdapterPosition());
        holder.setInfo(hospital);
    }

    @Override
    public int getItemCount() {
        return info.size();
    }
    public void addItem(Hospital hos){
        info.add(hos);
        notifyDataSetChanged();
    }
    public void removeAllItem(){
        info.clear();
    }



    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView hosname;
        private TextView address;
        private TextView onoff;

        public ViewHolder(View hosView){
            super(hosView);
            hosname = hosView.findViewById(R.id.hosname);
            address = hosView.findViewById(R.id.hosaddress);
            onoff = hosView.findViewById(R.id.onoff);
        }
        public void setInfo(Hospital hos){
            hosname.setText(hos.getHosname());
            address.setText(hos.getHosaddress());
            onoff.setText(hos.getHosonoff());
        }
    }
}
