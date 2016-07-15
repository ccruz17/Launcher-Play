package cruga.team.fragents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cruga.team.ResideMenu.ResideMenu;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

/**
 * Created by christian on 12/07/16.
 */
public class AppsFragment extends Fragment {

    ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_apps, container, false);
        MainActivity parentActivity = (MainActivity)getActivity();
        ResideMenu resideMenu = parentActivity.getResideMenu();
        resideMenu.closeMenu();
        getActivity().setTitle(MainActivity.TITLE_MENU_SETTINGS);

        return rootView;
    }
}
