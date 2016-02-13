package io.pumpkinz.pumpkinreader;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.etc.PumpkinCustomTab;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.ActionUtil;
import io.pumpkinz.pumpkinreader.util.PreferencesUtil;

public class NewsCommentsActivity extends PumpkinReaderActivity
    implements NewsDetailFragment.NewsListener {

    private News news;
    private Menu menu;
    private PumpkinCustomTab pumpkinCustomTab;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_comments);
        setUpToolbar();

        Uri data = getIntent().getData();
        if (data != null) {
            String id = data.getQueryParameter("id");
            news = new News(Integer.valueOf(id));
            PreferencesUtil.markNewsAsRead(this, news);
        } else {
            news = Parcels.unwrap(getIntent().getParcelableExtra(Constants.NEWS));
        }

        if (news.isUrlValid()) {
            pumpkinCustomTab = new PumpkinCustomTab(this, news);
            openLinkWhenAppropriate();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        this.menu = menu;
        ActionUtil.toggleSaveAction(this, menu, news);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_open:
                if (pumpkinCustomTab == null) return true;
                pumpkinCustomTab.openPage();
                return true;
            case R.id.action_save:
                ActionUtil.save(this, menu, news);
                return true;
            case R.id.action_share:
                ActionUtil.share(this, news);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (pumpkinCustomTab == null) return;
        pumpkinCustomTab.unbindCustomTabService();
        unbindService(pumpkinCustomTab);
    }

    private void setUpToolbar() {
        Toolbar appBar = (Toolbar) findViewById(R.id.app_bar);
        appBar.setTitle(R.string.comments);

        setSupportActionBar(appBar);
        setScrollFlag((AppBarLayout.LayoutParams) appBar.getLayoutParams());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void openLinkWhenAppropriate() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean shouldOpenLink = pref.getBoolean(Constants.CONFIG_SHOW_LINK, true);
        if (shouldOpenLink) {
            pumpkinCustomTab.openPage();
        }
    }

    @Override public void onNewsLoaded(News news) {
        this.news = news;
        if (this.news.isUrlValid() && pumpkinCustomTab == null) {
            pumpkinCustomTab = new PumpkinCustomTab(this, news);
        }
    }
}
