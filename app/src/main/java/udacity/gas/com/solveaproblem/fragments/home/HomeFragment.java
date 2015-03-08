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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import udacity.gas.com.solveaproblem.AddProblem;
import udacity.gas.com.solveaproblem.DetailProblem;
import udacity.gas.com.solveaproblem.EditProblem;
import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.adapters.CursorRecyclerViewAdapter;
import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.data.PailUtilities;
import udacity.gas.com.solveaproblem.data.ProblemItem;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	public static int ID = 0;
	public static int PROBLEM_LOADER_ID = 0;
	public final static String EXTRA_ID = "udacity.gas.com.solveaproblem.PROBLEMID";
	private ProblemsAdapter problemsAdapter;
	private RecyclerView recyclerView;

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
		getLoaderManager().initLoader(PROBLEM_LOADER_ID, savedInstanceState, this);

		problemsAdapter = new ProblemsAdapter(getActivity(), null);
		recyclerView = (RecyclerView) getActivity().findViewById(R.id.problemsList);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		recyclerView.setAdapter(problemsAdapter);

		FloatingActionButton btAddProblem = (FloatingActionButton) getActivity().findViewById(R.id.btAddProblem);
		btAddProblem.setOnClickListener(this);
		btAddProblem.attachToRecyclerView(recyclerView);
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
		//Data found
		if (data.moveToFirst()) {
			problemsAdapter.swapCursor(data);
		} else {
			problemsAdapter.swapCursor(null);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		problemsAdapter.swapCursor(null);
	}

	public class ProblemsAdapter extends CursorRecyclerViewAdapter<ProblemsAdapter.ProblemViewHolder> {

		public ProblemsAdapter(Context context, Cursor cursor) {
			super(context, cursor);
		}

		@Override
		public ProblemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			try {
				CardView view = (CardView) getActivity().getLayoutInflater().inflate(R.layout.problem_card_normal, parent, false);
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
			viewHolder._PROBLEM_ID = problemItem.getPROB_ID();
			viewHolder._ID = problemItem.get_ID();
			viewHolder.privacy = problemItem.getPrivacy();
			viewHolder.date = problemItem.getDate();
			viewHolder.date_modified = problemItem.getDate_modified();
			viewHolder.status = problemItem.getStatus();
			viewHolder.carddate.setText(Long.toString(viewHolder.date));
			if (problemItem.getDescription().length() <= 0) {
				viewHolder.description.setVisibility(View.GONE);
			} else {
				viewHolder.description.setVisibility(View.VISIBLE);
				viewHolder.description.setText(problemItem.getDescription());
			}
			if (problemItem.getPrivacy() == PailContract.VAL_PRIVACY_PRIVATE) {
				viewHolder.problemLock.setImageDrawable(getResources().getDrawable(R.drawable.device_access_secure));
			} else {
				viewHolder.problemLock.setImageDrawable(getResources().getDrawable(R.drawable.device_access_not_secure));
			}
		}

		class ProblemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

			final ImageButton problemEdit;
			final ImageButton problemLock;
			final TextView carddate;
			TextView title;
			TextView description;
			ImageButton problemShare;
			ImageButton problemDelete;
			long _PROBLEM_ID;
			long _ID;
			int privacy;
			int status;
			public long date;
			public long date_modified;

			public ProblemViewHolder(View itemView) {
				super(itemView);
				title = (TextView) itemView.findViewById(R.id.problemTitle);
				description = (TextView) itemView.findViewById(R.id.problemDescription);
				problemShare = (ImageButton) itemView.findViewById(R.id.sharecard);
				problemDelete = (ImageButton) itemView.findViewById(R.id.deletecard);
				problemEdit = (ImageButton) itemView.findViewById(R.id.editcard);
				problemLock = (ImageButton) itemView.findViewById(R.id.lockcard);
				carddate = (TextView) itemView.findViewById(R.id.carddate);

				problemDelete.setOnClickListener(this);
				problemEdit.setOnClickListener(this);
				problemLock.setOnClickListener(this);
				problemShare.setOnClickListener(this);
				itemView.setOnClickListener(this);
				title.setOnClickListener(this);
			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case(R.id.editcard) : {
						//Load the edit activity
						Intent intent = new Intent(getActivity(), EditProblem.class);
						intent.putExtra(EXTRA_ID, _PROBLEM_ID);
						startActivity(intent);
						break;
					}
					case (R.id.lockcard) : {
						privacy = PailUtilities.switchPrivacy(getActivity(), privacy, problemLock, PailContract.ProblemEntry.buildProblemWithIdUri(_PROBLEM_ID));
						break;
					}
					case(R.id.sharecard) : {
						Toast.makeText(v.getContext(), "Share", Toast.LENGTH_SHORT).show();
						break;
					}
					case(R.id.deletecard) : {
						getActivity()
							.getContentResolver()
							.delete(PailContract.ProblemEntry.buildProblemWithIdUri(_PROBLEM_ID), null, null);
						break;
					}
					case(R.id.card_view) :
					case(R.id.problemTitle) : {
						/*Toast.makeText(v.getContext(), "Card View", Toast.LENGTH_SHORT).show();*/
						Intent intent = new Intent(getActivity(), DetailProblem.class);
						intent.putExtra(EXTRA_ID, _PROBLEM_ID);
						startActivity(intent);
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