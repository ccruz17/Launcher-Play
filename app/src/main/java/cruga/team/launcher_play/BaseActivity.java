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
import org.solovyev.android.checkout.EmptyRequestListener;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;

import static java.util.Arrays.asList;

public class BaseActivity extends FragmentActivity {

    private class PurchaseListener extends EmptyRequestListener<Purchase> {
        // your code here
    }

    private class InventoryCallback implements Inventory.Callback {
        @Override
        public void onLoaded(Inventory.Products products) {
            // your code here
        }
    }

    private final ActivityCheckout mCheckout = Checkout.forActivity(this, CheckoutApplication.get().getBilling());
    private Inventory mInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckout.start();

        mCheckout.createPurchaseFlow(new PurchaseListener());

        mInventory = mCheckout.makeInventory();
        mInventory.load(Inventory.Request.create()
                .loadAllPurchases()
                .loadSkus(
                        ProductTypes.IN_APP,
                        asList(MainActivity.BILLING_BEER, MainActivity.BILLING_COFFEE, MainActivity.BILLING_EXCLUSIVE_FEATURES, MainActivity.BILLING_TICKET)
                ),
                new InventoryCallback());
                //.loadSkus(ProductTypes.IN_APP, "sku_01"), new InventoryCallback());
    }

    @Override
    protected void onDestroy() {
        mCheckout.stop();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCheckout.onActivityResult(requestCode, resultCode, data);
    }

    /*
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
    }*/

}