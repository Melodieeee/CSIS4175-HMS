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

import com.example.howsMyStylist.Model.Salon;
import com.example.howsMyStylist.Model.Stylist;
import com.example.howsMyStylist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PopularSalonAdapter extends RecyclerView.Adapter<PopularSalonAdapter.PopularSalonViewHolder> {

    Context context;
    ArrayList<Salon> list;

    public PopularSalonAdapter(Context context, ArrayList<Salon> salon){
        this.context = context;
        this.list = salon;
    }

    @NonNull
    @Override
    public PopularSalonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_stylist_salon, parent, false);
        return new PopularSalonViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularSalonViewHolder holder, int position) {
        Salon salon = list.get(position);
        Uri uri = Uri.parse(salon.getUriImage());
        Picasso.with(context).load(uri).into(holder.profile);
        holder.name.setText(salon.getSalonName());
//        holder.rating.setRating(Float.parseFloat(String.valueOf(salon.getAvgRating())));
        holder.call.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String num = salon.getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(num));
                context.startActivity(intent);
            }
        });
        holder.map.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String loc = salon.getCountry() + salon.getCity() + salon.getAddress();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + loc));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PopularSalonViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        RatingBar rating;
        Button call, map;
        ImageView profile;

        public PopularSalonViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.popularStylistImage);
            name = itemView.findViewById(R.id.popularStylistName);
            rating = itemView.findViewById(R.id.popularStylistRatingBar);
            call = itemView.findViewById(R.id.call_stylist);
            map = itemView.findViewById(R.id.locate_stylist);
        }
    }
}
