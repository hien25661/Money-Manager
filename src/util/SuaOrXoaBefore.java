package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import application.MyApplication;
import application.MySharedPreference;

public class SuaOrXoaBefore {
	private LayDate_Month_Yeah lay_ngay = new LayDate_Month_Yeah();

	/*
	 * Dc dung khi update 
	 * Muc dich cua ham suadulieu_BeforeUpdate
	 * kiem tra quay lai gia tri Tong ban dau khi chua insert
	 * neu da Thu (+ vao bien Gobal -> thuc hien - lai gia tri ban dau)
	 * neu da Chi (- bien Gobal - > thuc hien + lai gia tri ban dau)
	 */
	public void suadulieu_BeforeUpdate(String ma_id, String ngayDatabase,float so_tienDatabase,String ngay_of_edit,float sotien_of_edit) {
		/*
		 * neu chi chinh sua: ten giao dich, ghi chu, the loai => return true
		 * nguoc lai chinh sua: ngay, sotien => reture false
		 */
		if(so_tienDatabase==sotien_of_edit && ngayDatabase.equals(ngay_of_edit)){
			return;
		}else{
			SimpleDateFormat dateDatabase = new SimpleDateFormat("yyyy-MM-dd");
			if (ma_id.equals("thu")) {
				if (ngayDatabase.equals(lay_ngay.NgayHienTai_THEO_InsertDataBase())) {
					// Log.i("TAG","Cung Ngay Insert" + MyApplication.homnay + " " +
					// MyApplication.tongtien);
					MyApplication.homnay -= so_tienDatabase;
					MyApplication.tongtien -= so_tienDatabase;
					MyApplication.thang -= so_tienDatabase;
					MyApplication.tuan -= so_tienDatabase;
					// Log.i("TAG","Cung Ngay Insert luc sau" + MyApplication.homnay
					// + " " + MyApplication.tongtien);

				} else { // khac nam thang ngay

					Calendar cal = Calendar.getInstance(); // thang nam hien tai
					// cal.get(Calendar.MONTH) = thang
					Calendar cal2 = Calendar.getInstance(); // thang nam cua ngayDatabase
					/*
					 * int month = cal.get(Calendar.MONTH); // thang hien tai int
					 * year = cal.get(Calendar.YEAR); // int week_of_month =
					 * cal.get(Calendar.WEEK_OF_MONTH);
					 */
					try {
						cal2.setTime(dateDatabase.parse(ngayDatabase)); 
						// chuyen cal2 thanh Date
						// khac ngay hien tai => ko + voi MyApplication.homnay
						// luon + voi MyApplication.tongtien bat buoc
						MyApplication.tongtien -= so_tienDatabase;
						if(cal.get(Calendar.YEAR)== cal2.get(Calendar.YEAR)){ // luon cung nam 
							if (cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
								MyApplication.thang -= so_tienDatabase; // cung thang => +
								if (cal.get(Calendar.WEEK_OF_MONTH) == cal2
										.get(Calendar.WEEK_OF_MONTH)) {
									MyApplication.tuan -= so_tienDatabase;
								}
							}						}

					} catch (ParseException e) {
						Log.e("TAG", "Loi method xulytinhtoan_THUCHI ThuChiAcitviy"
								+ e.getMessage());
						cal2 = null;
					}
				}// close else
			}// close if ma_id = Thu
			/*
			 * Thuc hien chi
			 */
			else {// neu ma_id == chi
				if (ngayDatabase.equals(lay_ngay.NgayHienTai_THEO_InsertDataBase())) {
					// Log.i("TAG","Cung Ngay Insert" + MyApplication.homnay + " " +
					// MyApplication.tongtien);
					MyApplication.homnay += so_tienDatabase;
					MyApplication.tongtien += so_tienDatabase;
					MyApplication.thang += so_tienDatabase;
					MyApplication.tuan += so_tienDatabase;
					// Log.i("TAG","Cung Ngay Insert luc sau" + MyApplication.homnay
					// + " " + MyApplication.tongtien);

				} else { // khac nam thang ngay
					Calendar cal = Calendar.getInstance(); // thang nam hien tai
					// cal.get(Calendar.MONTH) = thang
					Calendar cal2 = Calendar.getInstance(); // thang nam cua
					// ngayDatabase (nam trong database)
					/*
					 * int month = cal.get(Calendar.MONTH); // thang hien tai int
					 * year = cal.get(Calendar.YEAR); // int week_of_month =
					 * cal.get(Calendar.WEEK_OF_MONTH);
					 */
					try {
						// chuyen cal2 thanh Date
						cal2.setTime(dateDatabase.parse(ngayDatabase)); 
						// khac ngay hien tai => ko +voi MyApplication.homnay
						// luon + voi MyApplication.tongtien bat buoc
						MyApplication.tongtien += so_tienDatabase;
						if(cal.get(Calendar.YEAR)== cal2.get(Calendar.YEAR)){ // luon cung nam 
							if (cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
								MyApplication.thang += so_tienDatabase; // cung thang => +
								if (cal.get(Calendar.WEEK_OF_MONTH) == cal2
										.get(Calendar.WEEK_OF_MONTH)) {
									MyApplication.tuan += so_tienDatabase;
								}
							}
						}

					} catch (ParseException e) {
						Log.e("TAG", "Loi method xulytinhtoan_THUCHI ThuChiAcitviy"
								+ e.getMessage());
						cal2 = null;
					}
				}//close else khac thang tuan
			}//close else thuc hien ma chi
		}

	}//close suadulieu_BeforeUpdate
	public void Xoa_suadulieu_BeforeRemove(String ma_id, String ngayDatabase,float so_tienDatabase) {
		/*
		 * Truoc khi xoa thuc hien chinh sua lai du lieu cho variable Gobal
		 */
		SimpleDateFormat dateDatabase = new SimpleDateFormat("yyyy-MM-dd");
		if (ma_id.equals("thu")) {
			if (ngayDatabase.equals(lay_ngay.NgayHienTai_THEO_InsertDataBase())) {
				// Log.i("TAG","Cung Ngay Insert" + MyApplication.homnay + " " +
				// MyApplication.tongtien);
				MyApplication.homnay -= so_tienDatabase;
				MyApplication.tongtien -= so_tienDatabase;
				MyApplication.thang -= so_tienDatabase;
				MyApplication.tuan -= so_tienDatabase;
				// Log.i("TAG","Cung Ngay Insert luc sau" + MyApplication.homnay
				// + " " + MyApplication.tongtien);

			} else { // khac nam thang ngay

				Calendar cal = Calendar.getInstance(); // thang nam hien tai
				// cal.get(Calendar.MONTH) = thang
				Calendar cal2 = Calendar.getInstance(); // thang nam cua ngayDatabase
				/*
				 * int month = cal.get(Calendar.MONTH); // thang hien tai int
				 * year = cal.get(Calendar.YEAR); // int week_of_month =
				 * cal.get(Calendar.WEEK_OF_MONTH);
				 */
				cal.setFirstDayOfWeek(Calendar.MONDAY); 
				try {
					cal2.setTime(dateDatabase.parse(ngayDatabase)); 
					// chuyen cal2 thanh Date
					// khac ngay hien tai => ko + voi MyApplication.homnay
					// luon + voi MyApplication.tongtien bat buoc
					MyApplication.tongtien -= so_tienDatabase;
					if(cal.get(Calendar.YEAR)== cal2.get(Calendar.YEAR)){ // luon cung nam 
						if (cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
							MyApplication.thang -= so_tienDatabase; // cung thang => +
							if (cal.get(Calendar.WEEK_OF_MONTH) == cal2.get(Calendar.WEEK_OF_MONTH)) {
								if(cal.get(Calendar.DAY_OF_WEEK)== cal.getFirstDayOfWeek()){
									MyApplication.tuan -= so_tienDatabase;
								}
							}
						}
					}

				} catch (ParseException e) {
					Log.e("TAG", "Loi method xulytinhtoan_THUCHI ThuChiAcitviy"
							+ e.getMessage());
					cal2 = null;
				}
			}// close else
		}// close if ma_id = Thu
		/*
		 * Thuc hien chi
		 */
		else {// neu ma_id == chi
			if (ngayDatabase.equals(lay_ngay.NgayHienTai_THEO_InsertDataBase())) {
				// Log.i("TAG","Cung Ngay Insert" + MyApplication.homnay + " " +
				// MyApplication.tongtien);
				MyApplication.homnay += so_tienDatabase;
				MyApplication.tongtien += so_tienDatabase;
				MyApplication.thang += so_tienDatabase;
				MyApplication.tuan += so_tienDatabase;
				// Log.i("TAG","Cung Ngay Insert luc sau" + MyApplication.homnay
				// + " " + MyApplication.tongtien);

			} else { // khac nam thang ngay
				Calendar cal = Calendar.getInstance(); // thang nam hien tai
				// cal.get(Calendar.MONTH) = thang
				Calendar cal2 = Calendar.getInstance(); // thang nam cua
				// ngayDatabase (nam trong database)
				/*
				 * int month = cal.get(Calendar.MONTH); // thang hien tai int
				 * year = cal.get(Calendar.YEAR); // int week_of_month =
				 * cal.get(Calendar.WEEK_OF_MONTH);
				 */
				
				cal.setFirstDayOfWeek(Calendar.MONDAY);
				try {
					// chuyen cal2 thanh Date
					cal2.setTime(dateDatabase.parse(ngayDatabase)); 
					// khac ngay hien tai => ko +voi MyApplication.homnay
					// luon + voi MyApplication.tongtien bat buoc
					MyApplication.tongtien += so_tienDatabase;
					if(cal.get(Calendar.YEAR)== cal2.get(Calendar.YEAR)){ // luon cung nam 
						if (cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
							MyApplication.thang += so_tienDatabase; // cung thang => +
							if (cal.get(Calendar.WEEK_OF_MONTH) == cal2
									.get(Calendar.WEEK_OF_MONTH)) {
								if(cal.get(Calendar.DAY_OF_WEEK)== cal.getFirstDayOfWeek()){

									MyApplication.tuan += so_tienDatabase;
								}
							}
						}
					}

				} catch (ParseException e) {
					Log.e("TAG", "Loi method xulytinhtoan_THUCHI ThuChiAcitviy"
							+ e.getMessage());
					cal2 = null;
				}
			}//close else khac thang tuan
		}//close else thuc hien ma chi
	}//close suadulieu_BeforeUpdate
}
