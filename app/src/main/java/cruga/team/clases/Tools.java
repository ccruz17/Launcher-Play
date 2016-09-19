package cruga.team.clases;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Icon;
import android.util.Log;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cruga.team.launcher_play.MainActivity;
import cruga.team.clases.IconPackManager;
import cruga.team.clases.IconPackManager.IconPack;

public class Tools {
    public static ArrayList<App> obtenerApps(Context mContext) {
        PackageManager pm = mContext.getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pacsList = pm.queryIntentActivities(mainIntent, 0);
        ArrayList<App> apps = new ArrayList<App>();

        IconPackManager iP = new IconPackManager();
        iP.setContext(mContext);
        HashMap<String, IconPack> map = iP.getAvailableIconPacks(true);
        String theme = "mundoiux.cruga.iux_icon_pack";
        if(map.size()>0) {
            map.get(theme).load();
        }

        for(int i=0;i<pacsList.size();i++){
            App appMomento = new App();
            appMomento.name = pacsList.get(i).activityInfo.name;
            appMomento.packageName = pacsList.get(i).activityInfo.packageName;
            appMomento.activity = pacsList.get(i).activityInfo.name;
            appMomento.label = pacsList.get(i).loadLabel(pm).toString();
            boolean existTheme = false;
            if(existTheme) {

                if(map.size()>0) {
                   appMomento.icono = map.get(theme).getDrawableIconForPackage(pacsList.get(i).activityInfo.packageName, pacsList.get(i).loadIcon(pm));
                }else {
                    appMomento.icono = pacsList.get(i).loadIcon(pm);
                }
            } else {
                appMomento.icono = pacsList.get(i).loadIcon(pm);
            }

            appMomento.idx = i;
            apps.add(appMomento);
        }

        apps = SortApps.ordenar_alfabeticamente(apps);

        for(int i=0;i<apps.size();i++){

            App appMomento = apps.get(i);
            appMomento.idx = i;
            apps.set(i, appMomento);
        }
        return apps;
    }

    public static ArrayList<App> obtenerCustomApps(Activity act) {

        ArrayList<App> apps = new ArrayList<App>();
        apps = obtenerApps(act.getApplicationContext());
        ArrayList<App> tmpApps = new ArrayList<App>();

        Set<String> mySet = getSharePrefset(act, MainActivity.PREF_CUSTOM_APPS);
        if(mySet != null) {
            Iterator iter = mySet.iterator();

            while (iter.hasNext()) {
                Object s = iter.next();

                for(int i=0;i<apps.size();i++){
                    if(apps.get(i).activity.compareTo(s.toString()) == 0) {
                        tmpApps.add(apps.get(i));
                    }
                }
            }
        } else {
            //PRELOAD BASIC APPS
            mySet = new HashSet<>();
            for(int i=0;i<apps.size();i++){

                if(apps.get(i).packageName.toLowerCase().contains("dialer")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                } else if(apps.get(i).packageName.toLowerCase().contains("contact")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                } else if(apps.get(i).packageName.toLowerCase().contains("mms")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                } else if(apps.get(i).packageName.toLowerCase().contains("calendar")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                } else if(apps.get(i).packageName.toLowerCase().contains("chrome")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                } else if(apps.get(i).packageName.toLowerCase().contains("camera")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                } else if(apps.get(i).packageName.toLowerCase().contains("calculator")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                } else if(apps.get(i).packageName.toLowerCase().contains("mail")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                } else if(apps.get(i).packageName.toLowerCase().contains("vending")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                }else if(apps.get(i).packageName.toLowerCase().contains("conversations")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                }else if(apps.get(i).packageName.toLowerCase().contains("music")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                }else if(apps.get(i).packageName.toLowerCase().contains("whatsapp")) {
                    tmpApps.add(apps.get(i));
                    mySet.add(apps.get(i).activity);
                }
                Tools.setSharePref(act, MainActivity.PREF_CUSTOM_APPS, mySet);
            }
        }

        tmpApps = SortApps.ordenar_alfabeticamente(tmpApps);

        for(int i=0;i<tmpApps.size();i++){

            App appMomento = tmpApps.get(i);
            appMomento.idx = i;
            tmpApps.set(i, appMomento);
        }

        return tmpApps;
    }

    public static String getSharePref(Activity act, String name) {
        SharedPreferences prefs =  act.getSharedPreferences(MainActivity.PREFERENCE_KEY, act.MODE_PRIVATE);
        String pref = prefs.getString(name, "");
        return pref;
    }

    public static void setSharePref(Activity act, String name, String val) {
        SharedPreferences prefs =  act.getSharedPreferences(MainActivity.PREFERENCE_KEY, act.MODE_PRIVATE);
        prefs.edit().putString(name, val).commit();
    }

    public static Set<String> getSharePrefset(Activity act, String name) {
        SharedPreferences prefs =  act.getSharedPreferences(MainActivity.PREFERENCE_KEY, act.MODE_PRIVATE);
        Set<String> pref = prefs.getStringSet(name, null);
        return pref;
    }

    public static void setSharePref(Activity act, String name, Set<String> val) {
        SharedPreferences prefs =  act.getSharedPreferences(MainActivity.PREFERENCE_KEY, act.MODE_PRIVATE);
        prefs.edit().putStringSet(name, val).commit();
    }

    public IntentFilter filterApps()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        return filter;
    }

}
