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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

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
	private TextView textView;
	private ProblemsAdapter problemsAdapter;
	private RecyclerView recyclerView;


	public static HomeFragment getInstance(int position) {
		HomeFragment myFragment = new HomeFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		myFragment.setArguments(args);

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
		getLoaderManager().initLoader(PROBLEM_LOADER_ID, savedInstanceState, this);
		problemsAdapter = new ProblemsAdapter(getActivity(), null);

		FloatingActionButton btAddProblem = (FloatingActionButton) getActivity().findViewById(R.id.btAddProblem);
		btAddProblem.setOnClickListener(this);
		recyclerView = (RecyclerView) getActivity().findViewById(R.id.problemsList);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
		recyclerView.setAdapter(problemsAdapter);
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
				View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_home_list_view, parent, false);
				ProblemViewHolder vh = new ProblemViewHolder(view);
				return vh;
			} catch (NullPointerException e) {
				Log.e("HomeFragment", e.getMessage()+"");
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