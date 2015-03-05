package udacity.gas.com.solveaproblem.data;

import android.database.Cursor;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public class ProblemItem {
	private String title;
	private String description;
	private long _ID;

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

	private int status;
	private int privacy;

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
		pItem.setTitle(cursor.getString(PailContract.ProblemEntry.i_PROBLEM_TITLE));
		pItem.setDescription(cursor.getString(PailContract.ProblemEntry.i_PROBLEM_DESCRIPTION));
		pItem.setPrivacy(cursor.getInt(PailContract.ProblemEntry.i_PROBLEM_PRIVACY));
		pItem.setStatus(cursor.getInt(PailContract.ProblemEntry.i_PROBLEM_PROBLEM_STATUS));

		return pItem;
	}
}