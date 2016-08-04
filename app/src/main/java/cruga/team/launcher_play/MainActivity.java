package cruga.team.launcher_play;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cruga.team.CircleMenu.CircleMenu;
import cruga.team.fragents.HomeFragment;
import cruga.team.fragents.PreferenceFragment;
import cruga.team.listeners.IconClickListener;
import cruga.team.ResideMenu.ResideMenu;
import cruga.team.ResideMenu.ResideMenuItem;

import java.util.ArrayList;

import cruga.team.clases.App;
import cruga.team.clases.Tools;

public class MainActivity extends FragmentActivity {

    //FINAL VARS
    public static final String PREF_CUSTOM_APPS = "pref_custom_apps";
    public static final String PREF_MAX_APPS = "pref_max_apps";
    public static final String PREF_ROTATION = "pref_rotaion";
    public static final String PREF_3D_ANIMATION = "pref_animation";

    public static final String TITLE_MENU_CONFIG = "title_config";
    public static final String TITLE_MENU_SETTINGS = "title_settings";
    public static final String TITLE_HOME = "title_home";
    public static final String TITLE_ALL_APSS = "title_all_apps";

    public static final String PREFERENCE_KEY = "CCG_GA";
    //END FINAL
    //DEFAULT VARS FOR APP
    public static final int DEF_MAX_APPS = 7;
    public static final boolean DEF_ROTATION = true;
    public static final boolean DEF_3D_ANIMATION = true;
    //END DEFAULT VARS FOR APP
    public static ArrayList<App> allApps = null;
    public static ArrayList<App> customApps = null;
    private ResideMenu resideMenu;
    public static CircleMenu circleMenu = null;

    @Override
    public void onBackPressed() {
        Log.i("CRUGA-", getTitle() + "");
        if(getTitle() == TITLE_MENU_SETTINGS) {

            /*Fragment f = getSupportFragmentManager().findFragmentByTag("SETTINGS");
            getSupportFragmentManager().beginTransaction().remove(f).commit();
            */
            changeFragment(new HomeFragment());

            setTitle(MainActivity.TITLE_HOME);
        } else if(getTitle() == MainActivity.TITLE_ALL_APSS) {
            resideMenu.closeMenu();
        } else if(getTitle() == TITLE_HOME) {
           //Nothing XD CCG
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
        circleMenu = new CircleMenu(this);
        //resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.6f);

        if(Tools.getSharePref(this, PREF_3D_ANIMATION).compareTo("") == 0) {
            resideMenu.setUse3D(false);
        } else {
            resideMenu.setUse3D(true);
        }

        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        allApps = Tools.obtenerApps(pm);
        customApps = Tools.obtenerCustomApps(this);

        // create menu items;
        for (int i = 0; i < allApps.size(); i++){
            ResideMenuItem item = new ResideMenuItem(this, allApps.get(i).icono, allApps.get(i).label);

            item.setOnClickListener(new IconClickListener(this, allApps.get(i)));
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }

        ResideMenuItem settings = new ResideMenuItem(this, R.drawable.ic_settings_white_48dp, getString(R.string.settings));
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new PreferenceFragment(), "SETTINGS")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                resideMenu.closeMenu();
                setTitle(MainActivity.TITLE_MENU_SETTINGS);
            }
        });
        resideMenu.addMenuItem(settings, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(new ResideMenuItem(this, R.drawable.ic_wallpaper_white_48dp, getString(R.string.wallpaper)), ResideMenu.DIRECTION_RIGHT);


        resideMenu.setMenuListener(menuListener);


        if( savedInstanceState == null )
            changeFragment(new HomeFragment());

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i("EVENT", "dispatchTouchEvent");
        if(getTitle() == MainActivity.TITLE_MENU_SETTINGS) {
            return super.dispatchTouchEvent(ev);
        }else {
            return resideMenu.dispatchTouchEvent(ev);
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

    private void changeFragment(Fragment targetFragment){
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
     // What good method is to access circleMenu

    public CircleMenu getCircleMenu() {
        return circleMenu;
    }

}
