package udacity.gas.com.solveaproblem.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Fagbohungbe on 03/03/2015.
 */
public class ProblemProvider extends ContentProvider {

	private static final UriMatcher sUriMatcher = buildUriMatcher();
	private PailDbHelper mOpenHelper;

	static final int PROBLEMS = 100;
	static final int PROBLEMS_WITH_ID = 101;
	static final int PROBLEMS_WITH_ID_AND_ATTACHMENTS = 102;
	static final int PROBLEMS_WITH_ID_AND_ATTACHMENTS_AND_ATTACHMENTTYPE = 103;
	static final int PROBLEMS_WITH_ID_AND_ATTACHMENTS_AND_ATTACHMENTTYPE_AND_ATTACHMENTID = 104;

	static final int ATTACHMENTS = 300;
	static final int ATTACHMENT_WITH_ID = 304;
	static final int ATTACHMENTS_WITH_ATTACHMENTTYPE = 301;
	static final int ATTACHMENTS_WITH_ATTACHMENTTYPE_AND_ID = 303;

	private static final SQLiteQueryBuilder sQueryProblemAndAttachment;
	//Joins
	private static String problemTable = PailContract.ProblemEntry.TABLE_NAME;
	private static String noteJoin = " INNER JOIN " +PailContract.NoteAttachmentEntry.TABLE_NAME + " ON (" + PailContract.NoteAttachmentEntry.TABLE_NAME + "."+PailContract.NoteAttachmentEntry.COLUMN_PROB_KEY + " = " + PailContract.ProblemEntry.TABLE_NAME +"." + PailContract.ProblemEntry.COLUMN_PROB_ID+")";
	private static String linkJoin = " INNER JOIN " +PailContract.LinkAttachmentEntry.TABLE_NAME + " ON (" + PailContract.LinkAttachmentEntry.TABLE_NAME + "."+PailContract.LinkAttachmentEntry.COLUMN_PROB_KEY + " = " + PailContract.ProblemEntry.TABLE_NAME +"." + PailContract.ProblemEntry.COLUMN_PROB_ID+")";
	private static String imageJoin = " INNER JOIN " +PailContract.ImageAttachmentEntry.TABLE_NAME + " ON (" + PailContract.ImageAttachmentEntry.TABLE_NAME + "."+PailContract.ImageAttachmentEntry.COLUMN_PROB_KEY + " = " + PailContract.ProblemEntry.TABLE_NAME +"." + PailContract.ProblemEntry.COLUMN_PROB_ID+")";
	private static String videoJoin = " INNER JOIN " +PailContract.VideoAttachmentEntry.TABLE_NAME + " ON (" + PailContract.VideoAttachmentEntry.TABLE_NAME + "."+PailContract.VideoAttachmentEntry.COLUMN_PROB_KEY + " = " + PailContract.ProblemEntry.TABLE_NAME +"." + PailContract.ProblemEntry.COLUMN_PROB_ID+")";
	private static String audioJoin = " INNER JOIN " +PailContract.AudioAttachmentEntry.TABLE_NAME + " ON (" + PailContract.AudioAttachmentEntry.TABLE_NAME + "."+PailContract.AudioAttachmentEntry.COLUMN_PROB_KEY + " = " + PailContract.ProblemEntry.TABLE_NAME +"." + PailContract.ProblemEntry.COLUMN_PROB_ID+")" ;
	private static String fileJoin = " INNER JOIN " +PailContract.FileAttachmentEntry.TABLE_NAME + " ON (" + PailContract.FileAttachmentEntry.TABLE_NAME + "."+PailContract.FileAttachmentEntry.COLUMN_PROB_KEY + " = " + PailContract.ProblemEntry.TABLE_NAME +"." + PailContract.ProblemEntry.COLUMN_PROB_ID+")";

	static {
		sQueryProblemAndAttachment = new SQLiteQueryBuilder();
		sQueryProblemAndAttachment.setTables(
			problemTable + noteJoin + linkJoin + imageJoin + videoJoin + audioJoin + fileJoin
		);
	}

	private static final String sProblemQuery =
			PailContract.ProblemEntry.TABLE_NAME +
					"."+PailContract.ProblemEntry.COLUMN_PROB_ID + " = ?";

	private static final String sAttachQuery =
			PailContract.Attachment._ID + " = ?";

	private static final String sProblemAttachQuery =
			PailContract.Attachment.COLUMN_PROB_KEY + " = ? AND " + PailContract.Attachment._ID + " = ? ";


	private String getQuery (String table) {
		return table+"."+sAttachQuery;
	}
	//create an instance of AttachmentInterface based on attachmenttype
	private PailContract.AttachmentInterface getInstanceofAttachment (String type) {
		switch (type) {
			case PailContract.NoteAttachmentEntry.PATH : {
				return new PailContract.NoteAttachmentEntry();
			}
			case PailContract.FileAttachmentEntry.PATH : {
				return new PailContract.FileAttachmentEntry();
			}
			case PailContract.AudioAttachmentEntry.PATH : {
				return new PailContract.AudioAttachmentEntry();
			}
			case PailContract.VideoAttachmentEntry.PATH : {
				return new PailContract.VideoAttachmentEntry();
			}
			case PailContract.LinkAttachmentEntry.PATH : {
				return new PailContract.LinkAttachmentEntry();
			}
			case PailContract.ImageAttachmentEntry.PATH : {
				return new PailContract.ImageAttachmentEntry();
			}
			default:
				throw new UnsupportedOperationException("Unsupported attachment type specified");
		}
	}
	//get the table of an attachment based on the type sent in
	private String getTable (String type) {
		switch (type) {
			case PailContract.NoteAttachmentEntry.PATH : {
				return PailContract.NoteAttachmentEntry.TABLE_NAME;
			}
			case PailContract.FileAttachmentEntry.PATH : {
				return PailContract.FileAttachmentEntry.TABLE_NAME;
			}
			case PailContract.AudioAttachmentEntry.PATH : {
				return PailContract.AudioAttachmentEntry.TABLE_NAME;
			}
			case PailContract.VideoAttachmentEntry.PATH : {
				return PailContract.VideoAttachmentEntry.TABLE_NAME;
			}
			case PailContract.LinkAttachmentEntry.PATH : {
				return PailContract.LinkAttachmentEntry.TABLE_NAME;
			}
			case PailContract.ImageAttachmentEntry.PATH : {
				return PailContract.ImageAttachmentEntry.TABLE_NAME;
			}
			default:
				throw new UnsupportedOperationException("Unsupported attachment type specified");
		}
	}
	//getsAttachmentsBased on the problemid
	private Cursor getProblemAndAttachmentById(Uri uri, String[] projection, String sortOrder) {
		long problemId = PailContract.ProblemEntry.getProblemIdFromUri(uri);
		if (problemId == 0) {
			Log.e(ProblemProvider.class.getSimpleName(), "Problem id is 0");
		}

		//Query the db by the problemId
		return sQueryProblemAndAttachment.query(mOpenHelper.getReadableDatabase(),
				projection,
				sProblemQuery,
				new String[] {Long.toString(problemId)},
				null,
				null,
				sortOrder);
	}
	//Query the database by the problemId
	private Cursor getProblemById(Uri uri, String[] projection, String sortOrder) {
		long id = PailContract.ProblemEntry.getProblemIdFromUri(uri);

		return mOpenHelper.getReadableDatabase()
				.query(PailContract.ProblemEntry.TABLE_NAME,
						projection,
						sProblemQuery,
						new String[]{ Long.toString(id) },
						null,
						null,
						sortOrder
		);
	}
	//getAttachments
	private Cursor getAttachments(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		//Using this queryBuilder so that it query the problems db and gets all the attachments
		//as long as it has a valid probid
		return sQueryProblemAndAttachment.query(mOpenHelper.getReadableDatabase(),
				projection,
				selection,
				selectionArgs,
				null,
				null,
				sortOrder);
	}
	//getProblems
	private Cursor getProblems(String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		return mOpenHelper.getReadableDatabase().query(
				PailContract.ProblemEntry.TABLE_NAME,
				projection,
				selection,
				selectionArgs,
				null,
				null,
				sortOrder
		);
	}
	//get a specified attachment type based on the id
	private Cursor getProblemAttachmentsByAttachmentTypeAndId(Uri uri, String[] projection, String sortOrder) {
		String attachmentType = PailContract.ProblemEntry.getAttachmentTypeFromProblemUri(uri);
		long problemId = PailContract.ProblemEntry.getProblemIdFromUri(uri);
		//construct a different query builder based on the uri sent back
		String table = getTable(attachmentType);
		return mOpenHelper.getReadableDatabase().query(table,
				projection,
				table +"."+PailContract.Attachment.COLUMN_PROB_KEY + " = ?",
				new String[] { Long.toString(problemId) },
				null,
				null,
				sortOrder);
	}
	//get the attachment based on the prodid and attachid
	private Cursor getAttachmentByProbIdAndId(Uri uri, String[] projection, String sortOrder) {
		String attachmentType = PailContract.Attachment.getAttachmentTypeFromUri(uri);
		//Attachmenttype was specified
		long probid = PailContract.ProblemEntry.getProblemIdFromUri(uri);
		long attaId = PailContract.ProblemEntry.getAttachmentIdFromProblemUri(uri);
		return
		mOpenHelper.getReadableDatabase()
		.query(getTable(attachmentType),
				projection,
				sProblemAttachQuery,
				new String[]{Long.toString(probid), Long.toString(attaId)},
				null,
				null,
				sortOrder
		);
	}
	//getAttachmentsByType
	private Cursor getAttachmentsBasedOnType(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		String attachmentType = PailContract.Attachment.getAttachmentTypeFromUri(uri);
		return mOpenHelper.getReadableDatabase().query(getTable(attachmentType), projection, selection, selectionArgs, null, null, sortOrder);
	}
	//getAttachment by Id
	private Cursor getAttachment(Uri uri, String[] projection, String sortOrder) {
		long attachId = PailContract.Attachment.getAttachmentIdFromUri(uri);
		return lookForAttachment(attachId, projection, sortOrder);
	}
	//go through all the attachment db till you find a match
	private Cursor lookForAttachment(long attachId, String[] projection, String sortOrder) {
		Cursor c;
		c =  performAttachQuery(attachId, PailContract.NoteAttachmentEntry.TABLE_NAME, projection, sortOrder);
		//Found - Note
		if (c.moveToFirst()) return c;
		c =  performAttachQuery(attachId, PailContract.ImageAttachmentEntry.TABLE_NAME, projection, sortOrder);
		//Found - Image
		if (c.moveToFirst()) return c;
		c =  performAttachQuery(attachId, PailContract.LinkAttachmentEntry.TABLE_NAME, projection, sortOrder);
		//Found - Link
		if (c.moveToFirst()) return c;
		c =  performAttachQuery(attachId, PailContract.VideoAttachmentEntry.TABLE_NAME, projection, sortOrder);
		//Found - Video
		if (c.moveToFirst()) return c;
		c =  performAttachQuery(attachId, PailContract.FileAttachmentEntry.TABLE_NAME, projection, sortOrder);
		//Found - File
		if (c.moveToFirst()) return c;
		c =  performAttachQuery(attachId, PailContract.AudioAttachmentEntry.TABLE_NAME, projection, sortOrder);
		//Found - Audio
		if (c.moveToFirst()) return c;
		return c;
	}
	//performs querys, optimised only for attachment
	private Cursor performAttachQuery(long attachId, String table, String[] projection, String sortOrder) {
		Cursor c;
		c =  mOpenHelper.getReadableDatabase()
		.query(table,
				projection,
				getQuery(PailContract.NoteAttachmentEntry.TABLE_NAME),
				new String[]{ Long.toString(attachId) },
				null,
				null,
				sortOrder
		);
		return c;
	}
	//buildUri
	static UriMatcher buildUriMatcher () {
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = PailContract.CONTENT_AUTHORITY;
		//# - number
		//* - string

		// For each type of URI you want to add, create a corresponding code.
		/*Problems*/
		//problems
		matcher.addURI(authority, PailContract.PATH_PROBLEM, PROBLEMS);
		//problem/id/
		matcher.addURI(authority,
				PailContract.PATH_PROBLEM+"/#", PROBLEMS_WITH_ID);
		//problems/id/attachments/
		matcher.addURI(authority,
				PailContract.PATH_PROBLEM+"/#/"+PailContract.PATH_ATTACHMENT, PROBLEMS_WITH_ID_AND_ATTACHMENTS);
		//problems/id/attachments/attachment_type
		matcher.addURI(authority,
				PailContract.PATH_PROBLEM+"/#/"+PailContract.PATH_ATTACHMENT+"/*",
				PROBLEMS_WITH_ID_AND_ATTACHMENTS_AND_ATTACHMENTTYPE);
		//problems/id/attachments/attachment_type/attachment_id
		matcher.addURI(authority,
				PailContract.PATH_PROBLEM+"/#/"+PailContract.PATH_ATTACHMENT+"/*/#",
				PROBLEMS_WITH_ID_AND_ATTACHMENTS_AND_ATTACHMENTTYPE_AND_ATTACHMENTID
			);
		/*Attachments*/
		//attachments
		matcher.addURI(authority, PailContract.PATH_ATTACHMENT,
				ATTACHMENTS);
		//attachments/attachmentId
		matcher.addURI(authority, PailContract.PATH_ATTACHMENT+"/#", ATTACHMENT_WITH_ID);
		//attachments/attachmentType/
		matcher.addURI(authority, PailContract.PATH_ATTACHMENT+"/*",
				ATTACHMENTS_WITH_ATTACHMENTTYPE);
		//attachments/attachmentType/attachmentId
		matcher.addURI(authority, PailContract.PATH_ATTACHMENT+"/*/#",
				ATTACHMENTS_WITH_ATTACHMENTTYPE_AND_ID);

		return matcher;
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new PailDbHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		Cursor retCursor = null;
		switch (sUriMatcher.match(uri)) {
			//problems - //get all problems
			case PROBLEMS : {
				retCursor = getProblems(projection, selection, selectionArgs, sortOrder);
				break;
			}
			//problems/id - //get the problem details
			case PROBLEMS_WITH_ID : {
				retCursor = getProblemById(uri, projection, sortOrder);
				break;
			}
			//problems/id/attachments
			case PROBLEMS_WITH_ID_AND_ATTACHMENTS : {
				retCursor = getProblemAndAttachmentById(uri, projection, sortOrder);
				break;
			}
			//problems/id/attachments/attachmenttype
			case PROBLEMS_WITH_ID_AND_ATTACHMENTS_AND_ATTACHMENTTYPE : {
				retCursor = getProblemAttachmentsByAttachmentTypeAndId(uri, projection, sortOrder);
				break;
			}
			//problems/id/attachments/attachmenttype/attachmentid
			case PROBLEMS_WITH_ID_AND_ATTACHMENTS_AND_ATTACHMENTTYPE_AND_ATTACHMENTID : {
				retCursor = getAttachmentByProbIdAndId(uri, projection, sortOrder);
				break;
			}
			//attachments
			case ATTACHMENTS : {
				retCursor = getAttachments(projection, selection, selectionArgs, sortOrder);
				break;
			}
			//attachments/id
			case ATTACHMENT_WITH_ID : {
				retCursor = getAttachment(uri, projection, sortOrder);
				break;
			}
			//attachments/attachmenttype
			case ATTACHMENTS_WITH_ATTACHMENTTYPE : {
				retCursor = getAttachmentsBasedOnType(uri, projection, selection, selectionArgs, sortOrder);
				break;
			}
			//attachments/attachmenttype/id
			case ATTACHMENTS_WITH_ATTACHMENTTYPE_AND_ID : {
				retCursor = getAttachment(uri, projection, sortOrder);
				break;
			}
		}
		if (retCursor != null) {
			retCursor.setNotificationUri(getContext().getContentResolver(), uri);
		}
		return retCursor;
	}

	@Override
	public String getType(Uri uri) {

		final int match = sUriMatcher.match(uri);
		switch (match) {
			case PROBLEMS:
				return PailContract.ProblemEntry.CONTENT_TYPE;
			case PROBLEMS_WITH_ID:
				return PailContract.ProblemEntry.CONTENT_ITEM_TYPE;
			case PROBLEMS_WITH_ID_AND_ATTACHMENTS:
				return PailContract.ProblemEntry.CONTENT_TYPE;
			case PROBLEMS_WITH_ID_AND_ATTACHMENTS_AND_ATTACHMENTTYPE:
				return PailContract.ProblemEntry.CONTENT_TYPE;
			case PROBLEMS_WITH_ID_AND_ATTACHMENTS_AND_ATTACHMENTTYPE_AND_ATTACHMENTID:
				return PailContract.ProblemEntry.CONTENT_ITEM_TYPE;
			case ATTACHMENTS:
				return PailContract.Attachment.CONTENT_TYPE;
			case ATTACHMENTS_WITH_ATTACHMENTTYPE:
				return PailContract.Attachment.CONTENT_TYPE;
			case ATTACHMENT_WITH_ID:
				return PailContract.Attachment.CONTENT_ITEM_TYPE;
			case ATTACHMENTS_WITH_ATTACHMENTTYPE_AND_ID:
				return PailContract.Attachment.CONTENT_ITEM_TYPE;
			default:
				throw new UnsupportedOperationException("Unknown uri: "+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		Uri returnUri;
		switch (match) {
			//problems - //get all problems
			case PROBLEMS : {
				values = PailUtilities.normalizeData(values);
				long _id = db.insert(PailContract.ProblemEntry.TABLE_NAME, null, values);
				if (_id > 0)
					returnUri = PailContract.ProblemEntry.buildProblemWithIdUri(_id);
				else
					throw new SQLException("Failed to insert row into " + uri);
				break;
			}
			//attachments/attachmenttype
			case ATTACHMENTS_WITH_ATTACHMENTTYPE : {
				values = PailUtilities.normalizeData(values);
				String type = PailContract.Attachment.getAttachmentTypeFromUri(uri);
				String table = getTable(type);
				long _id = db.insertOrThrow(table, null, values);
				if (_id !=  -1)
					returnUri = PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(getInstanceofAttachment(type), values.getAsLong(PailContract.Attachment.COLUMN_ATTACH_ID));
				else
					throw new SQLException("Failed to insert row into " + uri);
				break;
			}
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		db.close();
		return returnUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int rowsDeleted;
		if (null == selection) selection = "1";
		switch (match) {
			//problems
			case PROBLEMS : {
				rowsDeleted = db.delete(PailContract.ProblemEntry.TABLE_NAME, selection, selectionArgs);
				break;
			}
			case PROBLEMS_WITH_ID : {
				long id = PailContract.ProblemEntry.getProblemIdFromUri(uri);
				rowsDeleted = db.delete(PailContract.ProblemEntry.TABLE_NAME, sProblemQuery, new String[] { Long.toString(id) });
				break;
			}
			case ATTACHMENTS_WITH_ATTACHMENTTYPE_AND_ID : {
				long id = PailContract.Attachment.getAttachmentIdFromAttachmentTypeUri(uri);
				String type = PailContract.Attachment.getAttachmentTypeFromUri(uri);
				rowsDeleted = db.delete(getTable(type), sAttachQuery, new String[] { Long.toString(id) });
				break;
			}
			//attachments/attachmenttype
			case ATTACHMENTS_WITH_ATTACHMENTTYPE : {
				String type = PailContract.Attachment.getAttachmentTypeFromUri(uri);
				rowsDeleted = db.delete(getTable(type), selection, selectionArgs);
				break;
			}
			default:
				throw new UnsupportedOperationException("Unknown uri: " +uri);
		}

		if (rowsDeleted != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int rowsUpdated;
		values = PailUtilities.normalizeData(values);
		switch (match) {
			//problems
			case PROBLEMS : {
				rowsUpdated = db.update(PailContract.ProblemEntry.TABLE_NAME, values, selection, selectionArgs);
				break;
			}
			case PROBLEMS_WITH_ID : {
				long id = PailContract.ProblemEntry.getProblemIdFromUri(uri);
				rowsUpdated = db
						.update(
								PailContract.ProblemEntry.TABLE_NAME,
								values, sProblemQuery,  new String[] { Long.toString(id) });
				break;
			}
			//attachments/attachmenttype
			case ATTACHMENTS_WITH_ATTACHMENTTYPE : {
				if (!values.containsKey(PailContract.EntryBaseColumns.COLUMN_DATE_MODIFIED)) {
					values.put(PailContract.EntryBaseColumns.COLUMN_DATE_MODIFIED, PailUtilities.currentDate());
				}
				String type = PailContract.Attachment.getAttachmentTypeFromUri(uri);
				rowsUpdated = db.update(getTable(type), values, selection, selectionArgs);
				break;
			}
			case ATTACHMENTS_WITH_ATTACHMENTTYPE_AND_ID : {
				if (!values.containsKey(PailContract.EntryBaseColumns.COLUMN_DATE_MODIFIED)) {
					values.put(PailContract.EntryBaseColumns.COLUMN_DATE_MODIFIED, PailUtilities.currentDate());
				}
				String type = PailContract.Attachment.getAttachmentTypeFromUri(uri);
				long id = PailContract.Attachment.getAttachmentIdFromAttachmentTypeUri(uri);
				rowsUpdated = db.update(getTable(type), values, sAttachQuery, new String[] { Long.toString(id) });
				break;
			}
			default:
				throw new UnsupportedOperationException("Unknown uri: " +uri);
		}

		if (rowsUpdated != 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return rowsUpdated;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		switch (match) {
			//problems
			case PROBLEMS : {
				db.beginTransaction();
				int returnCount = 0;
				try {
					for (ContentValues value : values) {
						value = PailUtilities.normalizeData(value);
						long _id = db.insert(PailContract.ProblemEntry.TABLE_NAME, null, value);
						if (_id != -1) {
							returnCount++;
						}
					}
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				getContext().getContentResolver().notifyChange(uri, null);
				return returnCount;
			}
			//attachments/attachmenttype
			case ATTACHMENTS_WITH_ATTACHMENTTYPE : {
				db.beginTransaction();
				int returnCount = 0;
				try {
					for (ContentValues value : values) {
						value = PailUtilities.normalizeData(value);
						String type = PailContract.Attachment.getAttachmentTypeFromUri(uri);
						String table = getTable(type);
						long _id = db.insert(table, null, value);
						if (_id > 0) {
							returnCount++;
						}

					}
					db.setTransactionSuccessful();
				} finally {
					db.endTransaction();
				}
				getContext().getContentResolver().notifyChange(uri, null);
				return returnCount;
			}
			default:
				throw new UnsupportedOperationException("Unknown uri: " +uri);
		}
	}
}