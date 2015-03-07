package udacity.gas.com.solveaproblem.fragments.attach;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;

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
	private static int LOADER_ID=0;
	private RecyclerView recyclerView;
	private FrameLayout mainNoteContent;
	private LinearLayout tempView;
	private FloatingActionButton btAddNote;
	private ImagesAdapter mImageAdapter;

	public static ImagesFragment getInstance(long problemid) {
		ImagesFragment myFragment = new ImagesFragment();
		Bundle args = new Bundle();
		args.putLong(PailContract.ProblemEntry.BUNDLE_KEY, problemid);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_image, container, false);
		Bundle bundle = getArguments();
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

		recyclerView = (RecyclerView) getActivity().findViewById(R.id.notesList);
		mainNoteContent = (FrameLayout) getActivity().findViewById(R.id.note_main_content);
		tempView = (LinearLayout) getActivity().findViewById(R.id.note_tempview);
		btAddNote = (FloatingActionButton) getActivity().findViewById(R.id.btAddNote);

		tempView.setOnClickListener(this);
		btAddNote.setOnClickListener(this);
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

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mImageAdapter.swapCursor(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case(R.id.image_tempview) :
			case(R.id.btAddImage) : {
				startImageSelector();
			}
		}
	}

	/**/
	private void startImageSelector() {

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

//			viewHolder.swImageHolder.setImageBitmap();
			viewHolder.swCardDate.setText(Long.toString(viewHolder._date));

			viewHolder.swImageFileName.setText(viewHolder._file_name);
			viewHolder.swImageFileLocation.setText(viewHolder._file_uri);
			viewHolder.swImageFileSize.setText(Long.toString(viewHolder._file_size));
			viewHolder.swImageDescription.setText(viewHolder._file_description);
			if (imageItem.getPrivacy() == PailContract.VAL_PRIVACY_PRIVATE) {
				viewHolder.swLockCard.setImageDrawable(getResources().getDrawable(R.drawable.device_access_secure));
			} else {
				viewHolder.swLockCard.setImageDrawable(getResources().getDrawable(R.drawable.device_access_not_secure));
			}
		}

		@Override
		public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			CardView view = (CardView) getActivity().getLayoutInflater().inflate(R.layout.image_card_normal, parent, false);
			return new ImageViewHolder(view);
		}

		class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

			public final TextView swImageFileName;
			public final TextView swImageFileLocation;
			public final TextView swImageFileSize;
			public final TextView swImageDescription;
			public final TextView swCardDate;
			public final ImageButton swDeleteCard;
			public final ImageButton swLockCard;
			public long _id;
			public String _file_mime_type;
			public String _file_name;
			public String _file_uri;
			public long _file_size;
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
				swImageFileLocation = (TextView) itemView.findViewById(R.id.image_file_uri);
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
					case(R.id.image_holder) : {
						//open image viewer
						break;
					}
				}
			}
		}
	}
}