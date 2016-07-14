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
import android.widget.Toast;

import cruga.team.ResideMenu.ResideMenu;

import cruga.team.clases.App;
import cruga.team.framework.widget.CircleMenu;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

public class HomeFragment extends Fragment {

    ViewGroup rootView;
    private View parentView;
    private ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //----------------------------------Inicializar vistas----------------------------------------\\
        rootView = (ViewGroup) inflater.inflate(R.layout.activity_main, container, false);

        final CircleMenu circleMenu = (CircleMenu) rootView.findViewById(R.id.circle_menu_items);
        circleMenu.setRotating(true);//enable rotation
        circleMenu.setFirstChildPosition(CircleMenu.FirstChildLocation.North);
        circleMenu.setItems(MainActivity.customApps, MainActivity.DEF_MAX_APPS);//pass items and number of visibles iteams
        circleMenu.setIconSize(60);//set uicon size
        circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {
            @Override
            public void onItemClick(CircleMenu.ItemView view) {

                App currentApp  = MainActivity.customApps.get(view.getIdx());
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(currentApp.packageName, currentApp.activity));
                getActivity().startActivity(intent);
            }
        });

        Log.i("MyCCG", circleMenu.getSelectedItem().getPosition() + "");
        Log.i("MyCCG", circleMenu.getSelectedItem().getIdx() + "");

        rootView.findViewById(R.id.circle_menu_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "click center", Toast.LENGTH_SHORT).show();
                MainActivity parentActivity = (MainActivity) getActivity();

                resideMenu = parentActivity.getResideMenu();
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        getActivity().setTitle(getResources().getString(R.string.app_name));

        return rootView;
    }
}
