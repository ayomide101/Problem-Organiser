package udacity.gas.com.solveaproblem.fragments.attach;

import android.graphics.Bitmap;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.SecureRandom;

import udacity.gas.com.solveaproblem.R;
import udacity.gas.com.solveaproblem.data.PailContract;

/**
 * Created by Fagbohungbe on 02/03/2015.
 */
public class LinksFragment extends Fragment implements View.OnClickListener {

	public static int ID = 2;
	private static long PROB_ID;
	private RecyclerView lRecyclerView;
	private FrameLayout lMainNoteContent;
	private LinearLayout lTempView;
	private FloatingActionButton lBtAddNote;
	private MaterialDialog lMaterialDialog;
	private LinearLayout lLayout;
	private EditText swLinkUrl;
	private Switch swPrivacy;
	private boolean mUrlLoaded = false;
	private Button swLoadUrlButton;
	private ImageView swImageView;
	private TextView swLinkTitle;
	private TextView swLinkDescription;
	private int stPrivacy;
	private String stScreenShot;

	public static LinksFragment getInstance(long position) {
		LinksFragment myFragment = new LinksFragment();
		Bundle args = new Bundle();
		args.putLong(PailContract.ProblemEntry.BUNDLE_KEY, position);
		myFragment.setArguments(args);

		return myFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_link, container, false);
		Bundle bundle = getArguments();
		if (bundle != null) {
			PROB_ID = bundle.getLong(PailContract.ProblemEntry.BUNDLE_KEY);
		}
		return layout;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		//show the notes
		lRecyclerView = (RecyclerView) getActivity().findViewById(R.id.linklists);
		lMainNoteContent = (FrameLayout) getActivity().findViewById(R.id.mainNoteContent);
		lTempView = (LinearLayout) getActivity().findViewById(R.id.link_tempview);
		lBtAddNote = (FloatingActionButton) getActivity().findViewById(R.id.btAddNote);

		lTempView.setOnClickListener(this);
		lBtAddNote.setOnClickListener(this);
		lRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));

		lMaterialDialog = new MaterialDialog.Builder(getActivity())
				.title("ADD LINK")
				.autoDismiss(false)
				.customView(R.layout.link_add_form, true)
				.positiveText("Add")
				.positiveColor(R.color.primaryColor)
				.negativeText("Cancel")
				.callback(new MaterialDialog.ButtonCallback() {
					@Override
					public void onPositive(MaterialDialog dialog) {
						//Add note to the database
						if (!mUrlLoaded) {
							Toast.makeText(getActivity(), "Please click the LOAD URL button to load the url", Toast.LENGTH_LONG).show();
							return;
						}
						createLink(dialog);
					}

					@Override
					public void onNegative(MaterialDialog dialog) {
						dialog.dismiss();
					}
				})
				.build();
		lLayout = (LinearLayout) lMaterialDialog.getCustomView();
		swLinkUrl = (EditText) lLayout.findViewById(R.id.link_url);
		swPrivacy = (Switch) lLayout.findViewById(R.id.note_privacy);
		swLoadUrlButton = (Button) lLayout.findViewById(R.id.load_url);
		swImageView = (ImageView) lLayout.findViewById(R.id.link_screenshot);
		swLinkTitle = (TextView) lLayout.findViewById(R.id.linkTitle);
		swLinkDescription = (TextView) lLayout.findViewById(R.id.linkDescription);
		swLoadUrlButton.setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Load the url here
				//show a loading dialog box
				//Show another dialog box until the content finish shows up
				loadUrl();
			}
		});
		swPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					stPrivacy = PailContract.VAL_PRIVACY_PRIVATE;
				} else {
					stPrivacy = PailContract.VAL_PRIVACY_PUBLIC;
				}
			}
		});
	}

	public void loadUrl() {
		if (swLinkUrl.getText().length() <= 0) {
			Toast.makeText(getActivity(), "Url not valid", Toast.LENGTH_LONG).show();
			return;
		}

		Uri uri = Uri.parse(swLinkUrl.getText().toString());
		if (uri != null) {
			WebView webView = new WebView(getActivity());
			webView.getSettings().setJavaScriptEnabled(true);
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageStarted(WebView view, String url, Bitmap favicon) {
					super.onPageStarted(view, url, favicon);
					Toast.makeText(getActivity(), "Loading page", Toast.LENGTH_LONG).show();
				}

				@Override
				public void onPageFinished(WebView view, String url) {
					super.onPageFinished(view, url);
					swLinkTitle.setText(view.getTitle());
					swLinkDescription.setText(view.getContentDescription());

					Picture picture = view.capturePicture();
					Bitmap b = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(),
							Bitmap.Config.ARGB_8888);
					String randomFilenamePart = String.valueOf(new SecureRandom().nextInt(1000000));
					String filename = Environment.getExternalStorageDirectory().toString()
							+ "/Screenshot_"+randomFilenamePart+".jpg";
					File imageFile = new File(filename);

					//Stream the file out to external storage as a JPEG
					OutputStream fout = null;
					try {
						fout = new FileOutputStream(imageFile);
						b.compress(Bitmap.CompressFormat. JPEG, 100, fout);
						fout.close();
					} catch (Exception e) {
						Log.e("LinksFragment", e.getMessage()+"");
					}

					stScreenShot = filename;
					swImageView.setImageBitmap(b);
					Toast.makeText(getActivity(), "Page load complete", Toast.LENGTH_LONG).show();
				}
			});
		} else {
			Toast.makeText(getActivity(), "Invalid url specified", Toast.LENGTH_LONG).show();
		}
	}

	public void createLink(MaterialDialog d) {
		d.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case (R.id.link_tempview) :
			case (R.id.btAddNote) : {
				Log.e(LinksFragment.class.getSimpleName(), "Click responding");
				//Show the dialog here
				lMaterialDialog.show();
				break;
			}
		}
	}
}