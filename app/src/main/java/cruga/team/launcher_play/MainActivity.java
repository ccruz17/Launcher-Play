package cruga.team.launcher_play;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;

import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import java.util.ArrayList;

import cruga.team.clases.App;
import cruga.team.clases.Tools;
import cruga.team.fragents.HomeFragment;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    public static ArrayList<App> allApps = null;
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
        resideMenu.setBackground(R.drawable.shadow);
        resideMenu.attachToActivity(this);

        // create menu items;
        for (int i = 0; i < allApps.size(); i++){
            ResideMenuItem item = new ResideMenuItem(this, allApps.get(i).id_rcs_icon, allApps.get(i).label);
            item.setOnClickListener(this);
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }



        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        allApps = Tools.obtenerApps(pm);

        HomeFragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, homeFragment).commit();

    }

    @Override
    public void onClick(View v) {

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }
}
