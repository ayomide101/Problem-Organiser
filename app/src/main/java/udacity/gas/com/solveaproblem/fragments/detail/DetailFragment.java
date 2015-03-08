package udacity.gas.com.solveaproblem.fragments.detail;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.data.PailContract;

/**
 * Created by Fagbohungbe on 07/03/2015.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	public static int ID = 8;
	private static long PROB_ID;
	private TextView textView;
	private TextView swProblemTitle;
	private TextView swProblemDate;
	private TextView swProblemPrivacyText;
	private TextView swProblemAttachmentCount;
	private TextView swProblemDescription;
	private ActionMenuItemView swAction;
	private int mPrivacy;

	public static DetailFragment getInstance(long problemId) {
		DetailFragment myFragment = new DetailFragment();
		Bundle args = new Bundle();
		args.putLong(PailContract.ProblemEntry.BUNDLE_KEY, problemId);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_detail, container, false);
		Bundle bundle = getArguments();
		if (bundle != null) {
			PROB_ID = bundle.getLong(PailContract.ProblemEntry.BUNDLE_KEY);
		}
		return layout;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(1, savedInstanceState, this);

		swProblemTitle = (TextView) getActivity().findViewById(R.id.detail_problem_title);
		swProblemDate = (TextView) getActivity().findViewById(R.id.detail_problem_date);
		swProblemPrivacyText = (TextView) getActivity().findViewById(R.id.detail_problem_privacy);
		swProblemAttachmentCount = (TextView) getActivity().findViewById(R.id.detail_problem_attachment_counts);
		swProblemDescription = (TextView) getActivity().findViewById(R.id.detail_problem_description);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(getActivity(),
				PailContract.ProblemEntry
						.buildProblemWithIdUri(PROB_ID),
				PailContract.ProblemEntry.PROBLEM_COLUMNS,
				null,
				null,
				null
		);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		if (data.moveToFirst()) {
			//populate data
			populateView(data);
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}

	private void populateView(Cursor cursor) {
		swProblemTitle.setText(cursor.getString(PailContract.ProblemEntry.i_PROBLEM_TITLE));
		swProblemDate.setText(cursor.getString(PailContract.ProblemEntry.i_PROBLEM_DATE));
		swProblemDescription.setText(cursor.getString(PailContract.ProblemEntry.i_PROBLEM_DESCRIPTION));
		swProblemAttachmentCount.setText("Attachment : 50");
		mPrivacy = cursor.getInt(PailContract.ProblemEntry.i_PROBLEM_PRIVACY);
		if (mPrivacy == PailContract.VAL_PRIVACY_PRIVATE) {
			swProblemPrivacyText.setText("Private");
		} else {
			swProblemPrivacyText.setText("Public");
		}
	}
}