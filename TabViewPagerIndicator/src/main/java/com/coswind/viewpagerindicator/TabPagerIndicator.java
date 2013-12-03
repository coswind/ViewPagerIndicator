package com.coswind.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by coswind on 12/3/13.
 */
public class TabPagerIndicator extends HorizontalScrollView implements ViewPager.OnPageChangeListener {
    private static final CharSequence EMPTY_TITLE = "";

    private int mMaxTabWidth;
    private int mSelectedTabIndex;
    private ViewPager mViewPager;

    private Runnable mTabSelector;

    private final IcsLinearLayout mTabLayout;

    public TabPagerIndicator(Context context) {
        this(context, null);
    }

    public TabPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Hide Scroll Bar.
        setHorizontalScrollBarEnabled(false);

        mTabLayout = new IcsLinearLayout(context, R.attr.tabPageIndicatorStyle);

        // Add Tab Layout.
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            setCurrentItem(mSelectedTabIndex);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }

        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }

        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();

        PagerAdapter pagerAdapter = mViewPager.getAdapter();

        int count = pagerAdapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence charSequence = pagerAdapter.getPageTitle(i);

            if (charSequence == null) {
                charSequence = EMPTY_TITLE;
            }

            addTab(i, charSequence);
        }

        if (mSelectedTabIndex >= count) {
            mSelectedTabIndex = count - 1;
        }

        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    protected void addTab(int index, CharSequence text) {
        TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                TabView tabView = (TabView)view;
                int newSelected = tabView.getIndex();
                mViewPager.setCurrentItem(newSelected);
            }
        });
        tabView.setText(text);

        // Set Weight to 1.
        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
    }

    public void setCurrentItem(int index) {
        mSelectedTabIndex = index;

        mViewPager.setCurrentItem(mSelectedTabIndex);

        for (int i = 0, len = mTabLayout.getChildCount(); i < len; i++) {
            View child = mTabLayout.getChildAt(i);
            boolean isSelected = (i == index);

            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(index);
            }
        }
    }

    protected void animateToTab(int position) {
        final View tabView = mTabLayout.getChildAt(position);

        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }

        mTabSelector = new Runnable() {
            @Override
            public void run() {
                int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        setCurrentItem(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    private class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.tabPageIndicatorStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }

        public int getIndex() {
            return mIndex;
        }
    }
}
