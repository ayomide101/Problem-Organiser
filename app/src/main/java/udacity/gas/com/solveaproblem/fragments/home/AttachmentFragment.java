package udacity.gas.com.solveaproblem.fragments.home;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import udacity.gas.com.solveaproblem.AttachmentActivity;
import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.fragments.attach.LinksFragment;
import udacity.gas.com.solveaproblem.fragments.attach.NotesFragment;

/**
 * Created by Fagbohungbe on 27/02/2015.
 */
public class AttachmentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	public static int ID = 1;
	private int LOADER_ID = 0;
	private TextView mLinkCount;
	private TextView mNotesCount;
	private LinearLayout bTOpenLinks;
	private LinearLayout bTOpenNotes;

	public static AttachmentFragment getInstance(int position) {
		return new AttachmentFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_attachments, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);

		mLinkCount = (TextView) getActivity().findViewById(R.id.links_count);
		mNotesCount = (TextView) getActivity().findViewById(R.id.notes_count);
		bTOpenLinks = (LinearLayout) getActivity().findViewById(R.id.open_links);
		bTOpenNotes = (LinearLayout) getActivity().findViewById(R.id.open_notes);
		bTOpenLinks.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AttachmentActivity.class);
				intent.putExtra(AttachmentActivity.ATTACHMENT_TYPE_KEY, LinksFragment.ID);
				startActivity(intent);
			}
		});
		bTOpenNotes.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), AttachmentActivity.class);
				intent.putExtra(AttachmentActivity.ATTACHMENT_TYPE_KEY, NotesFragment.ID);
				startActivity(intent);
			}
		});
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(),
				PailContract.Attachment.buildAttachmentsUri(),
				null,
				PailContract.Attachment.ATTACHMENT_COUNTER,
				null,
				null
		);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data != null) {
			if (data.moveToFirst()) {
				populateView(data);
			} else {
				populateView(data);
			}
		} else {
			mLinkCount.setText("");
			mNotesCount.setText("");
		}
	}

	private void populateView(Cursor data) {
		mNotesCount.setText("("+ data.getLong(data.getColumnIndex(PailContract.NoteAttachmentEntry.TABLE_NAME))+")");
		mLinkCount.setText("("+ data.getLong(data.getColumnIndex(PailContract.LinkAttachmentEntry.TABLE_NAME))+")");
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mLinkCount.setText("");
		mNotesCount.setText("");
	}
}