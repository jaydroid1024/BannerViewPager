package org.jay.bannerviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity {
    public static List<?> images = new ArrayList<>();
    public static List<String> titles = new ArrayList<>();
    @BindView(R.id.banner1)
    BannerViewPager mBanner1;
    @BindView(R.id.banner2)
    BannerViewPager mBanner2;
    @BindView(R.id.banner3)
    BannerViewPager mBanner3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initData();
        initBanner();
    }

    private void initBanner() {
        mBanner1.setImages(images)
                .start();

        mBanner2.setImages(images)
                .start();

        mBanner3.setImages(images)
                .setBannerTitles(titles)
                .setBannerStyle(BannerViewPager.BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                .start();
    }

    private void initData() {
        String[] urls = getResources().getStringArray(R.array.url);
        String[] tips = getResources().getStringArray(R.array.title);
        List list = Arrays.asList(urls);
        images = new ArrayList(list);
        List list1 = Arrays.asList(tips);
        titles = new ArrayList(list1);
    }
}
