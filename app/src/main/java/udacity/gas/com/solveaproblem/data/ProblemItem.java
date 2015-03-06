package udacity.gas.com.solveaproblem.data;

import android.database.Cursor;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public class ProblemItem {
	private String title;
	private String description;
	private long _ID;
	private int status;
	private int privacy;
	private long PROB_ID;
	private long date;
	private long date_modified;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static ProblemItem fromCursor(Cursor cursor) {
		ProblemItem pItem = new ProblemItem();
		//Use valid projections
		pItem.set_ID(cursor.getLong(PailContract.ProblemEntry.i_PROBLEM_ID));
		pItem.setPROB_ID(cursor.getLong(PailContract.ProblemEntry.i_PROBLEM_PROBLEMID));
		pItem.setTitle(cursor.getString(PailContract.ProblemEntry.i_PROBLEM_TITLE));
		pItem.setDescription(cursor.getString(PailContract.ProblemEntry.i_PROBLEM_DESCRIPTION));
		pItem.setPrivacy(cursor.getInt(PailContract.ProblemEntry.i_PROBLEM_PRIVACY));
		pItem.setStatus(cursor.getInt(PailContract.ProblemEntry.i_PROBLEM_PROBLEM_STATUS));
		pItem.setDate(cursor.getLong(PailContract.ProblemEntry.i_PROBLEM_DATE));
		pItem.setDate_modified(cursor.getLong(PailContract.ProblemEntry.i_PROBLEM_DATE_MODIFIED));
		return pItem;
	}

	public long getDate_modified() {
		return date_modified;
	}

	public void setDate_modified(long date_modified) {
		this.date_modified = date_modified;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}
}