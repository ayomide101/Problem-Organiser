package udacity.gas.com.solveaproblem.fragments.attach;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.util.Patterns;
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
import udacity.gas.com.solveaproblem.data.LinkItem;
import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.data.PailUtilities;

/**
 * Created by Fagbohungbe on 02/03/2015.
 */
public class LinksFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	public static int ID = 2;
	private static int LOADER_ID = 1;
	private static long PROB_ID;
	private FrameLayout lMainNoteContent;
	private LinearLayout lTempView;
	private MaterialDialog lMaterialDialog;
	private EditText swLinkUrl;
	private EditText swLinkDescription;
	private int stPrivacy;
	private LinkAdapter linkAdapter;
	private LinksFragment mObject;
	private TextView add_links_text;

	public static LinksFragment getInstance(long position) {
		LinksFragment myFragment = new LinksFragment();
		Bundle args = new Bundle();
		args.putLong(PailContract.ProblemEntry.BUNDLE_KEY, position);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_link, container, false);
		Bundle bundle = getArguments();
		if (bundle != null) {
			PROB_ID = bundle.getLong(PailContract.ProblemEntry.BUNDLE_KEY);
		} else {
			PROB_ID = PailContract.ProblemEntry.PROD_ID_NOT_SET;
		}
		return layout;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(LOADER_ID, savedInstanceState, this);

		linkAdapter = new LinkAdapter(getActivity(), null);

		//show the notes
		RecyclerView lRecyclerView = (RecyclerView) getActivity().findViewById(R.id.linklists);
		lMainNoteContent = (FrameLayout) getActivity().findViewById(R.id.mainLinkContent);
		lTempView = (LinearLayout) getActivity().findViewById(R.id.link_tempview);
		FloatingActionButton lBtAddNote = (FloatingActionButton) getActivity().findViewById(R.id.btAddLink);
		add_links_text = (TextView) getActivity().findViewById(R.id.add_links_text);

		if (PROB_ID == PailContract.ProblemEntry.PROD_ID_NOT_SET) {
			lBtAddNote.setVisibility(View.GONE);
			add_links_text.setText(R.string.not_found);
		} else {
			lTempView.setOnClickListener(this);
			lBtAddNote.setOnClickListener(this);
			lBtAddNote.setVisibility(View.VISIBLE);
			add_links_text.setText(R.string.add_links);
		}

		lRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
		lRecyclerView.setAdapter(linkAdapter);

		lMaterialDialog = new MaterialDialog.Builder(getActivity()).title("ADD LINK").autoDismiss(false).customView(R.layout.link_add_form, true).positiveText("Add").positiveColor(R.color.primaryColor).negativeText("Cancel").callback(new MaterialDialog.ButtonCallback() {
			@Override
			public void onPositive(MaterialDialog dialog) {
				//check if url is valid
				String url = swLinkUrl.getText().toString();
				if (url.length() >= 1 && !Patterns.WEB_URL.matcher(url).matches()) {
					Toast.makeText(getActivity(), "Invalid url provided, correct : http://www.example.com or https://www.example.com", Toast.LENGTH_LONG).show();
					return;
				}
				createLink(dialog);
			}

			@Override
			public void onNegative(MaterialDialog dialog) {
				dialog.dismiss();
			}
		}).build();

		LinearLayout lLayout = (LinearLayout) lMaterialDialog.getCustomView();
		swLinkUrl = (EditText) lLayout.findViewById(R.id.link_url);
		swLinkDescription = (EditText) lLayout.findViewById(R.id.link_description);
		Switch swPrivacy = (Switch) lLayout.findViewById(R.id.note_privacy);
		swPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) stPrivacy = PailContract.VAL_PRIVACY_PRIVATE;
				else stPrivacy = PailContract.VAL_PRIVACY_PUBLIC;
			}
		}
		);
		mObject = this;
	}

	private ContentValues getData() {
		ContentValues contentValues = new ContentValues();
		contentValues.put(PailContract.LinkAttachmentEntry.COLUMN_PROB_KEY, PROB_ID);
		contentValues.put(PailContract.LinkAttachmentEntry.COLUMN_ATTACH_ID, PailContract.ProblemEntry.generateProblemId());
		contentValues.put(PailContract.LinkAttachmentEntry.COLUMN_PRIVACY, stPrivacy);
		contentValues.put(PailContract.LinkAttachmentEntry.COLUMN_RELEVANCE, 0);
		contentValues.put(PailContract.LinkAttachmentEntry.COLUMN_LINK_URL, swLinkUrl.getText().toString());
		contentValues.put(PailContract.LinkAttachmentEntry.COLUMN_LINK_DESCRIPTION, swLinkDescription.getText().toString());
		//Autogenerate contents
		contentValues.put(PailContract.LinkAttachmentEntry.COLUMN_LINK_SCREENSHOT, "");
		contentValues.put(PailContract.LinkAttachmentEntry.COLUMN_LINK_TITLE, "");

		return contentValues;
	}

	public void createLink(MaterialDialog d) {
		ContentValues cn = getData();
		getActivity().getContentResolver()
				.insert(
						PailContract.Attachment.buildAttachmentWithAttachmentTypeUri(new PailContract.LinkAttachmentEntry()),
						cn
				);
		swLinkUrl.setText("");
		swLinkDescription.setText("");
		getLoaderManager().restartLoader(LOADER_ID, getArguments(), this); //Call this the content can reload
		PailUtilities.hideKeyBoardFromScreen(getActivity(), swLinkUrl);
		PailUtilities.hideKeyBoardFromScreen(getActivity(), swLinkDescription);
		Toast.makeText(getActivity(), "Link created", Toast.LENGTH_LONG).show();
		d.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case (R.id.link_tempview):
			case (R.id.btAddLink): {
				//Show the dialog here
				lMaterialDialog.show();
				break;
			}
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String sortOrder = PailContract.LinkAttachmentEntry.COLUMN_DATE + " DESC";
		//check if the probid is valid, call the get all if otherwise
		Uri uri = (PROB_ID != PailContract.ProblemEntry.PROD_ID_NOT_SET) ? PailContract.ProblemEntry.buildProblemWithAttachmentTypeUri(PROB_ID, new PailContract.LinkAttachmentEntry()): PailContract.Attachment.buildAttachmentsUri();
		return new CursorLoader(getActivity(),
				uri,
				PailContract.LinkAttachmentEntry.LINK_COLUMNS,
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
			lTempView.setVisibility(View.GONE); //hide btAdd Problem
			lMainNoteContent.setVisibility(View.VISIBLE); //show mainNoteContent
			linkAdapter.swapCursor(data);
		} else {
			lTempView.setVisibility(View.VISIBLE); //hide btAdd Problem
			lMainNoteContent.setVisibility(View.GONE); //show mainNoteContent
			linkAdapter.swapCursor(null);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		linkAdapter.swapCursor(null);
	}

	public class LinkAdapter extends CursorRecyclerViewAdapter<LinkAdapter.LinkViewHolder> {

		public LinkAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public LinkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			CardView view = (CardView) getActivity().getLayoutInflater().inflate(R.layout.link_card_normal, parent, false);
			return new LinkViewHolder(view);
		}

		@Override
		public void onBindViewHolder(LinkViewHolder viewHolder, Cursor cursor) {
			LinkItem linkItem = LinkItem.fromCursor(cursor);
			viewHolder._PROBLEM_ID = linkItem.getPROB_ID();
			viewHolder._ID = linkItem.get_ID();
			viewHolder._ATTACH_ID = linkItem.getATTACH_ID();
			viewHolder._relevance = linkItem.getRelevance();
			viewHolder._privacy = linkItem.getPrivacy();
			viewHolder._date = linkItem.getDate();
			viewHolder._date_modified = linkItem.getDate_modified();
			viewHolder._title = linkItem.getTitle();
			viewHolder._description = linkItem.getUrl_description();
			viewHolder._url = linkItem.getUrl();
			viewHolder._screenshot = linkItem.getScreenshot();

			viewHolder.card_date.setText(viewHolder._date_modified);
			viewHolder.link_description.setText(viewHolder._description);
			viewHolder.link_url.setText(viewHolder._url);

			if (viewHolder.link_description.getText().length() <= 0) { //Hide
				viewHolder.link_description.setVisibility(View.GONE);
			}
			if (viewHolder.link_url.getText().length() <= 0) {
				viewHolder.link_url.setText("https://www.example.org");
			}

			if (linkItem.getPrivacy() == PailContract.VAL_PRIVACY_PRIVATE) {
				viewHolder.lockCard.setImageDrawable(getResources().getDrawable(R.drawable.device_access_secure));
			} else {
				viewHolder.lockCard.setImageDrawable(getResources().getDrawable(R.drawable.device_access_not_secure));
			}
		}

		class LinkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

			final ImageButton deleteCard;
			final TextView card_date;
			final ImageButton lockCard;
			private final TextView link_description;
			private final TextView link_url;
			long _ATTACH_ID;
			long _ID;
			long _PROBLEM_ID;
			int _privacy;
			long _relevance;
			String _date;
			String _date_modified;
			String _title;
			public String _description;
			public String _url;
			public String _screenshot;

			public LinkViewHolder(View itemView) {
				super(itemView);
				link_description = (TextView) itemView.findViewById(R.id.link_description);
				link_url = (TextView) itemView.findViewById(R.id.link_url);

				card_date = (TextView) itemView.findViewById(R.id.carddate);
				deleteCard = (ImageButton) itemView.findViewById(R.id.deletecard);
				lockCard = (ImageButton) itemView.findViewById(R.id.lockcard);

				deleteCard.setOnClickListener(this);
				lockCard.setOnClickListener(this);
				link_url.setOnClickListener(this);
			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case (R.id.deletecard): {
						getActivity().getContentResolver()
								.delete(PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.LinkAttachmentEntry(), _ID), null, null);
						getLoaderManager().restartLoader(LOADER_ID, getArguments(), mObject);
						break;
					}
					case (R.id.lockcard): {
						_privacy = PailUtilities.switchPrivacy(
								getActivity(), _privacy, lockCard, PailContract.Attachment.buildAttachmentWithAttachmentTypeWithIdUri(new PailContract.LinkAttachmentEntry(), _ID));
						break;
					}
					case (R.id.link_url) : {
						//Open browser
						String url = link_url.getText().toString();
						if (url.length() > 1 && Patterns.WEB_URL.matcher(url).matches()) {
							try {
								Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
								if (browserIntent.resolveActivity(getActivity().getPackageManager()) != null) {
									startActivity(browserIntent);
								} else {
									Toast.makeText(getActivity(), "Either the url you provided is invalid or you do not have any app for displaying websites", Toast.LENGTH_LONG).show();
								}
							} catch (ActivityNotFoundException e) {
								Toast.makeText(getActivity(), "Invalid url specified, cannot open browser", Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(getActivity(), "Invalid url specified, cannot open browser", Toast.LENGTH_LONG).show();
						}

						break;
					}
				}
			}
		}
	}
}