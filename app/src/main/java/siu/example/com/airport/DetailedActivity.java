package siu.example.com.airport;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DetailedActivity extends AppCompatActivity {
    private static final String TAG = DetailedActivity.class.getSimpleName();

    private static ListView mDeatilListView;
    protected static AirportsSQLiteHelper mAirportDb;
    protected static CursorAdapter mDetailedCursorAdapter;
    private static Button mFavButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        Intent detailedIntent = getIntent();
        long id = detailedIntent.getLongExtra(FlightResultsActivity.INTENT_DETAILED_KEY, -1);

        mAirportDb = AirportsSQLiteHelper.getInstance(getApplicationContext());

        showDetailedSearchResults(id);

        onFavButtonClick(id);

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

                airportNameDetailedTextView.setText("Name: "+cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_NAME)));
                airportLatitudeDetailedTextView.setText("Latitude: "+cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_LATITUDE)));
                airportLongitudeDetailedTextView.setText("Longitude: "+cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_LONGITUDE)));
                airportAddressDetailedTextView.setText("Address: "+cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_ADDRESS)));
                airportCityDetailedTextView.setText("City: "+cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_CITY)));
                airportStateDetailedTextView.setText("State: "+cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_STATE)));
                airportZipDetailedTextView.setText("Zip: "+cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_ZIP)));
                airportDescriptionDetailedTextView.setText("Description: "+cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_DESCRIPTION)));
            }
        };

        mDeatilListView = (ListView)findViewById(R.id.airport_detail_listView);
        mDeatilListView.setAdapter(mDetailedCursorAdapter);
    }

    private void onFavButtonClick(final long id){
        mFavButton = (Button)findViewById(R.id.detailed_favorite_button);
        mFavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAirportDb.updateFavorites(id);
            }
        });
    }


}
