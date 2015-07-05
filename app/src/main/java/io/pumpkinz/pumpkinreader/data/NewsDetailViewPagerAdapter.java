package io.pumpkinz.pumpkinreader.data;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class NewsDetailViewPagerAdapter extends FragmentPagerAdapter {

    private List<String> titles;
    private List<Fragment> fragments;

    public NewsDetailViewPagerAdapter(FragmentManager fm) {
        super(fm);

        this.titles = new ArrayList<>();
        this.fragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int postition) {
        return this.titles.get(postition);
    }

    public void addFragment(String title, Fragment fragment) {
        this.titles.add(title);
        this.fragments.add(fragment);
    }

}
