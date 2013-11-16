package com.example.subwaynavigator;

import java.io.PrintWriter;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private WifiManager wifi_scanner;
	private Spinner wifi_spinner;
	private Button
		view_saved_button,
		submit_button,
		locate_button,
		refresh_button;

	//@Override enforces compiler verification that this method
	//successfully overrides the method of the parent class.
	//It's valid to override a method without the @Override 
	//directive, but it loses the compiler enforcement which
	//can lead to run time errors as opposed to compile time
	//errors.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		wifi_scanner = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		wifi_spinner = (Spinner) findViewById(R.id.wifi_spinner);
		refresh_button = (Button) findViewById(R.id.refresh_button);
		locate_button = (Button) findViewById(R.id.auto_locate_button);
		submit_button = (Button) findViewById(R.id.submit_button);
		view_saved_button = (Button) findViewById(R.id.view_file_button);
		this.refreshWifiList();
		refresh_button.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				MainActivity.this.refreshWifiList();
			}
		});
		locate_button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				MainActivity.this.autoLocate();
			}
		});
		submit_button.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View arg0) 
			{
				MainActivity.this.submit();
			}
		});
		view_saved_button.setOnClickListener(new OnClickListener() 
		{			
			@Override
			public void onClick(View arg0) 
			{
				startActivity(new Intent(MainActivity.this, FileViewer.class));
			}
		});
	}
	
	private void submit()
	{
		Toast.makeText(this, "Submitting!", Toast.LENGTH_SHORT).show();
		try
		{
			EditText
				latitude = (EditText) findViewById(R.id.latitude_edit_text),
				longitude = (EditText) findViewById(R.id.longitude_edit_text),
				name = (EditText) findViewById(R.id.name_edit_text),
				trains = (EditText) findViewById(R.id.trains_edit_text);
			
			Spinner ssid = (Spinner) findViewById(R.id.wifi_spinner);
			
			PrintWriter output_file = new PrintWriter(openFileOutput(getString(R.string.file_name), Context.MODE_APPEND));
			output_file.println(name.getText().toString());
			output_file.println(ssid.getSelectedItem().toString());
			output_file.println(latitude.getText().toString());
			output_file.println(longitude.getText().toString());
			output_file.println(trains.getText().toString());
			output_file.println("");
			output_file.close();
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Failed to open file for writing :/", Toast.LENGTH_SHORT).show();
		}
	}
	
	private class MyLocationListener implements LocationListener
	{
		private LocationManager location_manager;
		
		public MyLocationListener(LocationManager _location_manager)
		{
			this.location_manager = _location_manager;
		}
		
		@Override
		public void onLocationChanged(Location location) 
		{
			EditText
				lat_text,
				lon_text;
			
			lat_text = (EditText) findViewById(R.id.latitude_edit_text);
			lon_text = (EditText) findViewById(R.id.longitude_edit_text);
			
			lat_text.setText(Double.toString(location.getLatitude()));
			lon_text.setText(Double.toString(location.getLongitude()));

			location_manager.removeUpdates(this);
		}
		
		@Override
		public void onProviderDisabled(String arg0) {}

		@Override
		public void onProviderEnabled(String arg0) {}

		@Override
		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
	}
	
	private void autoLocate()
	{
		LocationManager location_manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if(!location_manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			Toast.makeText(this, "Error: GPS not enabled", Toast.LENGTH_SHORT).show();
			return;
		}
		Toast.makeText(this, "Getting GPS location", Toast.LENGTH_SHORT).show();
		LocationListener listener = new MyLocationListener(location_manager);
		location_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, listener);
	}
	
	private void refreshWifiList()
	{
		List<ScanResult> available_wifi = wifi_scanner.getScanResults();
		this.setWifiListToSpinner(available_wifi, wifi_spinner);
	}
	
	private void setWifiListToSpinner(List<ScanResult> available_wifi, Spinner wifi_spinner)
	{
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		for(ScanResult wifi : available_wifi)
		{
			adapter.add(wifi.SSID + "@" + wifi.BSSID);
		}
		wifi_spinner.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
