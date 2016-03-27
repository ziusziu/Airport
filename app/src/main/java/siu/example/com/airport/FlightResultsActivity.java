package siu.example.com.airport;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class FlightResultsActivity extends AppCompatActivity {
    private static final String TAG = FlightResultsActivity.class.getSimpleName();

    private static AirportsSQLiteHelper mAirportDb;
    private static CursorAdapter mCursorAdapter;
    protected static ListView mListView;
    private static String[] searchTerms;
    private static String searchTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_results);

        mAirportDb = AirportsSQLiteHelper.getInstance(getApplicationContext());

        getSearchTerms();

        if (searchTerms != null) {
            showSearchResults(searchTerm);
        } else {
            handleMenuSearchIntent(getIntent());
        }

        Utils.onItemClickToDetail(FlightResultsActivity.this, getApplicationContext(), mListView);

    }

    @Override
    protected void onResume() {
        Utils.onItemClickToDetail(FlightResultsActivity.this, getApplicationContext(), mListView);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        initMenuBar(menu);
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleMenuSearchIntent(intent);
    }


    /**
     * Initializes search Menu in Action Bar
     * @param menu
     */
    private void initMenuBar(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);
        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        Drawable collapsedSearchIcon = searchItem.getIcon();
        collapsedSearchIcon.setTint(Color.WHITE);
        searchItem.setIcon(collapsedSearchIcon);
    }


    /**
     * Store search term passed from MainActivity
     */
    private void getSearchTerms(){
        Intent receivedIntent = getIntent();
        searchTerms = receivedIntent.getStringArrayExtra(Utils.INTENT_SEARCH_KEY);

        searchTerm = PreferenceManager.getDefaultSharedPreferences(FlightResultsActivity.this)
                .getString(Utils.SHARED_PREFERENCES_SEARCHTERM, "California");
        Log.d(TAG, "==================>" + searchTerm);
    }

    /**
     * Performs query with persisted searchTerms and updates cursorAdapter with results
     * @param searchTerms
     */
    private void showSearchResults(String searchTerms) {
        Cursor cursor = mAirportDb.searchAirportsList(searchTerms);
        mCursorAdapter = new CursorAdapter(getApplicationContext(), cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.airport_adapter_results_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView airportNameTextView = (TextView) view.findViewById(R.id.airport_name_textview);
                TextView airportCityTextView = (TextView) view.findViewById(R.id.airport_city_textview);

                ImageView imageView = (ImageView) view.findViewById(R.id.airport_imageView);

                airportNameTextView.setText(cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_NAME)));
                airportCityTextView.setText(cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_CITY)));

                Picasso.with(context)
                        .load(R.drawable.icon_search)
                        .resize(100,100)
                        .into(imageView);

            }
        };

        mListView = (ListView) findViewById(R.id.airport_listView);
        mListView.setAdapter(mCursorAdapter);
    }

    /**
     * Handle search queries from Action Bar and repopulates search items in adapter
     * @param intent
     */
    private void handleMenuSearchIntent(Intent intent) {
        Log.d(TAG, "MENU SEARCH HANDLEINTENT=====");
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, query);
            Cursor cursor = mAirportDb.menuSearchAirportsList(query);
            mCursorAdapter.swapCursor(cursor);

            mListView = (ListView) findViewById(R.id.airport_listView);
            mListView.setAdapter(mCursorAdapter);

            mCursorAdapter.changeCursor(cursor);
            mCursorAdapter.notifyDataSetChanged();
        }
    }

}
