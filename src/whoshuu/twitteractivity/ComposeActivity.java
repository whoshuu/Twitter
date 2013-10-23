package whoshuu.twitteractivity;

import org.json.JSONObject;

import whoshuu.twitteractivity.models.Tweet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends Activity {
	EditText etTweet;
	TextView tvCharacters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		etTweet = (EditText) findViewById(R.id.etTweet);
		tvCharacters = (TextView) findViewById(R.id.tvCharacters);
		
		etTweet.addTextChangedListener(new TextWatcher () {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable e) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int count, int after) {
				tvCharacters.setText(String.valueOf(etTweet.getText().toString().length()));
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose, menu);
		return true;
	}
	
	public boolean cancelCompose(MenuItem menu) {
		finish();
		return false;
	}
	
	public void sendTweet(View view) {
		if (etTweet.getText().toString().length() != 0) {
			TwitterClientApp.getRestClient().sendTweet(new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject object) {
					Tweet tweet = Tweet.fromJson(object);
					Intent data = new Intent();
					data.putExtra("tweet", tweet);
					setResult(RESULT_OK, data);
					finish();
				}
				
				@Override
				public void onFailure(Throwable e) {
					e.printStackTrace();
					Toast.makeText(ComposeActivity.this, "Error sending tweet!", Toast.LENGTH_SHORT).show();
					finish();
				}
			}, etTweet.getText().toString());
			Toast.makeText(ComposeActivity.this, "Sent tweet!", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(ComposeActivity.this, "Make sure to fill in your tweet!", Toast.LENGTH_SHORT).show();
		}
	}
}
