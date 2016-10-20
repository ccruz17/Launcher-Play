package cruga.team.launcher_play;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;


import cruga.team.fragents.HomeFragment;
import cruga.team.fragents.PreferenceFragment;
import cruga.team.listeners.AddAppBroadCastReceiver;
import cruga.team.listeners.IconClickListener;
import cruga.team.libs.ResideMenu;
import cruga.team.libs.ResideMenuItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cruga.team.clases.App;
import cruga.team.clases.Tools;
import cruga.team.listeners.MenuIconPackListener;
import cruga.team.listeners.MenuSettingsListener;
import cruga.team.listeners.MenuWallpaperListener;

public class MainActivity extends BaseActivity {

    //FINAL VARS
    public static final String PREF_CUSTOM_APPS = "pref_custom_apps";
    public static final String PREF_ROTATION = "pref_rotaion";
    public static final String PREF_3D_ANIMATION = "pref_animation";
    public static final String PREF_SIZE_ICON = "pref_size_icon";
    public static final String PREF_SHOW_FONT = "pref_show_font";
    public static final String PREF_ICON_PACK = "pref_icon_pack";
    public static final String PREFERENCE_KEY = "CCG_GA";

    public static final String TITLE_ALL_APSS = "title_all_apps";
    public static final String TITLE_HOME = "title_home";
    public static final String TITLE_MENU_CONFIG = "title_config";
    public static final String TITLE_MENU_SETTINGS = "title_settings";
    public static final String TITLE_MENU_SETTINGS_CUSTOM_APPS = "title_settings_custom_apps";

    public static final int [] ICON_SIZES = { 40, 50, 60};

    public static final String BILLING_EXCLUSIVE_FEATURES = "exclusive_features";
    public static final String BILLING_COFFEE = "coffee";
    public static final String BILLING_BEER = "beer";
    public static final String BILLING_TICKET = "bus_ticket";

    public static Boolean HOME_PRESS = true;

    public static final String API_KEY = "IDwnJiZEKDs6VlwxDwYILlVZUy0sPDAoJS5hKjQpCXYNLiQjKgtlIC48KDQdDwJoD0VBRAFwMh5K" +
            "BB0eAhkvQjw8Fg52OAxTWXowCx0AChxEWx8OPR9dF1ZAXjEgYwEtBBsLHQlcABUfJCMFUyhiXhAw" +
            "RQIHBjgsIQtXFR4uPyYjDhdaIh07QFUVH0YVWyBGFBcJKSxdNwx0EBQPUF4BDD4NOVVgVDY7HDJf" +
            "NF1eRl4vV255VggUPA5POzYrKT0kVS58L0MpZ1RzKzQFIENqDAI3O0QaV1ZaWBoAcE8BA0YuXi52" +
            "Al8OF00WKh1aUUYWeAAMKzQqUS12UyApKRkJABxpM0MJV3goBQUSBghACCJeOiM7NQlrE0Igc0cx" +
            "MwwwDTZLBgYmLgEJCCRMMDIIXHkSEjUNHSgWGRwYLxgJPT9cPidAQ1w3LAxRMzZMUCAXCCYIKRZk" +
            "Ow00QkAqFAVTK11fCywLJAAaPg5BHjEUXWMSBBtYXyt+Big9BiABHB9UCwIxdXYRJi8=";

    Bitmap bitmapWallPaper;

    //END FINAL
    BroadcastReceiver addApp;
    //END DEFAULT VARS FOR APP
    public ArrayList<App> allApps = null;
    private ResideMenu resideMenu;

    @Override
    public void onBackPressed() {
        Log.i("CRUGA-", getTitle() + "");
        if(getTitle() == TITLE_MENU_SETTINGS) {
            changeFragment(new HomeFragment());
            setTitle(MainActivity.TITLE_HOME);
        } else if(getTitle() == MainActivity.TITLE_MENU_SETTINGS_CUSTOM_APPS) {
            changeFragment(new PreferenceFragment());
        }else if(getTitle() == MainActivity.TITLE_ALL_APSS) {
            resideMenu.closeMenu();
        } else if(getTitle() == TITLE_HOME) {
           resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
           setTitle(MainActivity.TITLE_ALL_APSS);
        } else if(getTitle() == TITLE_MENU_CONFIG) {
           resideMenu.closeMenu();
        } else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout);

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.6f);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

        if(Tools.getSharePref(this, PREF_3D_ANIMATION).compareTo("") == 0) {
            resideMenu.setUse3D(false);
        } else {
            resideMenu.setUse3D(true);
        }

        final PackageManager pm = getPackageManager();
        //get a list of installed apps and update Menu Left.
        getAppsAndUpdateMenuLeft();
        //set Menu Settings
        setMenuRight();
        resideMenu.setMenuListener(menuListener);

        if( savedInstanceState == null )
            changeFragment(new HomeFragment());

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(getTitle() == MainActivity.TITLE_MENU_SETTINGS) {
            return super.dispatchTouchEvent(ev);
        }else {
           // return resideMenu.dispatchTouchEvent(ev);
            return super.dispatchTouchEvent(ev);

        }

    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
        }

        @Override
        public void closeMenu() {
            if(getTitle() != MainActivity.TITLE_MENU_SETTINGS) {
                setTitle(MainActivity.TITLE_HOME);
            }
        }
    };

    public void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu() {
        return resideMenu;
    }

    public ArrayList<App> getAllApps() {
        return allApps;
    }


    private void setMenuRight() {
        //Add Settings Listeners and Item
        ResideMenuItem settings = new ResideMenuItem(this, R.drawable.ic_settings_white_48dp, getString(R.string.settings));
        settings.setOnClickListener(new MenuSettingsListener(this));
        resideMenu.addMenuItem(settings, ResideMenu.DIRECTION_RIGHT);

        //Add Wallpaper Listener and Item
        ResideMenuItem wallpaper = new ResideMenuItem(this, R.drawable.ic_wallpaper_white_48dp, getString(R.string.wallpaper));
        wallpaper.setOnClickListener(new MenuWallpaperListener(this));
        resideMenu.addMenuItem(wallpaper, ResideMenu.DIRECTION_RIGHT);

        //Add IconPack Listener and Item
        ResideMenuItem iconPack = new ResideMenuItem(this, R.drawable.ic_icon_white_48dp, getString(R.string.icon_pack));
        iconPack.setOnClickListener(new MenuIconPackListener(this, this, resideMenu));
        resideMenu.addMenuItem(iconPack, ResideMenu.DIRECTION_RIGHT);
    }

    public void getAppsAndUpdateMenuLeft() {
        allApps = Tools.obtenerApps(this, this);
        List<ResideMenuItem> itemApps = new ArrayList<>();

        for (int i = 0; i < allApps.size(); i++){

            String lbl = allApps.get(i).label;
            Drawable ic = allApps.get(i).icono;

            ResideMenuItem itemMenuReside = new ResideMenuItem(this, ic, lbl);
            itemMenuReside.setOnClickListener(new IconClickListener(this, allApps.get(i)));
            itemApps.add(itemMenuReside);
        }

        resideMenu.setMenuItems(itemApps, ResideMenu.DIRECTION_LEFT);
    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(addApp);
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("GRUGA_EVENT", "OnStart");
        initBroadcastRecibers();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("GRUGA_EVENT", "OnRestart");
        initBroadcastRecibers();
    }

    public void initBroadcastRecibers()
    {
        addApp = new AddAppBroadCastReceiver();
        registerReceiver(addApp, new Tools().filterApps());
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Log.i("GRUGA_EVENT", "OnResumenFragment");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("GRUGA_EVENT", "OnResumenAct");
        Log.i("TITLE", getTitle().toString() + "");
        if(getTitle() == TITLE_MENU_SETTINGS && MainActivity.HOME_PRESS) {
            changeFragment(new HomeFragment());
            setTitle(MainActivity.TITLE_HOME);
        } else if(getTitle() == MainActivity.TITLE_MENU_SETTINGS_CUSTOM_APPS  && MainActivity.HOME_PRESS) {
            changeFragment(new HomeFragment());
            setTitle(MainActivity.TITLE_HOME);
        } else if(getTitle() == MainActivity.TITLE_ALL_APSS  && MainActivity.HOME_PRESS) {
            resideMenu.closeMenu();
        } else if(getTitle() == TITLE_MENU_CONFIG  && MainActivity.HOME_PRESS) {
            resideMenu.closeMenu();
        }

        MainActivity.HOME_PRESS = true;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        WallpaperManager wM;

        if(requestCode == 200 && resultCode == RESULT_OK && data != null)
        {
            try
            {
                InputStream stream = getContentResolver().openInputStream(
                        data.getData());
                if (bitmapWallPaper != null) {
                    bitmapWallPaper.recycle();
                }
                bitmapWallPaper = BitmapFactory.decodeStream(stream);
                stream.close();
                wM = WallpaperManager.getInstance(this);
                wM.setBitmap(bitmapWallPaper);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
