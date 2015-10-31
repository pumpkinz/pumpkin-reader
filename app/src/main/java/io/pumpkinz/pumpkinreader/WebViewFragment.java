package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.etc.Constants;
import io.pumpkinz.pumpkinreader.model.News;


public class WebViewFragment extends Fragment {

    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        News news = Parcels.unwrap(getActivity().getIntent().getParcelableExtra(Constants.NEWS));
        webView = (WebView) view.findViewById(R.id.news_web_view);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.web_view_progress);
        progressBar.setVisibility(View.VISIBLE);

        final SwipeRefreshLayout webViewRefresh = (SwipeRefreshLayout) view.findViewById(R.id.web_view_refresh);
        webViewRefresh.setColorSchemeResources(R.color.pumpkin_accent);
        webViewRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
            }
        });

        final int bgColor = getActivity().getResources().getColor(R.color.grey_1000w);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);

                if (progress >= 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                    webView.setBackgroundColor(bgColor);
                }

                if (progress >= 30 && webViewRefresh.isRefreshing()) {
                    webViewRefresh.setRefreshing(false);
                }
            }
        });

        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);

        webView.setBackgroundColor(0);

        if (savedInstanceState == null) {
            webView.loadUrl(news.getUrl());
        } else {
            webView.restoreState(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

}
