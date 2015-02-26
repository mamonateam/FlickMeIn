package com.codepath.apps.flickmein.models;

import android.net.Uri;

import com.activeandroid.Model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class FlickrPhoto extends Model implements Serializable {

    private static final long serialVersionUID = 100189L;

	private String uid;
	private String name;
	private String url;
    private String user;
    private String color;
    private Uri uri;
	
	public FlickrPhoto() {
		super();
	}

    public FlickrPhoto(String uid, String name, String url, String user, String color) {
        this.uid = uid;
        this.name = name;
        this.url = url;
        this.user = user;
        this.color = color;
    }

    public FlickrPhoto(JSONObject object){
		super();

		try {
			this.uid = object.getString("id");
			this.name = object.getString("title");
			// http://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
			this.url = "http://farm" + object.getInt("farm") + ".staticflickr.com/" + object.getInt("server") + 
			  "/" + uid + "_" + object.getString("secret") + ".jpg";
            // strip tags to get user and color
            String tags = object.getString("tags");
            this.user = "";
            this.color = "";
            if(tags.length() > 0) {
                String[] strippedTags = tags.split(" ");
                this.user = findTag(strippedTags, "fmiuser");
                this.color = findTag(strippedTags, "fmicolor");
            }
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

    public static ArrayList<FlickrPhoto> fromJSONArray(JSONArray jsonArray) {
        ArrayList<FlickrPhoto> photos = new ArrayList<>();

        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                FlickrPhoto photo = new FlickrPhoto(jsonArray.getJSONObject(i));
                if(photo != null) {
                    photos.add(photo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        return photos;
    }

	public String getUrl() {
		return url;
	}

	public String getName() {
		return name;
	}

    public String getUid() {
        return uid;
    }

    public String getUser() {
        return user;
    }

    public String getColor() {
        return color;
    }

    public Uri getUri() {
        return uri;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    private String findTag(String[] strippedTags, String prefix) {
        String result = "";
        for (String tag : strippedTags) {
            if (tag.startsWith(prefix)) {
                result = tag.replace(prefix, "");
                return result;
            }
        }
        return result;
    }
}
