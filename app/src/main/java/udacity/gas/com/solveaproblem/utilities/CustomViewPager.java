package udacity.gas.com.solveaproblem.utilities;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Fagbohungbe on 05/03/2015.
 */
public class CustomViewPager extends ViewPager{

	/*Controls the scrolling ability of the ViewPager*/
	private boolean isPagingEnabled = true;

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isPagingEnabled) {
			return super.onInterceptTouchEvent(ev);
		} else {
			return true;
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isPagingEnabled) {
			return super.onInterceptTouchEvent(ev);
		} else {
			return true;
		}
	}

	public boolean isPagingEnabled() {
		return isPagingEnabled;
	}

	public void setPagingEnabled(boolean isPagingEnabled) {
		this.isPagingEnabled = isPagingEnabled;
	}
}
