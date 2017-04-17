package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;


public class MainActivity extends PumpkinReaderActivity {

    private String checkedMenuItemTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpSideNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpToolbar();
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
        setScrollFlag((AppBarLayout.LayoutParams) appBar.getLayoutParams());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(checkedMenuItemTitle);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    private void setUpSideNav() {
        NavigationView sidenav = (NavigationView) findViewById(R.id.sidenav);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.sidenav_layout);
        final Handler handler = new Handler();

        if (checkedMenuItemTitle.isEmpty()) { //default to Top News
            checkedMenuItemTitle = getResources().getString(R.string.top);
        }

        sidenav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.sidenav_menu_setting:
                    case R.id.sidenav_menu_feedback:
                    case R.id.sidenav_menu_about:
                        break;
                    default:
                        menuItem.setChecked(true);
                        checkedMenuItemTitle = menuItem.getTitle().toString();
                }

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

        Log.v("ASD", "asdasdsad");
    }

    private void onSideNavMenuSelected(MenuItem menuItem) {
        Intent intent = null;
        NewsListFragment fragment = (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_main);

        fragment.forceUnsubscribe();

        switch (menuItem.getItemId()) {
            case R.id.sidenav_menu_saved:
                getSupportActionBar().setTitle(menuItem.getTitle());
                fragment.setNewsType(R.string.saved);
                fragment.forceRefresh();
                break;
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
            case R.id.sidenav_menu_feedback:
                intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "pumpkinz.dev@gmail.com", null));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"pumpkinz.dev@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Pumpkin Reader Feedback");
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
