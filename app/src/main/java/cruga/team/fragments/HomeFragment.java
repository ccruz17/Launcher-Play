package cruga.team.fragments;

/**
 * Created by christian on 12/07/16.
 */

import android.app.Activity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import cruga.team.clases.SortApps;
import cruga.team.libs.CircleMenu;
import cruga.team.libs.ResideMenu;

import cruga.team.clases.App;
import cruga.team.launcher_play.MainActivity;
import cruga.team.launcher_play.R;

public class HomeFragment extends Fragment {

    ViewGroup rootView;
    private ResideMenu resideMenu;
    public static CircleMenu circleMenu;
    public static ImageView setting_home = null;
    public static ArrayList<App> customApps = null;
    FloatingSearchView mSearchView;
    private String GoogleQuery = "";
    MainActivity parentActivity;
    MenuItem mActionVoice;
    private SharedPreferences mSecurePrefs;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == -1) {
            ArrayList<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            mSearchView.setSearchFocused(true);
            mSearchView.setSearchText(results.get(0));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("CRUGA-EVENT", "HomeFragment-OnCreateView");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));
        }
        //----------------------------------Inicializar vistas----------------------------------------\\
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        parentActivity = (MainActivity)getActivity();
        //Get customApps
        //CCG customApps = Tools.obtenerCustomApps(getActivity());
        //GET MENU
        resideMenu = parentActivity.getResideMenu();

        circleMenu = (CircleMenu) rootView.findViewById(R.id.circle_menu_items);
        loadPreferences();
        parentActivity.updateCustomApps();

        setting_home = (ImageView) rootView.findViewById(R.id.settings_home);
        mSecurePrefs = parentActivity.getSharedPref();

        //CCG - circleMenu.setItems(customApps, customApps.size());//pass items and number of visibles iteams

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

        /*
        circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {

            @Override
            public void onItemClick(CircleMenu.ItemView view) {
                App currentApp  = customApps.get(view.getIdx());

                PackageManager pm = parentActivity.getPackageManager();
                Intent intent = pm.getLaunchIntentForPackage(currentApp.packageName);

                intent.setComponent(new ComponentName(currentApp.packageName, currentApp.activity));
                getActivity().startActivity(intent);
            }
        });
        */

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

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                String myQuery = newQuery.trim();

                if(myQuery.compareTo("") != 0) {
                    ArrayList<SearchSuggestion> l = new ArrayList<SearchSuggestion>();

                    mSearchView.clearSuggestions();

                    App unique = null;
                    for(int i = 0; i<parentActivity.getAllApps().size(); i++) {
                        final String name = parentActivity.getAllApps().get(i).label;
                        String rgx = "(?i).*" +myQuery+".*";
                        final int idx = i;
                        if(name.matches(rgx)) {

                            unique = parentActivity.getAllApps().get(i);
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

                    GoogleQuery = myQuery;

                    if(l.size() == 0 && myQuery.length()>=3) {
                        SearchSuggestion sug = new SearchSuggestion() {
                            @Override
                            public String getBody() {
                                return getString(R.string.search_with_google);
                            }

                            @Override
                            public int describeContents() {
                                return -1;
                            }

                            @Override
                            public void writeToParcel(Parcel dest, int flags) {

                            }
                        };
                        l.add(sug);
                        mSearchView.swapSuggestions(l);
                    }
                } else {
                    mSearchView.clearSuggestions();
                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                if(searchSuggestion.describeContents() == -1) {
                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, GoogleQuery); // query contains search string
                    startActivity(intent);
                } else {
                    mSearchView.clearSearchFocus();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setComponent(new ComponentName(parentActivity.getAllApps().get(searchSuggestion.describeContents()).packageName, parentActivity.getAllApps().get(searchSuggestion.describeContents()).activity));
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void onSearchAction(String query) {
                GoogleQuery = query;
                Log.d("CCG", "onSearchAction()");
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, query); // query contains search string
                startActivity(intent);
            }

        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                if(item.describeContents() == -1) {
                    leftIcon.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.google_search));
                    textView.setTextColor(getResources().getColor(R.color.cardview_dark_background));
                } else {
                    leftIcon.setImageDrawable(parentActivity.getAllApps().get(item.describeContents()).icono);
                    textView.setTextColor(getResources().getColor(R.color.cardview_dark_background));
                }
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

        mActionVoice = (MenuItem) rootView.findViewById(R.id.action_voice_rec);

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {
                if(checkVoiceRecognition(getActivity())) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    //... put other settings in the Intent
                    startActivityForResult(intent, 0);
                } else {

                }
            }
        });

        return rootView;
    }


    private void loadPreferences(){

        circleMenu.setFirstChildPosition(CircleMenu.FirstChildPosition.NORTH);
        if(parentActivity.getSharedPref().getString(MainActivity.PREF_ROTATION, "").compareTo("") == 0) {
            circleMenu.setRotating(false);//enable rotation

            if(resideMenu.isInDisableDirection(ResideMenu.DIRECTION_LEFT)) {
                resideMenu.removeSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
            }
            if(resideMenu.isInDisableDirection(ResideMenu.DIRECTION_RIGHT)) {
                resideMenu.removeSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
            }
        }else {
            circleMenu.setRotating(true);//enable rotation
            resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
            resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
        }

        if(parentActivity.getSharedPref().getString(MainActivity.PREF_SIZE_ICON, "").compareTo("") == 0) {
            circleMenu.setIconSize(MainActivity.ICON_SIZES[1]);
        }else {
            int pos = Integer.parseInt(parentActivity.getSharedPref().getString(MainActivity.PREF_SIZE_ICON, "1"));
            circleMenu.setIconSize(MainActivity.ICON_SIZES[pos]);
        }

        if(parentActivity.getSharedPref().getString(MainActivity.PREF_SHOW_FONT, "").compareTo("") == 0) {
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

    public CircleMenu getCircleMenu() {
        return circleMenu;
    }

    public void setItemsCircleMenu(ArrayList<App> customApps){
        this.customApps = customApps;
        circleMenu.setItems(customApps, customApps.size());
    }

    public ArrayList<App> getItemsCircleManu(){
        return customApps;
    }

    public boolean checkVoiceRecognition(Activity act) {
        // Check if voice recognition is present
        PackageManager pm = act.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(
                RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() == 0) {
            return false;
        }
        return true;
    }

    public void updateOnClickListener() {
        circleMenu.setOnItemClickListener(new CircleMenu.OnItemClickListener() {

            @Override
            public void onItemClick(CircleMenu.ItemView view) {
                App currentApp  = customApps.get(view.getIdx());
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setComponent(new ComponentName(currentApp.packageName, currentApp.activity));
                getActivity().startActivity(intent);
            }
        });

    }
}
