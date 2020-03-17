package com.appleto.findteacher.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.appleto.findteacher.R;

import java.util.List;



public class IntroSliderAdapter extends PagerAdapter {

    private List<Integer> images;
    private List<String> title;
    private List<String> desc;
    private Context context;
    private LayoutInflater layoutInflater;

    public IntroSliderAdapter(Context context, List<Integer> images, List<String> title, List<String> desc) {
        this.context = context;
        this.images = images;
        this.title = title;
        this.desc = desc;
        this.layoutInflater = (LayoutInflater)this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.raw_intro_slider, container, false);
        ImageView mImageView = view.findViewById(R.id.raw_iv_slider_image);
        TextView tvTitle = view.findViewById(R.id.raw_tv_slider_title);
        TextView tvDesc = view.findViewById(R.id.raw_tv_slider_desc);

        mImageView.setImageResource(images.get(position));
        tvTitle.setText(title.get(position));
        tvDesc.setText(desc.get(position));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
