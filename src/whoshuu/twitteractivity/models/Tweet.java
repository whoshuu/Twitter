package whoshuu.twitteractivity.models;

import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "tweets")
public class Tweet extends Model implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4242954222507444080L;
	@Column(name = "text")
	public String text;
	@Column(name = "time")
	public Date time; 
	@Column(name = "user")
	public User user;
	@Column(name = "tid")
	public long tid;
	@Column(name = "ms")
	public long ms;
	
	public Tweet() {
		super();
	}
	
	@Override public String toString() {
		return this.user.getAuthor() + " " + this.user.getScreenName() + ": " + this.text + "   TIME: " + this.time; 
	}
	
	public static Tweet fromJson(JSONObject object) {
		Tweet tweet = new Tweet();
		try {
			tweet.text = object.getString("text");
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
			tweet.time = dateFormat.parse(object.getString("created_at"), new ParsePosition(0));
			tweet.user = User.fromJson(object.getJSONObject("user"));
			tweet.tid = object.getLong("id");
			tweet.ms = tweet.time.getTime();
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		tweet.save();
		return tweet;
	}
	
	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			Tweet tweet = Tweet.fromJson(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
			}
		}
		
		return tweets;
	}
	
	public static ArrayList<Tweet> getRecent() {
		return new Select()
				.from(Tweet.class)
				.orderBy("ms DESC")
				.limit("10")
				.execute();
	}
}
