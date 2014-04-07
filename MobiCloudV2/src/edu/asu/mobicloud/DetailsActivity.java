package edu.asu.mobicloud;

import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RatingBar;
import edu.asu.mobicloud.fragments.CommentDialogFragment;

public class DetailsActivity extends BaseActivity {
	private RatingBar editRating;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.details, menu);
		return true;
	}

}
