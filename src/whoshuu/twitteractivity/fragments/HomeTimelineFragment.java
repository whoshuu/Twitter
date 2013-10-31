package whoshuu.twitteractivity.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import whoshuu.twitteractivity.TwitterClientApp;
import whoshuu.twitteractivity.models.Tweet;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {
	
	@Override
	protected void updateTweets() {
		tweetType = "home";
		super.updateTweets();
	}
	
	@Override
	protected void getTweets() {
		TwitterClientApp.getRestClient().getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				Log.d("DEBUG", jsonTweets.toString());
				ArrayList<Tweet> results = Tweet.fromJson(jsonTweets, tweetType);
				if (results.size() > 0) {
					tweetsAdapter.addAll(results);
					max_id = results.get(results.size() - 1).tid - 1;
				}
				lvTweets.onRefreshComplete();
			}
			
			@Override
			public void onFailure(Throwable e) {
				Toast.makeText(getActivity(), "Failed to retrieve tweets", Toast.LENGTH_LONG).show();
			}
			
			@Override
			protected void handleFailureMessage(Throwable e, String responseBody) {
				Toast.makeText(getActivity(), "Currently being rate limited", Toast.LENGTH_LONG).show();
			}
		}, max_id, 10 - dbTweets.size());
	}
}
