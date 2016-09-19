package cruga.team.listeners;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by christian on 9/08/16.
 */
public class AddAppBroadCastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("EVENT", "onReciver");
        //update apps and preferences for apps
    }
}