package cruga.team.listeners;

import android.app.Activity;
import android.view.View;

import com.nightonke.boommenu.BoomMenuButton;

/**
 * Created by christian on 11/04/17.
 */

public class LongClickSideMenuItemListener implements View.OnLongClickListener {

    Activity act;
    BoomMenuButton bmb;

    public  LongClickSideMenuItemListener(Activity act, BoomMenuButton bmb) {
        this.bmb = bmb;
        this.act = act;

    }
    @Override
    public boolean onLongClick(View v) {
        bmb.boom();
        act.setTitle("FlottingtMenu");
        return true;
    }
}
