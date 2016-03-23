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

    private static final int DATABASE_VERSION = 2;
    protected static final String DATABASE_NAME = "AIRPORTS_DB";
    protected static final String AIRPORTS_TABLE_NAME = "AIRPORTS";

    // Column Names
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

    private static AirportsSQLiteHelper instance;

    protected static AirportsSQLiteHelper getInstance(Context context){
        if(instance == null){
            instance = new AirportsSQLiteHelper(context);
        }
        return instance;
    }

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

    protected Cursor getAirportsList(){
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

    protected Cursor getFavorites(){
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

    protected void updateFavorites(long id){
        Log.d(TAG, "ID THAT IS SENT FOR COLUM SEARCH: " +id);
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



    protected long insertAirport(String name, Double latitude, Double longitude, String address, String city, String state, int zip, String description, String favorite){

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_LATITUDE, latitude);
        values.put(COL_LONGITUDE, longitude);
        values.put(COL_ADDRESS, address);
        values.put(COL_CITY, city);
        values.put(COL_STATE, state);
        values.put(COL_ZIP, zip);
        values.put(COL_DESCRIPTION, description);
        values.put(COL_FAVORITE, favorite);

        SQLiteDatabase db = this.getWritableDatabase();
        long returnId = db.insert(AIRPORTS_TABLE_NAME, null, values);
        db.close();
        return returnId;
    }

    protected int deleteItem(int id){
        SQLiteDatabase db = getWritableDatabase();
        int deleteNum = db.delete(AIRPORTS_TABLE_NAME,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return deleteNum;
    }

    protected int deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        int deleteNum = db.delete(AIRPORTS_TABLE_NAME,null,null);
        db.close();
        return deleteNum;
    }

}
