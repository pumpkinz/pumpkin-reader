package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import io.pumpkinz.pumpkinreader.data.NewsDetailViewPagerAdapter;


public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);

        ViewPager viewPager = (ViewPager) findViewById(R.id.news_detail_pager);
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

    private void setUpViewPager(ViewPager viewPager) {
        NewsDetailViewPagerAdapter adapter = new NewsDetailViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(getResources().getString(R.string.link), new WebViewFragment());
        adapter.addFragment(getResources().getString(R.string.comments), new NewsDetailFragment());

        viewPager.setAdapter(adapter);
    }

}
