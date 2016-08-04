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

import cruga.team.CircleMenu.CircleMenu;
import cruga.team.ResideMenu.ResideMenu;

import cruga.team.clases.App;
import cruga.team.clases.Tools;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

public class HomeFragment extends Fragment {

    ViewGroup rootView;
    private View parentView;
    private ResideMenu resideMenu;
    public static CircleMenu circleMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("CRUGA-EVENT", "HomeFragment-OnCreateView");
        //----------------------------------Inicializar vistas----------------------------------------\\
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        MainActivity parentActivity = (MainActivity)getActivity();

        //GET MENUS
        circleMenu = parentActivity.getCircleMenu();
        resideMenu = parentActivity.getResideMenu();

        circleMenu = (CircleMenu) rootView.findViewById(R.id.circle_menu_items);
        if(Tools.getSharePref(getActivity(), MainActivity.PREF_ROTATION).compareTo("") == 0) {
            circleMenu.setRotating(false);//enable rotation
        }else {
            circleMenu.setRotating(true);//enable rotation
        }
        circleMenu.setFirstChildPosition(CircleMenu.FirstChildPosition.NORTH);
        circleMenu.setItems(parentActivity.customApps, MainActivity.DEF_MAX_APPS);//pass items and number of visibles iteams
        circleMenu.setIconSize(60);//set uicon size
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
                App currentApp  = MainActivity.customApps.get(view.getIdx());
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
