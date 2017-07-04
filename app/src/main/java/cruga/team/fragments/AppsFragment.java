package cruga.team.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import cruga.team.libs.ResideMenu;
import cruga.team.adapters.AppsAdapter;
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
        MainActivity  parent = (MainActivity)getActivity();
        parent.setTitle(MainActivity.TITLE_MENU_SETTINGS_CUSTOM_APPS);

        ListView ls = (ListView)rootView.findViewById(R.id.lstApps);

        ls.setAdapter(new AppsAdapter(getActivity(), parent.getAllApps()));

        ResideMenu resideMenu = parent.getResideMenu();

        LinearLayout ignored_view = (LinearLayout) rootView.findViewById(R.id.fragment_ignore);
        resideMenu.addIgnoredView(ignored_view);

        return rootView;
    }
}
