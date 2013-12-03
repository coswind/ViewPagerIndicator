package com.coswind.viewpagerindicator.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coswind.viewpagerindicator.TabPagerIndicator;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            protected final String[] CONTENT = new String[]{"ONE", "TWO", "THR"};

            @Override
            public Fragment getItem(int i) {
                return new PlaceholderFragment();
            }

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return CONTENT[position % CONTENT.length];
            }
        });

        TabPagerIndicator pageIndicator = (TabPagerIndicator) findViewById(R.id.titles);
        pageIndicator.setViewPager(viewPager);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
