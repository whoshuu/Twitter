package whoshuu.twitteractivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import whoshuu.twitteractivity.models.Tweet;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetAdapter extends ArrayAdapter<Tweet> {
	public TweetAdapter(Context context, List<Tweet> tweets) {
		super(context, R.layout.tweet, R.id.tvAuthor, tweets);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			view = inflater.inflate(R.layout.tweet, null);
		}
		TextView tvAuthor = (TextView) view.findViewById(R.id.tvAuthor);
		TextView tvScreen = (TextView) view.findViewById(R.id.tvScreen);
		TextView tvText = (TextView) view.findViewById(R.id.tvText);
		TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
		ImageView ivUser = (ImageView) view.findViewById(R.id.ivUser);
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.user.getImageUrl(), ivUser);
		tvAuthor.setText(tweet.user.getAuthor());
		tvScreen.setText(tweet.user.getScreenName());
		tvText.setText(Html.fromHtml(urlize(tweet.text)));
		tvText.setMovementMethod(LinkMovementMethod.getInstance());
		tvTime.setText(timeDifference(tweet.time));
		return view;
	}
	
	public static String urlize(String input) {
		String [] parts = input.split("\\s");
		String ret = "";
		for (String item : parts) try {
			URL url = new URL(item);
			if (url.toString().startsWith("https")) {
				ret += "<a style=\"text-decoration: none\" href=\"" + url + "\">" + url.toString().substring(8) + "</a> ";
			} else if (url.toString().startsWith("http")) {
				ret += "<a style=\"text-decoration: none\" href=\"" + url + "\">" + url.toString().substring(7) + "</a> ";
			} else {
				ret += "<a style=\"text-decoration: none\" href=\"" + url + "\">" + url + "</a> ";
			}
		} catch (MalformedURLException e) {
			ret += item + " ";
		}
		return ret;
	}
	
	public static String timeDifference(Date date) {
		Date now = new Date();
		long diff = now.getTime() - date.getTime();
		long val = diff / 1000; 
		if (val / 60 == 0) {
			if (val < 0) {
				val = 0;
			}
			return val + " s";
		} else {
			val /= 60;
			if (val / 60 == 0) {
				return val + " m";
			} else {
				val /= 60;
				if (val / 24 == 0) {
					return val + " h";
				} else {
					val /= 24;
					return val + " d";
				}
			}
		}
	}
}
