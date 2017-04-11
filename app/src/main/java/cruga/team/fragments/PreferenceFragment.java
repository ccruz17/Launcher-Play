package cruga.team.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;
import org.solovyev.android.checkout.ResponseCodes;
import org.solovyev.android.checkout.Sku;

import cruga.team.libs.ResideMenu;
import cruga.team.clases.Tools;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;
import cruga.team.listeners.TouchDonationsListener;

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


    RelativeLayout menu_settings_apps, menu_premium_features, menu_coffee, menu_beer, menu_ticket, menu_about;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.black));
        }

        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_settings, container, false);

        final MainActivity parentActivity = (MainActivity)getActivity();
        resideMenu = parentActivity.getResideMenu();

        menu_settings_apps = (RelativeLayout)rootView.findViewById(R.id.menu_settings_apps);
        menu_premium_features = (RelativeLayout)rootView.findViewById(R.id.menu_premium_fetures);
        menu_coffee = (RelativeLayout)rootView.findViewById(R.id.menu_coffee);
        menu_beer = (RelativeLayout)rootView.findViewById(R.id.menu_beer);
        menu_ticket = (RelativeLayout)rootView.findViewById(R.id.menu_ticket);
        menu_about = (RelativeLayout)rootView.findViewById(R.id.menu_about);

        txt_donations = (TextView)rootView.findViewById(R.id.txtDonations);
        linearLayoutPremium = (LinearLayout)rootView.findViewById(R.id.linearFeaturesFriends);

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

        menu_beer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendBuy(sku_beer, token_bear);
            }
        });



        Switch switch3d = (Switch) rootView.findViewById(R.id.switch_3d);
        Switch switchRotating = (Switch)rootView.findViewById(R.id.switch_rotating);
        Switch switchShowText = (Switch)rootView.findViewById(R.id.switch_show_text);
        Spinner iconSizeSpinner = (Spinner)rootView.findViewById(R.id.icon_size_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.icon_size_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        iconSizeSpinner.setAdapter(adapter);

        iconSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Tools.setSharePref(getActivity(), MainActivity.PREF_SIZE_ICON, position+"");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(Tools.getSharePref(getActivity(), MainActivity.PREF_SIZE_ICON).compareTo("") == 0) {
            iconSizeSpinner.setSelection(1);
        }else {
            int pos = Integer.parseInt(Tools.getSharePref(getActivity(), MainActivity.PREF_SIZE_ICON));
            iconSizeSpinner.setSelection(pos);
        }

        switch3d.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked) {
                    resideMenu.setUse3D(checked);
                    resideMenu.setScaleValue(0.6f);
                    Tools.setSharePref(getActivity(), MainActivity.PREF_3D_ANIMATION, "TRUE");
                } else {
                    resideMenu.setUse3D(checked);
                    resideMenu.setScaleValue(0.5f);
                    Tools.setSharePref(getActivity(), MainActivity.PREF_3D_ANIMATION, "");
                }
            }
        });

        switchRotating.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked) {
                    Tools.setSharePref(getActivity(), MainActivity.PREF_ROTATION, "TRUE");
                } else {
                    Tools.setSharePref(getActivity(), MainActivity.PREF_ROTATION, "");
                }
            }
        });

        switchShowText.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked) {
                    Tools.setSharePref(getActivity(), MainActivity.PREF_SHOW_FONT, "TRUE");
                } else {
                    Tools.setSharePref(getActivity(), MainActivity.PREF_SHOW_FONT, "");
                }
            }
        });

        if(Tools.getSharePref(getActivity(), MainActivity.PREF_3D_ANIMATION).compareTo("") == 0) {
            switch3d.setChecked(false);
        }else {
            switch3d.setChecked(true);
        }

        if(Tools.getSharePref(getActivity(), MainActivity.PREF_ROTATION).compareTo("") == 0) {
            switchRotating.setChecked(false);
        }else {
            switchRotating.setChecked(true);
        }

        if(Tools.getSharePref(getActivity(), MainActivity.PREF_SHOW_FONT).compareTo("") == 0) {
            switchShowText.setChecked(false);
        }else {
            switchShowText.setChecked(true);
        }


        parentActivity.setTitle(MainActivity.TITLE_MENU_SETTINGS);

        LinearLayout ignored_view = (LinearLayout) rootView.findViewById(R.id.fragment_ignore);
        resideMenu.addIgnoredView(ignored_view);


        inventory.whenLoaded(new InventoryLoadedListener());
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkout.createPurchaseFlow(new PurchaseListener());
        Log.i("CRUGA-EVENT", "onCreatePreferenceFragment");
    }

    @Override
    public void onDestroy() {
        checkout.destroyPurchaseFlow();
        super.onDestroy();
        Log.i("CRUGA-EVENT", "onDestroyPreferencesFragment");
    }


    private class PurchaseListener extends BaseRequestListener<Purchase> {
        @Override
        public void onSuccess(Purchase purchase) {
            onPurchased();
            Log.i("CRUGA-EVENT", "onSuccessPurchase");

        }

        private void onPurchased() {
            // let's update purchase information in local inventory
            inventory.load().whenLoaded(new InventoryLoadedListener());
            updateViews();
        }

        @Override
        public void onError(int response, Exception e) {
            // it is possible that our data is not synchronized with data on Google Play => need to handle some errors
            if (response == ResponseCodes.ITEM_ALREADY_OWNED) {
                onPurchased();
            } else {
                super.onError(response, e);
            }
            Log.i("CRUGA", "onErrrorPurchase");
        }
    }


    private void sendBuy(Sku sku, String token) {
        MainActivity.HOME_PRESS = false;
        if(token == null) {
            purchase(sku);
        } else {
            consume(token, new ConsumeListener());
        }
    }

    private void consume(final String token, final RequestListener<Object> onConsumed) {
        checkout.whenReady(new Checkout.ListenerAdapter() {
            @Override
            public void onReady(BillingRequests requests) {
                requests.consume(token, onConsumed);
            }
        });
    }

    private void purchase(final Sku sku) {
        checkout.whenReady(new Checkout.ListenerAdapter() {
            @Override
            public void onReady(BillingRequests requests) {
                requests.purchase(sku, null, checkout.getPurchaseFlow());
            }

        });
    }

    private class InventoryLoadedListener implements Inventory.Listener {
        @Override
        public void onLoaded( Inventory.Products products) {


            final Inventory.Product product = products.get(IN_APP);
            if (product.supported) {
                token_bear = null;
                token_coffee = null;
                token_premium = null;
                token_ticket = null;

                for (Sku sku : product.getSkus()) {
                    final Purchase purchase = product.getPurchaseInState(sku, Purchase.State.PURCHASED);

                    if(sku.id.compareTo(MainActivity.BILLING_EXCLUSIVE_FEATURES) == 0) {
                        sku_premium = sku;
                        token_premium = purchase != null ? purchase.token : null;
                    }else if(sku.id.compareTo(MainActivity.BILLING_BEER) == 0) {
                        sku_beer = sku;
                        token_bear = purchase != null ? purchase.token : null;
                    }else if(sku.id.compareTo(MainActivity.BILLING_COFFEE) == 0) {
                        token_coffee = purchase != null ? purchase.token : null;
                        sku_coffee = sku;
                    }else if(sku.id.compareTo(MainActivity.BILLING_TICKET) == 0) {
                        token_ticket = purchase != null ? purchase.token : null;
                        sku_ticket = sku;
                    }
                    Log.i("CRIGA-ID", sku.id);
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

    private abstract class BaseRequestListener<R> implements RequestListener<R> {

        @Override
        public void onError(int response, Exception e) {

        }
    }


    private class ConsumeListener extends BaseRequestListener<Object> {
        @Override
        public void onSuccess(Object result) {
            onConsumed();
        }

        private void onConsumed() {
            inventory.load().whenLoaded(new InventoryLoadedListener());
            Toast.makeText(getActivity(), "Item consumido", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(int response, Exception e) {
            // it is possible that our data is not synchronized with data on Google Play => need to handle some errors
            if (response == ResponseCodes.ITEM_NOT_OWNED) {
                onConsumed();
            } else {
                super.onError(response, e);
            }
        }
    }
}
