package com.example.findmein;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class LiveConnectivityManager extends Activity implements ConnectivityObserver
{
    private static List<ConnectivityObserver> mObservers;
    private boolean mConnected;
    private final Context mContext;
            /** Unique manager in application */
    private static LiveConnectivityManager mManager;
    /**
     * Implements singleton pattern

     * @param context Execution context

     * @return Connectivity Manager

     */
    public static LiveConnectivityManager singleton(Context context)
    {
        if (mManager == null)
        {
            mManager = new LiveConnectivityManager(context);
        }
        return mManager;
    }
            /** Class constructor */

    private LiveConnectivityManager(Context context)
    {
        mObservers = new ArrayList<ConnectivityObserver>();
        mContext = context;
        mConnected = isConnectionEnabled();
    }
            /** Test if connection is enabled */
    boolean isConnectionEnabled()
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        catch (Exception e)
        {
            return false;
        }
    }
            /** Adds observer to observers list */

    public void addObserver(ConnectivityObserver observer)
    {
        mObservers.add(observer);
        observer.manageNotification(mConnected);
    }
            /** Remove observer form observers list */
    public void removeObserver(ConnectivityObserver observer)
    {
        mObservers.remove(observer);
    }

            /** Called when there's a connectivity change. */

    void notifyConnectionChange() {
        mConnected = isConnectionEnabled();
        for (ConnectivityObserver observer : mObservers)
        {
            observer.manageNotification(mConnected);
        }
    }

    @Override
    public void manageNotification(boolean connectionEnabled)
    {

    }
}
