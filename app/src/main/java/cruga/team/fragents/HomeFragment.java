package cruga.team.fragents;

/**
 * Created by christian on 12/07/16.
 */

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cruga.team.CircleMenu.CircleMenu;
import cruga.team.ResideMenu.ResideMenu;

import cruga.team.clases.App;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

public class HomeFragment extends Fragment {

    ViewGroup rootView;
    private View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //----------------------------------Inicializar vistas----------------------------------------\\
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        final CircleMenu circleLayout = (CircleMenu) rootView.findViewById(R.id.circle_menu_items);
        circleLayout.setRotating(true);//enable rotation
        circleLayout.setFirstChildPosition(CircleMenu.FirstChildPosition.NORTH);
        circleLayout.setItems(MainActivity.customApps, MainActivity.DEF_MAX_APPS);//pass items and number of visibles iteams
        circleLayout.setIconSize(60);//set uicon size
        circleLayout.setOnLongClickListener(new CircleMenu.OnLongClickListener() {

            @Override
            public void onLongClick() {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                getActivity().setTitle(MainActivity.TITLE_MENU_CONFIG);
            }
        });

        circleLayout.setOnItemClickListener(new CircleMenu.OnItemClickListener() {

            @Override
            public void onItemClick(CircleMenu.ItemView view) {
                App currentApp  = MainActivity.customApps.get(view.getIdx());
                Intent intent = new Intent();
                intent.setPackage(currentApp.packageName);
                intent.setComponent(new ComponentName(currentApp.packageName, currentApp.activity));
                getActivity().startActivity(intent);
            }
        });


        //Settings to ResideMenu
        MainActivity parentActivity = (MainActivity)getActivity();
        resideMenu = parentActivity.getResideMenu();

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
}
