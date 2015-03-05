package udacity.gas.com.solveaproblem.cards;

import android.content.Context;

import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;
import udacity.gas.com.solveaproblem.R;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public class MaterialCard extends MaterialLargeImageCard implements CardInterface {
	private String mainTitle;
	private String mainDescription;

	public MaterialCard(Context context) {
		super(context, R.layout.card_bottom_view);
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
