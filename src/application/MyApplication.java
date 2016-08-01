package application;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

public class MyApplication extends Application {
	//@Bien toan cuc Gobal
	public static float homnay;
	public static float tongtien;
	public static float tuan;
	public static float thang;
	public static String date;

	@Override
	public void onCreate() {
		super.onCreate();
		SharedPreferences mySharedPreferences = getSharedPreferences(
				"SharedPreferences_MoneyLover", Activity.MODE_PRIVATE);
		Log.i("TAG", "Load mySharedPreferences in Application thuc hien");
		if (mySharedPreferences != null
				&& mySharedPreferences.contains("currentDate")) {
			// object and key found, show all saved values
			new MySharedPreference().showPreferences(mySharedPreferences);
			Log.i("TAG", "Load co shared");

		} else { //khoi tao gia tri Gobal
			homnay = 0;
			tongtien = 0;
			thang = 0;
			tuan = 0;
		}
		//Log.i("TAG", "gia tri" + homnay + " " + tongtien);

		// truong hop ko co sharedPreferece gia tri la null => textview =0

	}

}
