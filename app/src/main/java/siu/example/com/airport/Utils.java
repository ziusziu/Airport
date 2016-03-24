package siu.example.com.airport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * Created by samsiu on 3/23/16.
 */
public class Utils {
    private static String TAG = Utils.class.getSimpleName();

    protected static final String INTENT_DETAILED_KEY = "detailed_id";
    protected static final String INTENT_SEARCH_KEY = "search_terms";
    protected static final String SHARED_PREFERENCES_SEARCHTERM = "shared_pref_search_term";
    protected static final String ADD_TO_FAVORITES = "true";
    protected static final String REMOVE_FROM_FAVORITES = "false";

    //protected static final String FAB_BUTTON_COLOR = "#558B2F";
    protected static final String FAB_BUTTON_COLOR = "#00C853";

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



}
