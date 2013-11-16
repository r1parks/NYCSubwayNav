package com.example.subwaynavigator;

public class SubwayInfo {

	private String 
		img_file,
		stop_name,
		trains;
	
	public SubwayInfo() {}

	public SubwayInfo(String _img_file, String _stop_name, String _trains)
	{
		this.img_file = _img_file;
		this.stop_name = _stop_name;
		this.trains = _trains;
	}

	public String getTrains() 
	{
		return trains;
	}

	public void setTrains(String trains) 
	{
		this.trains = trains;
	}
	
	public String getImgFile() 
	{
		return img_file;
	}
	
	public void setImgFile(String img_file) 
	{
		this.img_file = img_file;
	}
	
	public String getStopName() 
	{		
		return stop_name;
	}
	
	public void setStopName(String stop_name) 
	{
		this.stop_name = stop_name;
	}
}
