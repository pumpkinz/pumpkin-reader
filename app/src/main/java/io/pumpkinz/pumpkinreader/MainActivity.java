package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
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
        switch (menuItem.getItemId()) {
            case R.id.sidenav_menu_top:
                // TODO: Display top news
                getSupportActionBar().setTitle(menuItem.getTitle());
                break;
            case R.id.sidenav_menu_new:
                // TODO: Display recent news
                getSupportActionBar().setTitle(menuItem.getTitle());
                break;
            case R.id.sidenav_menu_ask:
                // TODO: Display ask HN
                getSupportActionBar().setTitle(menuItem.getTitle());
                break;
            case R.id.sidenav_menu_show:
                // TODO: Display show HN
                getSupportActionBar().setTitle(menuItem.getTitle());
                break;
            case R.id.sidenav_menu_job:
                // TODO: Display HN jobs
                getSupportActionBar().setTitle(menuItem.getTitle());
                break;
            case R.id.sidenav_menu_about:
                goToAbout();
                break;
        }
    }

    private void goToAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

}
