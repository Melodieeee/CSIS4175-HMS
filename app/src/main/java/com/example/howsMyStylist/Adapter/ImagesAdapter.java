package com.example.howsMyStylist.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.howsMyStylist.R;

import java.util.List;
import java.util.Objects;

public class ImagesAdapter extends PagerAdapter {

    Context context;
    List<Uri> uriUploadImgs;
    LayoutInflater layoutInflater;

    public ImagesAdapter(Context context, List<Uri> uriUploadImgs) {
        this.context = context;
        this.uriUploadImgs = uriUploadImgs;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return uriUploadImgs.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.image_single, container, false);
        ImageView imgViewSinglePhoto = view.findViewById(R.id.imgView_singlePhoto);
        imgViewSinglePhoto.setImageURI(uriUploadImgs.get(position));
        Objects.requireNonNull(container).addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
