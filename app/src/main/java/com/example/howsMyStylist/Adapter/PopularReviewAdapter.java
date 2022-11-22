package com.example.howsMyStylist.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;;

import com.bumptech.glide.Glide;
import com.example.howsMyStylist.Model.Review;
import com.example.howsMyStylist.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PopularReviewAdapter extends RecyclerView.Adapter<PopularReviewAdapter.PopularReviewViewHolder> {

    Context context;
    ArrayList<Review> list;

    public PopularReviewAdapter(Context context, ArrayList<Review> review){
        this.context = context;
        this.list = review;
    }

    @NonNull
    @Override
    public PopularReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder_review, parent, false);
        return new PopularReviewViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularReviewViewHolder holder, int position) {
        Review review = list.get(position);

        holder.name.setText(review.getUsername());
        holder.reviewString.setText(review.getReview());
        Float rate = Float.parseFloat(String.valueOf(review.getRating()));
//        holder.rating.setRating(rate);
        Log.d("rating", String.valueOf(rate));

        StorageReference reviewImageReference = FirebaseStorage.getInstance().getReference("ReviewPhotos").child(review.getImages().get(0));
        reviewImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri downloadUrl) {
                Glide.with(holder.reviewImage.getContext())
                        .load(downloadUrl.toString())
                        .placeholder(R.drawable.ic_baseline_cloud_download_24)
                        .error(R.drawable.ic_baseline_cloud_download_24)
                        .into(holder.reviewImage);
            }
        });

//        holder.rating.setRating(Float.parseFloat(String.valueOf(review.getRating())));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class PopularReviewViewHolder extends RecyclerView.ViewHolder{
        TextView name, reviewString;
        RatingBar rating;
        ImageView reviewImage, userProfilePic;

        public PopularReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewImage = itemView.findViewById(R.id.reviewImage);
            userProfilePic = itemView.findViewById(R.id.userProfilePic);
            name = itemView.findViewById(R.id.userName);
            reviewString = itemView.findViewById(R.id.reviewString);
            rating = itemView.findViewById(R.id.popularStylistRatingBar);

        }
    }
}
