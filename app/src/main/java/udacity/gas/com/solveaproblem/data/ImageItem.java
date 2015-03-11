package udacity.gas.com.solveaproblem.data;

import android.database.Cursor;

/**
 * Created by Fagbohungbe on 07/03/2015.
 */
public class ImageItem {

	private long _id;
	private String file_mime_type;
	private String file_name;
	private String file_uri;
	private String file_size;
	private String file_type;
	private String file_description;
	private long prod_id;
	private long attach_id;
	private long relevance;
	private long date;
	private long date_modified;
	private int privacy;


	public static ImageItem fromCursor(Cursor cursor) {
		ImageItem imageitem = new ImageItem();
		imageitem.set_id(cursor.getLong(PailContract.ImageAttachmentEntry.i_IMAGE_ID));
		imageitem.setAttach_id(cursor.getLong(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_ATTACH_ID));
		imageitem.setProd_id(cursor.getLong(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_PROB_KEY));
		imageitem.setPrivacy(cursor.getInt(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_PRIVACY));
		imageitem.setRelevance(cursor.getLong(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_RELEVANCE));
		imageitem.setDate(cursor.getLong(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_DATE));
		imageitem.setDate_modified(cursor.getLong(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_DATE_MODIFIED));
		imageitem.setFile_mime_type(cursor.getString(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_MIME_TYPE));
		imageitem.setFile_name(cursor.getString(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_FILE_NAME));
		imageitem.setFile_uri(cursor.getString(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_FILE_URI));
		imageitem.setFile_size(cursor.getString(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_FILE_SIZE));
		imageitem.setFile_type(cursor.getString(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_FILE_TYPE));
		imageitem.setFile_description(cursor.getString(PailContract.ImageAttachmentEntry.i_IMAGE_COLUMN_FILE_DESCRIPTION));
		return imageitem;
	}

	public String getFile_mime_type() {
		return file_mime_type;
	}

	public void setFile_mime_type(String file_mime_type) {
		this.file_mime_type = file_mime_type;
	}

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getFile_uri() {
		return file_uri;
	}

	public void setFile_uri(String file_uri) {
		this.file_uri = file_uri;
	}

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getFile_description() {
		return file_description;
	}

	public void setFile_description(String file_description) {
		this.file_description = file_description;
	}

	public long getProd_id() {
		return prod_id;
	}

	public void setProd_id(long prod_id) {
		this.prod_id = prod_id;
	}

	public long getAttach_id() {
		return attach_id;
	}

	public void setAttach_id(long attach_id) {
		this.attach_id = attach_id;
	}

	public long getRelevance() {
		return relevance;
	}

	public void setRelevance(long relevance) {
		this.relevance = relevance;
	}

	public String getDate() {
		return PailUtilities.getReadableDateString(date);
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getDate_modified() {
		return PailUtilities.getReadableDateString(date_modified);
	}

	public void setDate_modified(long date_modified) {
		this.date_modified = date_modified;
	}

	public int getPrivacy() {
		return privacy;
	}

	public void setPrivacy(int privacy) {
		this.privacy = privacy;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}
}