package places.app.storage;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper sInstance;

    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }

        return sInstance;
    }

    public DatabaseHelper(Context context) {
        super(context.getApplicationContext(), "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PlaceTable.CREATE_TABLE_PLACE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public ArrayList<PlaceModel> getAll() {
        ArrayList<PlaceModel> result = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(PlaceTable.TABLE_PLACE, null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                result.add(PlaceTable.convert(cursor));
            }
        }
        db.close();
        return result;
    }

    public PlaceModel getPlaceById(int pid) {
        PlaceModel result = null;
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(PlaceTable.TABLE_PLACE, null, PlaceTable.KEY_PLACE_ID + "= ?", new String[]{String.valueOf(pid)}, null, null, null)) {
            if (cursor.moveToFirst()) {
                result = PlaceTable.convert(cursor);
            }
        }
        db.close();
        return result;
    }

    public void insertPlace(PlaceModel place) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = PlaceTable.bindPlaceToContentValues(place);
        db.insert(PlaceTable.TABLE_PLACE, null, contentValues);
        db.close();
    }

    public void updatePlace(PlaceModel place) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = PlaceTable.bindPlaceToContentValues(place);
        db.update(PlaceTable.TABLE_PLACE, contentValues, PlaceTable.KEY_PLACE_ID + "= ?", new String[]{place.getPid().toString()});
        db.close();
    }

    public void deletePlace(int pid) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(PlaceTable.TABLE_PLACE, PlaceTable.KEY_PLACE_ID + "= ?", new String[]{String.valueOf(pid)});
        db.close();
    }


}
