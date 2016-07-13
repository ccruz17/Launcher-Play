package cruga.team.ResideMenu;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

import cruga.team.clases.App;

/**
 * Created by christian on 13/07/16.
 */
public class IconClickListener implements OnClickListener {

    private App app;
    private Activity act;
    public IconClickListener(Activity act, App app) {
        this.app = app;
        this.act = act;
    }

    @Override
    public void onClick(View v) {
        Intent launchIntent = act.getPackageManager().getLaunchIntentForPackage(app.packageName);
        act.startActivity(launchIntent);
    }
}
