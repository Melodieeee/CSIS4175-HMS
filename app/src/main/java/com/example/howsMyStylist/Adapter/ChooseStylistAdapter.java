package com.example.howsMyStylist.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.howsMyStylist.Model.Stylist;
import com.example.howsMyStylist.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ChooseStylistAdapter extends RecyclerView.Adapter<ChooseStylistAdapter.CardViewHolder> {

    onListItemClick mListener;
    List<Stylist> stylistList;

    public void setmListenr(onListItemClick mListener) {
        this.mListener = mListener;
    }

    public ChooseStylistAdapter(List<Stylist> stylistList) {
        this.stylistList = stylistList;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_choose_stylist, parent, false);
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
        String stylistPhoto = stylistList.get(position).geturiStylistPic();
        //upload stylist need to save the pic name not the uri
        holder.txvStylistName.setText(stylistName);
        holder.txvStylistGender.setText(stylistGender);
        holder.txvSalonName.setText(salonName);
        //change the reference to each stylist pic
        if (!stylistPhoto.isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("StylistPhotos").child(stylistPhoto);
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUrl) {
                    Glide.with(holder.imvStylistPhoto.getContext())
                            .load(downloadUrl.toString())
                            .placeholder(R.drawable.ic_baseline_cloud_download_24)
                            .error(R.drawable.ic_baseline_cloud_download_24)
                            .into(holder.imvStylistPhoto);
                }
            });
        }

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
