package com.example.subwaynavigator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SubwayMode extends Activity {

	private Map<String, SubwayInfo> saved_subway_info;
	private TextView text_display;
	private WifiManager wifi_scanner;
	private Handler update_handler;
	private String found_last;
	private Runnable updater;

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.subway_mode);
		wifi_scanner = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		this.text_display = (TextView) findViewById(R.id.text_display);
		this.readSavedSubwayInfo();
		update_handler = new Handler();
		updater = new Runnable()
		{
			@Override
			public void run()
			{
				SubwayMode.this.scan();
				update_handler.postDelayed(updater, 5000);
			}	
		};
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		update_handler.removeCallbacks(this.updater);
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
		update_handler.removeCallbacks(this.updater);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		updater.run();
	}
	
	void readSavedSubwayInfo()
	{
		saved_subway_info = new HashMap<String, SubwayInfo>();
		Scanner info_file = new Scanner(getResources().openRawResource(R.raw.saved_wifi_data));
		while(info_file.hasNextLine())
		{
			String key = info_file.nextLine();
			String stop_name = info_file.nextLine();
			String image_name = info_file.nextLine();
			String trains = info_file.nextLine();
			if(info_file.hasNextLine())
			{
				info_file.nextLine(); //read blank line
			}
			saved_subway_info.put(key, new SubwayInfo(image_name, stop_name, trains));
		}
		info_file.close();
	}
	
	void scan()
	{
		List<ScanResult> available_wifi = wifi_scanner.getScanResults();
		StringBuilder sb = new StringBuilder("Current WiFi Access Points:\n\n");
		boolean still_at_same_stop = false;
		for(ScanResult wifi : available_wifi)
		{
			String wifi_id = wifi.SSID + "@" + wifi.BSSID;
			sb.append(wifi.SSID);
			sb.append("\n");
			if(saved_subway_info.containsKey(wifi_id) && still_at_same_stop == false)
			{
				if(wifi_id.equals(found_last)) 
				{ 
					still_at_same_stop = true;
				}
				else
				{
					found_last = wifi_id;
					this.displayStop(saved_subway_info.get(wifi_id));
					return;
				}
			}
		}
		if(still_at_same_stop == false)
		{
			found_last = null;
		}
		text_display.setText(sb.toString());
	}
	
	void displayStop(SubwayInfo stop_info)
	{
		Intent display = new Intent(this, LocationActivity.class);
		display.putExtra("locationImageName", stop_info.getImgFile());
		display.putExtra("nameOfLocation", stop_info.getStopName());
		display.putExtra("locationTrains", stop_info.getTrains());
		startActivity(display);
	}
}
