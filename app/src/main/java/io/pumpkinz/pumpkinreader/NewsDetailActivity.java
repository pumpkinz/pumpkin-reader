package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import io.pumpkinz.pumpkinreader.data.NewsDetailViewPagerAdapter;


public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);
        setUpToolbar();

        ViewPager viewPager = (ViewPager) findViewById(R.id.news_detail_viewpager);
        setUpViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.news_detail_tab);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        Toolbar appBar = (Toolbar) findViewById(R.id.news_detail_app_bar);
        setSupportActionBar(appBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setUpViewPager(ViewPager viewPager) {
        NewsDetailViewPagerAdapter adapter = new NewsDetailViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment("Comments", new NewsDetailFragment());
        adapter.addFragment("Link", new WebViewFragment());

        viewPager.setAdapter(adapter);
    }

}
