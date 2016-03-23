package siu.example.com.airport;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FlightResultsActivity extends AppCompatActivity {
    private static final String TAG = FlightResultsActivity.class.getSimpleName();
    protected static final String INTENT_DETAILED_KEY = "detailed_id";
    private static AirportsSQLiteHelper mAirportDb;

    private static CursorAdapter mCursorAdapter;
    private static ListView mListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_results);

        mAirportDb = AirportsSQLiteHelper.getInstance(getApplicationContext());

        Intent receivedIntent = getIntent();
        String[] searchTerms = receivedIntent.getStringArrayExtra(MainActivity.INTENT_KEY);

        if(searchTerms != null) {
            showSearchResults(searchTerms);
        }else{
            handleMenuSearchIntent(getIntent());
        }

        onItemClick();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleMenuSearchIntent(intent);
    }

    private void handleMenuSearchIntent(Intent intent){
        Log.d(TAG, "MENU SEARCH HANDLEINTENT=====");
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
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

    private void showSearchResults(String[] searchTerms){
        Cursor cursor = mAirportDb.searchAirportsList(searchTerms);
        mCursorAdapter = new CursorAdapter(getApplicationContext(), cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.airport_adapter_results_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView airportNameTextView = (TextView) view.findViewById(R.id.airport_name_textview);

                airportNameTextView.setText(cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_NAME)));
            }
        };

        mListView = (ListView) findViewById(R.id.airport_listView);
        mListView.setAdapter(mCursorAdapter);
    }


    private void onItemClick(){
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FlightResultsActivity.this, DetailedActivity.class);
                intent.putExtra(INTENT_DETAILED_KEY, id);
                startActivity(intent);
            }
        });
    }




}
