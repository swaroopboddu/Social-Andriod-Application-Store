package edu.asu.mobicloud;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import static edu.asu.mobicloud.util.Constants.API_URL;

import com.squareup.okhttp.OkHttpClient;

import edu.asu.mobicloud.adapters.ListAdapter;
import edu.asu.mobicloud.fragments.CommentDialogFragment;
import edu.asu.mobicloud.model.ListData;
import edu.asu.mobicloud.rest.model.ApplicationCapsule;
import edu.asu.mobicloud.util.PreferencesUtil;

public class DetailsActivity extends ListActivity {
	private RatingBar editRating;
	private ApplicationCapsule applicationCap;
	Button b;
	boolean isInstallClicked = false;
	boolean isUnistallClicked = false;
	private String packageName;
	private String path;
	private final String prefsName = "edu.asu.mobicloud.applications";
	// TODO: to be moved to more generic place
	// private final String url =
	// "http://androidgeekvm.vlab.asu.edu/webapp/applications/download_app/";
	private final String url = API_URL + "/applications/download_app/";
	public static final String TOKEN = "edu.asu.mobicloud.authenticator.token";

	private static final String TOKENPREF = "edu.asu.mobicloud.LoginActivity";
	private PreferencesUtil tokenUtil;
	private PreferencesUtil prefsUtil;
	private ProgressBar bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		tokenUtil = new PreferencesUtil(getApplicationContext(), TOKENPREF);
		prefsUtil = new PreferencesUtil(getApplicationContext(), prefsName);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			applicationCap = extras.getParcelable("application");
			System.out.println("Application ID:"
					+ applicationCap.getApplication().getTitle());
		}
		bar = (ProgressBar) findViewById(R.id.downloadProgress);
		TextView appName = (TextView) findViewById(R.id.tvAppName);
		appName.setText(applicationCap.getApplication().getTitle());
		TextView devName = (TextView) findViewById(R.id.tvDevName);
		devName.setText(applicationCap.getUser().getFirstName()
				+ applicationCap.getUser().getLastName());
		Resources res = getResources();
		// TODO swaroop when downloads count added to server
		String downloads = res.getQuantityString(R.plurals.downloads, 0, 0);

		TextView downloadCount = (TextView) findViewById(R.id.tvDownloadCt);
		downloadCount.setText(downloads);

		TextView description = (TextView) findViewById(R.id.description);
		description.setText(applicationCap.getApplication().getDescription());

		RatingBar r = (RatingBar) findViewById(R.id.pop_ratingbar);
		r.setRating(Float.parseFloat(applicationCap.getApplication()
				.getRating()));

		ListAdapter adapter = new ListAdapter(getApplicationContext(),
				new ArrayList<ListData>());
		b = (Button) findViewById(R.id.btnDownload);

		b.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (b.getText().equals("UnInstall")) {

					promptUninstall();
					isUnistallClicked = true;

				} else if (b.getText().equals("Install")) {
					promptInstall();
					isInstallClicked = true;

				} else if (b.getText().equals("Download")) {
					new DownloadFileFromURL()
							.execute(url
									+ applicationCap.getApplication().getId()
									+ ".json");

				}

			}
		});
		setListAdapter(adapter);
		editRating = (RatingBar) findViewById(R.id.ratingInput);
		editRating.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					CommentDialogFragment commentFragement = new CommentDialogFragment();
					float touchPositionX = event.getX();
					float width = editRating.getWidth();
					float starsf = (touchPositionX / width) * 5.0f;
					int stars = (int) starsf + 1;
					editRating.setRating(stars);
					commentFragement.setRating(stars);
					commentFragement.show(getFragmentManager(),
							"commentFragment");
					v.setPressed(false);
				}
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					v.setPressed(true);
				}

				if (event.getAction() == MotionEvent.ACTION_CANCEL) {
					v.setPressed(false);
				}
				return true;
			}
		});
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		boolean installed = false;
		installed = isPackageInstalled();
		if (installed) {
			b = (Button) findViewById(R.id.btnDownload);
			b.setText("UnInstall");

		} else {
			b.setText("Download");
		}
		if (isUnistallClicked) {
			Toast.makeText(this, "App uninstalled", Toast.LENGTH_SHORT).show();
		}
		if (isInstallClicked) {
			prefsUtil.update(applicationCap.getApplication().getId(),
					packageName);
			Toast.makeText(this, "App installed", Toast.LENGTH_SHORT).show();
			b.setText("UnInstall");
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}

	private void promptInstall() {
		if (path != null) {
			/*
			 * packageName = getApplicationContext().getPackageManager()
			 * .getInstallerPackageName(path);
			 */
			PackageInfo inf = getApplicationContext().getPackageManager()
					.getPackageArchiveInfo(path, 0);
			packageName = inf.applicationInfo.packageName;
			Intent promptInstall = new Intent(Intent.ACTION_VIEW)
					.setDataAndType(Uri.fromFile(new File(path)),
							"application/vnd.android.package-archive");
			startActivity(promptInstall);

		}
	}

	private boolean isPackageInstalled() {
		PackageManager pm = getApplicationContext().getPackageManager();
		String applicationPackage = prefsUtil.getPreference(applicationCap
				.getApplication().getId());
		try {
			pm.getPackageInfo(applicationPackage, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	private void promptUninstall() {
		String applicationPackage = prefsUtil.getPreference(applicationCap
				.getApplication().getId());
		Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts(
				"package", applicationPackage, null));
		startActivity(intent);
	}

	/**
	 * Background Async Task to download file
	 * */
	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			String fileName = null;
			try {
				OkHttpClient client = new OkHttpClient();
				URL url = new URL(f_url[0]);
				HttpURLConnection connection = client.open(url);
				connection.setRequestProperty("token",
						tokenUtil.getPreference(TOKEN));
				Log.v("Token", tokenUtil.getPreference(TOKEN));
				connection.setRequestMethod("GET");
				connection.setDoOutput(true);

				connection.connect();
				fileName = connection.getHeaderField("Content-Disposition");
				fileName = fileName.split("=")[1].split("\"")[1];
				OutputStream output = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ File.separator
						+ "Download"
						+ File.separator
						+ fileName + ".apk");
				Log.v("Path", "PATH: "
						+ Environment.getExternalStorageDirectory().toString()
						+ File.separator + "Download" + File.separator
						+ fileName + ".apk");
				int lenghtOfFile = connection.getContentLength();

				InputStream input = connection.getInputStream();

				byte data[] = new byte[1024];
				long total = 0;
				Log.d("DetailsTag", "" + lenghtOfFile);
				while ((count = input.read(data)) != -1) {
					total += count;
					Log.d("DetailsTag", "" + total);
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();

			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}
			return fileName;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			bar.setProgress(Integer.parseInt(progress[0]));
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String filename) {

			path = Environment.getExternalStorageDirectory().toString()
					+ File.separator + "Download" + File.separator + filename
					+ ".apk";
			b.setText("Install");

		}

	}

}
