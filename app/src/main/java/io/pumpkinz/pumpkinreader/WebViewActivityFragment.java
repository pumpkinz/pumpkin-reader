package io.pumpkinz.pumpkinreader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import io.pumpkinz.pumpkinreader.etc.Constants;


public class WebViewActivityFragment extends Fragment {

    public WebViewActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String link = getActivity().getIntent().getStringExtra(Constants.LINK);
        WebView webView = (WebView) view.findViewById(R.id.news_web_view);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.web_view_progress);
        progressBar.setVisibility(View.VISIBLE);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(progress);

                if (progress >= 100) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(link);
    }

}
