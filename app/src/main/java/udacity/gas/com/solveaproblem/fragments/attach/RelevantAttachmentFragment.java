package udacity.gas.com.solveaproblem.fragments.attach;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.data.LinkItem;
import udacity.gas.com.solveaproblem.data.NoteItem;
import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.data.PailUtilities;

/**
 * Created by Fagbohungbe on 02/03/2015.
 */
public class RelevantAttachmentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

	public static int ID = 0;
	private static long PROB_ID;
	private View mLayout;
	private ViewGroup mContainer;
	private int LOAD_LINKS = 1;
	private int LOAD_NOTES = 2;
	private LinearLayout mNoteHolder;
	private LinearLayout mLinkHolder;
	private LayoutInflater mInflater;
	private CardView mNoteCardView;
	private TextView mNoteCardDate;
	private TextView mNoteCardContent;
	private ImageButton mNoteCardDelete;
	private ImageButton mNoteCardLock;
	private CardView mLinkCardView;
	private TextView mLinkCardDescription;
	private TextView mLinkCardUrl;
	private TextView mLinkCardDate;
	private ImageButton mLinkCardDelete;
	private ImageButton mLinkCardLock;
	private int mLinkPrivacy;
	private LinkItem mLinkItem;
	private NoteItem mNoteItem;
	private int mNotePrivacy;
	private TextView mNoneFound;
	private TextView mNoneFound2;

	public static RelevantAttachmentFragment getInstance(long problemId) {
		RelevantAttachmentFragment myFragment = new RelevantAttachmentFragment();
		Bundle args = new Bundle();
		args.putLong(PailContract.ProblemEntry.BUNDLE_KEY, problemId);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mContainer = container;
		mLayout = inflater.inflate(R.layout.fragment_relevant_attachment, container, false);
		mInflater = inflater;
		Bundle bundle = getArguments();
		if (bundle != null) {
			PROB_ID = bundle.getLong(PailContract.ProblemEntry.BUNDLE_KEY);
		}  else {
			PROB_ID = PailContract.ProblemEntry.PROD_ID_NOT_SET;
		}
		return mLayout;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getLoaderManager().initLoader(LOAD_LINKS, savedInstanceState, this);
		getLoaderManager().initLoader(LOAD_NOTES, savedInstanceState, this);

		mNoteHolder = (LinearLayout) getActivity().findViewById(R.id.hold_note);
		mLinkHolder = (LinearLayout) getActivity().findViewById(R.id.hold_link);
		mNoneFound = (TextView) mInflater.inflate(R.layout.none_found, mContainer, false);
		mNoneFound2 = (TextView) mInflater.inflate(R.layout.none_found, mContainer, false);

		mNoteCardView = (CardView) mInflater.inflate(R.layout.note_card_normal, mContainer, false);
		mNoteCardDate = (TextView) mNoteCardView.findViewById(R.id.carddate);
		mNoteCardContent = (TextView) mNoteCardView.findViewById(R.id.note_content);
		mNoteCardDelete = (ImageButton) mNoteCardView.findViewById(R.id.deletecard);
		mNoteCardLock = (ImageButton) mNoteCardView.findViewById(R.id.lockcard);

		mNoteCardDelete.setOnClickListener(this);
		mNoteCardLock.setOnClickListener(this);

		mLinkCardView = (CardView) mInflater.inflate(R.layout.link_card_normal, mContainer, false);
		mLinkCardDescription = (TextView) mLinkCardView.findViewById(R.id.link_description);
		mLinkCardUrl = (TextView) mLinkCardView.findViewById(R.id.link_url);
		mLinkCardDate = (TextView) mLinkCardView.findViewById(R.id.carddate);
		mLinkCardDelete = (ImageButton) mLinkCardView.findViewById(R.id.deletecard);
		mLinkCardLock = (ImageButton) mLinkCardView.findViewById(R.id.lockcard);

		mLinkCardDelete.setOnClickListener(this);
		mLinkCardLock.setOnClickListener(this);
		mLinkCardUrl.setOnClickListener(this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		if (id == LOAD_NOTES) {
			Uri uri = (PROB_ID != PailContract.ProblemEntry.PROD_ID_NOT_SET) ?
					PailContract.ProblemEntry.buildProblemWithAttachmentTypeUri(PROB_ID, new PailContract.NoteAttachmentEntry())
					: PailContract.Attachment.buildAttachmentWithAttachmentTypeUri(new PailContract.NoteAttachmentEntry());
			return new CursorLoader(getActivity(),
					uri,
					PailContract.NoteAttachmentEntry.NOTES_COLUMNS,
					null,
					null,
					null
			);
		}
		if (id == LOAD_LINKS) {
			Uri uri = (PROB_ID != PailContract.ProblemEntry.PROD_ID_NOT_SET) ?
					PailContract.ProblemEntry.buildProblemWithAttachmentTypeUri(PROB_ID, new PailContract.LinkAttachmentEntry())
					: PailContract.Attachment.buildAttachmentWithAttachmentTypeUri(new PailContract.LinkAttachmentEntry());
			return new CursorLoader(getActivity(),
					uri,
					PailContract.LinkAttachmentEntry.LINK_COLUMNS,
					null,
					null,
					null
			);
		}
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (loader.getId() == LOAD_NOTES) {
			if (data.moveToFirst()) {
				populateNoteCard(data);
				data.close();
			} else {
				mNoteHolder.removeAllViews();
				mNoteHolder.addView(mNoneFound);
			}
		}
		if (loader.getId() == LOAD_LINKS) {
			if (data.moveToFirst()) {
				populateLinkCard(data);
				data.close();
			} else {
				mLinkHolder.removeAllViews();
				mLinkHolder.addView(mNoneFound2);
			}
		}
	}

	private void populateLinkCard(Cursor data) {
		mLinkItem = LinkItem.fromCursor(data);
		mLinkCardDate.setText(mLinkItem.getDate());
		mLinkCardDescription.setText(mLinkItem.getUrl_description());
		mLinkCardUrl.setText(mLinkItem.getUrl());

		if (mLinkItem.getUrl_description().length() <= 0) { //Hide
			mLinkCardDescription.setVisibility(View.GONE);
		}
		if (mLinkCardUrl.getText().length() <= 0) {
			mLinkCardUrl.setText("https://www.example.org");
		}

		if (mLinkItem.getPrivacy() == PailContract.VAL_PRIVACY_PRIVATE) {
			mLinkCardLock.setImageDrawable(getResources().getDrawable(R.drawable.device_access_secure));
		} else {
			mLinkCardLock.setImageDrawable(getResources().getDrawable(R.drawable.device_access_not_secure));
		}

		//Attach Card
		mLinkHolder.addView(mLinkCardView);
	}

	private void populateNoteCard(Cursor data) {
		mNoteItem = NoteItem.fromCursor(data);
		mNoteCardContent.setText(mNoteItem.getContent());
		mNoteCardDate.setText(mNoteItem.getDate());
		if (mNoteItem.getPrivacy() == PailContract.VAL_PRIVACY_PRIVATE) {
			mNoteCardLock.setImageDrawable(getResources().getDrawable(R.drawable.device_access_secure));
		} else {
			mNoteCardLock.setImageDrawable(getResources().getDrawable(R.drawable.device_access_not_secure));
		}

		//Attach Card
		mNoteHolder.addView(mNoteCardView);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

	@Override
	public void onClick(View v) {
		int dID = v.getId();
		if (dID == mLinkCardUrl.getId()) {
			//Open browser
			String url = mLinkCardUrl.getText().toString();
			PailUtilities.openLink(getActivity(), url);
		}
		if (dID == mLinkCardDelete.getId()) {
			getActivity().getContentResolver()
					.delete(PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.LinkAttachmentEntry(), mLinkItem.get_ID()), null, null);
			getLoaderManager().restartLoader(LOAD_LINKS, getArguments(), this);
		}
		if (dID == mLinkCardLock.getId()) {
			mLinkPrivacy = PailUtilities.switchPrivacy(
					getActivity(), mLinkPrivacy, mLinkCardLock, PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.LinkAttachmentEntry(), mLinkItem.get_ID()));
		}

		if (dID == mNoteCardDelete.getId()) {
			getActivity().getContentResolver()
					.delete(PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.NoteAttachmentEntry(), mNoteItem.get_ID()), null, null);
			getLoaderManager().restartLoader(LOAD_NOTES, getArguments(), this);
		}
		if (dID == mNoteCardLock.getId()) {
			mNotePrivacy = PailUtilities.switchPrivacy(
					getActivity(), mNotePrivacy, mNoteCardLock, PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.NoteAttachmentEntry(), mNoteItem.get_ID()));
		}
	}
}