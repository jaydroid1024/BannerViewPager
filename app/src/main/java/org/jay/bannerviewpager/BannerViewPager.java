package org.jay.bannerviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.support.v4.view.ViewPager.OnPageChangeListener;
import static android.support.v4.view.ViewPager.PageTransformer;

/**
 * Created by jay on 2017/8/3.
 */

public class BannerViewPager extends FrameLayout implements OnPageChangeListener {
    public static final String TAG="banner";
    private int mIndicatorMargin = BannerConfig.PADDING_SIZE;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int indicatorSize;
    private int bannerStyle = BannerConfig.CIRCLE_INDICATOR;
    private int delayTime = BannerConfig.TIME;
    private int scrollTime = BannerConfig.DURATION;
    private boolean isAutoPlay = BannerConfig.IS_AUTO_PLAY;
    private boolean isScroll = BannerConfig.IS_SCROLL;
    private int mIndicatorSelectedResId = R.drawable.dot_selected_gray;
    private int mIndicatorUnselectedResId = R.drawable.dot_normal_white;
    private int mLayoutResId = R.layout.layout_banner;
    private int titleHeight;
    private int titleBackground;
    private int titleTextColor;
    private int titleTextSize;
    private int count = 0;
    private int currentItem=-1;
    private int gravity = -1;
    private int lastPosition = 1;
    private int scaleType = 1;
    private List<String> titles;
    private List imageUrls;
    private List<ImageView> imageViews;
    private List<ImageView> indicatorImages;
    private Context context;
    private ViewPager viewPager;
    private TextView bannerTitle, numIndicatorInside, numIndicator;
    private LinearLayout indicator, indicatorInside, titleView;
    private BannerPagerAdapter adapter;
    private OnPageChangeListener mOnPageChangeListener;
    private BannerScroller mScroller;
    private OnBannerClickListener bannerListener;
    private ImageHandler mImageHandler;
    public static int H,W;
    int mIndicatorPrePosition = 0;

    public BannerViewPager(@NonNull Context context) {
        this(context,null);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerViewPager(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        titles = new ArrayList<>();
        imageUrls = new ArrayList<>();
        imageViews = new ArrayList<>();
        indicatorImages = new ArrayList<>();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        H=dm.heightPixels;
        W=dm.widthPixels;
        mImageHandler=new ImageHandler(this);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        imageViews.clear();
        //初始化自定义属性
        handleTypedArray(context, attrs);
        View view = LayoutInflater.from(context).inflate(mLayoutResId, this, true);
        viewPager = (ViewPager) view.findViewById(R.id.bannerViewPager);
        titleView = (LinearLayout) view.findViewById(R.id.titleView);
        indicator = (LinearLayout) view.findViewById(R.id.circleIndicator);
        indicatorInside = (LinearLayout) view.findViewById(R.id.indicatorInside);
        bannerTitle = (TextView) view.findViewById(R.id.bannerTitle);
        numIndicator = (TextView) view.findViewById(R.id.numIndicator);
        numIndicatorInside = (TextView) view.findViewById(R.id.numIndicatorInside);
        //反射修改viewpager的滑动间隔
        initViewPagerScroll();
    }


    private void handleTypedArray(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Banner);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_width, indicatorSize);
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_height, indicatorSize);
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.Banner_indicator_margin, BannerConfig.PADDING_SIZE);
        mIndicatorSelectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_drawable_selected, R.drawable.dot_selected_gray);
        mIndicatorUnselectedResId = typedArray.getResourceId(R.styleable.Banner_indicator_drawable_unselected, R.drawable.dot_normal_white);
        scaleType = typedArray.getInt(R.styleable.Banner_image_scale_type, scaleType);
        delayTime = typedArray.getInt(R.styleable.Banner_delay_time, BannerConfig.TIME);
        scrollTime = typedArray.getInt(R.styleable.Banner_scroll_time, BannerConfig.DURATION);
        isAutoPlay = typedArray.getBoolean(R.styleable.Banner_is_auto_play, BannerConfig.IS_AUTO_PLAY);
        titleBackground = typedArray.getColor(R.styleable.Banner_title_background, BannerConfig.TITLE_BACKGROUND);
        titleHeight = typedArray.getDimensionPixelSize(R.styleable.Banner_title_height, BannerConfig.TITLE_HEIGHT);
        titleTextColor = typedArray.getColor(R.styleable.Banner_title_textcolor, BannerConfig.TITLE_TEXT_COLOR);
        titleTextSize = typedArray.getDimensionPixelSize(R.styleable.Banner_title_textsize, BannerConfig.TITLE_TEXT_SIZE);
        mLayoutResId = typedArray.getResourceId(R.styleable.Banner_layout_id, mLayoutResId);
        typedArray.recycle();
    }
    private void initViewPagerScroll() {
        try {
            Field mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new BannerScroller(viewPager.getContext());
            mScroller.setDuration(scrollTime);
            mField.set(viewPager, mScroller);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    //------------------------------------配置BannerViewPager---------------------------------------//

    public BannerViewPager isAutoPlay(boolean isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
        return this;
    }


    public BannerViewPager setDelayTime(int delayTime) {
        this.delayTime = delayTime;
        return this;
    }

    public BannerViewPager setIndicatorGravity(int type) {
        switch (type) {
            case BannerConfig.LEFT:
                this.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case BannerConfig.CENTER:
                this.gravity = Gravity.CENTER;
                break;
            case BannerConfig.RIGHT:
                this.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        return this;
    }

    public BannerViewPager setBannerAnimation(Class<? extends PageTransformer> transformer) {
        try {
            setPageTransformer(true, transformer.newInstance());
        } catch (Exception e) {
            Log.e(TAG, "Please set the PageTransformer class");
        }
        return this;
    }

    public BannerViewPager setOffscreenPageLimit(int limit) {
        if (viewPager != null) {
            viewPager.setOffscreenPageLimit(limit);
        }
        return this;
    }


    public BannerViewPager setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        viewPager.setPageTransformer(reverseDrawingOrder, transformer);
        return this;
    }

    public BannerViewPager setBannerTitles(List<String> titles) {
        this.titles = titles;
        return this;
    }

    public BannerViewPager setBannerStyle(int bannerStyle) {
        this.bannerStyle = bannerStyle;
        return this;
    }

    public BannerViewPager setViewPagerIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
        return this;
    }

    public BannerViewPager setImages(List<?> imageUrls) {
        this.imageUrls = imageUrls;
        this.count = imageUrls.size();
        return this;
    }

    public BannerViewPager setOnBannerClickListener(OnBannerClickListener listener) {
        this.bannerListener = listener;
        return this;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }


    public BannerViewPager start() {
        setBannerStyleUI();
        setImageList(imageUrls);
        setData();
        return this;
    }

    private void setBannerStyleUI() {
        int visibility;
        if (count > 1) visibility = View.VISIBLE;
        else visibility = View.GONE;
        switch (bannerStyle) {
            case BannerConfig.CIRCLE_INDICATOR:
                indicator.setVisibility(visibility);
                break;
            case BannerConfig.NUM_INDICATOR:
                numIndicator.setVisibility(visibility);
                break;
            case BannerConfig.NUM_INDICATOR_TITLE:
                numIndicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE:
                indicator.setVisibility(visibility);
                setTitleStyleUI();
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE:
                indicatorInside.setVisibility(visibility);
                setTitleStyleUI();
                break;
        }
    }
    private void setTitleStyleUI() {
        if (titles.size() != imageUrls.size()) {
            throw new RuntimeException("[Banner] --> The number of titles and images is different");
        }
        if (titleBackground != -1) {
            titleView.setBackgroundColor(titleBackground);
        }
        if (titleHeight != -1) {
            titleView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleHeight));
        }
        if (titleTextColor != -1) {
            bannerTitle.setTextColor(titleTextColor);
        }
        if (titleTextSize != -1) {
            bannerTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize);
        }
        if (titles != null && titles.size() > 0) {
            bannerTitle.setText(titles.get(0));
            bannerTitle.setVisibility(View.VISIBLE);
            titleView.setVisibility(View.VISIBLE);
        }
    }

    private void setImageList(List<String> imagesUrl) {
        if (imagesUrl == null || imagesUrl.size() <= 0) {
            Log.e(TAG, "Please set the images data.");
            return;
        }
        initImages();
        for (int i = 0; i < count; i++) {
            ImageView imageView=new ImageView(context);
            setScaleType(imageView);
            Glide.with(context).load(imagesUrl.get(i)).into(imageView);
            imageViews.add(imageView);
        }
    }
    private void setScaleType(View imageView) {
        if (imageView instanceof ImageView) {
            ImageView view = ((ImageView) imageView);
            switch (scaleType) {
                case 0:
                    view.setScaleType(ScaleType.CENTER);
                    break;
                case 1:
                    view.setScaleType(ScaleType.CENTER_CROP);
                    break;
                case 2:
                    view.setScaleType(ScaleType.CENTER_INSIDE);
                    break;
                case 3:
                    view.setScaleType(ScaleType.FIT_CENTER);
                    break;
                case 4:
                    view.setScaleType(ScaleType.FIT_END);
                    break;
                case 5:
                    view.setScaleType(ScaleType.FIT_START);
                    break;
                case 6:
                    view.setScaleType(ScaleType.FIT_XY);
                    break;
                case 7:
                    view.setScaleType(ScaleType.MATRIX);
                    break;
            }

        }
    }
    private void initImages() {
        imageViews.clear();
        if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) {
            createIndicator();
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR_TITLE) {
            numIndicatorInside.setText("1/" + count);
        } else if (bannerStyle == BannerConfig.NUM_INDICATOR) {
            numIndicator.setText("1/" + count);
        }
    }
    private void createIndicator() {
        indicatorImages.clear();
        indicator.removeAllViews();
        indicatorInside.removeAllViews();
        for (int i = 0; i < count; i++) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
            params.leftMargin = mIndicatorMargin;
            params.rightMargin = mIndicatorMargin;
            if (i == 0) {
                imageView.setImageResource(mIndicatorSelectedResId);
            } else {
                imageView.setImageResource(mIndicatorUnselectedResId);
            }
            indicatorImages.add(imageView);
            if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                    bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE)
                indicator.addView(imageView, params);
            else if (bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
                indicatorInside.addView(imageView, params);
        }
    }

    private void setData() {
        currentItem = 0;
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            viewPager.addOnPageChangeListener(this);
        }
        viewPager.setAdapter(adapter);
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(currentItem);
        if (gravity != -1)
            indicator.setGravity(gravity);
        if (isAutoPlay)
            startAutoPlay();
    }

    public void startAutoPlay() {
        if (count > 1) {
            mImageHandler.sendEmptyMessageDelayed(ImageHandler.MSG_UPDATE_IMAGE, delayTime);
        }
    }

    public void stopAutoPlay() {
        mImageHandler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        Log.i(TAG, ev.getAction() + "--" + isAutoPlay);
        if (isAutoPlay) {
            int action = ev.getAction();
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL
                    || action == MotionEvent.ACTION_OUTSIDE) {
                startAutoPlay();
            }
            else if (action == MotionEvent.ACTION_DOWN) {
                stopAutoPlay();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
        mImageHandler.sendMessage(Message.obtain(mImageHandler, ImageHandler.MSG_PAGE_CHANGED, position, 0));

    }
    @Override
    public void onPageSelected(int position) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageSelected(position);
        }

        if (bannerStyle == BannerConfig.CIRCLE_INDICATOR ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE ||
                bannerStyle == BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) {
            indicatorImages.get(position % count).setImageResource(mIndicatorSelectedResId);
            indicatorImages.get(mIndicatorPrePosition % count).setImageResource(mIndicatorUnselectedResId);
            mIndicatorPrePosition = position;
        }
        switch (bannerStyle) {
            case BannerConfig.CIRCLE_INDICATOR:
                break;
            case BannerConfig.NUM_INDICATOR:
                numIndicator.setText(position+1 + "/" + count);
                break;
            case BannerConfig.NUM_INDICATOR_TITLE:
                numIndicatorInside.setText(position+1 + "/" + count);
                bannerTitle.setText(titles.get(position));
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE:
                bannerTitle.setText(titles.get(position));
                break;
            case BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE:
                bannerTitle.setText(titles.get(position));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageScrollStateChanged(state);
        }
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                stopAutoPlay();
                break;
            case ViewPager.SCROLL_STATE_IDLE:
                startAutoPlay();
                break;
            default:
                break;
        }
    }


    class BannerPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView view = imageViews.get(position);
            ViewPager.LayoutParams params=new ViewPager.LayoutParams();
            params.width=W;
            params.height=W/4*3;
            view.setLayoutParams(params);
            Glide.with(context).load(imageUrls.get(position)).into(view);
            if (bannerListener != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bannerListener.OnBannerClick(position);
                    }
                });
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    public interface OnBannerClickListener {
        public void OnBannerClick(int position);
    }

    private static class ImageHandler extends Handler {
        WeakReference<BannerViewPager> mWeakReference;
        private BannerViewPager mBannerViewPager;

        public ImageHandler(BannerViewPager bannerViewPager) {
            mWeakReference =new WeakReference<BannerViewPager>(bannerViewPager);
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
         * 记录最新的页号。
         */
        protected static final int MSG_PAGE_CHANGED = 4;


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference == null) {
                return;
            }
            mBannerViewPager = mWeakReference.get();

            //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
            if (mBannerViewPager.mImageHandler.hasMessages(MSG_UPDATE_IMAGE)) {
                mBannerViewPager.mImageHandler.removeMessages(MSG_UPDATE_IMAGE);
            }
            switch (msg.what) {
                case MSG_UPDATE_IMAGE:
                    if (mBannerViewPager.currentItem == mBannerViewPager.count-1) {
                        mBannerViewPager.currentItem = -1;
                    }
                    mBannerViewPager.currentItem+=1;
                    mBannerViewPager.viewPager.setCurrentItem(mBannerViewPager.currentItem);
                    //准备下次播放
                    mBannerViewPager.mImageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, mBannerViewPager.delayTime);
                    break;
                case MSG_KEEP_SILENT:
                    //只要不发送消息就暂停了
                    break;
                case MSG_BREAK_SILENT:
                    mBannerViewPager.mImageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, mBannerViewPager.delayTime);
                    break;
                case MSG_PAGE_CHANGED:
                    //记录当前的页号，避免播放的时候页面显示不正确。
                    mBannerViewPager.currentItem = msg.arg1;
                    mBannerViewPager.mImageHandler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, mBannerViewPager.delayTime);
                    break;
                default:
                    break;
            }
        }
    }

    class BannerConfig {
        /**
         * indicator style
         */
        public static final int NOT_INDICATOR = 0;
        public static final int CIRCLE_INDICATOR = 1;
        public static final int NUM_INDICATOR = 2;
        public static final int NUM_INDICATOR_TITLE = 3;
        public static final int CIRCLE_INDICATOR_TITLE = 4;
        public static final int CIRCLE_INDICATOR_TITLE_INSIDE = 5;
        /**
         * indicator gravity
         */
        public static final int LEFT = 5;
        public static final int CENTER = 6;
        public static final int RIGHT = 7;

        /**
         * banner
         */
        public static final int PADDING_SIZE = 5;
        public static final int TIME = 2000;
        public static final int DURATION = 800;
        public static final boolean IS_AUTO_PLAY = true;
        public static final boolean IS_SCROLL = true;

        /**
         * title style
         */
        public static final int TITLE_BACKGROUND = -1;
        public static final int TITLE_HEIGHT = -1;
        public static final int TITLE_TEXT_COLOR = -1;
        public static final int TITLE_TEXT_SIZE = -1;

    }

    class BannerScroller extends Scroller {
        private int mDuration = BannerConfig.DURATION;

        public BannerScroller(Context context) {
            super(context);
        }

        public BannerScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        public void setDuration(int time) {
            mDuration = time;
        }

    }

}
