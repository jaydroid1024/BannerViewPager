package org.jay.bannerviewpager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jay.bannerviewpager.module.Banner;
import org.jay.bannerviewpager.transformer.BackgroundToForegroundTransformer;
import org.jay.bannerviewpager.transformer.CubeInTransformer;
import org.jay.bannerviewpager.transformer.RotateDownTransformer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class MainActivity extends AppCompatActivity {
    public final String TAG = this.getClass().getSimpleName();
    public static List<?> images = new ArrayList<>();
    public static List<String> titles = new ArrayList<>();
    @BindView(R.id.banner1)
    BannerViewPager mBanner1;
    @BindView(R.id.banner2)
    BannerViewPager mBanner2;
    @BindView(R.id.banner3)
    BannerViewPager mBanner3;
    private List<Banner.DataBean.BannersBean> mBanners;

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
                .setPageTransformer(true, new CubeInTransformer())
                .setDelayTime(2500)
                .start();

        mBanner2.setModule(mBanners)
                .setPageTransformer(false, new BackgroundToForegroundTransformer())
                .setBannerStyle(BannerViewPager.BannerConfig.NUM_INDICATOR_TITLE)
                .setDelayTime(2000)
                .start();

        mBanner3.setImages(images)
                .setDelayTime(3000)
                .setPageTransformer(true, new RotateDownTransformer())
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
        String json=getJsonData();
        if (!TextUtils.isEmpty(json)) {
            Gson gson= new GsonBuilder().create();
            Banner banner=gson.fromJson(json, Banner.class);
            mBanners = banner.getData().getBanners();
        }
    }

    //读取assets中的文件
    private String getJsonData() {
        try {
            InputStream is = getAssets().open("banner.txt");
            InputStreamReader reader = new InputStreamReader(is,"UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuffer buffer = new StringBuffer("");
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            return buffer.toString();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG,e.toString());
        }
        return null;
    }

}
