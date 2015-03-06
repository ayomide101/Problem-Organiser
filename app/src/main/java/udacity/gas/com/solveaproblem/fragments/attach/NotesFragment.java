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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import me.drakeet.materialdialog.MaterialDialog;
import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.adapters.CursorRecyclerViewAdapter;
import udacity.gas.com.solveaproblem.data.NoteItem;
import udacity.gas.com.solveaproblem.data.PailContract;

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
		mainNoteContent = (FrameLayout) getActivity().findViewById(R.id.mainNoteContent);
		tempView = (LinearLayout) getActivity().findViewById(R.id.tempView);
		btAddNote = (FloatingActionButton) getActivity().findViewById(R.id.btAddNote);

		tempView.setOnClickListener(this);
		btAddNote.setOnClickListener(this);
		recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
		recyclerView.setAdapter(notesAdapter);
		mMaterialDialog = new MaterialDialog(getActivity());
		EditText contentView = new EditText(getActivity());
		contentView.setHint("Please add a note about/on/for the problem you're having");
		mMaterialDialog
				.setTitle("ADD NOTE")
				.setMessage("")
				.setNegativeButton("CANCEL", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMaterialDialog.dismiss();
					}
				})
				.setView(contentView)
				.setPositiveButton("ADD", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mMaterialDialog.dismiss();
					}
				});
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
		if (null != data) {
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
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case (R.id.tempView) :
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
			try {
				CardView view = (CardView) getActivity().getLayoutInflater().inflate(R.layout.note_card_normal, parent, false);
				NoteViewHolder vh = new NoteViewHolder(view);
				return vh;
			} catch (NullPointerException e) {
				Log.e("HomeFragment", e.getMessage() + "");
				return null;
			}
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

			private final TextView note_content;
			private final ImageButton shareCard;
			private final ImageButton deleteCard;
			private final ImageButton editCard;
			private final TextView card_date;
			private final ImageButton lockCard;
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
				shareCard = (ImageButton) itemView.findViewById(R.id.sharecard);
				deleteCard = (ImageButton) itemView.findViewById(R.id.deletecard);
				editCard = (ImageButton) itemView.findViewById(R.id.editcard);
				lockCard = (ImageButton) itemView.findViewById(R.id.lockcard);

				deleteCard.setOnClickListener(this);
				shareCard.setOnClickListener(this);
				editCard.setOnClickListener(this);
				lockCard.setOnClickListener(this);

				itemView.setOnClickListener(this);
				itemView.setOnLongClickListener(this);

			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case (R.id.editcard): {
						//Show the dialog

						break;
					}
					case (R.id.sharecard): {
						Toast.makeText(v.getContext(), "Share", Toast.LENGTH_SHORT).show();
						break;
					}
					case (R.id.deletecard): {
						getActivity().getContentResolver()
								.delete(PailContract.ProblemEntry.buildProblemWithIdUri(_PROBLEM_ID), null, null);
						break;
					}
					case (R.id.lockcard): {
						Toast.makeText(v.getContext(), "Locked", Toast.LENGTH_SHORT).show();
						break;
					}
					case (R.id.card_view): {
						Toast.makeText(v.getContext(), "Card View", Toast.LENGTH_SHORT).show();
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