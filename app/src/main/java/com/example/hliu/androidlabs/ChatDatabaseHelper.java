package com.example.hliu.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by H.LIU on 2017-10-19.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper{
    private final static int VERSION_NUM = 1;
    private final static String TAG_SQL = ChatDatabaseHelper.class.getSimpleName();

    private final static String DATABASE_NAME = "Message.db";
    private final static String Str_TABLE = "Table_Message";
    private final static String Str_ID_COL = "MessageID";
    private final static String Str_MESSAGE_COL = "MessageContent";

    // db is the object hold the database reference
    private SQLiteDatabase myDatabase;

    //    create table Table_Message (MessageID integer primary key cutoincreament, messageContent text);
    private static final String STR_createTable =
            "CREATE TABLE " + Str_TABLE
            + " ("
            + Str_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Str_MESSAGE_COL + " TEXT"
            + ")";

    public ChatDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override // create database if not exists
    public void onCreate(SQLiteDatabase db){
        db.execSQL(STR_createTable);
        Log.i(TAG_SQL, "Calling onCreate SQLhelper");
    }

    @Override // create database if exists; update the version
    // add new columns and etc.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG_SQL, "Calling onUpgrage SQL old version= " + oldVersion + "; new version= " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + Str_TABLE);
        onCreate(db);
    }

    // onOpen() gets called the last. and gets called regardless

    public void openDatabase(){
        myDatabase = this.getWritableDatabase();
    }

    public void insertEntry(String msg){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Str_MESSAGE_COL, msg);
        myDatabase.insert(Str_TABLE, null, contentValues);
    }

    public void closeDatabase(){
        if(myDatabase !=null && myDatabase.isOpen()){
            myDatabase.close();
        }
    }

    public Cursor getCursor_RecordList(){
//        Cursor cursor = myDatabase.query(Str_TABLE, null,null, null,null, null,null);
        Cursor cursor = myDatabase.query(Str_TABLE,null,null,null,null,null,null,null);

//        Cursor cursor = myDatabase.rawQuery("select * from Table_Message ", null);
//        Cursor cursor = myDatabase.query(false, Str_TABLE, new String[]{getStr_MESSAGE_COL()}, null, null,null,null,null,null );
//        Cursor cursor  = myDatabase.rawQuery("select * from ? ", new String[] {Str_TABLE});
        return cursor;
    }
    public void deleteEntry_mydb_firstEntry() {
        myDatabase.execSQL("DELETE FROM " + Str_TABLE + " WHERE " + Str_ID_COL + " = (SELECT MIN(" + Str_ID_COL + ") FROM " + Str_TABLE + ")");
/*        int i =0;
        String string = "DELETE FROM " + db_TableName +
                        " WHERE " + Str_ID_COL +
                        " IN (SELECT " + Str_ID_COL +
                        " FROM " + db_TableName +
                        " ORDER BY " + Str_ID_COL +
                        " ASC LIMIT " + count + " )";
        mydb.rawQuery(db_TableName, string[]);
        mydb.execSQL(string);
        return deleteEntry_mydb_NumOfRows(i);*/
    }

    public void deleteEntry_mydb_LastEntry() {
        myDatabase.execSQL("DELETE FROM " + Str_TABLE + " WHERE " + Str_ID_COL + " = (SELECT MAX(" + Str_ID_COL + ") FROM " + Str_TABLE + ")");
    }
    // get the table name to pass the name of the table
    public String getTableName(){
        return Str_TABLE;
    }

    public String getStr_MESSAGE_COL(){
        return Str_MESSAGE_COL;
    }

}
