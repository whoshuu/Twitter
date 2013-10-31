package whoshuu.twitteractivity;

import org.json.JSONObject;

import whoshuu.twitteractivity.fragments.UserTimelineFragment;
import whoshuu.twitteractivity.models.User;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		loadUserTimeline(getIntent().getIntExtra("uid", -1));
		loadProfileInfo(getIntent().getIntExtra("uid", -1));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	private void loadProfileInfo(int uid) {
		TwitterClientApp.getRestClient().getUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				User user = User.fromJson(json);
				getActionBar().setTitle(user.getScreenName());
				populateProfileHeader(user);
			}
		}, uid);
	}
	
	private void populateProfileHeader(User user) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		tvName.setText(user.getAuthor());
		TextView tvTag = (TextView) findViewById(R.id.tvTag);
		tvTag.setText(user.getTag());
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		tvFollowers.setText(user.getFollowers() + " Followers");
		TextView tvFriends = (TextView) findViewById(R.id.tvFriends);
		tvFriends.setText(user.getFriends() + " Following");
		ImageView ivProfile = (ImageView) findViewById(R.id.ivProfile);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(user.getImageUrl(), ivProfile);
	}
	
	private void loadUserTimeline(int uid) {
		FragmentManager manager = getSupportFragmentManager();
		UserTimelineFragment userTimeline = (UserTimelineFragment) manager.findFragmentByTag("User");
		userTimeline.setUid(uid);
	}
}
