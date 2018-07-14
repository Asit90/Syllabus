package cakart.cakart.in.syllabus.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_DECK_TABLE = "create table flash_decs (deck_id integer primary key autoincrement, number_of_cards integer, name TEXT, downloaded_date DATETIME DEFAULT CURRENT_TIMESTAMP)";

    public static final String CREATE_CARDS_TABLE = "create table flash_cards (card_id integer primary key autoincrement, deck_id integer, known integer default 0, title TEXT, description TEXT)";

    public static final  String CREATE_QUIZ_DECK_TABLE = "create table quiz_deck (quiz_deck_id integer primary key , name TEXT ,taken_time integer,score integer)";

    public static final String CREATE_QUESTION_TABLE = "create table questions(quiz_id integer , question_id integer primary key, question TEXT, question_type VARCHAR(255),user_ans integer )";

    public static final String CREATE_QUESTION_ANSWER = "create table answers( id integer primary key ,question_id integer ,option TEXT , is_correct integer)";

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    public static final String DATABASE_NAME = "study";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        db.execSQL(CREATE_DECK_TABLE);
        db.execSQL(CREATE_CARDS_TABLE);
        db.execSQL(CREATE_QUIZ_DECK_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);
        db.execSQL(CREATE_QUESTION_ANSWER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS flash_decs");
        db.execSQL("DROP TABLE IF EXISTS flash_cards");
        db.execSQL("DROP TABLE IF EXISTS quiz_deck");
        db.execSQL("DROP TABLE IF EXISTS questions");
        db.execSQL("DROP TABLE IF EXISTS answers");

        // Create tables again
        onCreate(db);
    }
}
