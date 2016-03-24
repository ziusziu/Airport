package siu.example.com.airport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    private static AirportsSQLiteHelper mAirportDb;

    private EditText mNameEditText;
    private FloatingActionButton mFlightSearchFabButton;
    private ListView mFavoritesListView;
    private CursorAdapter mFavoritesCursorAdapter;
    private TextView mFavTitleTextView;

    //TODO Add a background image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d(TAG, "CONTEXT OF APP =======getBASECONTEXT   " + this.getBaseContext());

        initViews();
        mAirportDb = AirportsSQLiteHelper.getInstance(getApplicationContext());


        int color = Color.parseColor(Utils.FAB_BUTTON_COLOR);
        mFlightSearchFabButton.setImageResource(R.drawable.icon_search);
        mFlightSearchFabButton.setColorFilter(color);


        //mAirportDb.deleteAll();
        //insertAirportData();

        onFabSearchButtonClick();

        Utils.onItemClickToDetail(MainActivity.this, getApplicationContext(), mFavoritesListView);

    }


    @Override
    protected void onResume() {
        displayFavorites();
        super.onResume();
    }


    private void initViews(){
        mNameEditText = (EditText)findViewById(R.id.main_name_editText);
        mFlightSearchFabButton = (FloatingActionButton)findViewById(R.id.main_flightInput_search_fab_button);
        mFavoritesListView = (ListView)findViewById(R.id.main_favorites_listView);
        mFavTitleTextView = (TextView)findViewById(R.id.main_favoriteTitle_TextView);
    }

    private void onFabSearchButtonClick(){
        mFlightSearchFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] searchTerms = {
                        mNameEditText.getText().toString(),
                };

                PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                        .edit()
                        .putString(Utils.SHARED_PREFERENCES_SEARCHTERM, searchTerms[0])
                        .apply();
                        //.commit();

                Intent mFlightResultsIntent = new Intent(MainActivity.this, FlightResultsActivity.class);
                mFlightResultsIntent.putExtra(Utils.INTENT_SEARCH_KEY, searchTerms);
                startActivity(mFlightResultsIntent);
            }
        });
    }



    private static void insertAirportData(){
        Airport sf = new Airport("San Francisco International Airport", 37.618763, -122.3823531, "San Francisco International Airport PO Box 8097", "San Francisco", "California", 94128, "San Francisco International Airport is an international airport 13 miles south of downtown San Francisco, California, United States, near Millbrae and San Bruno in unincorporated San Mateo County.", "false");
        Airport oakland = new Airport("Oakland International Airport", 37.711786, -122.220581, "1 Airport Dr", "Oakland", "California", 94621, "Oakland International Airport is five miles south of downtown Oakland, in Alameda County, California. It is owned by the Port of Oakland. It is one of three international airports in the San Francisco Bay Area.", "false");
        Airport hayward = new Airport("Hayward Executive Airport", 37.658047, -122.121785, "20301 Skywest Dr", "Hayward", "Californai", 94541, "Hayward Executive Airport is a city owned public airport two miles west of downtown Hayward, in Alameda County, California. The National Plan of Integrated Airport Systems for 2011–2015 categorized it as a reliever airport", "false");
        Airport sanCarlos = new Airport("San Carlos Airport", 37.513498, -122.250624, "620 Airport Way", "San Carlos", "California", 94070, "San Carlos Airport is two miles northeast of San Carlos, California, in San Mateo County, California. The FAA's National Plan of Integrated Airport Systems classifies San Carlos as a reliever airport for San Francisco International Airport.", "false");
        Airport halfMoonBay = new Airport("Half Moon Bay Airport", 37.512259, -122.496414, "9850 Cabrillo Hwy", "Half Moon Bay", "California", 94019, "Eddie Andreini Sr. Airfield is a public airport in San Mateo County, six miles northwest of Half Moon Bay, California. The airport is on the Pacific Coast, south of San Francisco", "false");
        Airport paloAlto = new Airport("Palo Alto Airport", 37.454724, -122.110723, "1925 Embarcadero Rd.", "Palo Alto", "California", 94303, "Palo Alto Airport is a general aviation airport in the city of Palo Alto in Santa Clara County, California, USA, near the south end of San Francisco Bay on the western shore.", "false");
        Airport sanJose = new Airport("Mineta San Jose International Airport", 37.363332, -121.928996, "1701 Airport Blvd", "San Jose", "California", 95110, "Norman Y. Mineta San José International Airport is a city-owned public airport in San Jose, Santa Clara County, California", "false");
        Airport livermore = new Airport("Livermore Municipal Airport", 37.695737, -121.818811, "636 Terminal Cir", "Livermore", "California", 94551, "Livermore Municipal Airport is in Livermore, California, USA, east of San Francisco Bay. The airport is three miles northwest of the downtown area. Near the 650-acre airport are the Water Reclamation Plant and the Las Positas Golf Course.", "false");

        mAirportDb.insertAirport(sf);
        mAirportDb.insertAirport(oakland);
        mAirportDb.insertAirport(hayward);
        mAirportDb.insertAirport(sanCarlos);
        mAirportDb.insertAirport(halfMoonBay);
        mAirportDb.insertAirport(paloAlto);
        mAirportDb.insertAirport(sanJose);
        mAirportDb.insertAirport(livermore);
    }

    private void displayFavorites(){
        Cursor cursor = mAirportDb.getFavoriteAirports();

        setTextViewVisibility(cursor);

        mFavoritesCursorAdapter = new CursorAdapter(getApplicationContext(), cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return getLayoutInflater().from(context).inflate(R.layout.favorites_adapter_items,parent,false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView favoriteTextView = (TextView)view.findViewById(R.id.favorites_item_textView);
                favoriteTextView.setText(cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_NAME)));
            }
        };

        mFavoritesListView.setAdapter(mFavoritesCursorAdapter);
    }

    private void setTextViewVisibility(Cursor cursor){
        if(cursor.getCount() != 0){
            mFavTitleTextView.setVisibility(mFavTitleTextView.VISIBLE);
        }else if (cursor.getCount() == 0){
            mFavTitleTextView.setVisibility(mFavTitleTextView.INVISIBLE);
        }
    }


}
