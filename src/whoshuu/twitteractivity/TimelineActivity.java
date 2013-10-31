package whoshuu.twitteractivity;

import whoshuu.twitteractivity.fragments.HomeTimelineFragment;
import whoshuu.twitteractivity.fragments.MentionsFragment;
import whoshuu.twitteractivity.models.Tweet;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TimelineActivity extends FragmentActivity implements TabListener{
	private static final int COMPOSE_CODE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupNavigationTabs();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
	    super.onSaveInstanceState(outState);
	}

	public boolean composeTweet(MenuItem menu) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, COMPOSE_CODE);
        return false;
    }

	public boolean onProfileView(MenuItem menu) {
        startActivity(new Intent(this, ProfileActivity.class));
        return false;
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.timeline, menu);
		return true;
	}
	
	@SuppressLint("NewApi")
	protected void onActivityResult(int rq, int rs, Intent data) {
		if (rq == COMPOSE_CODE) {
			if (rs == RESULT_OK) {
				Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
				ActionBar actionBar = getActionBar();
				Tab tabHome = actionBar.getTabAt(0);
				actionBar.selectTab(tabHome);

				FragmentManager manager = getSupportFragmentManager();
				HomeTimelineFragment home = (HomeTimelineFragment) manager.findFragmentByTag("Home");
				home.getAdapter().insert(tweet, 0);
				home.getLvTweets().setSelection(0);
			}
		}
	}

	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Tab tabHome = actionBar
				.newTab()
				.setText("Home")
				.setTag("HomeTimelineFragment")
				.setIcon(R.drawable.ic_home)
				.setTabListener(this);
		Tab tabMentions = actionBar
				.newTab()
				.setText("Mention")
				.setTag("MentionsFragment")
				.setIcon(R.drawable.ic_mentions)
				.setTabListener(this);
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
	}
	
	public void loadProfile(View v) {
		Intent i = new Intent(this, ProfileActivity.class);
		int uid = (Integer) v.getTag();
		i.putExtra("uid", uid);
        startActivity(i);
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if (tab.getTag() == "HomeTimelineFragment") {
			fts.replace(R.id.frame_container, new HomeTimelineFragment(), "Home");
		} else if (tab.getTag() == "MentionsFragment") {
			fts.replace(R.id.frame_container, new MentionsFragment(), "Mentions");
		}
		fts.commitAllowingStateLoss();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
