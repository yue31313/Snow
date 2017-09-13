package com.example.showviewtest;

import com.example.showviewtest.snowview.SnowView;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;

public class StartView extends Activity {

	//View
	Button addButton=null;
	Button subtarctButton=null;
	SnowView snowView=null;
	//SensorManager
	SensorManager sensorManager=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start_view);
		//get service
		sensorManager=(SensorManager)StartView.this.getSystemService(Context.SENSOR_SERVICE);
		//get id
		addButton=(Button)findViewById(R.id.addButton);
		subtarctButton=(Button)findViewById(R.id.subtractButton);
		snowView=(SnowView)findViewById(R.id.showView);
		//启动传感器功能
		snowView.regSensor(sensorManager);
		//click event
		addButton.setOnClickListener(new ClickEvent());
		subtarctButton.setOnClickListener(new ClickEvent());
	}
	
	private class ClickEvent implements View.OnClickListener
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.addButton:
				
				snowView.addSnows(snowView.getNumSnows());
				break;

			case R.id.subtractButton:
				
				snowView.subtractSnows(snowView.getNumSnows()/2);
				break;
				
			default:
				break;
			}
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		snowView.pause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		snowView.resume();
	}
	
}
