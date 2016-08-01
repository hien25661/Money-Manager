package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import util.LayDate_Month_Yeah;
import android.content.SharedPreferences;
import android.util.Log;

public class MySharedPreference {

	public void showPreferences(SharedPreferences mySharedPreferences) {
		String dateCurentShared = null;
		float TongHomNay;
		float TongTuan;
		float TongThang;
		float Total;
		// extract the <key/value> pairs, use default param for missing data
		dateCurentShared = mySharedPreferences.getString("currentDate",
				"defNameValue");
		TongHomNay = mySharedPreferences.getFloat("tvMoneyToday", 0F);
		Total = mySharedPreferences.getFloat("tvMoneyTotal", 0F);
		TongTuan = mySharedPreferences.getFloat("tvMoneyTuan", 0F);
		TongThang = mySharedPreferences.getFloat("tvMoneyThang", 0F);
		
		// ////////
		//xu ly cac TongTien, Thang, Nam/////
		// khac ngay thang
		if (!dateCurentShared.equals("defNameValue")) {
			if (!dateCurentShared.equals(new LayDate_Month_Yeah()
			.NgayHienTai_THEO_InsertDataBase())) {
				// xu ly Tuan, Thang, nam
				SimpleDateFormat dateDatabase = new SimpleDateFormat(
						"yyyy-MM-dd");
				Calendar cal = Calendar.getInstance(); // thang nam hien tai
				// cal.get(Calendar.MONTH) = thang
				// cal.get(Calendar.YEAR)== nam
				Calendar cal2 = Calendar.getInstance(); // thang nam trong
				// shared
				/*
				 * int month = cal.get(Calendar.MONTH); // thang hien tai int
				 * year = cal.get(Calendar.YEAR); // int week_of_month =
				 * cal.get(Calendar.WEEK_OF_MONTH);
				 */
				// chuoi doi chuoi thanh Date
				try {
					cal2.setTime(dateDatabase.parse(dateCurentShared)); // date
					/*trong Shared Prefered MyShared thuc hien chuyen doi
					 *  Khac ngay hien tai => TongHomNay =0
					*/
					cal.setFirstDayOfWeek(Calendar.MONDAY); 
					
					TongHomNay = 0;
					// khac thang => thang va tuan =0;
					if (cal.get(Calendar.MONTH) != cal2.get(Calendar.MONTH)) { // thang
					/* bat dautu 0 => tru 1 neu in*/
						TongThang = 0;
						TongTuan = 0;
					}// close != thang =>out
					else { // cung thang gia tri ko doi
						// neu cung thang khac tuan
						if (cal.get(Calendar.WEEK_OF_MONTH) != cal2.get(Calendar.WEEK_OF_MONTH)) {
							if(cal.get(Calendar.DAY_OF_WEEK)== cal.getFirstDayOfWeek()){
								TongTuan = 0;	
							}
						}// close tuan
					}// =>out
				} catch (ParseException e) {
					Log.e("TAG",
							"Loi method GridView showPreferences"
									+ e.getMessage());
					cal2 = null;
				}
				// neu cung nam khac thang
				// if(cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){// luon
				// cung nam

				/*
				 */

			}// close { if
			MyApplication.homnay = TongHomNay;
			MyApplication.tuan = TongTuan;
			MyApplication.thang = TongThang;
			MyApplication.tongtien = Total;
		}
	}// loadPreferences

	public void savePreferences(SharedPreferences mySharedPreferences) {
		// obtain an editor to add data to (my)SharedPreferences object
		SharedPreferences.Editor myEditor = mySharedPreferences.edit();
		//myEditor.clear(); // lam sach

		// put some <key/value> data in the preferences object
		myEditor.putString("currentDate",
				new LayDate_Month_Yeah().NgayHienTai_THEO_InsertDataBase());
		myEditor.putFloat("tvMoneyToday", Float.valueOf(MyApplication.homnay));
		myEditor.putFloat("tvMoneyTotal", Float.valueOf(MyApplication.tongtien));
		myEditor.putFloat("tvMoneyTuan", Float.valueOf(MyApplication.tuan));
		myEditor.putFloat("tvMoneyThang", Float.valueOf(MyApplication.thang));

		myEditor.commit();
	}// savePreferences
	
}
