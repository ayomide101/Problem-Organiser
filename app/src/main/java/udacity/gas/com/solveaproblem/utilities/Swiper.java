package udacity.gas.com.solveaproblem.utilities;

import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

/**
 * Created by Fagbohungbe on 05/03/2015.
 */
public class Swiper extends SwipeableRecyclerViewTouchListener {
	/**
	 * Constructs a new swipe touch listener for the given {@link android.support.v7.widget.RecyclerView}
	 *
	 * @param recyclerView The recycler view whose items should be dismissable by swiping.
	 * @param listener     The listener for the swipe events.
	 */
	public Swiper(RecyclerView recyclerView, SwipeListener listener) {
		super(recyclerView, listener);
	}

	@Override
	public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
		return handleTouchEvent(motionEvent);
	}

	@Override
	public void onTouchEvent(RecyclerView rv, MotionEvent motionEvent) {
		handleTouchEvent(motionEvent);
	}
}
