package cruga.team.listeners;


import android.annotation.TargetApi;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cruga.team.launcher_play.MainActivity;


/**
 * Created by christian on 6/07/17.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)

public class MyNotificationListenerService extends NotificationListenerService {
    public static int notificationCount = 0;
    public static ArrayList<String> names = new ArrayList<String>();
    public static ArrayList<Drawable> icons = new ArrayList<Drawable>();
    public static String TITLE = null;

    @TargetApi(20)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn, NotificationListenerService.RankingMap rankingMap) {
        TITLE = sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE).toString();

        Application app = getApplication();

        Toast t = Toast.makeText(getApplicationContext(), "GROUP KEY" + sbn.getPostTime(), Toast.LENGTH_LONG);
        t.show();
        t.makeText(getApplicationContext(), "FROM: " + sbn.getNotification().contentIntent.getCreatorPackage(), Toast.LENGTH_SHORT);
        t.show();

        if(sbn.getTag() == null && TITLE != null){
            notificationCount = sbn.getNotification().number;
            notificationCount++;

            t.makeText(getApplicationContext(), "A: " + notificationCount, Toast.LENGTH_SHORT);
            t.show();

        }else if ((sbn.getTag() != null) &&
                //Utilities.isAllowNotificationCountPrefEnabled(getApplicationContext())
                true // esta la preferencia establecida para mostrar noficicaciones
                ) {

            notificationCount = sbn.getNotification().number;
            notificationCount++;

            t.makeText(getApplicationContext(), "B: " + notificationCount, Toast.LENGTH_SHORT);
            t.show();

            //for cada app revisar los componentes
            /*
            for (final AppInfo app : AllAppsList.data) {
                if (sbn.getPackageName().equals(app.getTargetComponent().getPackageName())) {
                    if(getNotificationCount(getApplicationContext(), app.getTargetComponent().getPackageName()) != -1){
                        int notificationCountPrev = getNotificationCount(getApplicationContext(), app.getTargetComponent().getPackageName());
                        notificationCountPrev++;
                        setNotificationCount(getApplicationContext(), app.getTargetComponent().getPackageName(), notificationCountPrev);
                    }else {
                        setNotificationCount(getApplicationContext(), app.getTargetComponent().getPackageName(), notificationCount);
                    }
                    Launcher.getLauncherAppState().reloadWorkspace();
                }
            }*/
        }
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn){

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn){

    }
    /*
    public static void setNotificationCount(Context context, String packageName, int number){
        getPrefs(context).edit().putInt("NOTIFICATION" + packageName, number).apply();
    }

    public static int getNotificationCount(Context context, String packageName){
        return getPrefs(context).getInt("NOTIFICATION" + packageName, -1);
    }
    */
}