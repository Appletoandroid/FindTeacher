package com.appleto.findteacher.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appleto.findteacher.R;
import com.appleto.findteacher.adapter.IntroSliderAdapter;
import com.appleto.findteacher.utils.Storage;
import com.appleto.findteacher.utils.Utils;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {

    ViewPager viewPager;
    IntroSliderAdapter imageSliderAdapter;
    PageIndicatorView pageIndicatorView;
    TextView tvSkip, tvNext;
    LinearLayout bottomView;
    LinearLayout nextView;
    private int currentIndex;

    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);// SDK21
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }

        storage = new Storage(this);
        if(storage.getIsFirstTime()){
            skippingWorks();
        }
        initViews();

        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.dummy);
        imageList.add(R.drawable.dummy);
        imageList.add(R.drawable.dummy);

        List<String> title = new ArrayList<>();
        title.add("WELCOME TO FIND TEACHER");
        title.add("CRATE SOCIAL VIDEO");
        title.add("1M+ VIDEO UPLOAD");

        List<String> desc = new ArrayList<>();
        desc.add("Create engaging videos to inform\n" +
                "     Your fr");
        desc.add("Generate Subtitle from your video \n" +
                " To get your Video Heard");
        desc.add("See 1M+ Video and Share");

        imageSliderAdapter = new IntroSliderAdapter(this, imageList, title, desc);
        viewPager.setAdapter(imageSliderAdapter);

        pageIndicatorView.setViewPager(viewPager);
        pageIndicatorView.setCount(3);
        pageIndicatorView.setSelection(viewPager.getCurrentItem());

        addListener();
    }

    private void addListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentIndex = position;
                pageIndicatorView.setSelection(position);
                switch (position) {
                    case 0:
                        bottomView.setVisibility(View.VISIBLE);
                        setTextDesapiarAnimator(position);
                        break;

                    case 1:
                        bottomView.setVisibility(View.VISIBLE);
                        setTextDesapiarAnimator(position);
                        break;

                    case 2:
                        bottomView.setVisibility(View.INVISIBLE);
                        setTextDesapiarAnimator(position);
                        skippingWorks();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void skippingWorks() {
        Utils.navigateTo(StartActivity.this, LoginActivity.class);
    }

    private void initViews() {
        viewPager = findViewById(R.id.vp_splash);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        bottomView = findViewById(R.id.bottom_view);
    }

    private void setTextDesapiarAnimator(final int position) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(2500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) animation.getAnimatedValue();
            }
        });
        valueAnimator.start();
    }
}
