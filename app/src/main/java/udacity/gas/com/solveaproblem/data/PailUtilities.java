package udacity.gas.com.solveaproblem.data;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by Fagbohungbe on 02/03/2015.
 */
public class PailUtilities {


	public static void validateCurentRecord(String tag_name, Cursor valueCursor, ContentValues expectedValues) {
		Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
		for (Map.Entry<String, Object> entry : valueSet) {
			String columnName = entry.getKey();
			int idx = valueCursor.getColumnIndex(columnName);
			if (idx == -1) {
				Log.e(tag_name, "Column '"+columnName+"' found.");
			} else {
				Log.e(tag_name, "Column '"+columnName+"' not found.");
			}
			String expectedValue = entry.getValue().toString();
			if (valueCursor.getString(idx).equals(expectedValue)) {
				Log.e(tag_name, "Value '" + entry.getValue().toString() + "' did matched the expected value '" + expectedValue + "'");
			} else {
				Log.e(tag_name, "Value '" + entry.getValue().toString() + "' did not match the expected value '" + expectedValue + "'");
			}
		}
	}

	public static boolean hideKeyBoardFromScreen(Activity activity, View view) {
		InputMethodManager IMM = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		return IMM.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
	}

	public static long normalizeDate(long date) {
		Time time = new Time();
		time.setToNow();
		int julianDay = Time.getJulianDay(date, time.gmtoff);
		return time.setJulianDay(julianDay);
	}

	public static long currentDate() {
		return new Date().getTime();
	}
}