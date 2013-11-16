package com.example.subwaynavigator;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity {
	
	ImageView locationImage;
	TextView locationName;
	TextView trainsAtThisLocation;

	 protected void onCreate(Bundle savedInstanceState) {
	    	
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_location);
	        
	        locationImage = (ImageView) findViewById(R.id.locationImage);
	        locationName = (TextView) findViewById(R.id.locationName);
	        trainsAtThisLocation = (TextView) findViewById(R.id.trainsAtThisStop);
	        
	        // get info that was saved via bundle
	        Bundle infoAboutLocation = getIntent().getExtras();
	        
	        if(infoAboutLocation!= null){
	        	String locationImageName = infoAboutLocation.getString("locationImageName");
	        	String nameOfLocation = infoAboutLocation.getString("nameOfLocation");
	        	String locationTrains = infoAboutLocation.getString("locationTrains");
	        	
	        	//update location name, picture, and trains served at this location
	        	locationName.setText(nameOfLocation);
	        	trainsAtThisLocation.setText("Trains at this location:" + locationTrains);
	        	
	        	// should have image inside of resources folder
	        	Resources resources = getResources();
	        	try
	        	{
		        	int resourceId = resources.getIdentifier(locationImageName, "drawable", getPackageName());
		        	Drawable locationImageDrawable = resources.getDrawable(resourceId);
		        	locationImage.setImageDrawable(locationImageDrawable);
	        	}
	        	catch(Exception e)
	        	{
	        		Toast.makeText(this, "Could not find: " + locationImageName, Toast.LENGTH_SHORT).show();
	        	}
	        }
	    }
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
}
