package udacity.gas.com.solveaproblem.adapters;

import android.database.Cursor;

public class MyListItem{

	private String name;

	public MyListItem setName(String name){
		this.name=name;
		return this;
	}

	public String getName(){
		return name;
	}

	public static MyListItem fromCursor(Cursor cursor) {
		//TODO return your MyListItem from cursor.
		return new MyListItem().setName("TExt");
	}
}