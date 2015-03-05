package udacity.gas.com.solveaproblem.cards;

import android.content.Context;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.cards.actions.BaseSupplementalAction;
import it.gmariotti.cardslib.library.cards.actions.IconSupplementalAction;

/**
 * Created by Fagbohungbe on 05/03/2015.
 */
public class IconSupplementalActions {
	public ArrayList<BaseSupplementalAction> mActions = new ArrayList<BaseSupplementalAction>();
	private Context mContext;
	private int mLayout;

	public IconSupplementalActions(Context context, int layout) {
		mContext = context;
		mLayout = layout;
	}

	public boolean addAction(int rText, BaseSupplementalAction.OnActionClickListener listener) {
		IconSupplementalAction t1 = new IconSupplementalAction(mContext, rText);
		t1.setOnActionClickListener(listener);
		return mActions.add(t1);
	}

	public int getmLayout() {
		return mLayout;
	}

	public void setmLayout(int mLayout) {
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