package siu.example.com.airport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {
    private static final String TAG = DetailedActivity.class.getSimpleName();

    private static ListView mDeatilListView;
    protected static AirportsSQLiteHelper mAirportDb;
    protected static CursorAdapter mDetailedCursorAdapter;
    private static FloatingActionButton mFavFabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        mFavFabButton = (FloatingActionButton)findViewById(R.id.detailed_favorite_fab_button);


        //mFavFabButton.setBackgroundResource(R.drawable.ic_favorite_border_black_18dp);


        Intent detailedIntent = getIntent();
        long id = detailedIntent.getLongExtra(Utils.INTENT_DETAILED_KEY, -1);

        mAirportDb = AirportsSQLiteHelper.getInstance(getApplicationContext());

        showDetailedSearchResults(id);

        onFavFabButtonClick(id);

        Log.d(TAG, "BEFOR CHECK STATUS     " + id);
        getFavButtonStatus(id);
        Log.d(TAG, "AFTER CHECK STATUS     " + id);
    }


    private void showDetailedSearchResults(long id){
        Cursor cursor = mAirportDb.searchAirport(id);

        mDetailedCursorAdapter = new CursorAdapter(getApplicationContext(), cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.airport_adapter_detailed_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                TextView airportNameDetailedTextView = (TextView) view.findViewById(R.id.airport_detailedName_textview);
                TextView airportLatitudeDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedLatitude_textview);
                TextView airportLongitudeDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedLongitude_textview);
                TextView airportAddressDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedAddress_textview);
                TextView airportCityDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedCity_textview);
                TextView airportStateDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedState_textview);
                TextView airportZipDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedZip_textview);
                TextView airportDescriptionDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedDescription_textview);

                Log.d(TAG, "  LATITUDE   "+cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_LATITUDE)));

                Airport airportResult = new Airport(
                        cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_NAME)),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_LATITUDE))),
                        Double.parseDouble(cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_LONGITUDE))),
                        cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_CITY)),
                        cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_STATE)),
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_ZIP))),
                        cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_FAVORITE)));

                airportNameDetailedTextView.setText("Name: "+ airportResult.getName());
                airportLatitudeDetailedTextView.setText("Latitude: "+ airportResult.getLatitude());
                airportLongitudeDetailedTextView.setText("Longitude: "+ airportResult.getLongitude());
                airportAddressDetailedTextView.setText("Address: "+ airportResult.getAddress());
                airportCityDetailedTextView.setText("City: "+ airportResult.getCity());
                airportStateDetailedTextView.setText("State: "+ airportResult.getState());
                airportZipDetailedTextView.setText("Zip: "+ airportResult.getZip());
                airportDescriptionDetailedTextView.setText("Description: "+ airportResult.getDescription());
            }
        };

        mDeatilListView = (ListView)findViewById(R.id.airport_detail_listView);
        mDeatilListView.setAdapter(mDetailedCursorAdapter);
    }

    private void onFavFabButtonClick(final long id){
        mFavFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAirportDb.updateAirportFavorite(id);
                getFavButtonStatus(id);
            }
        });
    }

    private static void getFavButtonStatus(long id){
        Cursor cursor = mAirportDb.searchAirport(id);
        cursor.moveToFirst();
        String checkFav = cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_FAVORITE));
        int color = Color.parseColor(Utils.FAB_BUTTON_COLOR);
        switch(checkFav){
            case Utils.ADD_TO_FAVORITES:
                Log.d(TAG, "FAVORITE TRUE");
                mFavFabButton.clearColorFilter();
                mFavFabButton.setImageResource(R.drawable.ic_favorite_black_18dp);
                mFavFabButton.setColorFilter(color);
                break;
            case Utils.REMOVE_FROM_FAVORITES:
                Log.d(TAG, "FAVORITE FALSE");
                mFavFabButton.clearColorFilter();
                mFavFabButton.setImageResource(R.drawable.ic_favorite_border_black_18dp);
                mFavFabButton.setColorFilter(color);
                break;
        }
    }


}
