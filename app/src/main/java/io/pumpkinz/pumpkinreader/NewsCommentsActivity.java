package io.pumpkinz.pumpkinreader;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.ActionUtil;
import io.pumpkinz.pumpkinreader.util.PreferencesUtil;


public class NewsCommentsActivity extends PumpkinReaderActivity {

    private News news;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
        this.menu = menu;
        ActionUtil.toggleSaveAction(this, menu, news);

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
                ActionUtil.save(this, menu, news);
                return true;
            case R.id.action_share:
                ActionUtil.share(this, news);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        Toolbar appBar = (Toolbar) findViewById(R.id.app_bar);
        appBar.setTitle(R.string.title_activity_news_detail);

        setSupportActionBar(appBar);
        setScrollFlag((AppBarLayout.LayoutParams) appBar.getLayoutParams());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
