package cruga.team.libs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nightonke.boommenu.Animation.EaseEnum;
import com.nightonke.boommenu.Animation.OrderEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.ArrayList;
import java.util.Set;

import cruga.team.clases.App;
import cruga.team.clases.Tools;
import cruga.team.fragments.HomeFragment;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

public class ResideMenuItem extends LinearLayout{

    /** menu item  icon  */
    private ImageView iv_icon;
    /** menu item  title */
    private TextView tv_title;
    BoomMenuButton bmb;
    private String packagename = null;
    private String appActivity = null;

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(Drawable icon, String title, String packagename, String appActivity, Activity act, Context context) {
        super(act);
        this.packagename = packagename;
        this.appActivity = appActivity;

        initViews(context);
        loadBmbMenu(context, act);
        iv_icon.setImageDrawable(icon);
        /*Animation pulse = AnimationUtils.loadAnimation(context, R.anim.pulse);
        iv_icon.startAnimation(pulse);
        */
        tv_title.setText(title);


    }

    public ResideMenuItem(Context context, int icon, String title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    private void initViews(final Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(appActivity != null) {
            inflater.inflate(R.layout.residemenu_item_apps, this);
        } else {
            inflater.inflate(R.layout.residemenu_item, this);
        }

        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
    }

    public void loadBmbMenu(final Context context, final Activity act){

        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);
        bmb.setOrderEnum(OrderEnum.DEFAULT);
        bmb.setShowMoveEaseEnum(EaseEnum.EaseOutBack);

        final Rect padding = new Rect();
        padding.bottom = 40;
        padding.top = 40;
        padding.right = 40;
        padding.left = 40;
        final MainActivity parentActivity = (MainActivity) act;

        Set<String> appsPreferences = parentActivity.getSharedPref().getStringSet(MainActivity.PREF_CUSTOM_APPS, null);
        Boolean existAppPreference = false;
        HamButton.Builder builder;

        if(appsPreferences != null) {
             existAppPreference = appsPreferences.contains(appActivity);
        }

        HomeFragment currentFragment = (HomeFragment) ((MainActivity)act).getSupportFragmentManager().findFragmentById(R.id.fragment_container);


        if(existAppPreference){
            builder = new HamButton.Builder()
                    .normalImageRes(R.drawable.app_remove_48)
                    .imagePadding(padding)
                    .normalText(getContext().getString(R.string.remove_to_circle_menu))
                    .subNormalText(getContext().getString(R.string.remove_to_circle_menu_long))
                    .normalColor(getResources().getColor(R.color.amber_remove))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            HomeFragment currentFragment = (HomeFragment) ((MainActivity)act).getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            if(currentFragment != null) {
                                if (currentFragment.getCircleMenu() != null) {

                                    Set<String> appsPref  = parentActivity.getSharedPref().getStringSet(MainActivity.PREF_CUSTOM_APPS, null);
                                    appsPref.remove(appActivity);
                                    parentActivity.getSharedPref().edit().putStringSet(MainActivity.PREF_CUSTOM_APPS, appsPref).commit();

                                    //Tools.setSharePref(act, MainActivity.PREF_CUSTOM_APPS, appsPreferences);
                                    ArrayList<App> customApps = Tools.obtenerCustomApps(act, parentActivity.allApps);
                                    currentFragment.setItemsCircleMenu(customApps);
                                    currentFragment.updateOnClickListener();
                                    loadBmbMenu(context, act);
                                    act.setTitle(MainActivity.TITLE_ALL_APSS);
                                }
                            }
                        }
                    });
        } else {
            builder = new HamButton.Builder()
                    .normalImageRes(R.drawable.app_add_48)
                    .imagePadding(padding)
                    .normalText(getContext().getString(R.string.add_to_circle_menu))
                    .subNormalText(getContext().getString(R.string.add_to_circle_menu_long))
                    .normalColor(getResources().getColor(R.color.green_add))
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            HomeFragment currentFragment = (HomeFragment) ((MainActivity)act).getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                            if(currentFragment != null) {
                                if (currentFragment.getCircleMenu() != null) {
                                    Set<String> appsPref  = parentActivity.getSharedPref().getStringSet(MainActivity.PREF_CUSTOM_APPS, null);
                                    appsPref.add(appActivity);
                                    parentActivity.getSharedPref().edit().putStringSet(MainActivity.PREF_CUSTOM_APPS, appsPref).commit();
                                    Log.i("CCG", "Remove");
                                    //Tools.setSharePref(act, MainActivity.PREF_CUSTOM_APPS, appsPreferences);
                                    ArrayList<App> customApps = Tools.obtenerCustomApps(act, parentActivity.allApps);
                                    currentFragment.setItemsCircleMenu(customApps);
                                    currentFragment.updateOnClickListener();
                                    loadBmbMenu(context, act);
                                    act.setTitle(MainActivity.TITLE_ALL_APSS);
                                }
                            }
                        }
                    });
        }

        bmb.addBuilder(builder);

        builder = new HamButton.Builder()
                .normalImageRes(R.drawable.trash)
                .imagePadding(padding)
                .normalText(getContext().getString(R.string.uninstall))
                .subNormalText(getContext().getString(R.string.remove_this_app))
                .normalColor(getResources().getColor(R.color.red_uninstall))
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setData(Uri.parse("package:" + packagename));
                        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
                        act.setTitle(MainActivity.TITLE_ALL_APSS);
                        context.startActivity(intent);
                    }
                });
        bmb.addBuilder(builder);

        builder = new HamButton.Builder()
                .normalImageRes(R.drawable.app_info)
                .imagePadding(padding)
                .normalText(getContext().getString(R.string.app_info))
                .subNormalText(getContext().getString(R.string.go_to_app_info))
                .normalColor(getResources().getColor(R.color.blue_info))
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + packagename));
                        act.setTitle(MainActivity.TITLE_ALL_APSS);
                        context.startActivity(i);

                    }
                });


        bmb.addBuilder(builder);
    }

    /**
     * set the icon color;
     *
     * @param icon
     */
    public void setIcon(int icon){
        iv_icon.setImageResource(icon);
    }

    /**
     * set the title with resource
     * ;
     * @param title
     */
    public void setTitle(int title){
        tv_title.setText(title);
    }

    /**
     * set the title with string;
     *
     * @param title
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }

    public  BoomMenuButton getBoomMenu() {
        return bmb;
    }

    public String getPackagename() {
        return packagename;
    }

}
