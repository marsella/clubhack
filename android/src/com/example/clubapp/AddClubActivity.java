package com.example.clubapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddClubActivity extends Activity {

	SharedPreferences mPreferences;
	private String TAG = "AddClubActivity";
	Thread t;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        setContentView(R.layout.activity_addclub);

    }
	
    
	public void processAddClub(View view) {
		
	   	t = new Thread() {
				public void run() {
					Looper.prepare();
					if(addClub())
					{
				    	Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
				    	startActivity(intent);
					}
					else
					{
						Log.i(TAG, "failed to add club");
						Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
				    	startActivity(intent);
						Toast.makeText(getApplicationContext(), "failed to add club", Toast.LENGTH_LONG).show();
					}
					Looper.loop();
				}
			};
			t.start();
		
    }

	private boolean addClub() {
		try {
	    	EditText mClubField = (EditText) findViewById(R.id.name);
			EditText mPasswordField = (EditText) findViewById(R.id.password);
			EditText mCalIDField = (EditText) findViewById(R.id.calID);
			
			String club = mClubField.getText().toString();
			String password = mPasswordField.getText().toString();
			String calID = mCalIDField.getText().toString();

			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(getString(R.string.api_base)+"/add");

			List<NameValuePair> params = new ArrayList<NameValuePair>(2);
			params.add(new BasicNameValuePair("name", club));
			params.add(new BasicNameValuePair("password", password));
			
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
			Log.i(TAG, "entity: " + post.getEntity().toString());
			post.setHeader("Accept", "application/json");
			Log.i(TAG, "header: " + post.getAllHeaders(). toString());
			Log.i(TAG, "post: " + post.getURI());
			
			String response = null;
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = client.execute(post, responseHandler);
			
			Log.i(TAG, "response: " + response.toString());
			
			JSONObject jObject = new JSONObject(response);
			JSONObject sessionObject = jObject.getJSONObject("user");
			return true;
			
		} catch (Exception e) {
			Log.e(TAG, "Add Club Exception: " + e.toString());
			e.printStackTrace();
			return false;
		}
		
		
		
	}
	
}


