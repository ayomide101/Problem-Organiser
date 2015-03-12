package udacity.gas.com.solveaproblem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.fragments.attach.LinksFragment;
import udacity.gas.com.solveaproblem.fragments.attach.NotesFragment;
import udacity.gas.com.solveaproblem.utilities.SetupUI;

/**
 * Created by Fagbohungbe on 11/03/2015.
 */
public class AttachmentActivity extends ActionBarActivity {

	public static String ATTACHMENT_TYPE_KEY = "key";
	private int mAttachmentType;
	private FragmentTransaction transaction;
	private String FRAGMENT_MANAGER = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attachment);
		Intent intent = getIntent();
		mAttachmentType = intent.getIntExtra(ATTACHMENT_TYPE_KEY, -1);
		SetupUI setupUI = new SetupUI(this);
		setupUI.setupToolbar();
		transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.main_attachment_container, getInstance(), FRAGMENT_MANAGER);
		transaction.commit();
	}

	private Fragment getInstance() {
		//setting an invalid prod_id so that the fragments can load all their contents
		//instead of the loading the content for the prodid
		int invalid_prod_id = PailContract.ProblemEntry.PROD_ID_NOT_SET;
		if (mAttachmentType == LinksFragment.ID) {
			return new LinksFragment();
		} else if (mAttachmentType == NotesFragment.ID) {
			return new NotesFragment();
		} else {
			Toast.makeText(this, "Could not load contents", Toast.LENGTH_LONG).show();
			finish();
			return null;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.home) {
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}