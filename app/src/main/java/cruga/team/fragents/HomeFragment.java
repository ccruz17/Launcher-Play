package cruga.team.fragents;

/**
 * Created by christian on 12/07/16.
 */

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import cruga.team.libs.CircleMenu;
import cruga.team.libs.ResideMenu;

import cruga.team.clases.App;
import cruga.team.clases.Tools;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

public class HomeFragment extends Fragment {

    ViewGroup rootView;
    private ResideMenu resideMenu;
    public static CircleMenu circleMenu;
    public static ArrayList<App> customApps = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("CRUGA-EVENT", "HomeFragment-OnCreateView");
        //----------------------------------Inicializar vistas----------------------------------------\\
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        MainActivity parentActivity = (MainActivity)getActivity();
        customApps = Tools.obtenerCustomApps(getActivity());

        //GET MENU
        resideMenu = parentActivity.getResideMenu();

        circleMenu = (CircleMenu) rootView.findViewById(R.id.circle_menu_items);
        circleMenu.setFirstChildPosition(CircleMenu.FirstChildPosition.NORTH);
        loadPreferences();
        circleMenu.setItems(customApps, customApps.size());//pass items and number of visibles iteams

        circleMenu.setOnLongClickListener(new CircleMenu.OnLongClickListener() {

            @Override
            public void onLongClick() {
                if(getActivity().getTitle() == MainActivity.TITLE_HOME) {
                    resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                    getActivity().setTitle(MainActivity.TITLE_MENU_CONFIG);
                }
            }
        });

        circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {

            @Override
            public void onItemClick(CircleMenu.ItemView view) {
                App currentApp  = customApps.get(view.getIdx());
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName(currentApp.packageName, currentApp.activity));
                getActivity().startActivity(intent);
            }
        });

        FrameLayout ignored_view = (FrameLayout) rootView.findViewById(R.id.fragment_ignore);
        resideMenu.addIgnoredView(ignored_view);

        rootView.findViewById(R.id.circle_menu_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity parentActivity = (MainActivity) getActivity();
                resideMenu = parentActivity.getResideMenu();
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                getActivity().setTitle(MainActivity.TITLE_ALL_APSS);
            }
        });
        getActivity().setTitle(MainActivity.TITLE_HOME);

        return rootView;
    }

    private void loadPreferences(){
        if(Tools.getSharePref(getActivity(), MainActivity.PREF_ROTATION).compareTo("") == 0) {
            circleMenu.setRotating(false);//enable rotation
        }else {
            circleMenu.setRotating(true);//enable rotation
        }

        if(Tools.getSharePref(getActivity(), MainActivity.PREF_SIZE_ICON).compareTo("") == 0) {
            circleMenu.setIconSize(MainActivity.ICON_SIZES[1]);
        }else {
            int pos = Integer.parseInt(Tools.getSharePref(getActivity(), MainActivity.PREF_SIZE_ICON));
            circleMenu.setIconSize(MainActivity.ICON_SIZES[pos]);
        }

        if(Tools.getSharePref(getActivity(), MainActivity.PREF_SHOW_FONT).compareTo("") == 0) {
            circleMenu.setShowFont(false);
        }else {
            circleMenu.setShowFont(true);
        }

    }

    @Override
    public void onPause() {
        Log.i("CRUGA-EVENT", "HomeFragment-OnPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i("CRUGA-EVENT", "HomeFragment-OnResumen");
        super.onResume();
    }

}
