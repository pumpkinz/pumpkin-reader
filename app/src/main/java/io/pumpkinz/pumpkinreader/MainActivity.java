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

import io.pumpkinz.pumpkinreader.rest.RestClient;
import io.pumpkinz.pumpkinreader.model.ItemPOJO;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    private void setListener() {
        RestClient.service().listTopStories()
                .flatMap(new Func1<List<Integer>, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(List<Integer> integers) {
                        return Observable.from(integers);
                    }
                })
                .take(10)
                .flatMap(new Func1<Integer, Observable<ItemPOJO>>() {
                    @Override
                    public Observable<ItemPOJO> call(Integer integer) {
                        return RestClient.service().getItem(integer);
                    }
                })
                .subscribe(new Action1<ItemPOJO>() {
                    @Override
                    public void call(ItemPOJO item) {
                        Log.d("topstory id", item.toString());
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
