package cruga.team.fragents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rey.material.widget.Switch;

import cruga.team.CircleMenu.CircleMenu;
import cruga.team.ResideMenu.ResideMenu;
import cruga.team.clases.Tools;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

/**
 * Created by christian on 12/07/16.
 */
public class PreferenceFragment extends Fragment {

    ViewGroup rootView;
    ResideMenu resideMenu;
    CircleMenu circleMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        MainActivity parentActivity = (MainActivity)getActivity();
        resideMenu = parentActivity.getResideMenu();
        circleMenu = parentActivity.getCircleMenu();


        Switch switch3d = (Switch) rootView.findViewById(R.id.switch_3d);
        Switch switchRotating = (Switch)rootView.findViewById(R.id.switch_rotating);

        switch3d.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked) {
                    resideMenu.setUse3D(checked);
                    resideMenu.setScaleValue(0.6f);
                    Tools.setSharePref(getActivity(), MainActivity.PREF_3D_ANIMATION, "TRUE");
                } else {
                    resideMenu.setUse3D(checked);
                    resideMenu.setScaleValue(0.5f);
                    Tools.setSharePref(getActivity(), MainActivity.PREF_3D_ANIMATION, "");
                }
            }
        });

        switchRotating.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked) {

                    //MainActivity.circleMenu.setRotating(checked);
                    //circleMenu.setRotating(checked);
                    Tools.setSharePref(getActivity(), MainActivity.PREF_ROTATION, "TRUE");
                    Log.i("PREF", "SI" +Tools.getSharePref(getActivity(), MainActivity.PREF_ROTATION));
                } else {
                    //MainActivity.circleMenu.setRotating(checked);
                    //circleMenu.setRotating(checked);

                    Tools.setSharePref(getActivity(), MainActivity.PREF_ROTATION, "");
                    Log.i("PREF", "NO" + Tools.getSharePref(getActivity(), MainActivity.PREF_ROTATION));
                }
            }
        });

        if(Tools.getSharePref(getActivity(), MainActivity.PREF_3D_ANIMATION).compareTo("") == 0) {
            switch3d.setChecked(false);
        }else {
            switch3d.setChecked(true);
        }

        if(Tools.getSharePref(getActivity(), MainActivity.PREF_ROTATION).compareTo("") == 0) {
            switchRotating.setChecked(false);
        }else {
            switchRotating.setChecked(true);
        }

        getActivity().setTitle(MainActivity.TITLE_MENU_SETTINGS);

        return rootView;
    }
}
