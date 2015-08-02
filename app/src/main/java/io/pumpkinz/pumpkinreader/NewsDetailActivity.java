package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.data.NewsDetailViewPagerAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.News;


public class NewsDetailActivity extends PumpkinReaderActivity {

    public static final int TAB_LINK_IDX = 0;
    public static final int TAB_COMMENTS_IDX = 1;

    private News news;
    private WebView webView;
    private TabLayout tabLayout;
    private NewsDetailViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);

        news = Parcels.unwrap(getIntent().getParcelableExtra(Constants.NEWS));

        ViewPager viewPager = (ViewPager) findViewById(R.id.news_detail_pager);
        setUpViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.news_detail_tab);
        setUpTab(viewPager);

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

    @Override
    public void onBackPressed() {
        if (tabLayout.getSelectedTabPosition() != TAB_LINK_IDX) {
            super.onBackPressed();
            return;
        }

        if (getWebView().canGoBack()) {
            getWebView().goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void setUpViewPager(ViewPager viewPager) {
        pagerAdapter = new NewsDetailViewPagerAdapter(getSupportFragmentManager());

        pagerAdapter.addFragment(getResources().getString(R.string.link), new WebViewFragment());
        pagerAdapter.addFragment(getResources().getString(R.string.comments), new NewsDetailFragment());

        viewPager.setAdapter(pagerAdapter);
    }

    private void setUpTab(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);

        if (news.getUrl() == null || news.getUrl().isEmpty()) {
            tabLayout.getTabAt(TAB_COMMENTS_IDX).select();
        }
    }

    private void setUpFAB(FloatingActionButton fab) {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, news.getTitle());

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

    private WebView getWebView() {
        if (webView == null) {
            webView = (WebView) pagerAdapter.getItem(TAB_LINK_IDX).getView().findViewById(R.id.news_web_view);
        }

        return webView;
    }

}
