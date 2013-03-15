package com.example.clubapp;

import org.json.JSONArray;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuActivity extends ListActivity {

	private SharedPreferences mPreferences;
	private String TAG = "MenuActivity";
	
	protected void onCreate(Bundle savedInstanceState) {
    	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		
		final ListView mListView = (ListView) findViewById(android.R.id.list);
		

		try {
			JSONArray nameJSONArray = new JSONArray(mPreferences.getString("names", ""));
			
			String[] nameArray = new String[nameJSONArray.length()];
			for(int i = 0; i < nameJSONArray.length(); i++) {
			    nameArray[i] = nameJSONArray.getString(i);
			}
			
			
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			  android.R.layout.simple_list_item_1, android.R.id.text1, nameArray);
			setListAdapter(adapter);
			
		} catch (Exception e) {
			Log.e(TAG, "Uncaught exception in menu: " + e.toString());
		}
		
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
		      @SuppressWarnings("unused")
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
		          String clubSelected = (String) (mListView.getItemAtPosition(myItemInt));
		          		          
		          SharedPreferences.Editor editor = mPreferences.edit();
		          editor.putString("selectedClub", clubSelected);
		          editor.commit();
		          
		          Intent intent = new Intent(getApplicationContext(), ViewClassPage.class);
		          
		          startActivity(intent);
		      }
		}); 
		
	
	}
    
	public void launchAddClub(View view)
    {
    	Intent intent = new Intent(getApplicationContext(), AddClubActivity.class);
    	startActivity(intent);
    }
	
	public void launchClubPage(View view) {
		Intent intent = new Intent(getApplicationContext(), ViewClassPage.class);
		startActivity(intent);
	}

}