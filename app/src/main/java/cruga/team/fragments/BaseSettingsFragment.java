package cruga.team.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Inventory;

import cruga.team.launcher_play.BaseActivity;

/**
 * Created by christian on 15/08/16.
 */
public class BaseSettingsFragment extends Fragment {

    protected ActivityCheckout checkout;

    protected Inventory inventory;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        checkout = ((BaseActivity) activity).getCheckout();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inventory = checkout.loadInventory();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        BaseActivity myBase;
        if (context instanceof Activity) {
            myBase = (BaseActivity) context;
            checkout = myBase.getCheckout();
        }
    }
}