package com.example.howsMyStylist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;;

import com.bumptech.glide.Glide;
import com.example.howsMyStylist.Model.Salon;
import com.example.howsMyStylist.Model.Stylist;
import com.example.howsMyStylist.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularStylistAdapter extends RecyclerView.Adapter<PopularStylistAdapter.PopularStylistViewHolder> {

    Context context;
    ArrayList<Stylist> list;

    public PopularStylistAdapter(Context context, ArrayList<Stylist> stylist){
        this.context = context;
        this.list = stylist;
    }

    @NonNull
    @Override
    public PopularStylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_stylist_salon, parent, false);
        return new PopularStylistViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularStylistViewHolder holder, int position) {
        Stylist stylist = list.get(position);

        String stylistPic = stylist.geturiStylistPic();
        if (!stylistPic.isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("StylistPhotos").child(stylistPic);
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri downloadUrl) {
                    Glide.with(holder.profile.getContext())
                            .load(downloadUrl.toString())
                            .placeholder(R.drawable.ic_baseline_cloud_download_24)
                            .error(R.drawable.ic_baseline_cloud_download_24)
                            .into(holder.profile);
                }
            });
        } else {
            holder.profile.setImageResource(R.drawable.hms_logo2);
        }


        holder.name.setText(stylist.getfName());
        holder.rating.setRating((float)stylist.getAvgRating());
        holder.call.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String num = stylist.getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(num));
                context.startActivity(intent);
            }
        });
        holder.map.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Salon salon = new Salon(stylist.getSalonName());
                String loc = salon.getCountry() + salon.getCity() + salon.getAddress();;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + loc));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PopularStylistViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        RatingBar rating;
        Button call, map;
        ImageView profile;

        public PopularStylistViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.popularStylistImage);
            name = itemView.findViewById(R.id.popularStylistName);
            rating = itemView.findViewById(R.id.popularStylistRatingBar);
            call = itemView.findViewById(R.id.call_stylist);
            map = itemView.findViewById(R.id.locate_stylist);
        }
    }
}
