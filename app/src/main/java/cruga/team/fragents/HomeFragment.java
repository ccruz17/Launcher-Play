package cruga.team.fragents;

/**
 * Created by christian on 12/07/16.
 */

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Parcel;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;

import cruga.team.clases.SortApps;
import cruga.team.libs.CircleMenu;
import cruga.team.libs.ResideMenu;

import cruga.team.clases.App;
import cruga.team.clases.Tools;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

public class HomeFragment extends Fragment {

    ViewGroup rootView;
    private ResideMenu resideMenu;
    public static CircleMenu circleMenu;
    public static ImageView setting_home = null;
    public static ArrayList<App> customApps = null;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1023;
    FloatingSearchView mSearchView;
    private String mLastQuery = "";
    MainActivity parentActivity;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == resultCode) {
            ArrayList<String> matches = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            //mSearchView.populateEditText(matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("CRUGA-EVENT", "HomeFragment-OnCreateView");
        //----------------------------------Inicializar vistas----------------------------------------\\
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        parentActivity = (MainActivity)getActivity();
        customApps = Tools.obtenerCustomApps(getActivity());

        //GET MENU
        resideMenu = parentActivity.getResideMenu();


        circleMenu = (CircleMenu) rootView.findViewById(R.id.circle_menu_items);
        setting_home = (ImageView) rootView.findViewById(R.id.settings_home);

        circleMenu.setFirstChildPosition(CircleMenu.FirstChildPosition.NORTH);
        loadPreferences();
        circleMenu.setItems(customApps, customApps.size());//pass items and number of visibles iteams

        setting_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getActivity().getTitle() == MainActivity.TITLE_HOME) {
                    resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                    getActivity().setTitle(MainActivity.TITLE_MENU_CONFIG);
                }
            }
        });

        /*circleMenu.setOnLongClickListener(new CircleMenu.OnLongClickListener() {

            @Override
            public void onLongClick() {
                if(getActivity().getTitle() == MainActivity.TITLE_HOME) {
                    resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
                    getActivity().setTitle(MainActivity.TITLE_MENU_CONFIG);
                }
            }
        });*/

        circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {

            @Override
            public void onItemClick(CircleMenu.ItemView view) {
                App currentApp  = customApps.get(view.getIdx());
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName(currentApp.packageName, currentApp.activity));
                getActivity().startActivity(intent);
            }
        });

        FrameLayout ignored_view = (FrameLayout) rootView.findViewById(R.id.fragment_ignore);
        resideMenu.addIgnoredView(ignored_view);


        rootView.findViewById(R.id.circle_menu_center).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity parentActivity = (MainActivity) getActivity();
                resideMenu = parentActivity.getResideMenu();
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
                getActivity().setTitle(MainActivity.TITLE_ALL_APSS);
            }
        });
        getActivity().setTitle(MainActivity.TITLE_HOME);
        mSearchView = (FloatingSearchView) rootView.findViewById(R.id.floating_search_view);

        final ArrayList<App> myApps = SortApps.ordenar_alfabeticamente(parentActivity.getAllApps());


        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                String myQuery = newQuery.trim();

                if(myQuery.compareTo("") != 0) {
                    ArrayList<SearchSuggestion> l = new ArrayList<SearchSuggestion>();

                    mSearchView.clearSuggestions();

                    App unique = null;
                    for(int i = 0; i<myApps.size(); i++) {
                        final String name = myApps.get(i).label;
                        String rgx = "(?i).*" +myQuery+".*";
                        final int idx = i;
                        if(name.matches(rgx)) {

                            unique = myApps.get(i);
                            //pass them on to the search view
                            SearchSuggestion sug = new SearchSuggestion() {
                                @Override
                                public String getBody() {
                                    return name;
                                }

                                @Override
                                public int describeContents() {
                                    return idx;
                                }

                                @Override
                                public void writeToParcel(Parcel dest, int flags) {

                                }
                            };
                            l.add(sug);
                            mSearchView.swapSuggestions(l);
                        }
                    }

                    if(l.size() == 1 && myQuery.length()>=3) {
                        mSearchView.clearSearchFocus();
                        mSearchView.clearQuery();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.setComponent(new ComponentName(unique.packageName, unique.activity));
                        getActivity().startActivity(intent);
                    }
                } else {
                    mSearchView.clearSuggestions();
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {
                mSearchView.clearSearchFocus();
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName(myApps.get(searchSuggestion.describeContents()).packageName, myApps.get(searchSuggestion.describeContents()).activity));
                getActivity().startActivity(intent);
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                Log.d("CCG", "onSearchAction()");
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, query); // query contains search string
                startActivity(intent);
            }

        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                leftIcon.setImageDrawable(myApps.get(item.describeContents()).icono);
                textView.setTextColor(getResources().getColor(R.color.cardview_dark_background));
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

            }

            @Override
            public void onFocusCleared() {
                mSearchView.clearQuery();
            }
        });

        return rootView;
    }

    private void loadPreferences(){
        if(Tools.getSharePref(getActivity(), MainActivity.PREF_ROTATION).compareTo("") == 0) {
            circleMenu.setRotating(false);//enable rotation
        }else {
            circleMenu.setRotating(true);//enable rotation
        }

        if(Tools.getSharePref(getActivity(), MainActivity.PREF_SIZE_ICON).compareTo("") == 0) {
            circleMenu.setIconSize(MainActivity.ICON_SIZES[1]);
        }else {
            int pos = Integer.parseInt(Tools.getSharePref(getActivity(), MainActivity.PREF_SIZE_ICON));
            circleMenu.setIconSize(MainActivity.ICON_SIZES[pos]);
        }

        if(Tools.getSharePref(getActivity(), MainActivity.PREF_SHOW_FONT).compareTo("") == 0) {
            circleMenu.setShowFont(false);
        }else {
            circleMenu.setShowFont(true);
        }

    }

    @Override
    public void onPause() {
        Log.i("CRUGA-EVENT", "HomeFragment-OnPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        Log.i("CRUGA-EVENT", "HomeFragment-OnResumen");
        super.onResume();
    }

}
