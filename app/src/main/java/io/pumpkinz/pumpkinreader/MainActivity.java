package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.util.Log;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

import io.pumpkinz.pumpkinreader.model.HnApiMgr;
import io.pumpkinz.pumpkinreader.model.Story;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        setListener();
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

    private void setListener() {
        HnApiMgr.api().listTopStories(new Callback<List<Integer>>() {
            @Override
            public void success(List<Integer> integers, Response response) {
                Log.d("topstories", integers.toString());
                Log.d("topstories", response.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("top error", error.toString());
            }
        });

        HnApiMgr.api().getItem(8863, new Callback<Story>() {
            @Override
            public void success(Story story, Response response) {
                Log.d("story", story.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("story", error.toString());
            }
        });
    }

    private void setUpToolbar() {
        Toolbar appBar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(appBar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_18dp);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
