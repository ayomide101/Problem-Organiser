package udacity.gas.com.solveaproblem.cards;

import android.content.Context;

import it.gmariotti.cardslib.library.cards.material.MaterialLargeImageCard;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public class MaterialCardWithImage extends MaterialLargeImageCard implements CardInterface {

	private String mainTitle;
	private String mainDescription;

	public MaterialCardWithImage(Context context) {
		super(context);
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
