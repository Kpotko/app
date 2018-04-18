package places.app.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.HashMap;


public class PlaceTable {
    static final String TABLE_PLACE = "TABLE_PLACE";
    static final String KEY_PLACE_ID = "KEY_PLACE_ID";
    static final String KEY_PLACE_LAT = "KEY_PLACE_LAT";
    static final String KEY_PLACE_LON = "KEY_PLACE_LONG";
    static final String KEY_PLACE_DESC = "KEY_PLACE_DESC";
    static final String KEY_PLACE_IMAGE = "KEY_PLACE_IMAGE";

    static final String SQL_INSERT_STETEMENT = "INSERT INTO " + TABLE_PLACE + " VALUES (?,?,?,?);";

    private static final HashMap<String, Integer> sColumnIndexCache = new HashMap<>();

    static {
        sColumnIndexCache.put(KEY_PLACE_ID, 0);
        sColumnIndexCache.put(KEY_PLACE_LAT, 1);
        sColumnIndexCache.put(KEY_PLACE_LON, 2);
        sColumnIndexCache.put(KEY_PLACE_DESC, 3);
        sColumnIndexCache.put(KEY_PLACE_IMAGE, 4);
    }

    static final String CREATE_TABLE_PLACE = "CREATE TABLE IF NOT EXISTS " + TABLE_PLACE + " (" +
            KEY_PLACE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +     // 0
            KEY_PLACE_LAT + " REAL, " + // 1
            KEY_PLACE_LON + " REAL, " +  // 2
            KEY_PLACE_DESC + " TEXT, " +  // 3
            KEY_PLACE_IMAGE + " TEXT " +  // 4
            ")";

    static PlaceModel convert(Cursor cursor) {
        int pid = cursor.getInt(sColumnIndexCache.get(KEY_PLACE_ID));
        double lat = cursor.getDouble(sColumnIndexCache.get(KEY_PLACE_LAT));
        double lon = cursor.getDouble(sColumnIndexCache.get(KEY_PLACE_LON));
        String desc = cursor.getString(sColumnIndexCache.get(KEY_PLACE_DESC));
        String img = cursor.getString(sColumnIndexCache.get(KEY_PLACE_IMAGE));
        return new PlaceModel(pid, lat, lon, desc, img);
    }

    static ContentValues bindPlaceToContentValues(PlaceModel place) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_PLACE_LAT, place.getLatitude());
        cv.put(KEY_PLACE_LON, place.getLongitude());
        cv.put(KEY_PLACE_DESC, place.getDescription());
        cv.put(KEY_PLACE_IMAGE, place.getImage());
        return cv;
    }

}
