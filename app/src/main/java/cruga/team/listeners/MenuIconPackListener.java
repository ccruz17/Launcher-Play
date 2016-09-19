package cruga.team.listeners;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cruga.team.adapters.IconPackAdapter;
import cruga.team.clases.App;
import cruga.team.clases.IconPackManager;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;
import cruga.team.libs.ResideMenu;

/**
 * Created by christian on 14/09/16.
 */
public class MenuIconPackListener implements View.OnClickListener {

    Context mContext;
    ResideMenu resideMenu;
    public MenuIconPackListener(Context mContext, ResideMenu resideMenu) {
        this.mContext = mContext;
        this.resideMenu = resideMenu;
    }

    @Override
    public void onClick(View v) {

        ArrayList<App> themesInstalled = new ArrayList<>(0);


        IconPackManager iP = new IconPackManager();
        iP.setContext(mContext);
        //Get Themes
        HashMap<String, IconPackManager.IconPack> map = iP.getAvailableIconPacks(true);
        //Iter map and add to list
        for (HashMap.Entry<String, IconPackManager.IconPack> entry : map.entrySet()) {

            IconPackManager.IconPack iconPack = entry.getValue();

            App theme = new App();
            theme.packageName = iconPack.packageName;
            theme.icono = iconPack.iconForTheme;
            themesInstalled.add(theme);

        }

        String theme = "mundoiux.cruga.iux_icon_pack";
        if(map.size()>0) {

            IconPackManager.IconPack iconPack = map.get(theme);

        }

        IconPackAdapter iconAdapter = new IconPackAdapter(mContext, themesInstalled);


        DialogPlus dialog = DialogPlus.newDialog(mContext)
                .setAdapter(iconAdapter)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                    }
                })
                .setFooter(R.layout.footer)
                .setExpanded(true)
                .create();

        resideMenu.closeMenu();
        dialog.show();
    }
}
