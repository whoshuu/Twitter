package whoshuu.twitteractivity.fragments;

import java.util.ArrayList;

import whoshuu.twitteractivity.EndlessScrollListener;
import whoshuu.twitteractivity.R;
import whoshuu.twitteractivity.TweetAdapter;
import whoshuu.twitteractivity.models.Tweet;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Select;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetsListFragment extends Fragment {
	protected TweetAdapter tweetsAdapter;
	protected PullToRefreshListView lvTweets;
	protected ArrayList<Tweet> dbTweets;
	protected String tweetType;
	protected long max_id;
	
	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup parent, Bundle savedInstanceState) {
		return inf.inflate(R.layout.fragment_tweets_list, parent, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		max_id = -1;
		lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);
		tweetsAdapter = new TweetAdapter(getActivity(), new ArrayList<Tweet>());
		lvTweets.setAdapter(tweetsAdapter);
		tweetType = "home";
		
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
		        updateTweets();
		    }
	    });
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
	        @Override
	        public void onRefresh() {
	        	tweetsAdapter.clear();
	        	ArrayList<Tweet> dbTweets = new Select().from(Tweet.class)
						   								.where("type = ?", tweetType)
	        											.execute();
	        	for (Tweet tweet : dbTweets) {
	        		tweet.delete();
	        	}
	        	max_id = -1;
	        }
	    });
	}
	
	protected void updateTweets() {
		if (max_id >= 0) {
			dbTweets = new Select().from(Tweet.class)
								   .where("tid <= ? AND type = ?", max_id, tweetType)
								   .orderBy("tid DESC")
								   .limit("10")
								   .execute();
		} else {
			dbTweets = new Select().from(Tweet.class)
					   			   .where("type = ?", tweetType)
								   .orderBy("tid DESC")
								   .limit("10")
								   .execute();
			if (dbTweets.size() != 0) {
				max_id = dbTweets.get(dbTweets.size() - 1).tid - 1;
			}
		}
		if (dbTweets.size() < 10) {
			if (dbTweets.size() != 0) {
				tweetsAdapter.addAll(dbTweets);
			}
			getTweets();
			
		} else {
			tweetsAdapter.addAll(dbTweets);
			max_id = dbTweets.get(dbTweets.size() - 1).tid - 1;
			lvTweets.onRefreshComplete();
		}
	}
	
	protected void getTweets() {
		// Must be overridden to populate the adapter and listview
	}
	
	public TweetAdapter getAdapter() {
		return tweetsAdapter;
	}

	public ListView getLvTweets() {
		return lvTweets;
	}
}
