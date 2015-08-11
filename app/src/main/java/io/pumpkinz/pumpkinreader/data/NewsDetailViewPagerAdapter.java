package io.pumpkinz.pumpkinreader.data;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.pumpkinz.pumpkinreader.NewsDetailFragment;
import io.pumpkinz.pumpkinreader.R;
import io.pumpkinz.pumpkinreader.WebViewFragment;


public class NewsDetailViewPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_FRAGMENTS = 2;
    public static final int TAB_LINK_IDX = 0;
    public static final int TAB_COMMENTS_IDX = 1;

    private Resources res;

    public NewsDetailViewPagerAdapter(FragmentManager fm, Resources res) {
        super(fm);
        this.res = res;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case TAB_LINK_IDX:
                return new WebViewFragment();
            case TAB_COMMENTS_IDX:
                return new NewsDetailFragment();
            default:
                throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public int getCount() {
        return NUM_FRAGMENTS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case TAB_LINK_IDX:
                return res.getString(R.string.link);
            case TAB_COMMENTS_IDX:
                return res.getString(R.string.comments);
            default:
                throw new IndexOutOfBoundsException();
        }
    }

}
