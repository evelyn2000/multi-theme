package com.jzs.android.systemstyle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jzs.android.systemstyle.activities.ListSupportAppsActivity;

public class ThemeLauncherReceiver extends BroadcastReceiver {

    private final Uri mEmUri = Uri.parse("android_secret_code://35947537");
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(
                android.provider.Telephony.Intents.SECRET_CODE_ACTION)) {
            Uri uri = intent.getData();
            if (uri.equals(mEmUri)) {
                Intent intentEm = new Intent(context, ListSupportAppsActivity.class);
                intentEm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentEm);
            }
        }
    }
}
