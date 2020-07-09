package app.buu.znsz.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    public DBHandler(Context context) {
        super(context, "money", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE balance (id INTEGER PRIMARY KEY,amount INTEGER)");
        db.execSQL("CREATE TABLE expense (id INTEGER PRIMARY KEY ,idk INTEGER,description TEXT,amount INTEGER,date DEFAULT CURRENT_DATE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
