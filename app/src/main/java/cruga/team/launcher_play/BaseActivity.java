package cruga.team.launcher_play;

/**
 * Created by christian on 15/08/16.
 */


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Checkout;

public class BaseActivity extends FragmentActivity {

    protected ActivityCheckout checkout = Checkout.forActivity(this, CheckoutApplication.get().getCheckout());
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkout.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        checkout.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        checkout.stop();
        super.onDestroy();
    }

    public ActivityCheckout getCheckout() {
        return checkout;
    }

    protected final void addFragment(Fragment fragment, int viewId, boolean retain) {
        fragment.setRetainInstance(retain);
        getSupportFragmentManager().beginTransaction()
                .add(viewId, fragment)
                .commit();
    }

}