package cruga.team.fragents;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cruga.team.clases.App;
import cruga.team.launcher_play.R;

/**
 * Created by christian on 12/07/16.
 */
public class AppsFragment extends Fragment {
    ViewGroup rootView;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.apps_layout, container, false);
        getActivity().setTitle(getResources().getString(R.string.all_apps));




        return rootView;
    }
}
