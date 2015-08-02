package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.News;


public class NewsCommentsActivity extends PumpkinReaderActivity {

    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_news_comments);
        setUpToolbar();

        news = Parcels.unwrap(getIntent().getParcelableExtra(Constants.NEWS));

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

    private void setUpToolbar() {
        Toolbar appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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

}
