package siu.example.com.airport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by samsiu on 3/23/16.
 */
public class Utils {
    private static String TAG = Utils.class.getSimpleName();

    //region Protected Variables
    protected static final String INTENT_DETAILED_KEY = "detailed_id";
    protected static final String INTENT_SEARCH_KEY = "search_terms";
    protected static final String SHARED_PREFERENCES_SEARCHTERM = "shared_pref_search_term";
    protected static final String ADD_TO_FAVORITES = "true";
    protected static final String REMOVE_FROM_FAVORITES = "false";
    protected static final String FAB_BUTTON_COLOR = "#00C853"; //"#558B2F"
    //endregion Protected Variables

    /**
     * On Item click, persist id and start DeatiledActivity
     *
     * @param activity
     * @param context
     * @param currentListView
     */
    protected static void onItemClickToDetail(final Activity activity,final Context context, ListView currentListView){
        currentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra(Utils.INTENT_DETAILED_KEY, id);
                activity.startActivity(intent);
            }
        });
    }

    /**
     * Change the color of FAB Image Resource
     */
    protected static void setFabIconColor(FloatingActionButton searchFab, String fabColor){
        int color = Color.parseColor(fabColor);
        searchFab.setImageResource(R.drawable.icon_search);
        searchFab.setColorFilter(color);
    }


}
