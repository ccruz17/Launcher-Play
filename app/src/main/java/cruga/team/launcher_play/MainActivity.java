package cruga.team.launcher_play;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import cruga.team.ResideMenu.IconClickListener;
import cruga.team.ResideMenu.ResideMenu;
import cruga.team.ResideMenu.ResideMenuItem;

import java.util.ArrayList;

import cruga.team.clases.App;
import cruga.team.clases.Tools;
import cruga.team.fragents.HomeFragment;

public class MainActivity extends FragmentActivity {

    //FINAL VARS
    public static final String PREF_CUSTOM_APPS = "pref_custom_apps";
    public static final String PREF_MAX_APPS = "pref_max_apps";
    //END FINAL
    //DEFAULT VARS FOR APP
    public static final int DEF_MAX_APPS = 7;
    //END DEFAULT VARS FOR APP
    public static ArrayList<App> allApps = null;
    public static ArrayList<App> customApps = null;
    private ResideMenu resideMenu;

    @Override
    public void onBackPressed() {
       if(getTitle() == getResources().getString(R.string.all_apps)) {
           setTitle(getResources().getString(R.string.app_name));
           super.onBackPressed();
       }else if(getTitle() == getResources().getString(R.string.app_name)) {
           //Nothing XD CCG
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
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.7f);
        resideMenu.setUse3D(true);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
       // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);

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

        if( savedInstanceState == null )
            changeFragment(new HomeFragment());

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            Toast.makeText(getApplicationContext(), "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
            Toast.makeText(getApplicationContext(), "Menu is closed!", Toast.LENGTH_SHORT).show();
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
}
