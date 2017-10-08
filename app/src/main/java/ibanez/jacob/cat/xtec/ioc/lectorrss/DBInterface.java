package ibanez.jacob.cat.xtec.ioc.lectorrss;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class for operating on the database
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class DBInterface {

    //Tag for logging purposes
    public static final String TAG = DBInterface.class.getSimpleName();

    //Database columns
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_FEED_LINK = "FEED_LINK";
    public static final String COLUMN_AUTHOR = "AUTHOR";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_PUBLISH_DATE = "PUBLISH_DATE";
    public static final String COLUMN_CATHEGORY = "CATHEGORY";
    public static final String COLUMN_THUMBNAIL = "THUMBNAIL";

    //Database variables
    public static final String DB_NAME = "FEEDS_DB";
    public static final String TABLE_FEEDS = "FEEDS";
    public static final int VERSION = 1;

    //Database queries
    public static final String CREATE_TABLE_FEEDS =
            "CREATE TABLE IF NOT EXISTS " + DB_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL," +
                    COLUMN_FEED_LINK + " TEXT NOT NULL," +
                    COLUMN_AUTHOR + " TEXT NOT NULL," +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                    COLUMN_PUBLISH_DATE + " TEXT NOT NULL," +
                    COLUMN_CATHEGORY + " TEXT NOT NULL," +
                    COLUMN_THUMBNAIL + " TEXT NOT NULL" +
                    ");";

    //class members
    private final Context mContext;
    private DBHelp mHelp;
    private SQLiteDatabase mDatabase;

    //Constructor
    public DBInterface(Context mContext) {
        this.mContext = mContext;
    }

    /**
     *
     * @return
     * @throws SQLException
     */
    public DBInterface open() throws SQLException {
        mDatabase = mHelp.getWritableDatabase();
        return this;
    }

    /**
     *
     */
    public void close() {
        mHelp.close();
    }

    public long insertFeed() {

    }

    /**
     *
     */
    private static class DBHelp extends SQLiteOpenHelper {

        //Constructor
        DBHelp(Context con) {
            super(con, DB_NAME, null, VERSION);
        }

        /**
         *
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_FEEDS);
            } catch (SQLException e) {
                Log.w(TAG, "Error executing statement " + CREATE_TABLE_FEEDS, e);
            }
        }

        /**
         *
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Updating database from " + oldVersion + " to " + newVersion +
                    ". This process wil erase all data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDS);

            onCreate(db);
        }
    }
}
