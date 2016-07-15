package cruga.team.fragents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.Switch;

import cruga.team.ResideMenu.ResideMenu;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

/**
 * Created by christian on 12/07/16.
 */
public class PreferenceFragment extends Fragment {

    ViewGroup rootView;
    ResideMenu resideMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        MainActivity parentActivity = (MainActivity)getActivity();
        resideMenu = parentActivity.getResideMenu();

        Switch switch3d = (Switch) rootView.findViewById(R.id.switch_3d);
        Switch switchRotating = (Switch)rootView.findViewById(R.id.switch_rotating);

        switch3d.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(Switch view, boolean check) {
                if(check) {
                    resideMenu.setUse3D(true);
                } else {
                    resideMenu.setUse3D(false);
                }
            }
        });

        switchRotating.setOncheckListener(new Switch.OnCheckListener() {
            @Override
            public void onCheck(Switch view, boolean check) {
                if(check) {
                    Log.i("cRUGA", "A");

                } else {
                    Log.i("cRUGA", "B");
                }
            }
        });

        getActivity().setTitle(MainActivity.TITLE_MENU_SETTINGS);


        Log.i("cRUGA", "entro");
        return rootView;
    }
}
