package cruga.team.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import cruga.team.clases.App;
import cruga.team.clases.Tools;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;
import cruga.team.listeners.CheckChangeListener;

/**
 * Created by christian on 4/08/16.
 */
public class AppsAdapter extends BaseAdapter {

    public ArrayList<App> apps = null;
    Activity acti;
    Set<String> mySet = null;

    public AppsAdapter(Activity activity, ArrayList<App> apps) {
        this.apps = apps;
        acti = activity;
        MainActivity mA = (MainActivity) activity ;

        mySet = mA.getSharedPref().getStringSet(MainActivity.PREF_CUSTOM_APPS, null);
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = acti.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item_app, null,true);

        TextView title = (TextView) rowView.findViewById(R.id.app_text);
        ImageView image = (ImageView) rowView.findViewById(R.id.app_icon);
        CheckBox check = (CheckBox) rowView.findViewById(R.id.app_check);

        title.setText(apps.get(position).label);
        image.setImageDrawable(apps.get(position).icono);
        check.setChecked(false);
        if(mySet != null) {
            Iterator iter = mySet.iterator();
            while (iter.hasNext()) {
                Object obj = iter.next();
                if(obj.toString().compareTo(apps.get(position).activity) == 0) {
                    check.setChecked(true);
                    break;
                }
            }
        }

        check.setOnCheckedChangeListener(new CheckChangeListener(acti, apps.get(position)));

        rowView.setOnClickListener(new OnItemClickListener( position ));

        return rowView;

    };


    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            CheckBox check = (CheckBox) arg0.findViewById(R.id.app_check);
            if(check.isChecked()) {
                check.setChecked(false);
            }else {
                check.setChecked(true);
            }
        }
    }
}
