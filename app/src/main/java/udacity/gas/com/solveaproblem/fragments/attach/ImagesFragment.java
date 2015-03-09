package udacity.gas.com.solveaproblem.fragments.attach;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.adapters.CursorRecyclerViewAdapter;
import udacity.gas.com.solveaproblem.data.ImageItem;
import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.data.PailUtilities;

/**
 * Created by Fagbohungbe on 28/02/2015.
 */
public class ImagesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	public static int ID = 3;
	private static long PROB_ID;
	private static int LOADER_ID = 0;
	private RecyclerView recyclerView;
	private FrameLayout mainNoteContent;
	private LinearLayout tempView;
	private FloatingActionButton btAddImage;
	private ImagesAdapter mImageAdapter;
	private FloatingActionButton btCaptureImage;
	private String mCurrentPhotoPath;
	private Intent takePictureIntent;
	private String imageFileName;
	private String imageFileSize;
	private AlbumStorageDirFactory mAlbumStorageDirFactory;
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_IMAGE_GET = 2;

	abstract class AlbumStorageDirFactory {
		public abstract File getAlbumStorageDir(String albumName);
	}

	public static ImagesFragment getInstance(long problemid) {
		ImagesFragment myFragment = new ImagesFragment();
		Bundle args = new Bundle();
		args.putLong(PailContract.ProblemEntry.BUNDLE_KEY, problemid);
		myFragment.setArguments(args);

		return myFragment;
	}

	public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

		// Standard storage location for digital camera files
		private static final String CAMERA_DIR = "/dcim/";

		@Override
		public File getAlbumStorageDir(String albumName) {
			return new File(
					Environment.getExternalStorageDirectory()
							+ CAMERA_DIR
							+ albumName
			);
		}
	}

	public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

		@Override
		public File getAlbumStorageDir(String albumName) {
			// TODO Auto-generated method stub
			return new File(
					Environment.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_PICTURES
					),
					albumName
			);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_image, container, false);
		Bundle bundle = getArguments();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
			mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
		} else {
			mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		}
		if (bundle != null) {
			PROB_ID = bundle.getLong(PailContract.ProblemEntry.BUNDLE_KEY);
		}
		return layout;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);

		mImageAdapter = new ImagesAdapter(getActivity(), null);

		recyclerView = (RecyclerView) getActivity().findViewById(R.id.imageslist);
		mainNoteContent = (FrameLayout) getActivity().findViewById(R.id.image_main_content);
		tempView = (LinearLayout) getActivity().findViewById(R.id.image_tempview);
		btAddImage = (FloatingActionButton) getActivity().findViewById(R.id.btAddImage);

		tempView.setOnClickListener(this);
		/*if (!getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			btCaptureImage.setVisibility(View.GONE);
		} else {
			btCaptureImage.setVisibility(View.VISIBLE);
			btCaptureImage.setOnClickListener(this);
		}*/
		btAddImage.setOnClickListener(this);
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
		recyclerView.setAdapter(mImageAdapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String sortOrder = PailContract.ImageAttachmentEntry.COLUMN_DATE + " DESC";
		return new CursorLoader(getActivity(),
				PailContract.ProblemEntry.buildProblemWithAttachmentTypeUri(PROB_ID, new PailContract.ImageAttachmentEntry()),
				PailContract.ImageAttachmentEntry.IMAGE_COLUMNS,
				null,
				null,
				sortOrder
		);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (getActivity() == null) {
			return;
		}
		//Data found
		if (data.moveToFirst()) {
			tempView.setVisibility(View.GONE); //hide btAdd Problem
			mainNoteContent.setVisibility(View.VISIBLE); //show mainNoteContent
			mImageAdapter.swapCursor(data);
		} else {
			tempView.setVisibility(View.VISIBLE); //hide btAdd Problem
			mainNoteContent.setVisibility(View.GONE); //show mainNoteContent
			mImageAdapter.swapCursor(null);
		}
	}

	/* Photo album for this application */
	private String getAlbumName() {
		return getString(R.string.album_name);
	}

	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		imageFileName = "IMG_" + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, ".jpg", albumF);
		return imageF;
	}

	private File setUpPhotoFile() throws IOException {
		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();
		return f;
	}

	private void dispatchTakePictureIntent() {
		takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			File f = null;
			try {
				f = setUpPhotoFile();
				mCurrentPhotoPath = f.getAbsolutePath();
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				Log.e("ImagesFragment", e.getMessage());
				mCurrentPhotoPath = null;
			}
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
			if (mCurrentPhotoPath != null) {
				//
				File f = new File(mCurrentPhotoPath);
				imageFileSize = PailUtilities.humanReadableByteCount(f.length(), false);
				ContentValues cn = compileData();
				//Add picture to db here
				insertImage(cn);
				// Reload loader
				reloadManager();
			}
		} else if (requestCode == REQUEST_IMAGE_GET && resultCode == getActivity().RESULT_OK) {
			Uri fullPhotoUri = data.getData();
			//Get the file information and dump into the variables
			dumpImageMetaData(fullPhotoUri);
			if (imageFileName.toString().length() >= 0) {
				ContentValues cn = compileData();
				if (insertImage(cn) != null) {
					reloadManager();
				} else {
					Toast.makeText(getActivity(), "Failed to attach image to problem", Toast.LENGTH_LONG).show();
					Log.e("ImagesFragment", "Failed ot attach image to problem");
				}
			} else {
				Toast.makeText(getActivity(), "Could not load image", Toast.LENGTH_LONG).show();
				Log.e("ImagesFragment", "Failed ot attach image to problem");
			}
		} else {
			Log.e("ImagesFragment", "Nothing was returned from camera.");
		}
	}

	private Loader<Cursor> reloadManager() {
		return getLoaderManager().restartLoader(LOADER_ID, getArguments(), this);
	}

	private Uri insertImage(ContentValues cn) {
		return getActivity().getContentResolver()
				.insert(
						PailContract.Attachment.buildAttachmentWithAttachmentTypeUri(new PailContract.ImageAttachmentEntry()),
						cn
				);
	}

	private ContentValues compileData() {
		ContentValues cn = new ContentValues();
		cn.put(PailContract.ImageAttachmentEntry.COLUMN_ATTACH_ID, PailContract.ProblemEntry.generateProblemId());
		cn.put(PailContract.ImageAttachmentEntry.COLUMN_PRIVACY, PailContract.VAL_PRIVACY_PRIVATE);
		cn.put(PailContract.ImageAttachmentEntry.COLUMN_RELEVANCE, 0);
		cn.put(PailContract.ImageAttachmentEntry.COLUMN_FILE_DESCRIPTION, "");
		cn.put(PailContract.ImageAttachmentEntry.COLUMN_FILE_NAME, imageFileName);
		cn.put(PailContract.ImageAttachmentEntry.COLUMN_FILE_SIZE, imageFileSize);
		cn.put(PailContract.ImageAttachmentEntry.COLUMN_FILE_TYPE, PailContract.ImageAttachmentEntry.PATH);
		cn.put(PailContract.ImageAttachmentEntry.COLUMN_MIME_TYPE, "image/*");
		cn.put(PailContract.ImageAttachmentEntry.COLUMN_PROB_KEY, PROB_ID);
		cn.put(PailContract.ImageAttachmentEntry.COLUMN_FILE_URI, mCurrentPhotoPath);

		return cn;
	}

	//dumps data created from select pic
	private boolean dumpImageMetaData(Uri uri) {
		Cursor cursor = getActivity().getContentResolver()
				.query(uri, null, null, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				imageFileName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
				int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
				if (!cursor.isNull(sizeIndex)) {
					imageFileSize = PailUtilities.humanReadableByteCount(cursor.getInt(sizeIndex), false);
				} else {
					imageFileSize = "Unknown";
				}
				mCurrentPhotoPath = uri.toString();
				return true;
			} else {
				return false;
			}
		} finally {
			assert cursor != null;
			cursor.close();
			return false;
		}
	}

	public void selectImage() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		intent.putExtra(Intent.CATEGORY_OPENABLE, true);
		if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
			startActivityForResult(intent, REQUEST_IMAGE_GET);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mImageAdapter.swapCursor(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case (R.id.image_tempview):
			case (R.id.btAddImage): {
				selectImage();
			}
		}
	}

	public class ImagesAdapter extends CursorRecyclerViewAdapter<ImagesAdapter.ImageViewHolder> {

		public ImagesAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public void onBindViewHolder(ImageViewHolder viewHolder, Cursor cursor) {
			ImageItem imageItem = ImageItem.fromCursor(cursor);
			viewHolder._id = imageItem.get_id();
			viewHolder._file_mime_type = imageItem.getFile_mime_type();
			viewHolder._file_name = imageItem.getFile_name();
			viewHolder._file_uri = imageItem.getFile_uri();
			viewHolder._file_size = imageItem.getFile_size();
			viewHolder._file_type = imageItem.getFile_type();
			viewHolder._file_description = imageItem.getFile_description();
			viewHolder._prod_id = imageItem.getProd_id();
			viewHolder._attach_id = imageItem.getAttach_id();
			viewHolder._relevance = imageItem.getRelevance();
			viewHolder._date = imageItem.getDate();
			viewHolder._date_modified = imageItem.getDate_modified();

//			try {
//				viewHolder.swImageHolder.setImageBitmap(PailUtilities.getBitmapFromUri(getActivity(), Uri.parse(imageItem.getFile_uri())));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
			viewHolder.swImageHolder.setImageBitmap(PailUtilities.setPic(imageItem.getFile_uri(), viewHolder.swImageHolder));
			viewHolder.swCardDate.setText(Long.toString(viewHolder._date));

			viewHolder.swImageFileName.setText(viewHolder._file_name);
			viewHolder.swImageFileSize.setText(viewHolder._file_size);
			if (viewHolder._file_description.length() <= 0) {
				viewHolder.swImageDescription.setVisibility(View.GONE);
			} else {
				viewHolder.swImageDescription.setText(viewHolder._file_description);
				viewHolder.swImageDescription.setVisibility(View.VISIBLE);
			}
			if (imageItem.getPrivacy() == PailContract.VAL_PRIVACY_PRIVATE) {
				viewHolder.swLockCard.setImageDrawable(getResources().getDrawable(R.drawable.device_access_secure));
			} else {
				viewHolder.swLockCard.setImageDrawable(getResources().getDrawable(R.drawable.device_access_not_secure));
			}
		}

		@Override
		public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LinearLayout view = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.image_card_normal, parent, false);
			return new ImageViewHolder(view);
		}

		class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

			public final TextView swImageFileName;
			public final TextView swImageFileSize;
			public final TextView swImageDescription;
			public final TextView swCardDate;
			public final ImageButton swDeleteCard;
			public final ImageButton swLockCard;
			public long _id;
			public String _file_mime_type;
			public String _file_name;
			public String _file_uri;
			public String _file_size;
			public String _file_type;
			public String _file_description;
			public long _prod_id;
			public long _attach_id;
			public long _relevance;
			public long _date;
			public long _date_modified;
			public int _privacy;
			public ImageView swImageHolder;

			public ImageViewHolder(View itemView) {
				super(itemView);
				swImageHolder = (ImageView) itemView.findViewById(R.id.image_holder);
				swImageFileName = (TextView) itemView.findViewById(R.id.image_file_name);
				swImageFileSize = (TextView) itemView.findViewById(R.id.image_file_size);
				swImageDescription = (TextView) itemView.findViewById(R.id.image_description);

				swCardDate = (TextView) itemView.findViewById(R.id.carddate);
				swDeleteCard = (ImageButton) itemView.findViewById(R.id.deletecard);
				swLockCard = (ImageButton) itemView.findViewById(R.id.lockcard);


				swDeleteCard.setOnClickListener(this);
				swLockCard.setOnClickListener(this);
				swImageHolder.setOnClickListener(this);
			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case (R.id.deletecard): {
						getActivity().getContentResolver()
								.delete(PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.ImageAttachmentEntry(), _id), null, null);
						getLoaderManager().restartLoader(LOADER_ID, getArguments(), (LoaderManager.LoaderCallbacks<Object>) getActivity());
						break;
					}
					case (R.id.lockcard): {
						_privacy = PailUtilities.switchPrivacy(
								getActivity(), _privacy, swLockCard, PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.ImageAttachmentEntry(), _id));
						break;
					}
					case (R.id.image_holder): {
						//open image viewer
						break;
					}
				}
			}
		}
	}
}