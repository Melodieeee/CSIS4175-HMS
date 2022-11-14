package com.example.howsMyStylist.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.howsMyStylist.Model.Stylist;
import com.example.howsMyStylist.R;

import java.util.List;

public class PopupStylistAdapter extends RecyclerView.Adapter<PopupStylistAdapter.CardViewHolder> {

    onListItemClick mListener;
    List<Stylist> stylistList;

    public void setmListenr(onListItemClick mListener) {
        this.mListener = mListener;
    }

    public PopupStylistAdapter(List<Stylist> stylistList) {
        this.stylistList = stylistList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_popup_stylist, parent, false);
        CardViewHolder viewHolder = new CardViewHolder(itemView);

            viewHolder.txvStylistName = itemView.findViewById(R.id.txv_stylistName);
            viewHolder.txvSalonName = itemView.findViewById(R.id.txv_salonName);
            viewHolder.txvStylistGender = itemView.findViewById(R.id.txv_stylistGender);
            viewHolder.imvStylistPhoto = itemView.findViewById(R.id.imv_stylistPhoto);
            viewHolder.itemView = itemView;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        String stylistName = stylistList.get(position).getfName() + " " + stylistList.get(position).getlName();
        String stylistGender = stylistList.get(position).getGender();
        String salonName = stylistList.get(position).getSalonName();

        holder.txvStylistName.setText(stylistName);
        holder.txvStylistGender.setText(stylistGender);
        holder.txvSalonName.setText(salonName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStylistChosen(stylistName, salonName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stylistList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView txvStylistName, txvSalonName, txvStylistGender;
        ImageView imvStylistPhoto;
        View itemView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public interface onListItemClick{
        void onStylistChosen(String stylistName, String salonName);
    }
}
