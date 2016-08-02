package com.boxtimer365.moneylove;

import com.boxtimer365.moneylove.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;

public class Splash extends Activity {
	private Thread splashTread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash_main);

		SharedPreferences prefs = getSharedPreferences("SharedPreferences_MoneyLover",Activity.MODE_PRIVATE);
		if(!prefs.getBoolean("firstTime", false)) {//thuc hien lan dau tien
			// run your one time code
			SharedPreferences.Editor editor = prefs.edit(); 
			editor.putBoolean("firstTime", true);
			editor.commit();

			// thread for displaying the SplashScreen
			splashTread = new Thread() {
				@Override
				public void run() {
					try {
						synchronized(this){
							//wait 2 sec
							wait(3000);
						}

					} catch(InterruptedException e) {}
					finally {
						//start a new activity
						Intent i = new Intent(Splash.this,GridView_main.class);
						startActivity(i);
						finish();
					}
				}
			};
			splashTread.start();
		}else{
			Intent i = new Intent(Splash.this,GridView_main.class);
			startActivity(i);
			finish();
		}

	}
}
