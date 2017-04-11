package cruga.team.fragments;

/**
 * Created by christian on 12/07/16.
 */

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import cruga.team.launcher_play.R;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutFragment extends Fragment {

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.dark_gray));
        }

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.4");

        getActivity().setTitle("title_about");
        View aboutPage = new AboutPage(getActivity())
                .setDescription(getString(R.string.about))
                .isRTL(false)
                .addItem(versionElement)
                .setImage(R.drawable.ic_launcher_48)
                .addGroup("Connect with us")
                .addEmail("mundo.iux17@gmail.com")
                .addFacebook("LauncherPlay")
                .addPlayStore("cruga.team.launcher_play")
                .addGitHub("ccruz17")
                .create();
        return aboutPage;
    }
}
