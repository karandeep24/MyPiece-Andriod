package com.mi.asytask;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by mind on 10/9/15.
 */
public class LogoutBroadCastReceiver extends BroadcastReceiver {

    Activity mCurrentActivity;

    public LogoutBroadCastReceiver(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getExtras() != null)
        {
            Bundle bundle = intent.getExtras();
            if (bundle.getBoolean("islogout"))
            {
                mCurrentActivity.finish();
            }
        }
    }

    public void registerLogoutTask()
    {
        LocalBroadcastManager.getInstance(mCurrentActivity).registerReceiver(LogoutBroadCastReceiver.this, new IntentFilter("finishallact"));
    }

    public void unRegisterLogoutTask()
    {
        LocalBroadcastManager.getInstance(mCurrentActivity).unregisterReceiver(LogoutBroadCastReceiver.this);
    }

}
