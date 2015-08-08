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


public class MainActivity extends PumpkinReaderActivity implements NewsListFragment.OnNewsSelectedListener {
    private NewsDetailFragment newsDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            Log.d("MainActivity", "fragment_container NOT null");
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            newsDetailFragment = new NewsDetailFragment();

            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            newsDetailFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, newsDetailFragment).commit();
        } else {
            Log.d("MainActivity", "fragment_container NULL");
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

    @Override
    public void onNewsSelected(News news) {
        Log.d("MainActivity", "onNewsSelected, load comment");

        if (newsDetailFragment != null) {
            Log.d("MainActivity", "newsDetailFragment NOT null");
            newsDetailFragment.loadComments(news);
        } else {
            newsDetailFragment = (NewsDetailFragment)
                    getSupportFragmentManager().findFragmentById(R.id.news_detail);
            Log.d("MainActivity", "newsDetailFragment null");
        }
    }
}
