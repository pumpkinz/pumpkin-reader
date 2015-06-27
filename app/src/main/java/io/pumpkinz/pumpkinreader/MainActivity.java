package io.pumpkinz.pumpkinreader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import io.pumpkinz.pumpkinreader.model.HnApiMgr;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setListener() {
        HnApiMgr.getApi().listTopStories(new Callback<List<Integer>>() {
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

    }
}
