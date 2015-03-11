package udacity.gas.com.solveaproblem.data;

import android.database.Cursor;

/**
 * Created by Fagbohungbe on 06/03/2015.
 */
public class NoteItem {
	private String content;
	private String title;
	private long _ID;
	private int privacy;
	private long PROB_ID;
	private long ATTACH_ID;
	private long date;
	private long date_modified;
	private long relevance;

	public long getPROB_ID() {
		return PROB_ID;
	}

	public void setPROB_ID(long PROB_ID) {
		this.PROB_ID = PROB_ID;
	}
	public long get_ID() {
		return _ID;
	}

	public void set_ID(long _ID) {
		this._ID = _ID;
	}

	public int getPrivacy() {
		return privacy;
	}

	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static NoteItem fromCursor(Cursor cursor) {
		NoteItem pItem = new NoteItem();
		//Use valid projections
		pItem.set_ID(cursor.getLong(PailContract.NoteAttachmentEntry.i_NOTES_ID));
		pItem.setATTACH_ID(cursor.getLong(PailContract.NoteAttachmentEntry.i_NOTES_ATTACH_ID));
		pItem.setPROB_ID(cursor.getLong(PailContract.NoteAttachmentEntry.i_NOTES_PROB_KEY));
		pItem.setTitle(cursor.getString(PailContract.NoteAttachmentEntry.i_NOTES_TITLE));
		pItem.setContent(cursor.getString(PailContract.NoteAttachmentEntry.i_NOTES_CONTENT));
		pItem.setRelevance(cursor.getLong(PailContract.NoteAttachmentEntry.i_NOTES_RELEVANCE));
		pItem.setPrivacy(cursor.getInt(PailContract.NoteAttachmentEntry.i_NOTES_PRIVACY));
		pItem.setDate(cursor.getLong(PailContract.NoteAttachmentEntry.i_NOTES_DATE));
		pItem.setDate_modified(cursor.getLong(PailContract.NoteAttachmentEntry.i_NOTES_DATE_MODIFIED));
		return pItem;
	}

	public String getDate_modified() {
		return PailUtilities.getReadableDateString(date_modified);
	}

	public void setDate_modified(long date_modified) {
		this.date_modified = date_modified;
	}

	public String getDate() {
		return PailUtilities.getReadableDateString(date);
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getATTACH_ID() {
		return ATTACH_ID;
	}

	public void setATTACH_ID(long ATTACH_ID) {
		this.ATTACH_ID = ATTACH_ID;
	}

	public long getRelevance() {
		return relevance;
	}

	public void setRelevance(long relevance) {
		this.relevance = relevance;
	}
}
