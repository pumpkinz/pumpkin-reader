package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.data.NewsDetailViewPagerAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.News;


public class NewsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);

        ViewPager viewPager = (ViewPager) findViewById(R.id.news_detail_pager);
        setUpViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.news_detail_tab);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.news_detail_fab);
        setUpFAB(fab);
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

    private void setUpFAB(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);

                News news = Parcels.unwrap(getIntent().getParcelableExtra(Constants.NEWS));
                String text;

                if (news.getUrl() != null && !news.getUrl().isEmpty()) {
                    text = news.getUrl();
                } else {
                    text = Constants.HN_BASE_URL + news.getId();
                }

                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                shareIntent.setType(Constants.MIME_TEXT_PLAIN);

                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share)));
            }
        });
    }

}
