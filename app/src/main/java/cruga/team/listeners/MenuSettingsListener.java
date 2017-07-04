package cruga.team.listeners;

import android.support.v4.app.FragmentTransaction;
import android.view.View;

import cruga.team.fragments.PreferenceFragment;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

/**
 * Created by christian on 14/09/16.
 */
public class MenuSettingsListener implements View.OnClickListener{

    MainActivity mActivity;

    public MenuSettingsListener(MainActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onClick(View v) {

        mActivity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new PreferenceFragment(), "SETTINGS")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        mActivity.getResideMenu().closeMenu();
        mActivity.setTitle(MainActivity.TITLE_MENU_SETTINGS);
    }
}
