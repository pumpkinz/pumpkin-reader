package io.pumpkinz.pumpkinreader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.pumpkinz.pumpkinreader.data.NewsAdapter;
import io.pumpkinz.pumpkinreader.etc.DividerItemDecoration;
import io.pumpkinz.pumpkinreader.model.ItemPOJO;
import io.pumpkinz.pumpkinreader.service.RestClient;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.subscriptions.Subscriptions;


/**
 * Using RetainedFragmentActivity sample on
 * https://github.com/ReactiveX/RxAndroid/blob/0.x/sample-app/src/main/java/rx/android/samples/RetainedFragmentActivity.java
 */
public class NewsListFragment extends Fragment {

    private RecyclerView newsList;
    private NewsAdapter newsAdapter;
    private List<ItemPOJO> dataset;
    private Observable<List<ItemPOJO>> stories;
    private Subscription subscription = Subscriptions.empty();

    public NewsListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO: change magic number 20 to a constant
        stories = AppObservable.bindFragment(this, RestClient.service().getTopItems(20).cache());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO: show loading bar
        return inflater.inflate(R.layout.fragment_news_list, container, false);
    }

    @Override
    public void onDestroyView() {
        subscription.unsubscribe();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsList = (RecyclerView) view.findViewById(R.id.news_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        newsList.setLayoutManager(layoutManager);

        dataset = new ArrayList<>();
        newsAdapter = new NewsAdapter(dataset);
        newsList.setAdapter(newsAdapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        this.newsList.addItemDecoration(itemDecoration);

        subscription = stories.subscribe(new Subscriber<List<ItemPOJO>>() {
            @Override
            public void onCompleted() {
                newsAdapter.notifyDataSetChanged();
                //TODO: hide loading bar
                Log.d("stories", "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("stories err", e.toString());
            }

            @Override
            public void onNext(List<ItemPOJO> itemPOJOs) {
                Log.d("stories", String.valueOf(itemPOJOs.size()));
                dataset.addAll(itemPOJOs);
            }
        });
    }

}
