package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

    private News news;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private WebView webView;
    private NewsDetailViewPagerAdapter pagerAdapter;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);

        news = Parcels.unwrap(getIntent().getParcelableExtra(Constants.NEWS));

        viewPager = (ViewPager) findViewById(R.id.news_detail_pager);
        setUpViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.news_detail_tab);
        setUpTab(viewPager);

        fab = (FloatingActionButton) findViewById(R.id.news_detail_fab);
        setUpFAB(fab);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isCommentFirst = !pref.getBoolean(Constants.CONFIG_SHOW_LINK, false);

        if (isCommentFirst) {
            viewPager.setCurrentItem(Constants.TAB_COMMENTVIEW);
        }
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
        if (tabLayout.getSelectedTabPosition() != NewsDetailViewPagerAdapter.TAB_LINK_IDX) {
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
        pagerAdapter = new NewsDetailViewPagerAdapter(getSupportFragmentManager(), getResources());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                fab.show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void setUpTab(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);

        if (news.getUrl() == null || news.getUrl().isEmpty()) {
            tabLayout.getTabAt(NewsDetailViewPagerAdapter.TAB_COMMENTS_IDX).select();
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
            WebViewFragment webViewFragment = (WebViewFragment) pagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
            webView = (WebView) webViewFragment.getView().findViewById(R.id.news_web_view);
        }

        return webView;
    }

}
