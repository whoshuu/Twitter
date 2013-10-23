package whoshuu.twitteractivity.models;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.database.sqlite.SQLiteException;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "users")
public class User extends Model implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6257814010477587185L;
	@Column(name = "uid")
	public int uid;
	@Column(name = "author")
	public String author;
	@Column(name = "screen")
	public String screen;
	@Column(name = "imageUrl")
	public String imageUrl;
	
	public User() {
		super();
	}
	
	public static User fromJson(JSONObject object) {
		try {
			User user;
			try {
				user = new Select()
						.from(User.class)
						.where("uid = ?", object.getInt("id"))
						.executeSingle();
			} catch (SQLiteException e) {
				user = null;
			}
			if (user == null) {
				user = new User();
				user.uid = object.getInt("id");
				user.author = object.getString("name");
				user.screen = "@" + object.getString("screen_name");
				user.imageUrl = object.getString("profile_image_url");
				user.save();
			}
			return user;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Tweet> tweets() {
		return getMany(Tweet.class, "User");
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public String getScreenName() {
		return this.screen;
	}
	
	public String getImageUrl() {
		return this.imageUrl;
	}
}
