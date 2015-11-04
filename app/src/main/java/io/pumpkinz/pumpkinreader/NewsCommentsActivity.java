package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.ActionUtil;


public class NewsCommentsActivity extends PumpkinReaderActivity {

    private News news;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_comments);
        setUpToolbar();

        news = Parcels.unwrap(getIntent().getParcelableExtra(Constants.NEWS));
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
        setSupportActionBar(appBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}
