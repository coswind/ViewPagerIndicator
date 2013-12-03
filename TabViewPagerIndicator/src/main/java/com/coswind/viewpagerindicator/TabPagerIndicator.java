package com.coswind.viewpagerindicator;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by coswind on 12/3/13.
 */
public class TabPagerIndicator extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final CharSequence EMPTY_TITLE = "";

    private int mMaxTabWidth;
    private int mSelectedTabIndex;
    private ViewPager mViewPager;

    private final IcsLinearLayout mTabLayout;

    public TabPagerIndicator(Context context) {
        this(context, null);
    }

    public TabPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTabLayout = new IcsLinearLayout(context, R.attr.tabPageIndicatorStyle);

        // Add Tab Layout.
        addView(mTabLayout, new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
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
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private class TabView extends TextView {
        private int mIndex;

        public TabView(Context context) {
            super(context, null, R.attr.tabPageIndicatorStyle);
        }

        public int getIndex() {
            return mIndex;
        }
    }
}
