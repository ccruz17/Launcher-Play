package cruga.team.clases;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.Preference;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cruga.team.launcher_play.MainActivity;

public class Tools {
    public static ArrayList<App> obtenerApps(PackageManager pm) {

        Intent mainIntent = new Intent(Intent.ACTION_MAIN,null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pacsList = pm.queryIntentActivities(mainIntent, 0);
        ArrayList<App> apps = new ArrayList<App>();

        for(int i=0;i<pacsList.size();i++){
            App appMomento = new App();
            appMomento.name = pacsList.get(i).activityInfo.name;
            appMomento.packageName = pacsList.get(i).activityInfo.packageName;
            appMomento.activity = pacsList.get(i).activityInfo.name;
            appMomento.label = pacsList.get(i).loadLabel(pm).toString();
            appMomento.icono = pacsList.get(i).loadIcon(pm);
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
        apps = obtenerApps(act.getPackageManager());

        ArrayList<App> tmpApps = new ArrayList<App>();
        if(getSharePref(act, MainActivity.PREF_CUSTOM_APPS).compareTo("") == 0){//No tiene preferencias de Apps favoritas
            //PRELOAD BASIC APPS
            if(getSharePref(act, MainActivity.PREF_MAX_APPS).compareTo("") == 0){//No tiene preferencia de max app
                //PRELOAD BASIC APPS
                for(int i=0;i<apps.size();i++){

                    if(apps.get(i).packageName.toLowerCase().contains("dialer")) {
                        tmpApps.add(apps.get(i));
                    } else if(apps.get(i).packageName.toLowerCase().contains("contact")) {
                        tmpApps.add(apps.get(i));
                    } else if(apps.get(i).packageName.toLowerCase().contains("mms")) {
                        tmpApps.add(apps.get(i));
                    } else if(apps.get(i).packageName.toLowerCase().contains("calendar")) {
                        tmpApps.add(apps.get(i));
                    } else if(apps.get(i).packageName.toLowerCase().contains("chrome")) {
                        tmpApps.add(apps.get(i));
                    } else if(apps.get(i).packageName.toLowerCase().contains("camera")) {
                        tmpApps.add(apps.get(i));
                    } else if(apps.get(i).packageName.toLowerCase().contains("calculator")) {
                        tmpApps.add(apps.get(i));
                    } else if(apps.get(i).packageName.toLowerCase().contains("mail")) {
                        tmpApps.add(apps.get(i));
                    } else if(apps.get(i).packageName.toLowerCase().contains("vending")) {
                        tmpApps.add(apps.get(i));
                    }else if(apps.get(i).packageName.toLowerCase().contains("conversations")) {
                        tmpApps.add(apps.get(i));
                    }else if(apps.get(i).packageName.toLowerCase().contains("music")) {
                        tmpApps.add(apps.get(i));
                    }else if(apps.get(i).packageName.toLowerCase().contains("whatsapp")) {
                        tmpApps.add(apps.get(i));
                    }
                }
            } else {

            }
        } else {

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

        SharedPreferences prefs =  act.getSharedPreferences("LINKBUNCKS_CCG", act.MODE_PRIVATE);
        String pref = prefs.getString(name, "");
        return pref;
    }

}
