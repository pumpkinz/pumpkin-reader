package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.Util;


public class MainActivity extends PumpkinReaderActivity implements NewsListFragment.OnNewsSelectedListener {

    private static final String NEWS_DETAIL_TAG = "io.pumpkinz.pumpkinreader.news_detail_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Pumpkin", "MainActivity: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            Log.d("Pumpkin", "MainActivity: fragment_container NOT null");

            if (savedInstanceState == null) {
                Log.d("Pumpkin", "MainActivity: add newsDetailFragment");
                NewsDetailFragment newsDetailFragment = new NewsDetailFragment();
                newsDetailFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, newsDetailFragment).commit();
            } else {
                Log.d("Pumpkin", "MainActivity: NOT add newsDetailFragment");
            }
        } else {
            Log.d("Pumpkin", "MainActivity: fragment_container null");
        }

        setUpToolbar();
        setUpSideNav();
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
    public void onNewsSelected(News news) {
        Log.d("Pumpkin", "MainActivity " + Util.printFragment(getSupportFragmentManager().getFragments()));

        /*if (newsDetailFragment == null) {
            Log.d("Pumpkin", "MainActivity: newsDetailFragment null");
            NewsListFragment newsListFragment = (NewsListFragment)
                    getSupportFragmentManager().findFragmentById(R.id.fragment_main);
            newsListFragment.goToNewsDetail(news);
        } else {
            Log.d("Pumpkin", "MainActivity: newsDetailFragment NOT null");
            newsDetailFragment.updateNewsDetail(news);
        }*/
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
