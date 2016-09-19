package cruga.team.listeners;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Created by christian on 14/09/16.
 */
public class MenuWallpaperListener implements View.OnClickListener {

    Activity mActivity;
    public MenuWallpaperListener(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public void onClick(View v) {
        Intent wallpaper = new Intent (Intent.ACTION_SET_WALLPAPER);
        Intent chooser = Intent.createChooser (wallpaper, "Fondo de pantalla");
        mActivity.startActivityForResult (chooser, 200);
    }
}
