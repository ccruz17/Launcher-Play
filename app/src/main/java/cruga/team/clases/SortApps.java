package cruga.team.clases;

import java.text.Normalizer;
import java.util.ArrayList;

public class SortApps
{
    public static ArrayList<App> ordenar_alfabeticamente(ArrayList<App> apps)
    {
        int i, j;
        App temp;

        for (i = 0; i < apps.size()-1; i++)
        {
            for (j= i+1; j<apps.size(); j++)
            {
                App appi = apps.get(i);
                App appj = apps.get(j);

                String ap1 = Normalizer.normalize(appi.label, Normalizer.Form.NFD);
                ap1 = ap1.replaceAll("[^\\p{ASCII}]", "");
                String ap2 = Normalizer.normalize(appj.label, Normalizer.Form.NFD);
                ap2 = ap2.replaceAll("[^\\p{ASCII}]", "");

                if (ap1.compareToIgnoreCase(ap2)> 0)
                {                                             // ascending sort
                    temp = apps.get(i);
                    apps.set(i, apps.get( j ));    // swapping
                    apps.set( j ,temp);
                }
            }
        }
        return apps;
    }

    public void recientes(App[] apps)
    {

    }
}
