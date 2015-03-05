package udacity.gas.com.solveaproblem.cards;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.cards.actions.BaseSupplementalAction;
import it.gmariotti.cardslib.library.cards.actions.TextSupplementalAction;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public class TextSupplementalActions {
	private ArrayList<BaseSupplementalAction> mActions;
	private Context mContext;
	private View mLayout;

	public TextSupplementalActions(Context context, View layout) {
		mActions = new ArrayList<BaseSupplementalAction>();
		mContext = context;
		mLayout = layout;
	}

	public boolean addAction(int rText, BaseSupplementalAction.OnActionClickListener listener) {
		TextSupplementalAction t1 = new TextSupplementalAction(mContext, rText);
		t1.setOnActionClickListener(listener);
		return mActions.add(t1);
	}

	public View getmLayout() {
		return mLayout;
	}

	public void setmLayout(View mLayout) {
		this.mLayout = mLayout;
	}

	public ArrayList<BaseSupplementalAction> getmActions() {
		return mActions;
	}

	public void setmActions(ArrayList<BaseSupplementalAction> mActions) {
		this.mActions = mActions;
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}
}