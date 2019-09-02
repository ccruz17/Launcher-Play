package cruga.team.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rey.material.widget.Switch;

import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.Billing;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;
import org.solovyev.android.checkout.ResponseCodes;
import org.solovyev.android.checkout.Sku;

import cruga.team.launcher_play.CheckoutApplication;
import cruga.team.libs.ResideMenu;
import cruga.team.clases.Tools;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;
import cruga.team.listeners.TouchDonationsListener;

import static java.util.Arrays.asList;
import static org.solovyev.android.checkout.ProductTypes.IN_APP;

/**
 * Created by christian on 12/07/16.
 */
public class PreferenceFragment extends BaseSettingsFragment {

    ViewGroup rootView;
    ResideMenu resideMenu;
    Sku sku_premium, sku_coffee, sku_beer, sku_ticket;
    String token_premium, token_bear, token_ticket, token_coffee = null;
    TextView txt_donations;
    LinearLayout linearLayoutPremium;
    Switch switch3d, switchRotating, switchShowText;
    Spinner iconSizeSpinner;

    RelativeLayout menu_settings_apps, menu_premium_features, menu_coffee, menu_beer, menu_ticket, menu_about;
    private ActivityCheckout mCheckout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        final MainActivity parentActivity = (MainActivity)getActivity();
        resideMenu = parentActivity.getResideMenu();

        init_view();
        set_listeners_menus(parentActivity);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.icon_size_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iconSizeSpinner.setAdapter(adapter);

        iconSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parentActivity.getSharedPref().edit().putString( MainActivity.PREF_SIZE_ICON, position+"").commit();
                //Tools.setSharePref(getActivity(), MainActivity.PREF_SIZE_ICON, position+"");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(parentActivity.getSharedPref().getString(MainActivity.PREF_SIZE_ICON, "").compareTo("") == 0) {
            iconSizeSpinner.setSelection(1);
        }else {
            int pos = Integer.parseInt(parentActivity.getSharedPref().getString(MainActivity.PREF_SIZE_ICON, "1"));
            iconSizeSpinner.setSelection(pos);
        }

        switch3d.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked) {
                    resideMenu.setUse3D(checked);
                    resideMenu.setScaleValue(0.6f);

                    parentActivity.getSharedPref().edit().putString( MainActivity.PREF_3D_ANIMATION, "TRUE").commit();
                    //Tools.setSharePref(getActivity(), MainActivity.PREF_3D_ANIMATION, "TRUE");
                } else {
                    resideMenu.setUse3D(checked);
                    resideMenu.setScaleValue(0.5f);
                    parentActivity.getSharedPref().edit().putString( MainActivity.PREF_3D_ANIMATION, "").commit();
                    //Tools.setSharePref(getActivity(), MainActivity.PREF_3D_ANIMATION, "");
                }
            }
        });

        switchRotating.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked) {
                    parentActivity.getSharedPref().edit().putString( MainActivity.PREF_ROTATION, "TRUE").commit();
                    //Tools.setSharePref(getActivity(), MainActivity.PREF_ROTATION, "TRUE");
                } else {
                    parentActivity.getSharedPref().edit().putString( MainActivity.PREF_ROTATION, "").commit();
                    //Tools.setSharePref(getActivity(), MainActivity.PREF_ROTATION, "");
                }
            }
        });

        switchShowText.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked) {
                    parentActivity.getSharedPref().edit().putString( MainActivity.PREF_SHOW_FONT, "TRUE").commit();
                    //Tools.setSharePref(getActivity(), MainActivity.PREF_SHOW_FONT, "TRUE");
                } else {
                    parentActivity.getSharedPref().edit().putString( MainActivity.PREF_SHOW_FONT, "").commit();
                    //Tools.setSharePref(getActivity(), MainActivity.PREF_SHOW_FONT, "");
                }
            }
        });

        if(parentActivity.getSharedPref().getString(MainActivity.PREF_3D_ANIMATION, "").compareTo("") == 0) {
            switch3d.setChecked(false);
        }else {
            switch3d.setChecked(true);
        }

        if(parentActivity.getSharedPref().getString(MainActivity.PREF_ROTATION, "").compareTo("") == 0) {
            switchRotating.setChecked(false);
        }else {
            switchRotating.setChecked(true);
        }

        if(parentActivity.getSharedPref().getString(MainActivity.PREF_SHOW_FONT, "").compareTo("") == 0) {
            switchShowText.setChecked(false);
        }else {
            switchShowText.setChecked(true);
        }


        parentActivity.setTitle(MainActivity.TITLE_MENU_SETTINGS);

        LinearLayout ignored_view = (LinearLayout) rootView.findViewById(R.id.fragment_ignore);
        resideMenu.addIgnoredView(ignored_view);


        //inventory.whenLoaded(new InventoryLoadedListener());
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCheckout = Checkout.forActivity(getActivity(), CheckoutApplication.get().getBilling());

        mCheckout.start();
        reloadInventory();

        Log.i("CRUGA-EVENT", "onCreatePreferenceFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mCheckout.destroyPurchaseFlow();
        super.onDestroy();
        Log.i("CRUGA-EVENT", "onDestroyPreferencesFragment");
    }


    private void sendBuy(Sku sku, String token) {
        MainActivity.HOME_PRESS = false;
        /*
        if(token == null) {
            purchase(sku);
        } else {
            consume(token, new ConsumeListener());
        }*/

        Inventory.Product mProduct = Inventory.Products.empty().get(ProductTypes.IN_APP);

        final Purchase purchase = mProduct.getPurchaseInState(sku, Purchase.State.PURCHASED);

        if (token == null) {
            Log.i("CCG", "Go to Purchase");
            purchase(sku);
        } else {
            Log.i("CCG", "Go to Consume");
            consume(purchase);
            Log.i("CCG", "Go to reload");
        }
    }

    private void consume(final Purchase purchase) {
        mCheckout.whenReady(new Checkout.EmptyListener() {
            @Override
            public void onReady(BillingRequests requests) {
                requests.consume(purchase.token, makeRequestListener());
            }
        });
    }
    /*
    private void consume(final String token, final RequestListener<Object> onConsumed) {

        mCheckout.whenReady(new Checkout.EmptyListener() {
            @Override
            public void onReady(BillingRequests requests) {
                requests.purchase(ProductTypes.IN_APP, token, null, checkout.getPurchaseFlow());
            }
        });

        /*checkout.whenReady(new Checkout.ListenerAdapter() {
            @Override
            public void onReady(BillingRequests requests) {
                requests.consume(token, onConsumed);
            }
        });
        */
    /*}*/

    private <T> RequestListener<T> makeRequestListener() {
        return new RequestListener<T>() {
            @Override
            public void onSuccess( T result) {
                Log.i("CCG", "onSuccess");
                reloadInventory();

            }

            @Override
            public void onError(int response, Exception e) {
                Log.i("CCG", "onError" + response);
                Log.i("CCG", e.getMessage() + "");
                reloadInventory();
            }

        };
    }

    private void reloadInventory() {
        Log.i("CCG", "Reload");
        final Inventory.Request request = Inventory.Request.create();
        // load purchase info
        request.loadAllPurchases();
        // load SKU details
        request.loadSkus(ProductTypes.IN_APP, asList(MainActivity.BILLING_BEER, MainActivity.BILLING_COFFEE, MainActivity.BILLING_EXCLUSIVE_FEATURES, MainActivity.BILLING_TICKET));
        mCheckout.loadInventory(request, new InventoryCallback());
    }

    private void purchase(final Sku sku) {
        final RequestListener<Purchase> listener = makeRequestListener();
        mCheckout.startPurchaseFlow(sku, null, listener);

        /*checkout.whenReady(new Checkout.ListenerAdapter() {
            @Override
            public void onReady(BillingRequests requests) {
                requests.purchase(sku, null, checkout.getPurchaseFlow());
            }

        });*/
    }

    private class InventoryCallback implements Inventory.Callback {

        @Override
        public void onLoaded(Inventory.Products products) {
            final Inventory.Product product = products.get(IN_APP);
            if (product.supported) {
                token_bear = null;
                token_coffee = null;
                token_premium = null;
                token_ticket = null;

                for (Sku sku : product.getSkus()) {
                    final Purchase purchase = product.getPurchaseInState(sku, Purchase.State.PURCHASED);
                    Log.i("CRUGA-PURCHASE", sku.id.toString());
                    Log.i("CRUGA-PURCHASE", purchase != null ? purchase.token : "NULL");

                    if(sku.id.toString().compareTo("inapp/"+MainActivity.BILLING_EXCLUSIVE_FEATURES) == 0) {
                        token_premium = purchase != null ? purchase.token : null;
                        sku_premium = sku;
                    }else if(sku.id.toString().compareTo("inapp/"+MainActivity.BILLING_BEER) == 0) {
                        token_bear = purchase != null ? purchase.token : null;
                        sku_beer = sku;
                    }else if(sku.id.toString().compareTo("inapp/"+MainActivity.BILLING_COFFEE) == 0) {
                        token_coffee = purchase != null ? purchase.token : null;
                        sku_coffee = sku;
                    }else if(sku.id.toString().compareTo("inapp/"+MainActivity.BILLING_TICKET) == 0) {
                        token_ticket = purchase != null ? purchase.token : null;
                        sku_ticket = sku;
                    }
                }
            } else {
                //emptyView.setText(R.string.billing_not_supported);
                Log.i("No support", "No support");
            }
            updateViews();
        }
    }

    private void updateViews(){

    }

    private void init_view() {
        menu_settings_apps = (RelativeLayout)rootView.findViewById(R.id.menu_settings_apps);
        menu_premium_features = (RelativeLayout)rootView.findViewById(R.id.menu_premium_fetures);
        menu_coffee = (RelativeLayout)rootView.findViewById(R.id.menu_coffee);
        menu_beer = (RelativeLayout)rootView.findViewById(R.id.menu_beer);
        menu_ticket = (RelativeLayout)rootView.findViewById(R.id.menu_ticket);
        menu_about = (RelativeLayout)rootView.findViewById(R.id.menu_about);
        txt_donations = (TextView)rootView.findViewById(R.id.txtDonations);
        linearLayoutPremium = (LinearLayout)rootView.findViewById(R.id.linearFeaturesFriends);

        switch3d = (Switch) rootView.findViewById(R.id.switch_3d);
        switchRotating = (Switch)rootView.findViewById(R.id.switch_rotating);
        switchShowText = (Switch)rootView.findViewById(R.id.switch_show_text);
        iconSizeSpinner = (Spinner)rootView.findViewById(R.id.icon_size_spinner);
    }

    private void set_listeners_menus(final MainActivity parentActivity) {
        txt_donations.setOnTouchListener(new TouchDonationsListener(getContext(), linearLayoutPremium));

        menu_settings_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.changeFragment(new AppsFragment());
            }
        });

        menu_premium_features.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBuy(sku_premium, null);
            }
        });

        menu_coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBuy(sku_coffee, token_coffee);
            }
        });

        menu_ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBuy(sku_ticket, token_ticket);
            }
        });

        menu_beer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBuy(sku_beer, token_bear);
            }
        });

        menu_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutFragment about = new AboutFragment();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, about, "fragment")
                        .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
            }
        });

    }
}
