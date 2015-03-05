package udacity.gas.com.solveaproblem.cards;

/**
 * Created by Fagbohungbe on 04/03/2015.
 */
public interface CardInterface {
	public int position = 0;

	public void setMainTitle(String title);
	public void setMainDescription(String description);

	public String getMainTitle();
	public String getMainDescription();
}
