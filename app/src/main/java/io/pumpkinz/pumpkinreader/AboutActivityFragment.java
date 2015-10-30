package io.pumpkinz.pumpkinreader;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import io.pumpkinz.pumpkinreader.etc.Constants;


public class AboutActivityFragment extends Fragment {

    public AboutActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button shareBtn = (Button) view.findViewById(R.id.about_share);
        Button rateBtn = (Button) view.findViewById(R.id.about_rate);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareApp();
            }
        });

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rateApp();
            }
        });
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "Check this awesome HN app!");
        intent.putExtra(Intent.EXTRA_TEXT, "Pumpkin Reader for Hacker News:\n\nhttps://play.google.com/store/apps/details?id=io.pumpkinz.pumpkinreader");
        intent.setType(Constants.MIME_TEXT_PLAIN);

        getActivity().startActivity(Intent.createChooser(intent, getActivity().getResources().getString(R.string.share)));
    }

    private void rateApp() {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=io.pumpkinz.pumpkinreader");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        getActivity().startActivity(intent);
    }

}
