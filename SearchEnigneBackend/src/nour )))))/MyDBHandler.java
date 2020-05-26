package com.example.search_it;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import javax.naming.Context;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class MyDBHandler<SQLiteDatabase> {

    //these are the column names
    public static final String COL_pageID = "ID";
    public static final String COL_word = "word";
    public static final String COL_count = "count";

    public static final String COL_pageID_links = "ID";
    public static final String COL_links = "links";

    public static final String COL_ID = "ID";
    public static final String COL_title = "title";
    public static final String COL_URL = "URL";
	public static final String COL_pop = "popularity";

    public static final String COL_single_word = "single_word";
    public static final String COL_docs_count = "docs_count";

    //these are the corresponding indices
//    public static final int INDEX_ID = 0;
//    public static final int INDEX_CONTENT = INDEX_ID + 1;
//    public static final int INDEX_IMPORTANT = INDEX_ID + 2;

    //used for logging
    private static final String TAG = "RemindersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "db_indexer";
    private static final String TABLE_NAME1 = "tbl_words";
    private static final String TABLE_NAME2 = "tbl_links";
    private static final String TABLE_NAME3 = "tbl_info";
    private static final String TABLE_NAME4 = "tbl_word_count";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;

    //SQL statement used to create the database
    private static final String DATABASE_CREATE1 =
            "CREATE TABLE if not exists " + TABLE_NAME1 + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_title + " TEXT, " +
					COL_pop + " REAL, " +
                    COL_URL + " TEXT );";

    private static final String DATABASE_CREATE2 =
            "CREATE TABLE if not exists " + TABLE_NAME2 + " ( " +
                    COL_pageID + " INTEGER , " +
                    COL_word + " TEXT, " +
                    COL_count + " INTEGER," +
                    "PRIMARY KEY(" + COL_pageID + "," + COL_word + ")" +");";

    private static final String DATABASE_CREATE3 =
            "CREATE TABLE if not exists " + TABLE_NAME3 + " ( " +
                    COL_pageID_links + " INTEGER , " +
                    COL_links + " TEXT, " +
                    "PRIMARY KEY(" + COL_pageID_links + "," + COL_links + ")" +");";

    private static final String DATABASE_CREATE4 =
            "CREATE TABLE if not exists " + TABLE_NAME4 + " ( " +
                    COL_single_word + " TEXT, " +
                    COL_docs_count + " INTEGER," +
                    "PRIMARY KEY(" + COL_single_word + ")" +");";

    public MyDBHandler(Context ctx) {
        this.mCtx = ctx;
    }
    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }
    //close
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
    //------------------------------------------------------------------------------------
    //--------------------------------------retrive word count in all documents------------
    //--------------------------------------------------------------------------------------
    public Cursor retrieveWordsCount(String word) {
        mDbHelper = new DatabaseHelper(mCtx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME4+ " where "+COL_single_word+" = "+word, null );
        res.moveToFirst();
    }
    //---------------------------------------------------------------------------------------------
//-----------------------------------add a new word ---------------------------------------------
//------------------------------------------------------------------------------------------------
    public void addWordsCount(String word) {

        Cursor data = db.rawQuery( "select * from "+ TABLE_NAME4+ " where "+COL_single_word+" = "+word, null );
        data.moveToFirst()
        if (data == null)
        {
            ContentValues values = new ContentValues();
            values.put(COL_single_word, word);
            values.put(COL_docs_count, 1);
            mDb.insert(TABLE_NAME4,null, values);
        }
        else{
            ContentValues values2 = new ContentValues();

            Integer count = data.getInt((data.getColumnIndex(COL_docs_count)));
            values2.put(COL_docs_count, count+1);
            values2.put(COL_single_word, word);

            mDb.update(TABLE_NAME4, values2,COL_single_word + "=?",word);

        }

    }

    //////////////add page to the DB.
    public void createPage(testest p) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COL_title, p.getTitle());
        values.put(COL_URL, p.getURL());

        long ID = mDb.insert(TABLE_NAME1, null, values);

        addLinks((int)ID,p);
        addWords((int)ID,p);
        this.close();
    }

    //////////////retrieve page's popularity.
    public void setPop(testest p) {

		this.open();
        ContentValues values = new ContentValues();
        values.put(COL_pop, p.getPop());

        // updating row
        mDb.update(TABLE_NAME1, values, COL_ID + " = ?",
                new String[] { String.valueOf(p.getId()) });
        this.close();

    }

    //////////////retrieve page's info.
    public page retrivePage(String url) {
        mDbHelper = new DatabaseHelper(mCtx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME1+ " where "+COL_URL+" = "+url, null );
        res.moveToFirst();
        testest p = new testest(res.getString(res.getColumnIndex(COL_URL)), res.getString(res.getColumnIndex(COL_title)));
        return p;
    }
//
//

    //////////////retrieve page's info.
    public page retrivePageByID(int  id) {
        mDbHelper = new DatabaseHelper(mCtx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME1+ " where "+COL_ID+" = "+id, null );
        res.moveToFirst();
        testest p = new testest(res.getString(res.getColumnIndex(COL_URL)), res.getString(res.getColumnIndex(COL_title)));
        return p;
    }

//////////////retrieve page's ID.
    public long retrieveID(String url) {
        mDbHelper = new DatabaseHelper(mCtx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME1+ " where "+COL_URL+" = "+url, null );
        res.moveToFirst();
        long id = res.getInt((res.getColumnIndex(COL_ID)));
//        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME2+ " where "+COL_ID+" = "+Integer.toString(id), null );
        return id;
    }

    //
/////////////retrieve words in specific page.
    public Cursor retrieveWords(String url) {
        mDbHelper = new DatabaseHelper(mCtx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int id;
        id = (int)retrieveID(url);
        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME2+ " where "+COL_pageID+" = "+Integer.toString(id), null );
        res.moveToFirst();
        return res;
    }
/////////////retrieve pages containing a specific word.
    public List<testest> retrievePAgess(String word) {
        mDbHelper = new DatabaseHelper(mCtx);
        List<testest> tags = new ArrayList<testest>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int id;
        //id = (int)retrieveID(url);
        Cursor res =  db.rawQuery( "select "+COL_pageID + " from "+ TABLE_NAME2+ " where "+COL_word+" = "+word, null );
        //res.moveToFirst();
        if (res.moveToFirst()) {
            do {
                testest t = new testest();
                t = retrivePageByID(res.getColumnIndex(COL_ID));
                // adding to tags list
                tags.add(t);
            } while (res.moveToNext());
        }
        return tags;
    }
    /////////////retrieve pages containing a specific word.
    public List<testest> retrievePAgess(String word) {
        List<testest> tags = new ArrayList<testest>();
        List<testest> pages = new ArrayList<testest>();
        String[] arrOfStr = str.split(" ");
        for (String a : arrOfStr){
            pages = retrievePAgess(a);
            for (int i = 0; i < pages.size(); i++) {
                if(tags
                    r=sum
            }
        }

        return tags;
    }


    /////////////retrieve links in a specific page.
    public Cursor retrievelinks(String url) {
        mDbHelper = new DatabaseHelper(mCtx);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int id;
        id = (int)retrieveID(url);
        Cursor res =  db.rawQuery( "select * from "+ TABLE_NAME3+ " where "+COL_pageID_links+" = "+Integer.toString(id), null );
        res.moveToFirst();
        return res;
    }

    /////////////add words to a page.
    public void addWords(int id, testest p) {
        this.open();
        ContentValues values = new ContentValues();

        Hashtable<String,Integer> s = new Hashtable<String,Integer>();

        s = p.getwords();
        Set<String> keys = s.keySet();
        for(String key: keys){
            addWordsCount(key);
            values.put(COL_word, key);
            values.put(COL_pageID, id);
            values.put(COL_count, s.get(key));
            mDb.insert(TABLE_NAME2,null, values);
        }
        this.close();
    }

    /////////////add links to a page.
    public void addLinks(int id, testest p) {

        this.open();
        ContentValues values = new ContentValues();
        ArrayList <String>linkat = new ArrayList <String>();

        linkat = p.getLinks();

        for (int i=0; i<linkat.size();i++){
            values.put(COL_pageID_links, id);
            values.put(COL_links, linkat.get(i));
            mDb.insert(TABLE_NAME2,null, values);
        }
        this.close();
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE1);
            db.execSQL(DATABASE_CREATE1);
            db.execSQL(DATABASE_CREATE2);
            db.execSQL(DATABASE_CREATE3);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
            onCreate(db);
        }
    }


}
