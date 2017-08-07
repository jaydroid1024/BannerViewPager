package org.jay.bannerviewpager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jay on 2017/4/18.
 */

public class BannerImageHelper {
    private Context mContext;
    private ViewPager sliderViewPager;
    private List<Object> mBannersList;
    private Glide mImageLoader;

    Handler handler = new Handler();
    private ImageHandler mImageHandler;
    private int currentPage;
    private int NUM_PAGES;

    public BannerImageHelper(Context context, Glide imageLoader, ViewPager sliderViewPager, List<Object> bannersList) {
        this.mContext = context;
        mListener = (OnBannerItemClickListener) context;
        this.sliderViewPager = sliderViewPager;
        this.mBannersList = bannersList;
        mImageLoader = imageLoader;
        NUM_PAGES = mBannersList.size();
        mImageHandler = new ImageHandler(this);
        initialize();
    }


    public void setCurrentItem(int i) {
        sliderViewPager.setCurrentItem(i, true);
    }

    int mPrePosition = 0;
    private void initialize() {
//         sliderViewPager.setPageTransformer(true,new ZoomOutViewPagerTransformer());//自定义转场动画
        PagerAdapter adapter = new BannerImageHelper.CustomAdapter(mContext, mBannersList);
        sliderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                mImageHandler.sendMessage(Message.obtain(mImageHandler, ImageHandler.MSG_PAGE_CHANGED, position, 0));
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        mImageHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        mImageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
                        break;
                    default:
                        break;
                }
            }

        });
        //开始轮播效果
        mImageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, ImageHandler.MSG_DELAY);
        sliderViewPager.setAdapter(adapter);
//        configureAutoSlide();
    }

    private void configureAutoSlide() {
        final Runnable update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                sliderViewPager.setCurrentItem(currentPage++, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 2000, 2000);
    }

    public ViewPager getViewPager() {
        return this.sliderViewPager;
    }


    private  class CustomAdapter extends PagerAdapter {
        private Context context;
        private List<Object> imageList = new ArrayList<>();

        public CustomAdapter(Context context, List<Object> images) {
            this.context = context;
            this.imageList = images;
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            View viewItem = inflater.inflate(R.layout.layout_banner, container, false);
            ImageView imageView = (ImageView) viewItem.findViewById(R.id.image);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(viewItem);
            viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onBannerSelected(position);
                }
            });

            return viewItem;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }
    }


    public OnBannerItemClickListener mListener;

    public interface OnBannerItemClickListener {
        // TODO: Update argument type and name
        void onBannerSelected(int position);
    }

    private static class ImageHandler extends Handler {
        WeakReference<BannerImageHelper> mWeakBannerImageHelper;
        private BannerImageHelper mBannerImageHelper;

        public ImageHandler(BannerImageHelper bannerImageHelper) {
            mWeakBannerImageHelper =new WeakReference<BannerImageHelper>(bannerImageHelper);
        }

        /**
         * 请求更新显示的View。
         */
        protected static final int MSG_UPDATE_IMAGE = 1;
        /**
         * 请求暂停轮播。
         */
        protected static final int MSG_KEEP_SILENT = 2;
        /**
         * 请求恢复轮播。
         */
        protected static final int MSG_BREAK_SILENT = 3;
        /**
         * 记录最新的页号，当用户手动滑动时需要记录新页号，否则会使轮播的页面出错。
         * 例如当前如果在第一页，本来准备播放的是第二页，而这时候用户滑动到了末页，
         * 则应该播放的是第一页，如果继续按照原来的第二页播放，则逻辑上有问题。
         */
        protected static final int MSG_PAGE_CHANGED = 4;

        //轮播间隔时间
        protected static final long MSG_DELAY = 2000;
        private int currentItem = -1;

        @Override
        public void handleMessage(Message msg) {
            if (mWeakBannerImageHelper != null) {
                mBannerImageHelper = mWeakBannerImageHelper.get();
            }else{
                return;
            }
            super.handleMessage(msg);
            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (mBannerImageHelper.mImageHandler.hasMessages(MSG_UPDATE_IMAGE)) {
                mBannerImageHelper.mImageHandler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    if (currentItem == mBannerImageHelper.NUM_PAGES) {
                        currentItem = 0;
                    }
                    mBannerImageHelper.sliderViewPager.setCurrentItem(currentItem++);
                    //准备下次播放
                    mBannerImageHelper.mImageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    mBannerImageHelper.mImageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    currentItem = msg.arg1;
                    mBannerImageHelper.mImageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                    break;
                default:
                    break;
            }
        }
    }
    private final List<ImageView> mImageViewTips = new ArrayList<>();

    private void addTisView(LinearLayout linearLayout, int count) {
        for (int i = 0; i < count; i++) {
            ImageView imageViewTips = new ImageView(linearLayout.getContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(15, 15);
            lp.setMargins(10, 0, 10, 0);
            imageViewTips.setLayoutParams(lp);
            if (i == 0) {
                imageViewTips.setBackgroundColor(Color.YELLOW);
            } else {
                imageViewTips.setBackgroundColor(Color.GRAY);
            }
            linearLayout.addView(imageViewTips);
            mImageViewTips.add(imageViewTips);
        }
        if (mImageViewTips.size() == 1) {
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

}

