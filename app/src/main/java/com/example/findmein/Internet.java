package com.example.findmein;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Internet extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        LiveConnectivityManager.singleton(context).notifyConnectionChange();
    }
}
