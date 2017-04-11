package cruga.team.libs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.Animation.EaseEnum;
import com.nightonke.boommenu.Animation.OrderEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.BoomPiece;
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
    private Activity act;

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(Context context, int icon, int title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    public ResideMenuItem(Context context, Drawable icon, String title, String packagename) {
        super(context);
        initViews(context);
        iv_icon.setImageDrawable(icon);
        /*Animation pulse = AnimationUtils.loadAnimation(context, R.anim.pulse);
        iv_icon.startAnimation(pulse);
        */
        tv_title.setText(title);
        this.packagename = packagename;
    }

    public ResideMenuItem(Drawable icon, String title, String packagename, String appActivity, Activity act) {
        super(act);
        initViews(act);
        iv_icon.setImageDrawable(icon);
        /*Animation pulse = AnimationUtils.loadAnimation(context, R.anim.pulse);
        iv_icon.startAnimation(pulse);
        */
        tv_title.setText(title);
        this.packagename = packagename;
        this.appActivity = appActivity;
        this.act = act;

    }

    public String getPackageName() {
        return packagename;
    }

    public ResideMenuItem(Context context, int icon, String title) {
        super(context);
        initViews(context);
        iv_icon.setImageResource(icon);
        tv_title.setText(title);
    }

    private void initViews(final Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.residemenu_item, this);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
        bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);
        bmb.setOrderEnum(OrderEnum.DEFAULT);
        bmb.setShowMoveEaseEnum(EaseEnum.EaseOutBack);

        Rect padding = new Rect();
        padding.bottom = 40;
        padding.top = 40;
        padding.right = 40;
        padding.left = 40;

        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.app_add_48)
                .imagePadding(padding)
                .normalText(getContext().getString(R.string.add_to_circle_menu))
                .subNormalText(getContext().getString(R.string.add_to_circle_menu_long))
                .normalColor(getResources().getColor(R.color.green_add))
                .listener(new OnBMClickListener() {
                    @Override
                    public void onBoomButtonClick(int index) {
                        MainActivity mA = (MainActivity) act;
                        HomeFragment currentFragment = (HomeFragment) mA.getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                        if(currentFragment != null) {
                            if (currentFragment.getCircleMenu() != null) {
                                Set<String> mySet = Tools.getSharePrefset(act, MainActivity.PREF_CUSTOM_APPS);
                                mySet.add(appActivity);
                                Tools.setSharePref(act, MainActivity.PREF_CUSTOM_APPS, mySet);
                                ArrayList<App> customApps = Tools.obtenerCustomApps(act);
                                currentFragment.setItemsCircleMenu(customApps);
                            }
                        }
                    }
                });
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
                        intent.setData(Uri.parse("package:" + packagename));
                        intent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
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


}
