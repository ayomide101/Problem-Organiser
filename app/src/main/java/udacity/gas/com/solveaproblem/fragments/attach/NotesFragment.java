package udacity.gas.com.solveaproblem.fragments.attach;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import udacity.gas.com.solveaproblem.R;

/**
 * Created by Fagbohungbe on 02/03/2015.
 */
public class NotesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	public static int ID = 1;

	public static ImagesFragment getInstance(long problemId) {
		ImagesFragment myFragment = new ImagesFragment();
		Bundle args = new Bundle();
		args.putLong("position", problemId);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_note, container, false);
		return layout;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return null;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {

	}
}