package udacity.gas.com.solveaproblem.fragments.home;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import udacity.gas.com.solveaproblem.R;

/**
 * Created by Fagbohungbe on 27/02/2015.
 */
public class AttachmentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static int ID = 1;
    private TextView textView;

    public static AttachmentFragment getInstance(int position) {
        AttachmentFragment myFragment = new AttachmentFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        myFragment.setArguments(args);

        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_browse, container, false);
        textView = (TextView) layout.findViewById(R.id.position);
        Bundle bundle = getArguments();
        if (bundle != null) {
            textView.setText("Browse " + bundle.getInt("position"));
        }

        return layout;
    }

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		/*String sortOrder = PailContract.ProblemEntry.COLUMN_DATE + " DESC";
		return new CursorLoader(getActivity(),
				PailContract.ProblemEntry.buildProblemsUri(),
				PailContract.ALL_COLUMNS,
				null,
				null,
				sortOrder
		);*/
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}