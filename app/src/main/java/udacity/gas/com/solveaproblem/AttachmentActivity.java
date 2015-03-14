package udacity.gas.com.solveaproblem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import udacity.gas.com.solveaproblem.data.PailContract;
import udacity.gas.com.solveaproblem.fragments.attach.LinksFragment;
import udacity.gas.com.solveaproblem.fragments.attach.NotesFragment;
import udacity.gas.com.solveaproblem.fragments.attach.RelevantAttachmentFragment;
import udacity.gas.com.solveaproblem.utilities.SetupUI;

/**
 * Created by Fagbohungbe on 11/03/2015.
 */
public class AttachmentActivity extends ActionBarActivity {

	public static String ATTACHMENT_TYPE_KEY = "key";
	private int mAttachmentType;
	private FragmentTransaction transaction;
	private String FRAGMENT_MANAGER = "0";
	private TextView mFragmentTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attachment);
		Intent intent = getIntent();
		mAttachmentType = intent.getIntExtra(ATTACHMENT_TYPE_KEY, -1);
		SetupUI setupUI = new SetupUI(this);
		setupUI.setupToolbar();

		mFragmentTitle = (TextView) findViewById(R.id.fragment_title);

		if (savedInstanceState == null) {
			transaction = getSupportFragmentManager().beginTransaction();
			transaction.replace(R.id.main_attachment_container, getInstance(), FRAGMENT_MANAGER);
			transaction.commit();
		}
	}

	private Fragment getInstance() {
		//setting an invalid prod_id so that the fragments can load all their contents
		//instead of the loading the content for the prodid
		if (mAttachmentType == LinksFragment.ID) {
			mFragmentTitle.setText(PailContract.LinkAttachmentEntry.TABLE_NAME);
			return new LinksFragment();
		} else if (mAttachmentType == NotesFragment.ID) {
			mFragmentTitle.setText(PailContract.NoteAttachmentEntry.TABLE_NAME);
			return new NotesFragment();
		} else if (mAttachmentType == RelevantAttachmentFragment.ID) {
			mFragmentTitle.setText("MOST RELEVANT");
			return new RelevantAttachmentFragment();
		} else {
			Toast.makeText(this, "Could not load contents", Toast.LENGTH_LONG).show();
//			finish();
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
}