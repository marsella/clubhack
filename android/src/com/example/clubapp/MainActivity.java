package com.example.clubapp;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private SharedPreferences mPreferences;
	private String TAG = "MainActivity";
	Thread t;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        setContentView(R.layout.activity_main);
        processViewMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void processViewMenu(View view) {
    	processViewMenu();
    }
    
    public void processViewMenu() {
    	t = new Thread() {
			public void run() {
				Looper.prepare();
				if(viewMenu())
				{
			    	Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
			    	startActivity(intent);
				}
				else
				{
					Log.i(TAG, "Failed to view menu");
					Toast.makeText(getApplicationContext(), "Menu failed to load", Toast.LENGTH_LONG).show();
				}
				Looper.loop();
			}
    	};
    	
	t.start();
    }
    
    public boolean viewMenu() {
    	
    	try {
	    	DefaultHttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(getString(R.string.api_base)+"/");
			
			String response = null;
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			response = client.execute(post, responseHandler);

			JSONObject jObject = new JSONObject(response);
			JSONArray nameObject = jObject.getJSONArray("names");  
			Log.i(TAG, "NameObject: " + (nameObject.toString()));
			
			SharedPreferences.Editor editor = mPreferences.edit();
			editor.putString("names", nameObject.toString());
			editor.commit();
			return true;
		    
    	}
    	catch(Exception e) {
    		Log.e(TAG, "Uncaught exception: " + e.toString());
    		return false;
    	}
       
    }
    
}
