package udacity.gas.com.solveaproblem.cards;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.gmariotti.cardslib.library.internal.Card;
import udacity.gas.com.solveaproblem.R;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public class NormalCard extends Card implements CardInterface {

	private String mainTitle;
	private String mainDescription;

	public NormalCard(Context context) {
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
			mSecondaryTitleTextView.setText(mainDescription);
	}

	@Override
	public void setMainTitle(String title) {
		mainTitle = title;
	}

	@Override
	public void setMainDescription(String description) {
		mainDescription = description;
	}

	@Override
	public String getMainTitle() {
		return mainTitle;
	}

	@Override
	public String getMainDescription() {
		return mainDescription;
	}
}