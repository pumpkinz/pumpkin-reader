package io.pumpkinz.pumpkinreader.etc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.parceler.Parcels;

import io.pumpkinz.pumpkinreader.model.News;
import io.pumpkinz.pumpkinreader.util.ActionUtil;


public class ShareBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        News news = Parcels.unwrap(intent.getParcelableExtra(Constants.NEWS));

        Intent chooser = Intent.createChooser(ActionUtil.getPumpkinShareIntent(context, news), "Share");
        chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(chooser);
    }

}