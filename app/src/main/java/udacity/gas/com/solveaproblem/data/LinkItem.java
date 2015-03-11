package udacity.gas.com.solveaproblem.data;

import android.database.Cursor;

/**
 * Created by Fagbohungbe on 07/03/2015.
 */
public class LinkItem {
	private String title;
	private long _ID;
	private int privacy;
	private long PROB_ID;
	private long ATTACH_ID;
	private long date;
	private long date_modified;
	private long relevance;
	private String url;
	private String screenshot;
	private String url_description;

	public static LinkItem fromCursor(Cursor cursor) {
		LinkItem pItem = new LinkItem();
		//Use valid projections
		pItem.set_ID(cursor.getLong(PailContract.LinkAttachmentEntry.i_LINKS_ID));
		pItem.setATTACH_ID(cursor.getLong(PailContract.LinkAttachmentEntry.i_LINKS_ATTACH_ID));
		pItem.setPROB_ID(cursor.getLong(PailContract.LinkAttachmentEntry.i_LINKS_PROB_KEY));
		pItem.setTitle(cursor.getString(PailContract.LinkAttachmentEntry.i_LINKS_TITLE));
		pItem.setUrl(cursor.getString(PailContract.LinkAttachmentEntry.i_LINKS_URL));
		pItem.setUrl_description(cursor.getString(PailContract.LinkAttachmentEntry.i_LINKS_DESCRIPTION));
		pItem.setRelevance(cursor.getLong(PailContract.LinkAttachmentEntry.i_LINKS_RELEVANCE));
		pItem.setScreenshot(cursor.getString(PailContract.LinkAttachmentEntry.i_LINKS_SCREENSHOT));
		pItem.setPrivacy(cursor.getInt(PailContract.LinkAttachmentEntry.i_LINKS_PRIVACY));
		pItem.setDate(cursor.getLong(PailContract.LinkAttachmentEntry.i_LINKS_DATE));
		pItem.setDate_modified(cursor.getLong(PailContract.LinkAttachmentEntry.i_LINKS_DATE_MODIFIED));
		return pItem;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getScreenshot() {
		return screenshot;
	}

	public void setScreenshot(String screenshot) {
		this.screenshot = screenshot;
	}

	public String getUrl_description() {
		return url_description;
	}

	public void setUrl_description(String url_description) {
		this.url_description = url_description;
	}

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
