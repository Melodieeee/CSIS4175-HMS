package com.example.howsMyStylist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PopularSalonAdapter extends RecyclerView.Adapter<PopularSalonAdapter.PopularSalonViewHolder> {

    onButtonClick listener;

    Context context;
    boolean favoriteStatus;
    Map<String, Salon> salonMap;
    ArrayList<String> userFavSalonList;

    public void setListener(onButtonClick listener){
        this.listener = listener;
    }

    public PopularSalonAdapter(Context context, Map<String, Salon> salonMap){
        this.context = context;
        this.salonMap = salonMap;
    }

    public PopularSalonAdapter(Context context, Map<String, Salon> salonMap, ArrayList<String> userFavSalonList) {
        this.context = context;
        this.salonMap = salonMap;
        this.userFavSalonList = userFavSalonList;
    }

    @NonNull
    @Override
    public PopularSalonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_stylist_salon, parent, false);
        return new PopularSalonViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularSalonViewHolder holder, int position) {
        List<String> sKeyList = salonMap.keySet().stream().collect(Collectors.toList());
        Salon salon = salonMap.get(sKeyList.get(position));

        String salonPic = salon.getUriImage();
        if (!salonPic.isEmpty()) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference("SalonPhotos").child(salon.getUriImage());
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

        Log.d("salonRating", String.valueOf(salon.getSalonName()));
        holder.name.setText(salon.getSalonName());

        Log.d("salonRating", String.valueOf(salon.getAvgRating()));
        holder.rating.setRating((float)salon.getAvgRating());

        holder.call.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String num = salon.getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
                context.startActivity(intent);
            }
        });

        holder.map.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String loc = salon.getCountry() + salon.getCity() + salon.getAddress();;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + loc));
                context.startActivity(intent);
            }
        });

        // Check fav list
        if (userFavSalonList != null){
            for (String id: userFavSalonList){
                if (sKeyList.get(position).equals(id)){
                    holder.fav.setChecked(true);
                    holder.fav.setAlpha(1f);
                    Log.d("FavLoad", String.valueOf(holder.fav.isChecked()));
                }
                Log.d("FavLoad", id);
            }
        }

        holder.fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    holder.fav.setAlpha(1f);
                    favoriteStatus = true;
                    Log.d("FavCheck", "checked");
                } else {
                    // The toggle is disabled
                    holder.fav.setAlpha(0.2f);
                    favoriteStatus = false;
                    Log.d("FavCheck", "unchecked");
                }
                String id = sKeyList.get(holder.getAdapterPosition());
                listener.onFavoriteSalonChosen(id, favoriteStatus);
            }
        });
    }

    @Override
    public int getItemCount() {
        return salonMap.size();
    }

    public static class PopularSalonViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        RatingBar rating;
        Button call, map;
        ImageView profile;
        ToggleButton fav;

        public PopularSalonViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.popularStylistImage);
            name = itemView.findViewById(R.id.popularStylistName);
            rating = itemView.findViewById(R.id.popularStylistRatingBar);
            call = itemView.findViewById(R.id.call_stylist);
            map = itemView.findViewById(R.id.locate_stylist);
            fav = itemView.findViewById(R.id.favButton);
        }
    }

    public interface onButtonClick {
        void onFavoriteSalonChosen(String favoriteId, boolean favoriteStatus);
    }
}
