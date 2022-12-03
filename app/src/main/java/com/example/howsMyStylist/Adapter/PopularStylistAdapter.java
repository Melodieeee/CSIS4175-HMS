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
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;;

import com.bumptech.glide.Glide;
import com.example.howsMyStylist.Model.Salon;
import com.example.howsMyStylist.Model.Stylist;
import com.example.howsMyStylist.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PopularStylistAdapter extends RecyclerView.Adapter<PopularStylistAdapter.PopularStylistViewHolder> {

    onButtonClick listener;
    Context context;
    boolean favoriteStatus;
    Map<String,Stylist> stylistMap;
    ArrayList<String> userFavStylistList;

    public void setListener(onButtonClick listener) {
        this.listener = listener;
    }

    public PopularStylistAdapter(Context context, Map<String,Stylist> stylistMap){
        this.context = context;
        this.stylistMap = stylistMap;
    }
    public PopularStylistAdapter(Context context, Map<String,Stylist> stylistMap, ArrayList<String> uList){
        this.context = context;
        this.stylistMap = stylistMap;
        this.userFavStylistList = uList;
    }

    @NonNull
    @Override
    public PopularStylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_stylist_salon, parent, false);
        return new PopularStylistViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularStylistViewHolder holder, int position) {
        List<String> sKeyList = stylistMap.keySet().stream().collect(Collectors.toList());
        Stylist stylist = stylistMap.get(sKeyList.get(position));

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

        holder.name.setText(stylist.getfName() + " " + stylist.getlName());
        holder.rating.setRating((float)stylist.getAvgRating());
        holder.call.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String num = stylist.getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num));
                context.startActivity(intent);
            }
        });
        holder.map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Salon salon = new Salon(stylist.getSalonName());
                String loc = salon.getCountry() + salon.getCity() + salon.getAddress();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + loc));
                context.startActivity(intent);
            }
        });

        // check if it's in user's fav list
        // read user's favStylist
        if (userFavStylistList != null) {
            for(String id: userFavStylistList) {
                if (sKeyList.get(position).equals(id)) {
                    holder.fav.setChecked(true);
                    holder.fav.setAlpha(1f);
                    Log.d("FavLoad", String.valueOf(holder.fav.isChecked()));
                }
                Log.d("FavLoad", id);
            }
        }

        holder.fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
                listener.onFavoriteStylistChosen(id, favoriteStatus);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stylistMap.size();
    }

    public static class PopularStylistViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        RatingBar rating;
        Button call, map;
        ImageView profile;
        ToggleButton fav;

        public PopularStylistViewHolder(@NonNull View itemView) {
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
        void onFavoriteStylistChosen(String favoriteId, boolean favoriteStatus);
    }
}
