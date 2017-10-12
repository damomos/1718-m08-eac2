package ibanez.jacob.cat.xtec.ioc.lectorrss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ibanez.jacob.cat.xtec.ioc.lectorrss.model.RssItem;
import ibanez.jacob.cat.xtec.ioc.lectorrss.utils.DateUtils;

/**
 * Class for operating on the database
 *
 * @author <a href="mailto:jacobibanez@jacobibanez.com">Jacob Ibáñez Sánchez</a>.
 */
public class DBInterface {

    //Tag for logging purposes
    private static final String TAG = DBInterface.class.getSimpleName();

    //Database columns
    private static final String COLUMN_ID = "_ID";
    private static final String COLUMN_TITLE = "TITLE";
    private static final String COLUMN_LINK = "LINK";
    private static final String COLUMN_AUTHOR = "AUTHOR";
    private static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    private static final String COLUMN_PUB_DATE = "PUB_DATE";
    private static final String COLUMN_CATEGORIES = "CATEGORIES";
    private static final String COLUMN_THUMBNAIL = "THUMBNAIL";
    private static final String[] SELECT_ALL = new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_LINK,
            COLUMN_AUTHOR, COLUMN_DESCRIPTION, COLUMN_PUB_DATE, COLUMN_CATEGORIES, COLUMN_THUMBNAIL};

    //Database variables
    public static final String DB_NAME = "FEEDS_DB";
    public static final String TABLE_ITEMS = "ITEMS";
    public static final int VERSION = 1;

    //Database queries
    public static final String CREATE_TABLE_ITEMS =
            "CREATE TABLE IF NOT EXISTS " + TABLE_ITEMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL," +
                    COLUMN_LINK + " TEXT NOT NULL," +
                    COLUMN_AUTHOR + " TEXT NOT NULL," +
                    COLUMN_DESCRIPTION + " TEXT NOT NULL," +
                    COLUMN_PUB_DATE + " TEXT NOT NULL," +
                    COLUMN_CATEGORIES + " TEXT NOT NULL," +
                    COLUMN_THUMBNAIL + " TEXT NOT NULL" +
                    ");";

    //class members
    private DBHelp mHelp;
    private SQLiteDatabase mDatabase;

    //Constructor
    public DBInterface(Context context) {
        this.mHelp = new DBHelp(context);
    }

    //Open and close methods
    /**
     * This method opens the connection to the database
     *
     * @return A ready-to-use instance of the {@link DBInterface}
     * @throws SQLException If an error occurs
     */
    public DBInterface open() throws SQLException {
        mDatabase = mHelp.getWritableDatabase();
        return this;
    }

    /**
     * Closes the connection to the database
     */
    public void close() {
        mHelp.close();
    }

    //Methods for manipulating data
    /**
     *
     * @param item
     * @return
     */
    public long insertItem(RssItem item) {
        ContentValues initialValues = new ContentValues();

        //fill all columns
        initialValues.put(COLUMN_TITLE, item.getTitle());
        initialValues.put(COLUMN_LINK, item.getLink());
        initialValues.put(COLUMN_AUTHOR, item.getAuthor());
        initialValues.put(COLUMN_DESCRIPTION, item.getDescription());
        initialValues.put(COLUMN_PUB_DATE, DateUtils.dateToString(item.getPubDate(),
                DateUtils.RSS_DATE_FORMAT));
        initialValues.put(COLUMN_CATEGORIES, item.getCategories());
        initialValues.put(COLUMN_THUMBNAIL, item.getThumbnail());

        return mDatabase.insert(TABLE_ITEMS, null, initialValues);
    }

    /**
     *
     * @return
     */
    public Cursor getAllItems() {
        return mDatabase.query(TABLE_ITEMS, SELECT_ALL, null, null, null, null, null);
    }

    //Helper inner class
    /**
     * Helper inner class for accessing the database
     */
    private static class DBHelp extends SQLiteOpenHelper {

        //Constructor
        DBHelp(Context con) {
            super(con, DB_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_ITEMS);
            } catch (SQLException e) {
                Log.w(TAG, "Error executing statement " + CREATE_TABLE_ITEMS, e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Updating database from " + oldVersion + " to " + newVersion +
                    ". This process wil erase all data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);

            onCreate(db);
        }
    }
}
