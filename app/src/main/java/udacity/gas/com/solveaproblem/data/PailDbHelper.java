package udacity.gas.com.solveaproblem.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fagbohungbe on 28/02/2015.
 */
public class PailDbHelper extends SQLiteOpenHelper{

	private static final int DATABASE_VERSION = 2;
	static final String DATABASE_NAME = "pail.db";

	public PailDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		final String SQL_CREATE_PROBLEM_TABLE = "CREATE TABLE "+ PailContract.ProblemEntry.TABLE_NAME + " ("+
				PailContract.ProblemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				PailContract.ProblemEntry.COLUMN_DATE + " INTEGER NOT NULL, "+
				PailContract.ProblemEntry.COLUMN_DATE_MODIFIED + " INTEGER NOT NULL, "+
				PailContract.ProblemEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
				PailContract.ProblemEntry.COLUMN_TITLE + " TEXT NOT NULL, "+
				PailContract.ProblemEntry.COLUMN_PRIVACY + " INTEGER NOT NULL, "+
				PailContract.ProblemEntry.COLUMN_PROBLEM_STATUS + " INTEGER NOT NULL )";

		final String SQL_CREATE_NOTES_TABLE = "CREATE TABLE "+ PailContract.NoteAttachmentEntry.TABLE_NAME + " ("+
				PailContract.NoteAttachmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				PailContract.NoteAttachmentEntry.COLUMN_DATE + " INTEGER NOT NULL, "+
				PailContract.NoteAttachmentEntry.COLUMN_DATE_MODIFIED + " INTEGER NOT NULL, "+
				PailContract.NoteAttachmentEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
				PailContract.NoteAttachmentEntry.COLUMN_TITLE + " TEXT NOT NULL, "+
				PailContract.NoteAttachmentEntry.COLUMN_RELEVANCE + " INTEGER NOT NULL, " +
				PailContract.NoteAttachmentEntry.COLUMN_PRIVACY + " INTEGER NOT NULL, " +
				PailContract.NoteAttachmentEntry.COLUMN_PROB_KEY + " INTEGER NOT NULL, " +
				PailContract.NoteAttachmentEntry.COLUMN_ATTACH_ID + " INTEGER NOT NULL, " +
				" FOREIGN KEY (" + PailContract.NoteAttachmentEntry.COLUMN_PROB_KEY + ") REFERENCES " +
				PailContract.ProblemEntry.TABLE_NAME + " (" + PailContract.ProblemEntry._ID + ") )";

		final String SQL_CREATE_LINK_TABLE = "CREATE TABLE "+ PailContract.LinkAttachmentEntry.TABLE_NAME + " ("+
				PailContract.LinkAttachmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				PailContract.LinkAttachmentEntry.COLUMN_DATE + " INTEGER NOT NULL, "+
				PailContract.LinkAttachmentEntry.COLUMN_DATE_MODIFIED + " INTEGER NOT NULL, "+
				PailContract.LinkAttachmentEntry.COLUMN_LINK_TITLE + " TEXT NOT NULL, " +
				PailContract.LinkAttachmentEntry.COLUMN_LINK_URL + " TEXT NOT NULL, "+
				PailContract.LinkAttachmentEntry.COLUMN_LINK_SCREENSHOT + " TEXT NOT NULL, " +
				PailContract.LinkAttachmentEntry.COLUMN_RELEVANCE + " INTEGER NOT NULL, " +
				PailContract.LinkAttachmentEntry.COLUMN_PRIVACY + " INTEGER NOT NULL, " +
				PailContract.LinkAttachmentEntry.COLUMN_PROB_KEY + " INTEGER NOT NULL, " +
				PailContract.NoteAttachmentEntry.COLUMN_ATTACH_ID + " INTEGER NOT NULL, " +
				" FOREIGN KEY (" + PailContract.LinkAttachmentEntry.COLUMN_PROB_KEY + ") REFERENCES " +
				PailContract.ProblemEntry.TABLE_NAME + " (" + PailContract.ProblemEntry._ID + ") )";

		final String SQL_CREATE_IMAGE_TABLE = "CREATE TABLE "+ PailContract.ImageAttachmentEntry.TABLE_NAME + " ("+
				PailContract.ImageAttachmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				PailContract.ImageAttachmentEntry.COLUMN_DATE + " INTEGER NOT NULL, "+
				PailContract.ImageAttachmentEntry.COLUMN_DATE_MODIFIED + " INTEGER NOT NULL, "+
				PailContract.ImageAttachmentEntry.COLUMN_MIME_TYPE + " TEXT NOT NULL, " +
				PailContract.ImageAttachmentEntry.COLUMN_FILE_NAME + " TEXT NOT NULL, "+
				PailContract.ImageAttachmentEntry.COLUMN_FILE_URI + " TEXT NOT NULL, " +
				PailContract.ImageAttachmentEntry.COLUMN_FILE_SIZE + " DOUBLE(11, 0) NOT NULL, "+
				PailContract.ImageAttachmentEntry.COLUMN_FILE_TYPE + " INTEGER NOT NULL, " +
				PailContract.ImageAttachmentEntry.COLUMN_FILE_DESCRIPTION + " TEXT NOT NULL, " +
				PailContract.ImageAttachmentEntry.COLUMN_RELEVANCE + " INTEGER NOT NULL, " +
				PailContract.ImageAttachmentEntry.COLUMN_PRIVACY + " INTEGER NOT NULL, " +
				PailContract.ImageAttachmentEntry.COLUMN_PROB_KEY + " INTEGER NOT NULL, " +
				PailContract.NoteAttachmentEntry.COLUMN_ATTACH_ID + " INTEGER NOT NULL, " +
				" FOREIGN KEY (" + PailContract.ImageAttachmentEntry.COLUMN_PROB_KEY + ") REFERENCES " +
				PailContract.ProblemEntry.TABLE_NAME + " (" + PailContract.ProblemEntry._ID + ") )";

		final String SQL_CREATE_VIDEO_TABLE = "CREATE TABLE "+ PailContract.VideoAttachmentEntry.TABLE_NAME + " ("+
				PailContract.VideoAttachmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				PailContract.VideoAttachmentEntry.COLUMN_DATE + " INTEGER NOT NULL, "+
				PailContract.VideoAttachmentEntry.COLUMN_DATE_MODIFIED + " INTEGER NOT NULL, "+
				PailContract.VideoAttachmentEntry.COLUMN_MIME_TYPE + " TEXT NOT NULL, " +
				PailContract.VideoAttachmentEntry.COLUMN_FILE_NAME + " TEXT NOT NULL, "+
				PailContract.VideoAttachmentEntry.COLUMN_FILE_URI + " TEXT NOT NULL, " +
				PailContract.VideoAttachmentEntry.COLUMN_FILE_SIZE + " REAL NOT NULL, "+
				PailContract.VideoAttachmentEntry.COLUMN_FILE_TYPE + " INTEGER NOT NULL, " +
				PailContract.VideoAttachmentEntry.COLUMN_FILE_DESCRIPTION + " TEXT NOT NULL, " +
				PailContract.VideoAttachmentEntry.COLUMN_RELEVANCE + " INTEGER NOT NULL, " +
				PailContract.VideoAttachmentEntry.COLUMN_PRIVACY + " INTEGER NOT NULL, " +
				PailContract.VideoAttachmentEntry.COLUMN_PROB_KEY + " INTEGER NOT NULL, " +
				PailContract.NoteAttachmentEntry.COLUMN_ATTACH_ID + " INTEGER NOT NULL, " +
				" FOREIGN KEY (" + PailContract.VideoAttachmentEntry.COLUMN_PROB_KEY + ") REFERENCES " +
				PailContract.ProblemEntry.TABLE_NAME + " (" + PailContract.ProblemEntry._ID + ") )";

		final String SQL_CREATE_AUDIO_TABLE = "CREATE TABLE "+ PailContract.AudioAttachmentEntry.TABLE_NAME + " ("+
				PailContract.AudioAttachmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				PailContract.AudioAttachmentEntry.COLUMN_DATE + " INTEGER NOT NULL, "+
				PailContract.AudioAttachmentEntry.COLUMN_DATE_MODIFIED + " INTEGER NOT NULL, "+
				PailContract.AudioAttachmentEntry.COLUMN_MIME_TYPE + " TEXT NOT NULL, " +
				PailContract.AudioAttachmentEntry.COLUMN_FILE_NAME + " TEXT NOT NULL, "+
				PailContract.AudioAttachmentEntry.COLUMN_FILE_URI + " TEXT NOT NULL, " +
				PailContract.AudioAttachmentEntry.COLUMN_FILE_SIZE + " REAL NOT NULL, "+
				PailContract.AudioAttachmentEntry.COLUMN_FILE_TYPE + " INTEGER NOT NULL, " +
				PailContract.AudioAttachmentEntry.COLUMN_FILE_DESCRIPTION + " TEXT NOT NULL, " +
				PailContract.AudioAttachmentEntry.COLUMN_RELEVANCE + " INTEGER NOT NULL, " +
				PailContract.AudioAttachmentEntry.COLUMN_PRIVACY + " INTEGER NOT NULL, " +
				PailContract.AudioAttachmentEntry.COLUMN_PROB_KEY + " INTEGER NOT NULL, " +
				PailContract.NoteAttachmentEntry.COLUMN_ATTACH_ID + " INTEGER NOT NULL, " +
				" FOREIGN KEY (" + PailContract.AudioAttachmentEntry.COLUMN_PROB_KEY + ") REFERENCES " +
				PailContract.ProblemEntry.TABLE_NAME + " (" + PailContract.ProblemEntry._ID + ") )";

		final String SQL_CREATE_FILE_TABLE = "CREATE TABLE "+ PailContract.FileAttachmentEntry.TABLE_NAME + " ("+
				PailContract.FileAttachmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				PailContract.FileAttachmentEntry.COLUMN_DATE + " INTEGER NOT NULL, "+
				PailContract.FileAttachmentEntry.COLUMN_DATE_MODIFIED + " INTEGER NOT NULL, "+
				PailContract.FileAttachmentEntry.COLUMN_MIME_TYPE + " TEXT NOT NULL, " +
				PailContract.FileAttachmentEntry.COLUMN_FILE_NAME + " TEXT NOT NULL, "+
				PailContract.FileAttachmentEntry.COLUMN_FILE_URI + " TEXT NOT NULL, " +
				PailContract.FileAttachmentEntry.COLUMN_FILE_SIZE + " REAL NOT NULL, "+
				PailContract.FileAttachmentEntry.COLUMN_FILE_TYPE + " INTEGER NOT NULL, " +
				PailContract.FileAttachmentEntry.COLUMN_FILE_DESCRIPTION + " TEXT NOT NULL, " +
				PailContract.FileAttachmentEntry.COLUMN_RELEVANCE + " INTEGER NOT NULL, " +
				PailContract.FileAttachmentEntry.COLUMN_PRIVACY + " INTEGER NOT NULL, " +
				PailContract.FileAttachmentEntry.COLUMN_PROB_KEY + " INTEGER NOT NULL, " +
				PailContract.NoteAttachmentEntry.COLUMN_ATTACH_ID + " INTEGER NOT NULL, " +
				" FOREIGN KEY (" + PailContract.FileAttachmentEntry.COLUMN_PROB_KEY + ") REFERENCES " +
				PailContract.ProblemEntry.TABLE_NAME + " (" + PailContract.ProblemEntry._ID + ") )";

		db.execSQL(SQL_CREATE_PROBLEM_TABLE);
		db.execSQL(SQL_CREATE_NOTES_TABLE);
		db.execSQL(SQL_CREATE_LINK_TABLE);
		db.execSQL(SQL_CREATE_IMAGE_TABLE);
		db.execSQL(SQL_CREATE_VIDEO_TABLE);
		db.execSQL(SQL_CREATE_AUDIO_TABLE);
		db.execSQL(SQL_CREATE_FILE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + PailContract.ProblemEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + PailContract.ImageAttachmentEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + PailContract.AudioAttachmentEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + PailContract.ImageAttachmentEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + PailContract.FileAttachmentEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + PailContract.LinkAttachmentEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + PailContract.NoteAttachmentEntry.TABLE_NAME);
		db.execSQL("DROP TABLE IF EXISTS " + PailContract.VideoAttachmentEntry.TABLE_NAME);
		onCreate(db);
	}
}