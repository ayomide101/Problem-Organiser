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
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardCursorMultiChoiceAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;
import it.gmariotti.cardslib.library.view.base.CardViewWrapper;
import udacity.gas.com.solveaproblem.AddProblem;
import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.cards.MaterialCard;
import udacity.gas.com.solveaproblem.data.PailContract;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	public static int ID = 0;
	public static int PROBLEM_LOADER_CARD_ACTION_ID = 1;
	private MyCardCursorMultiChoiceAdapter mCAdapter;
	ActionMode mActionMode;
	private CardListView mListView;


	public static HomeFragment getInstance(int position) {
		return new HomeFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(PROBLEM_LOADER_CARD_ACTION_ID, savedInstanceState, this);

		//Set the arrayAdapter
		mCAdapter = new MyCardCursorMultiChoiceAdapter(getActivity());

		//ListView
		mListView = (CardListView) getActivity().findViewById(R.id.myList);
		if (mListView != null) {
			mListView.setAdapter(mCAdapter);
			mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		}

		FloatingActionButton btAddProblem = (FloatingActionButton) getActivity().findViewById(R.id.btAddProblem);
		btAddProblem.setOnClickListener(this);
		btAddProblem.attachToListView(mListView);
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
		String sortOrder = PailContract.ProblemEntry.COLUMN_DATE + " ASC";
		return new CursorLoader(
				getActivity(),
				PailContract.ProblemEntry.buildProblemsUri(),
				PailContract.ProblemEntry.PROBLEM_COLUMNS, null, null, sortOrder);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (getActivity() == null) {
			return;
		}
		if (data.moveToFirst()) {
			mCAdapter.swapCursor(data);
		} else {
			mCAdapter.swapCursor(null);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mCAdapter.swapCursor(null);
	}

	public class MyCardCursorMultiChoiceAdapter extends CardCursorMultiChoiceAdapter {

		public MyCardCursorMultiChoiceAdapter(Context context) {
			super(context);
		}

		@Override
		protected Card getCardFromCursor(Cursor cursor) {
			MaterialCard card = new MaterialCard(super.getContext());
			setBasicDataFromCursor(card, cursor);
			card.setShadow(true);
			card.setCardElevation(360);
			card.setCheckable(true);
			View v = (View) card.getCardView();
			TextView tV = (TextView) v.findViewById(R.id.carddemo_cursor_main_inner_subtitle);
			tV.setText(card.getMainDescription());
			ImageButton bI = (ImageButton) v.findViewById(R.id.action_share);
			bI.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(getContext(), "This things works", Toast.LENGTH_LONG).show();
				}
			});

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
			card.build();
			return card;
		}

		@Override
		public int getPosition(Card card) {
			return ((MaterialCard)card).position;
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

	private void setBasicDataFromCursor(MaterialCard card, Cursor cursor) {
		card.setSubTitle(cursor.getString(PailContract.ProblemEntry.i_PROBLEM_DESCRIPTION));
		//Create a CardHeader
		CardHeader header = new CardHeader(getActivity());

		//Set the header title
		header.setTitle(card.getTitle());
		header.setPopupMenu(R.menu.popupmain, new CardHeader.OnClickCardHeaderPopupMenuListener() {
			@Override
			public void onMenuItemClick(BaseCard card, MenuItem item) {
				Toast.makeText(getActivity(), "Click on card=" + card.getId() + " item=" + item.getTitle(), Toast.LENGTH_SHORT).show();
			}
		});

		//Add Header to card
		card.addCardHeader(header);
		card.setId("" + cursor.getInt(PailContract.ProblemEntry.i_PROBLEM_ID)); //_id
	}
}