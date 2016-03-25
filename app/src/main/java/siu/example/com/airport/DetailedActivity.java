package siu.example.com.airport;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class DetailedActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = DetailedActivity.class.getSimpleName();

    private static long mId;
    private GoogleMap mMap;
    private static Airport mAirportResult;

    //region Dynamic Items Instantiations
    private static ListView mDeatilListView;
    protected static AirportsSQLiteHelper mAirportDb;
    protected static CursorAdapter mDetailedCursorAdapter;
    private static FloatingActionButton mFavFabButton;
    //endregion Dynamic Items Instantiations

    //region TextView Instantiations
    private TextView mAirportNameDetailedTextView;
    private TextView mAirportLatitudeDetailedTextView;
    private TextView mAirportLongitudeDetailedTextView;
    private TextView mAirportAddressDetailedTextView;
    private TextView mAirportCityDetailedTextView;
    private TextView mAirportStateDetailedTextView;
    private TextView mAirportZipDetailedTextView;
    private TextView mAirportDescriptionDetailedTextView;
    //endregion TextView Instantiations

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        initViews();

        getQueryAirportId();

        mAirportDb = AirportsSQLiteHelper.getInstance(getApplicationContext());

        showDetailedSearchResults(mId);

        initGoogleMaps();

        onFavFabButtonClick(mId);

        getFavButtonStatus(mId);

    }

    /**
     * Initialize views
     */
    private void initViews(){
        mFavFabButton = (FloatingActionButton)findViewById(R.id.detailed_favorite_fab_button);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        initMenuBar(menu);
        return true;
    }

    /**
     * Initializes search Menu in Action Bar
     * @param menu
     */
    private void initMenuBar(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_detailed_activity, menu);
        MenuItem homeItem = menu.findItem(R.id.home);

        Drawable collapsedSearchIcon = homeItem.getIcon();
        collapsedSearchIcon.setTint(Color.WHITE);
        homeItem.setIcon(collapsedSearchIcon);
    }

    /**
     * Handle actions to Menu Bar Items
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String menuItemName = String.valueOf(item.getTitle());
        Log.d(TAG, "MENU ITEM NAME" + menuItemName);

        switch(menuItemName){
            case "Home":
                Intent intent = new Intent(DetailedActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                return false;
        }
        return false;
    }

    /**
     * Get id to query airports from intents
     */
    private void getQueryAirportId(){
        Intent detailedIntent = getIntent();
        mId = detailedIntent.getLongExtra(Utils.INTENT_DETAILED_KEY, -1);
    }

    /**
     * Query airports table for one airport
     * @param id
     */
    private void showDetailedSearchResults(long id){
        Cursor cursor = mAirportDb.searchAirport(id);
        mAirportResult = getAirportInfo(cursor);

        mDetailedCursorAdapter = new CursorAdapter(getApplicationContext(), cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.airport_adapter_detailed_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                initDetailedTextView(view);
                setDetailedTextView();
            }
        };

        mDeatilListView = (ListView)findViewById(R.id.airport_detail_listView);
        mDeatilListView.setAdapter(mDetailedCursorAdapter);
    }

    /**
     * Store column data to Airport object
     * @param cursor
     * @return airportData
     */
    private static Airport getAirportInfo(Cursor cursor){
        cursor.moveToFirst();
        Airport airportData = new Airport(
                cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_NAME)),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_LATITUDE))),
                Double.parseDouble(cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_LONGITUDE))),
                cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_CITY)),
                cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_STATE)),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_ZIP))),
                cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(AirportsSQLiteHelper.COL_FAVORITE)));
        return airportData;
    }

    /**
     * Initialze textViews
     * @param view
     */
    private void initDetailedTextView(View view){
        mAirportNameDetailedTextView = (TextView) view.findViewById(R.id.airport_detailedName_textview);
        mAirportLatitudeDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedLatitude_textview);
        mAirportLongitudeDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedLongitude_textview);
        mAirportAddressDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedAddress_textview);
        mAirportCityDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedCity_textview);
        mAirportStateDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedState_textview);
        mAirportZipDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedZip_textview);
        mAirportDescriptionDetailedTextView = (TextView)view.findViewById(R.id.airport_detailedDescription_textview);
    }

    /**
     * Set TextViews with data
     */
    private void setDetailedTextView(){
        mAirportNameDetailedTextView.setText(mAirportResult.getName());
        mAirportLatitudeDetailedTextView.setText(Double.toString(mAirportResult.getLatitude()));
        mAirportLongitudeDetailedTextView.setText(Double.toString(mAirportResult.getLongitude()));
        mAirportAddressDetailedTextView.setText(mAirportResult.getAddress());
        mAirportCityDetailedTextView.setText(mAirportResult.getCity());
        mAirportStateDetailedTextView.setText(mAirportResult.getState());
        mAirportZipDetailedTextView.setText(Integer.toString(mAirportResult.getZip()));
        mAirportDescriptionDetailedTextView.setText(mAirportResult.getDescription());
    }

    /**
     * Toggle and table favorites column on click
     * @param id
     */
    private void onFavFabButtonClick(final long id){
        mFavFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAirportDb.updateAirportFavorite(id);
                getFavButtonStatus(id);
            }
        });
    }

    /**
     * Edit FAB button based of airport favorite status
     * @param id
     */
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

    /**
     * Initialize google maps
     */
    private void initGoogleMaps(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String name = mAirportResult.getName();
        Double latitude = mAirportResult.getLatitude();
        Double longitude = mAirportResult.getLongitude();

        float zoomLevel = 13;

        // Add a marker in Sydney and move the camera
        LatLng airport = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(airport).title(name));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(airport, zoomLevel));
    }


}
