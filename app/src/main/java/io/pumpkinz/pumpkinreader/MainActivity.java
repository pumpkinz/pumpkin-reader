package io.pumpkinz.pumpkinreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.data.NewsDetailAdapter;
import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.CommentParcel;


public class MainActivity extends PumpkinReaderActivity implements NewsListFragment.onNewsClickedListener {

    public static final String NEWS_DETAIL_BUNDLE = "io.pumpkinz.pumpkinreader.model.news_detail_bundle";
    public static final String COMMENTS_DATASET = "io.pumpkinz.pumpkinreader.model.coments_dataset";
    public static final String COMMENTS_SCROLLSTATE = "io.pumpkinz.pumpkinreader.model.comments_scrollstate";

    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Pumpkin", "Main onCreate");
        setUpToolbar();
        setUpSideNav();

        Log.d("Pumpkin", "Main savedInstanceState = " + (savedInstanceState == null));
        if (savedInstanceState != null) {
            news = Parcels.unwrap(savedInstanceState.getParcelable(Constants.NEWS));
            if (news != null) {
                Log.d("Pumpkin", "Main onCreate, news = " + news.toString());
            }
        }

        if (news != null) {
            if (findViewById(R.id.fragment_detail) == null) {
                Log.d("Pumpkin", "MainActivity land and ndf null");
                Intent intent = new Intent(this, NewsCommentsActivity.class);
                intent.putExtra(Constants.NEWS, savedInstanceState.getParcelable(Constants.NEWS));
                intent.putExtra(NEWS_DETAIL_BUNDLE, savedInstanceState);
                startActivity(intent);
            } else {
                Log.d("Pumpkin", "MainActivity land and ndf NOT null");
                NewsDetailFragment newsDetailFragment = (NewsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
                newsDetailFragment.loadComments(news);
            }
        }
        /*if (findViewById(R.id.fragment_container) != null) {
            Log.d("Pumpkin", "Main landscape cont");

            if (savedInstanceState != null) {
                Log.d("Pumpkin", "Main landscape savedinstancestate not NULL");
                return;
            }

            Log.d("Pumpkin", "Main landscape newslistfragment added");
            NewsListFragment nlf = new NewsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, nlf, NEWS_LIST_FRAGMENT_TAG).commit();
        }*/
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.sidenav_layout);
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Pumpkin", "MainActivity saveInstanceState, news = " + (news == null));
        if (news != null) {
            Log.d("Pumpkin", "MainActivity saveInstanceState, news = " + news.toString());
        }
        outState.putParcelable(Constants.NEWS, Parcels.wrap(news));

        if (findViewById(R.id.fragment_detail) != null) {
            Log.d("Pumpkin", "MainActivity saveInstanceState ndf NOT null");

            NewsDetailFragment newsDetailFragment = (NewsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
            NewsDetailAdapter nda = (NewsDetailAdapter) newsDetailFragment.getNewsDetail().getAdapter();
            if (nda != null && !nda.hasLoadingMore()) {
                outState.putParcelable(COMMENTS_DATASET, Parcels.wrap(CommentParcel.fromComments(nda.getDataSet())));
                outState.putParcelable(Constants.COMMENT, Parcels.wrap(CommentParcel.fromComments(nda.getComments())));
                outState.putParcelable(COMMENTS_SCROLLSTATE, newsDetailFragment.getNewsDetail().getLayoutManager().onSaveInstanceState());
            }
        }
    }

    @Override
    public void onNewsClicked(final News news) {
        this.news = news;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        Intent intent;

        boolean shouldOpenLink = pref.getBoolean(Constants.CONFIG_SHOW_LINK, true);

        if (pref.getBoolean(Constants.CONFIG_EXTERNAL_BROWSER, false)) {
            intent = new Intent(this, NewsCommentsActivity.class);
            boolean isURLValid = news.getUrl() != null && !news.getUrl().isEmpty();

            if (shouldOpenLink && isURLValid) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Uri uri = Uri.parse(news.getUrl());
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                }, 300);
            }
        } else {
            if (shouldOpenLink) {
                intent = new Intent(this, NewsDetailActivity.class);
            } else {
                intent = new Intent(this, NewsCommentsActivity.class);
            }
        }

        /*if (findViewById(R.id.fragment_container) == null) {
            NewsDetailFragment newsDetailFragment = (NewsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
            newsDetailFragment.loadComments(news);
        } else {
            startActivity(intent);
        }*/

        if (findViewById(R.id.fragment_detail) != null) {
            Log.d("Pumpkin", "MainActivity ndf NOT null");
            NewsDetailFragment newsDetailFragment = (NewsDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_detail);
            newsDetailFragment.loadComments(news);
        } else {
            Log.d("Pumpkin", "MainActivity ndf null");
            intent.putExtra(Constants.NEWS, Parcels.wrap(news));
            startActivity(intent);
        }
    }

    public void asu() {
        Log.d("Pumpkin", "MainActivity ASUUUU");
    }

    private void setUpToolbar() {
        Toolbar appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.top));
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }
    }

    private void setUpSideNav() {
        NavigationView sidenav = (NavigationView) findViewById(R.id.sidenav);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.sidenav_layout);
        final Handler handler = new Handler();

        sidenav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawer(GravityCompat.START);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onSideNavMenuSelected(menuItem);
                    }
                }, 250);

                return true;
            }
        });
    }

    private void onSideNavMenuSelected(MenuItem menuItem) {
        Intent intent = null;
        NewsListFragment fragment = (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);

        if (fragment == null) {
            //fragment = (NewsListFragment) getSupportFragmentManager().findFragmentByTag(NEWS_LIST_FRAGMENT_TAG);
        }

        fragment.forceUnsubscribe();
        switch (menuItem.getItemId()) {
            case R.id.sidenav_menu_top:
                getSupportActionBar().setTitle(menuItem.getTitle());
                fragment.setNewsType(R.string.top);
                fragment.forceRefresh();
                break;
            case R.id.sidenav_menu_new:
                getSupportActionBar().setTitle(menuItem.getTitle());
                fragment.setNewsType(R.string.recent);
                fragment.forceRefresh();
                break;
            case R.id.sidenav_menu_ask:
                getSupportActionBar().setTitle(menuItem.getTitle());
                fragment.setNewsType(R.string.ask_hn);
                fragment.forceRefresh();
                break;
            case R.id.sidenav_menu_show:
                getSupportActionBar().setTitle(menuItem.getTitle());
                fragment.setNewsType(R.string.show_hn);
                fragment.forceRefresh();
                break;
            case R.id.sidenav_menu_job:
                getSupportActionBar().setTitle(menuItem.getTitle());
                fragment.setNewsType(R.string.job);
                fragment.forceRefresh();
                break;
            case R.id.sidenav_menu_setting:
                intent = new Intent(this, SettingsActivity.class);
                break;
            case R.id.sidenav_menu_about:
                intent = new Intent(this, AboutActivity.class);
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

}
