package siu.example.com.airport;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by samsiu on 3/17/16.
 */
public class AirportsSQLiteHelper extends SQLiteOpenHelper {
    private static final String TAG = SQLiteOpenHelper.class.getCanonicalName();

    //region Airport Table Column Instantiations
    protected static final String COL_ID = "_id";
    protected static final String COL_NAME = "NAME";
    protected static final String COL_LATITUDE = "LATITUDE";
    protected static final String COL_LONGITUDE = "LONGITUDE";
    protected static final String COL_ADDRESS = "ADDRESS";
    protected static final String COL_CITY = "CITY";
    protected static final String COL_STATE = "STATE";
    protected static final String COL_ZIP = "ZIP_CODE";
    protected static final String COL_DESCRIPTION = "DESCRIPTION";
    protected static final String COL_FAVORITE = "FAVORITE";

    public static final String[] AIRPORTS_COLUMNS = {COL_ID,
                                                    COL_NAME,
                                                    COL_LATITUDE,
                                                    COL_LONGITUDE,
                                                    COL_ADDRESS,
                                                    COL_CITY,
                                                    COL_STATE,
                                                    COL_ZIP,
                                                    COL_DESCRIPTION,
                                                    COL_FAVORITE};
    //endregion Airport Table column instantiations

    //region SQLite DB Instantiations
    private static final int DATABASE_VERSION = 2;
    protected static final String DATABASE_NAME = "AIRPORTS_DB";
    protected static final String AIRPORTS_TABLE_NAME = "AIRPORTS";
    private static final String CREATE_AIRPORTS_TABLE =
            "CREATE TABLE " + AIRPORTS_TABLE_NAME +
                    "(" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_NAME + " TEXT," +
                    COL_LATITUDE + " NUMERIC(3,5), " +
                    COL_LONGITUDE + " NUMERIC(3,5), " +
                    COL_ADDRESS + " TEXT, " +
                    COL_CITY + " TEXT, " +
                    COL_STATE + " TEXT, " +
                    COL_ZIP + " INTEGER, " +
                    COL_DESCRIPTION + " TEXT, " +
                    COL_FAVORITE + " TEXT )";
    //endregion SQLite DB Instantiations

    //region Instance Function
    private static AirportsSQLiteHelper instance;

    /**
     * Function to call the same instance of class
     * @param context
     * @return
     */
    protected static AirportsSQLiteHelper getInstance(Context context){
        if(instance == null){
            instance = new AirportsSQLiteHelper(context);
        }
        return instance;
    }
    //endregion Instance Function

    protected AirportsSQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_AIRPORTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AIRPORTS_TABLE_NAME);
        this.onCreate(db);
    }


    /**
     * Returns all the data in Table
     * @return cursor
     */
    protected Cursor getAll(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(AIRPORTS_TABLE_NAME, // a. table
                AIRPORTS_COLUMNS, // b. column names
                null, // c. selections
                null, // d. selection args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);// h. limit

        return cursor;
    }

    /**
     * Method that runs when search is performed in the Menu Bar
     *
     * @param query
     * @return cursor
     */
    protected Cursor menuSearchAirportsList(String query){
        SQLiteDatabase db = this.getReadableDatabase();

        if(query.isEmpty()){
            query = null;
        }

        Cursor cursor = db.query(AIRPORTS_TABLE_NAME, // a. table
                AIRPORTS_COLUMNS, // b. column names
                COL_NAME + " LIKE ? OR " + COL_CITY + " LIKE ? OR " + COL_STATE + " LIKE ?", // c. selections
                new String[]{"%" + query + "%", "%" + query + "%", "%" + query + "%"}, // d. selection args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);// h. limit

        return cursor;
    }

    /**
     * Query airports with id
     *
     * @param id
     * @return cursor
     */
    protected Cursor searchAirport(long id){
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d(TAG, "ID IN SEARCHAIRPORT   " + id);
        Cursor cursor = db.query(AIRPORTS_TABLE_NAME, // a. table
                AIRPORTS_COLUMNS, // b. column names
                COL_ID + " = ? ", // c. selections
                new String[]{String.valueOf(id)}, // d. selection args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);// h. limit

        return cursor;
    }

    /**
     * Query airports that were added to favorites
     * @return cursor
     */
    protected Cursor getFavoriteAirports(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(AIRPORTS_TABLE_NAME, // a. table
                AIRPORTS_COLUMNS, // b. column names
                COL_FAVORITE + " = ? ", // c. selections
                new String[]{"true"}, // d. selection args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        return cursor;
    }

    /**
     * Query airports with user search terms in String Array
     * @param searchTerms
     * @return cursor
     */
    protected Cursor searchAirportsList(String[] searchTerms){
        SQLiteDatabase db = this.getReadableDatabase();

        String searchName = searchTerms[0];
        if(searchName.isEmpty()){
            searchName = null;
        }

        Cursor cursor = db.query(AIRPORTS_TABLE_NAME, // a. table
                AIRPORTS_COLUMNS, // b. column names
                COL_NAME + " LIKE ? OR " + COL_CITY + " LIKE ? OR " + COL_STATE + " LIKE ?", // c. selections
                //new String[]{"%" + searchName + "%", "%" + searchCity + "%", "%" + searchState + "%"}, // d. selection args
                new String[]{"%" + searchName + "%", "%" + searchName + "%", "%" + searchName + "%"}, // d. selection args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);// h. limit

        return cursor;
    }

    /**
     * Query airports with user search term String
     * @param searchTerms
     * @return cursor
     */
    protected Cursor searchAirportsList(String searchTerms){
        SQLiteDatabase db = this.getReadableDatabase();

        String searchName = searchTerms;
        if(searchName.isEmpty()){
            searchName = null;
        }

        Cursor cursor = db.query(AIRPORTS_TABLE_NAME, // a. table
                AIRPORTS_COLUMNS, // b. column names
                COL_NAME + " LIKE ? OR " + COL_CITY + " LIKE ? OR " + COL_STATE + " LIKE ?", // c. selections
                //new String[]{"%" + searchName + "%", "%" + searchCity + "%", "%" + searchState + "%"}, // d. selection args
                new String[]{"%" + searchName + "%", "%" + searchName + "%", "%" + searchName + "%"}, // d. selection args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null);// h. limit

        return cursor;
    }
    //endregion Query Functions

    //region Insert/Edit functions

    /**
     * Insert airport data to table and returns db.insert id
     * @param airport
     * @return returnId
     */
    protected long insertAirport(Airport airport){

        ContentValues values = new ContentValues();
        values.put(COL_NAME, airport.getName());
        values.put(COL_LATITUDE, airport.getLatitude());
        values.put(COL_LONGITUDE, airport.getLongitude());
        values.put(COL_ADDRESS, airport.getAddress());
        values.put(COL_CITY, airport.getCity());
        values.put(COL_STATE, airport.getState());
        values.put(COL_ZIP, airport.getZip());
        values.put(COL_DESCRIPTION, airport.getDescription());
        values.put(COL_FAVORITE, airport.getFavorite());

        SQLiteDatabase db = this.getWritableDatabase();
        long returnId = db.insert(AIRPORTS_TABLE_NAME, null, values);
        db.close();
        return returnId;
    }

    /**
     * Toggles and updates favorite status in table
     * @param id
     */
    protected void updateAirportFavorite(long id){
        Log.d(TAG, "ID THAT IS SENT FOR COLUM SEARCH: " + id);
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(AIRPORTS_TABLE_NAME, // a. table
                AIRPORTS_COLUMNS, // b column names
                COL_ID + " = ? ", // c. selections
                new String[]{String.valueOf(id)}, // d. selection args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        cursor.moveToFirst();

        String toggleFavorite = cursor.getString(cursor.getColumnIndex(COL_FAVORITE));
        if(toggleFavorite.equals("true")){
            toggleFavorite = "false";
        }else if(toggleFavorite.equals("false")){
            toggleFavorite = "true";
        }

        Log.d(TAG, "VALUE OF FAVORITES: " + toggleFavorite);
        ContentValues values = new ContentValues();
        values.put(COL_FAVORITE, toggleFavorite);
        db.update(AIRPORTS_TABLE_NAME, values, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    //endregion Insert/Edit functions

    //region Delete Functions

    /**
     * Delete a single item in table with id
     *
     * @param id
     * @return deleteNum
     */
    protected int deleteItem(int id){
        SQLiteDatabase db = getWritableDatabase();
        int deleteNum = db.delete(AIRPORTS_TABLE_NAME,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deleteNum;
    }

    /**
     * Delete all items in table
     *
     * @return deleteNum
     */
    protected int deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        int deleteNum = db.delete(AIRPORTS_TABLE_NAME,null,null);
        db.close();
        return deleteNum;
    }
    //endregion Delete Functions

}
