package udacity.gas.com.solveaproblem.data;

import android.database.Cursor;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public class ProblemItem {
	private String title;
	private String description;

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
		pItem.setTitle(cursor.getString(PailContract.ProblemEntry.i_PROBLEM_TITLE));
		pItem.setDescription(cursor.getString(PailContract.ProblemEntry.i_PROBLEM_DESCRIPTION));

//		pItem.setTitle(cursor.getString(cursor.getColumnIndex(PailContract.ProblemEntry.COLUMN_TITLE)));
//		pItem.setDescription(cursor.getString(cursor.getColumnIndex(PailContract.ProblemEntry.COLUMN_DESCRIPTION)));

		return pItem;
	}
}