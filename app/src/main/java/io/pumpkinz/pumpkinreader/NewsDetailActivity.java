package io.pumpkinz.pumpkinreader;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.data.NewsDetailViewPagerAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.ActionUtil;


public class NewsDetailActivity extends PumpkinReaderActivity {

    private News news;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private WebView webView;
    private NewsDetailViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_detail);
        setUpToolbar();

        news = Parcels.unwrap(getIntent().getParcelableExtra(Constants.NEWS));

        viewPager = (ViewPager) findViewById(R.id.news_detail_pager);
        setUpViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.news_detail_tab);
        setUpTab(viewPager);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isCommentFirst = !pref.getBoolean(Constants.CONFIG_SHOW_LINK, false);

        if (isCommentFirst) {
            viewPager.setCurrentItem(Constants.TAB_COMMENTVIEW);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_open:
                ActionUtil.open(this, news);
                return true;
            case R.id.action_save:
                ActionUtil.save(this, news);
                return true;
            case R.id.action_share:
                ActionUtil.share(this, news);
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
    }

    private void setUpToolbar() {
        Toolbar appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setUpTab(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);

        if (news.getUrl() == null || news.getUrl().isEmpty()) {
            tabLayout.getTabAt(NewsDetailViewPagerAdapter.TAB_COMMENTS_IDX).select();
        }
    }

    private WebView getWebView() {
        if (webView == null) {
            WebViewFragment webViewFragment = (WebViewFragment) pagerAdapter.instantiateItem(viewPager, viewPager.getCurrentItem());
            webView = (WebView) webViewFragment.getView().findViewById(R.id.news_web_view);
        }

        return webView;
    }

}
