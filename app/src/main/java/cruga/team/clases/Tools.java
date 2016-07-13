package cruga.team.clases;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.ArrayList;
import java.util.List;

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
            appMomento.label = pacsList.get(i).loadLabel(pm).toString();
            appMomento.icono = pacsList.get(i).loadIcon(pm);
            appMomento.id_rcs_icon = pacsList.get(i).icon;
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
}
