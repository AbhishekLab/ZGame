package com.zgame.zgame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import com.zgame.zgame.R;

import java.util.ArrayList;

public class CommonSliderAdapter extends PagerAdapter {


    private ArrayList<String> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;

    public CommonSliderAdapter(Context context, ArrayList<String> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.sliding_row_zoom, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
        final ImageView imageViewBackground = (ImageView) imageLayout.findViewById(R.id.img_background);
        imageViewBackground.setAlpha(0.5f);
        Glide.with(context)
                .load(imageModelArrayList.get(position))
                .into(imageViewBackground);

        Glide.with(context)
                .load(imageModelArrayList.get(position))
                .into(imageView);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}