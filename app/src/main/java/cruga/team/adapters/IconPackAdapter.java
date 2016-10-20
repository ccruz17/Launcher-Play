package cruga.team.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cruga.team.clases.App;
import cruga.team.launcher_play.R;

/**
 * Created by christian on 14/09/16.
 */
public class IconPackAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<App> themes;
    public IconPackAdapter(Context context, ArrayList<App> themes) {
        layoutInflater = LayoutInflater.from(context);
        this.themes = themes;
    }

    @Override
    public int getCount() {
        return themes.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.simple_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.image_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Context context = parent.getContext();
        viewHolder.textView.setText(themes.get(position).label);
        viewHolder.imageView.setImageDrawable(themes.get(position).icono);


        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}