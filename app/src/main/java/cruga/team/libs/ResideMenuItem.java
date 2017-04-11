package cruga.team.libs;

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

import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

public class ResideMenuItem extends LinearLayout{

    /** menu item  icon  */
    private ImageView iv_icon;
    /** menu item  title */
    private TextView tv_title;
    BoomMenuButton bmb;
    private String packagename = null;


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
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_2);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_2);
        bmb.setOrderEnum(OrderEnum.DEFAULT);
        bmb.setShowMoveEaseEnum(EaseEnum.EaseOutBack);

        Rect padding = new Rect();
        padding.bottom = 30;
        padding.top = 30;
        padding.right = 30;
        padding.left = 30;
        HamButton.Builder builder = new HamButton.Builder()
                .normalImageRes(R.drawable.trash)
                .imagePadding(padding)
                .normalText("Unistall")
                .subNormalText("Remove this app")
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
                .normalText("App info")
                .subNormalText("Go to app settings info")
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
