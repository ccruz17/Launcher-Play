package cruga.team.listeners;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnItemClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cruga.team.adapters.IconPackAdapter;
import cruga.team.clases.App;
import cruga.team.clases.IconPackManager;
import cruga.team.clases.Tools;
import cruga.team.fragents.HomeFragment;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;
import cruga.team.libs.ResideMenu;
import cruga.team.libs.ResideMenuItem;

/**
 * Created by christian on 14/09/16.
 */
public class MenuIconPackListener implements View.OnClickListener {

    Context mContext;
    ResideMenu resideMenu;
    MainActivity mainActivity;

    public MenuIconPackListener(Context mContext, MainActivity mainActivity, ResideMenu resideMenu) {
        this.mContext = mContext;
        this.resideMenu = resideMenu;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View v) {

        final ArrayList<App> themesInstalled = new ArrayList<>(0);


        IconPackManager iP = new IconPackManager();
        iP.setContext(mContext);
        iP.setActivity(mainActivity);
        //Get Themes
        HashMap<String, IconPackManager.IconPack> map = iP.getAvailableIconPacks(true);
        //Iter map and add to list
        for (HashMap.Entry<String, IconPackManager.IconPack> entry : map.entrySet()) {

            IconPackManager.IconPack iconPack = entry.getValue();

            App theme = new App();
            theme.packageName = iconPack.packageName;
            theme.icono = iconPack.iconForTheme;
            theme.label = iconPack.name;

            themesInstalled.add(theme);
        }

        App defaultTheme = new App();
        defaultTheme.packageName = mContext.getResources().getString(R.string.app_packagename);
        defaultTheme.icono = mContext.getResources().getDrawable(R.drawable.ic_launcher_48);
        defaultTheme.label = mContext.getResources().getString(R.string.app_name);

        themesInstalled.add(defaultTheme);

        IconPackAdapter iconAdapter = new IconPackAdapter(mContext, themesInstalled);


        DialogPlus dialog = DialogPlus.newDialog(mContext)
                .setAdapter(iconAdapter)
                .setContentHolder(new ListHolder())
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {

                        Log.i("CRUGA", position + "-" + themesInstalled.get(position).packageName);

                        Tools t = new Tools();
                        t.setSharePref(mainActivity, MainActivity.PREF_ICON_PACK, themesInstalled.get(position).packageName);

                        resideMenu.clearIgnoredViewList();
                        mainActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new HomeFragment(), "fragment")
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();
                        mainActivity.setTitle(MainActivity.TITLE_HOME);
                        dialog.dismiss();
                        mainActivity.getAppsAndUpdateMenuLeft();

                    }
                })
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.footer_close_button:
                                dialog.dismiss();
                                break;
                            default:
                                dialog.dismiss();
                                break;
                        }
                    }
                })
                .setHeader(R.layout.header)
                .setFooter(R.layout.footer)
                .setExpanded(false)
                .setGravity(Gravity.CENTER)
                .create();

        resideMenu.closeMenu();
        dialog.show();
    }
}
