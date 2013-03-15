package com.example.clubapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ViewClassPage extends Activity {

	private SharedPreferences mPreferences;
	String TAG = "ViewClassPage";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_viewclasspage);
		mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
		String clubName = mPreferences.getString("selectedClub", "NOT A CLUB");
		
		TextView club = (TextView) findViewById(R.id.clubSelected);
		TextView description = (TextView) findViewById(R.id.description);
		
		club.setText(clubName);
		description.setText("IT'S A CLUB, MOTHAFUCKAS");
	}

}
