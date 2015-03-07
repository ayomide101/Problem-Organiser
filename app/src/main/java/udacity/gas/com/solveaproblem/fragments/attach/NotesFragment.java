package udacity.gas.com.solveaproblem.fragments.attach;

import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.adapters.CursorRecyclerViewAdapter;
import udacity.gas.com.solveaproblem.data.NoteItem;
import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.data.PailUtilities;

/**
 * Created by Fagbohungbe on 02/03/2015.
 */
public class NotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	public static int ID = 1;
	private static long PROB_ID;
	private static int LOADER_ID = 1;
	private NotesAdapter notesAdapter;
	private RecyclerView recyclerView;
	private LinearLayout tempView;
	private FloatingActionButton btAddNote;
	private FrameLayout mainNoteContent;
	private MaterialDialog mMaterialDialog;
	private int mPrivacy;
	private LinearLayout etLayout;
	private EditText etNoteContent;
	private Switch etPrivacy;
	private ContentResolver resolver;
	private Bundle mBundle;

	public static NotesFragment getInstance(long problemId) {
		NotesFragment myFragment = new NotesFragment();
		Bundle args = new Bundle();
		args.putLong(PailContract.ProblemEntry.BUNDLE_KEY, problemId);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_note, container, false);
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
		notesAdapter = new NotesAdapter(getActivity(), null);
		//show the notes
		recyclerView = (RecyclerView) getActivity().findViewById(R.id.notesList);
		mainNoteContent = (FrameLayout) getActivity().findViewById(R.id.note_main_content);
		tempView = (LinearLayout) getActivity().findViewById(R.id.note_tempview);
		btAddNote = (FloatingActionButton) getActivity().findViewById(R.id.btAddNote);

		tempView.setOnClickListener(this);
		btAddNote.setOnClickListener(this);
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
		recyclerView.setAdapter(notesAdapter);
		mMaterialDialog = new MaterialDialog.Builder(getActivity())
				.title("ADD NOTE")
				.autoDismiss(false)
				.customView(R.layout.note_add_form, true)
				.positiveText("Add")
				.positiveColor(R.color.primaryColor)
				.negativeText("Cancel")
				.callback(new MaterialDialog.ButtonCallback() {
					@Override
					public void onPositive(MaterialDialog dialog) {
						//Add note to the database
						createNote(dialog);
					}

					@Override
					public void onNegative(MaterialDialog dialog) {
						dialog.dismiss();
					}
				})
				.build();

		etLayout = (LinearLayout) mMaterialDialog.getCustomView();
		etNoteContent = (EditText) etLayout.findViewById(R.id.note_content);
		etPrivacy = (Switch) etLayout.findViewById(R.id.note_privacy);
		etPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					mPrivacy = PailContract.VAL_PRIVACY_PRIVATE;
				} else {
					mPrivacy = PailContract.VAL_PRIVACY_PUBLIC;
				}
			}
		});
	}

	private ContentValues getData() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PailContract.NoteAttachmentEntry.COLUMN_CONTENT, etNoteContent.getText().toString());
		contentValues.put(PailContract.NoteAttachmentEntry.COLUMN_PROB_KEY, PROB_ID);
		contentValues.put(PailContract.NoteAttachmentEntry.COLUMN_ATTACH_ID, PailContract.ProblemEntry.generateProblemId());
		contentValues.put(PailContract.NoteAttachmentEntry.COLUMN_PRIVACY, mPrivacy);
		contentValues.put(PailContract.NoteAttachmentEntry.COLUMN_RELEVANCE, 0);
		contentValues.put(PailContract.NoteAttachmentEntry.COLUMN_TITLE, "");

		return contentValues;
	}

	private void createNote(MaterialDialog dialog) {
		ContentValues contentValues = getData();

		getActivity().getContentResolver()
				.insert(
						PailContract.Attachment.buildAttachmentWithAttachmentTypeUri(new PailContract.NoteAttachmentEntry()),
						contentValues
				);
		etNoteContent.setText("");
		getLoaderManager().restartLoader(LOADER_ID, getArguments(), this); //Call this the content can reload
		PailUtilities.hideKeyBoardFromScreen(getActivity(), etNoteContent);
		dialog.dismiss();
		Toast.makeText(getActivity(), "Note created", Toast.LENGTH_LONG).show();
	}

	private void editNote(MaterialDialog dialog, long id, ContentValues contentValues) {
		resolver = getActivity().getContentResolver();
		getActivity().getContentResolver()
				.update(
						PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.NoteAttachmentEntry(), id),
						contentValues,
						null,
						null
				);
		etNoteContent.setText("");
		getLoaderManager().restartLoader(LOADER_ID, getArguments(), this); //Call this the content can reload
		dialog.dismiss();
	}

	private void editNote(MaterialDialog dialog, long id) {
		ContentValues contentValues = getData();
		editNote(dialog, id, contentValues);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String sortOrder = PailContract.NoteAttachmentEntry.COLUMN_DATE + " DESC";
		return new CursorLoader(getActivity(),
				PailContract.ProblemEntry.buildProblemWithAttachmentTypeUri(PROB_ID, new PailContract.NoteAttachmentEntry()),
				PailContract.NoteAttachmentEntry.NOTES_COLUMNS,
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
			notesAdapter.swapCursor(data);
		} else {
			tempView.setVisibility(View.VISIBLE); //hide btAdd Problem
			mainNoteContent.setVisibility(View.GONE); //show mainNoteContent
			notesAdapter.swapCursor(null);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		notesAdapter.swapCursor(null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case (R.id.note_tempview):
			case (R.id.btAddNote): {
				//Show the dialog here
				mMaterialDialog.show();
				break;
			}
		}
	}

	public class NotesAdapter extends CursorRecyclerViewAdapter<NotesAdapter.NoteViewHolder> {

		public NotesAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			CardView view = (CardView) getActivity().getLayoutInflater().inflate(R.layout.note_card_normal, parent, false);
			NoteViewHolder vh = new NoteViewHolder(view);
			return vh;
		}

		@Override
		public void onBindViewHolder(NoteViewHolder viewHolder, Cursor cursor) {
			NoteItem noteItem = NoteItem.fromCursor(cursor);
			viewHolder._PROBLEM_ID = noteItem.getPROB_ID();
			viewHolder._ID = noteItem.get_ID();
			viewHolder._ATTACH_ID = noteItem.getATTACH_ID();
			viewHolder._relevance = noteItem.getRelevance();
			viewHolder._privacy = noteItem.getPrivacy();
			viewHolder._date = noteItem.getDate();
			viewHolder._date_modified = noteItem.getDate_modified();
			viewHolder._content = noteItem.getContent();
			viewHolder._title = noteItem.getTitle();
			viewHolder.note_content.setText(viewHolder._content);
			viewHolder.card_date.setText((Long.toString(viewHolder._date_modified)));
			if (noteItem.getPrivacy() == PailContract.VAL_PRIVACY_PRIVATE) {
				viewHolder.lockCard.setImageDrawable(getResources().getDrawable(R.drawable.device_access_secure));
			} else {
				viewHolder.lockCard.setImageDrawable(getResources().getDrawable(R.drawable.device_access_not_secure));
			}
		}

		class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

			final TextView note_content;
			final ImageButton deleteCard;
			final TextView card_date;
			final ImageButton lockCard;
			long _ATTACH_ID;
			long _ID;
			long _PROBLEM_ID;
			int _privacy;
			long _relevance;
			long _date;
			long _date_modified;
			String _content;
			String _title;

			public NoteViewHolder(View itemView) {
				super(itemView);
				card_date = (TextView) itemView.findViewById(R.id.carddate);
				note_content = (TextView) itemView.findViewById(R.id.note_content);

				deleteCard = (ImageButton) itemView.findViewById(R.id.deletecard);
				lockCard = (ImageButton) itemView.findViewById(R.id.lockcard);


				deleteCard.setOnClickListener(this);
				lockCard.setOnClickListener(this);
			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case (R.id.deletecard): {
						getActivity().getContentResolver()
								.delete(PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.NoteAttachmentEntry(), _ID), null, null);
						getLoaderManager().restartLoader(LOADER_ID, getArguments(), (LoaderManager.LoaderCallbacks<Object>) getActivity());
						break;
					}
					case (R.id.lockcard): {
						_privacy = PailUtilities.switchPrivacy(
								getActivity(), _privacy, lockCard, PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.NoteAttachmentEntry(), _ID));
						break;
					}
				}
			}

			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		}
	}
}