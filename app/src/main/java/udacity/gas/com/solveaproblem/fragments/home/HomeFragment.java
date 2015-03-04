package udacity.gas.com.solveaproblem.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardCursorAdapter;
import it.gmariotti.cardslib.library.internal.CardCursorMultiChoiceAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.base.CardViewWrapper;
import udacity.gas.com.solveaproblem.AddProblem;
import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.adapters.CursorRecyclerViewAdapter;
import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.data.ProblemItem;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	public static int ID = 0;
	public static int PROBLEM_LOADER_ID = 0;
	public static int PROBLEM_LOADER_CARD_ID = 1;
	public static int PROBLEM_LOADER_CARD_ACTION_ID = 1;
	private TextView textView;
	private ProblemsAdapter problemsAdapter;
	private RecyclerView recyclerView;
	private MyCursorCardAdapter mAdapter;
	private MyCardCursorMultiChoiceAdapter mCAdapter;
	private CardListView mListView;
	ActionMode mActionMode;
	private CardListView listView;


	public static HomeFragment getInstance(int position) {
		HomeFragment myFragment = new HomeFragment();
		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_home, container, false);
		return layout;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		getLoaderManager().initLoader(PROBLEM_LOADER_ID, savedInstanceState, this);
//		getLoaderManager().initLoader(PROBLEM_LOADER_CARD_ID, savedInstanceState, this);
		getLoaderManager().initLoader(PROBLEM_LOADER_CARD_ACTION_ID, savedInstanceState, this);

//		problemsAdapter = new ProblemsAdapter(getActivity(), null);
//		recyclerView = (RecyclerView) getActivity().findViewById(R.id.problemsList);
//		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//		recyclerView.setAdapter(problemsAdapter);

//		mAdapter = new MyCursorCardAdapter(getActivity());
//		mListView = (CardListView) getActivity().findViewById(R.id.myList);
//		if (mListView != null) {
//			mListView.setAdapter(mAdapter);
//		}

		//Set the arrayAdapter
		mCAdapter = new MyCardCursorMultiChoiceAdapter(getActivity());

		//ListView
		listView = (CardListView) getActivity().findViewById(R.id.myList);
		if (listView != null) {
			listView.setAdapter(mCAdapter);
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		}


		FloatingActionButton btAddProblem = (FloatingActionButton) getActivity().findViewById(R.id.btAddProblem);
		btAddProblem.setOnClickListener(this);
		btAddProblem.attachToListView(listView);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btAddProblem) {
			Intent intent = new Intent(getActivity(), AddProblem.class);
			startActivity(intent);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String sortOrder = PailContract.ProblemEntry.COLUMN_DATE + " DESC";
		return new CursorLoader(getActivity(),
				PailContract.ProblemEntry.buildProblemsUri(),
				PailContract.ProblemEntry.PROBLEM_COLUMNS,
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
		if (loader.getId() == PROBLEM_LOADER_ID) {
			//Data found
			if (data.moveToFirst()) {
				problemsAdapter.swapCursor(data);
			} else {
				problemsAdapter.swapCursor(null);
			}
		} else if (loader.getId() == PROBLEM_LOADER_CARD_ACTION_ID) {
			if (data.moveToFirst()) {
				mCAdapter.swapCursor(data);
			} else {
				mCAdapter.swapCursor(null);
			}
		} else {
			if (data.moveToFirst()) {
				mAdapter.swapCursor(data);
			} else {
				mAdapter.swapCursor(null);
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		problemsAdapter.swapCursor(null);
	}

	public class MyCardCursorMultiChoiceAdapter extends CardCursorMultiChoiceAdapter {

		public MyCardCursorMultiChoiceAdapter(Context context) {
			super(context);
		}

		@Override
		protected Card getCardFromCursor(Cursor cursor) {
			MyCursorCard card = new MyCursorCard(super.getContext());
			setCardFromCursor(card,cursor);

			//Create a CardHeader
			CardHeader header = new CardHeader(getActivity());

			//Set the header title
			header.setTitle(card.mainHeader);
			header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
				@Override
				public void onMenuItemClick(BaseCard card, MenuItem item) {
					Toast.makeText(getContext(), "Click on card=" + card.getId() + " item=" + item.getTitle(), Toast.LENGTH_SHORT).show();
				}
			});

			//Add Header to card
			card.addCardHeader(header);

			//Add the thumbnail
//			CardThumbnail thumb = new CardThumbnail(getActivity());
//			thumb.setDrawableResource(card.resourceIdThumb);
//			card.addCardThumbnail(thumb);

			//It is very important.
			//You have to implement this onLongClickListener in your cards to enable the multiChoice
			card.setOnLongClickListener(new Card.OnLongCardClickListener() {
				@Override
				public boolean onLongClick(Card card, View view) {
					return startActionMode(getActivity());
				}
			});

			//Simple clickListener
			card.setOnClickListener(new Card.OnCardClickListener() {
				@Override
				public void onClick(Card card, View view) {
					Toast.makeText(getContext(), "Card id=" + card.getId() + " Title=" + card.getCardHeader().getTitle(), Toast.LENGTH_SHORT).show();
				}
			});

			return card;
		}

		@Override
		public int getPosition(Card card) {
			return ((MyCursorCard)card).position;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			//It is very important to call the super method
			super.onCreateActionMode(mode, menu);

			mActionMode=mode; // to manage mode in your Fragment/Activity

			//If you would like to inflate your menu
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.carddemo_multichoice, menu);
			return true;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false;
		}

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			if (item.getItemId() == R.id.menu_share) {
				Toast.makeText(getContext(), "Share;" + formatCheckedCard(), Toast.LENGTH_SHORT).show();
				return true;
			}
			if (item.getItemId() == R.id.menu_discard) {
				discardSelectedItems(mode);
				return true;
			}
			return false;
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked, CardViewWrapper cardView, Card card) {
			Toast.makeText(getContext(), "Click;" + position + " - " + checked, Toast.LENGTH_SHORT).show();
		}

		private void discardSelectedItems(ActionMode mode) {
			Toast.makeText(getContext(), "Not implemented in this demo", Toast.LENGTH_SHORT).show();
		}

		private String formatCheckedCard() {
			SparseBooleanArray checked = mCardListView.getCheckedItemPositions();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < checked.size(); i++) {
				if (checked.valueAt(i) == true) {
					sb.append("\nPosition=" + checked.keyAt(i));
				}
			}
			return sb.toString();
		}
	}

	public class MyCursorCardAdapter extends CardCursorAdapter {

		public MyCursorCardAdapter(Context context) {
			super(context);
		}

		@Override
		protected Card getCardFromCursor(Cursor cursor) {
			MyCursorCard card = new MyCursorCard(super.getContext());
			setCardFromCursor(card, cursor);

			//Create a CardHeader
			CardHeader header = new CardHeader(super.getContext());

			//Set the header title
			header.setTitle(card.mainHeader);

			//Add Header to card
			card.addCardHeader(header);

			return card;
		}
	}

	private void setCardFromCursor(MyCursorCard card, Cursor cursor) {
		card.mainTitle = cursor.getString(PailContract.ProblemEntry.i_PROBLEM_TITLE);
		card.secondaryTitle = cursor.getString(PailContract.ProblemEntry.i_PROBLEM_DESCRIPTION);
//			card.mainHeader = cursor.getString(CardCursorContract.CardCursor.IndexColumns.HEADER_COLUMN);
		card.setId("" + cursor.getInt(PailContract.ProblemEntry.i_PROBLEM_ID)); //_id
	}

	public class MyCursorCard extends Card {

		String mainTitle;
		String secondaryTitle;
		String mainHeader;
		int resourceIdThumb;
		int position;

		public MyCursorCard(Context context) {
			super(context, R.layout.carddemo_cursor_native_inner_content);
		}

		@Override
		public void setupInnerViewElements(ViewGroup parent, View view) {
			//Retrieve elements
			TextView mTitleTextView = (TextView) parent.findViewById(R.id.carddemo_cursor_main_inner_title);
			TextView mSecondaryTitleTextView = (TextView) parent.findViewById(R.id.carddemo_cursor_main_inner_subtitle);

			if (mTitleTextView != null)
				mTitleTextView.setText(mainTitle);

			if (mSecondaryTitleTextView != null)
				mSecondaryTitleTextView.setText(secondaryTitle);

		}
	}

	public class ProblemsAdapter extends CursorRecyclerViewAdapter<ProblemsAdapter.ProblemViewHolder> {

		public ProblemsAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public ProblemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			try {
				View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_home_list_view, parent, false);
				ProblemViewHolder vh = new ProblemViewHolder(view);
				return vh;
			} catch (NullPointerException e) {
				Log.e("HomeFragment", e.getMessage() + "");
				return null;
			}
		}

		@Override
		public void onBindViewHolder(ProblemViewHolder viewHolder, Cursor cursor) {
			ProblemItem problemItem = ProblemItem.fromCursor(cursor);
			viewHolder.title.setText(problemItem.getTitle());
			viewHolder.description.setText(problemItem.getDescription());
		}

		class ProblemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
			TextView title;
			TextView description;

			public ProblemViewHolder(View itemView) {
				super(itemView);
				title = (TextView) itemView.findViewById(R.id.problemTitle);
				description = (TextView) itemView.findViewById(R.id.problemDescription);
				itemView.setOnClickListener(this);
			}

			@Override
			public void onClick(View v) {
				//Use the getPosition(); to determine the position and launch the
				//respective activity
				Toast.makeText(v.getContext(), "cLICKED", Toast.LENGTH_SHORT).show();
			}
		}
	}
}