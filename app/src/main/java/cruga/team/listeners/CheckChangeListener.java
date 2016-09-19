package cruga.team.listeners;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.HashSet;
import java.util.Set;

import cruga.team.clases.App;
import cruga.team.clases.Tools;
import cruga.team.launcher_play.MainActivity;

/**
 * Created by christian on 5/08/16.
 */
public class CheckChangeListener implements CheckBox.OnCheckedChangeListener{

    App app;
    Activity act;

    public CheckChangeListener(Activity act, App app) {
        this.app = app;
        this.act = act;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        Set<String> mySet = Tools.getSharePrefset(act, MainActivity.PREF_CUSTOM_APPS);
        if(isChecked) {

            if(mySet != null) {
                mySet.add(app.activity);
            }else {
                mySet = new HashSet<String>();
                mySet.add(app.activity);
            }

            Tools.setSharePref(act, MainActivity.PREF_CUSTOM_APPS, mySet);

        } else {
            String s = app.activity;
            mySet.remove(s);
            Tools.setSharePref(act, MainActivity.PREF_CUSTOM_APPS, mySet);
        }
    }
}
