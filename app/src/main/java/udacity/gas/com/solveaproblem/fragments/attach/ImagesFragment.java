package udacity.gas.com.solveaproblem.fragments.attach;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import udacity.gas.com.solveaproblem.R;

/**
 * Created by Fagbohungbe on 28/02/2015.
 */
public class ImagesFragment extends Fragment{

	public static int ID = 3;
	private TextView textView;

	public static ImagesFragment getInstance(int position) {
		ImagesFragment myFragment = new ImagesFragment();
		Bundle args = new Bundle();
		args.putInt("position", position);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_image, container, false);
//		textView = (TextView) layout.findViewById(R.id.position);
//		Bundle bundle = getArguments();
//		if (bundle != null) {
//			textView.setText("Browse " + bundle.getInt("position"));
//		}

		return layout;
	}
}