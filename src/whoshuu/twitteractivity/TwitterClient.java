package whoshuu.twitteractivity;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1/"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "ZXBrYsl5CWc6FuhXUkrxKw";       // Change this
    public static final String REST_CONSUMER_SECRET = "jMnCt5D9Y2T0PsFaRtj8Tbt7gdgpnnkymKHz1zAas"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://twitterApp"; // Change this (here and in manifest)
    
    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getHomeTimeline(AsyncHttpResponseHandler handler, long max_id, int query) {
    	String url = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("count", String.valueOf(query));
    	if (max_id >= 0) {
    		params.put("max_id", String.valueOf(max_id));
    	}
    	client.get(url, params, handler);
    }
    
    public void getMentionsTimeline(AsyncHttpResponseHandler handler, long max_id, int query) {
    	String url = getApiUrl("statuses/mentions_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("count", String.valueOf(query));
    	if (max_id >= 0) {
    		params.put("max_id", String.valueOf(max_id));
    	}
    	client.get(url, params, handler);
    }
    
    public void getUserTimeline(AsyncHttpResponseHandler handler, long max_id, int query, int uid) {
    	String url = getApiUrl("statuses/user_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("count", String.valueOf(query));
    	if (uid >= 0) {
    		params.put("user_id", String.valueOf(uid));
    	}
    	if (max_id >= 0) {
    		params.put("max_id", String.valueOf(max_id));
    	}
    	client.get(url, params, handler);
    }
    
    public void getUserInfo(AsyncHttpResponseHandler handler, int uid) {
    	
    	if (uid >= 0) {
    		String apiUrl = getApiUrl("users/show.json");
    		RequestParams params = new RequestParams();
    		params.put("user_id", String.valueOf(uid));
    		client.get(apiUrl, params, handler);
    	} else {
    		String apiUrl = getApiUrl("account/verify_credentials.json");
    		client.get(apiUrl, null, handler);
    	}
    }
    
    public void sendTweet(AsyncHttpResponseHandler handler, String text) {
    	String url = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
    	params.put("status", text);
    	client.post(url, params, handler);
    }
}