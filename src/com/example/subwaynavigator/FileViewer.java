package com.example.subwaynavigator;

import java.util.Scanner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class FileViewer extends Activity {

	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		setContentView(R.layout.file_view);
		TextView text_display = (TextView) findViewById(R.id.file_text_view);
		try
		{
			Scanner input_file = new Scanner(openFileInput(getString(R.string.file_name)));
			StringBuilder sb = new StringBuilder();
			while(input_file.hasNextLine())
			{
				sb.append(input_file.nextLine() + "\n");
			}
			text_display.setText(sb.toString());
			input_file.close();
		}
		catch(Exception e)
		{
			text_display.setText("Error opening file, have you saved anything yet?\n\nHere's the error:\n" 
		                         + e.toString());
		}
	}
}
