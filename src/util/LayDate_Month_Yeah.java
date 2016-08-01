package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.util.Log;

public class LayDate_Month_Yeah {

	public SimpleDateFormat dateformate = new SimpleDateFormat("dd/MM/yyyy");
	SimpleDateFormat dateDatabase = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat datetTextView = new SimpleDateFormat("dd/MM");
	SimpleDateFormat datetTextView2 = new SimpleDateFormat("dd/MM/yyyy");

	public String NgayThangNamCurrent() {
		Calendar myCalendar = Calendar.getInstance();
		/*
		 * Cach 1 dung Calendar lay tung gia tri ngay thang nam int thisYear =
		 * myCalendar.get(Calendar.YEAR); int thisMonth =
		 * myCalendar.get(Calendar.MONTH); int thisDay =
		 * myCalendar.get(Calendar.DAY_OF_MONTH);
		 * 
		 * //in ket qua kt
		 * //Log.i("TAG",String.valueOf(thisDay)+" "+String.valueOf
		 * (thisMonth)+" "+String.valueOf(thisYear));
		 * //ngay.setText(String.valueOf
		 * (thisDay)+"/"+String.valueOf(thisMonth)+"/"
		 * +String.valueOf(thisYear));
		 */
		/*
		 * Cach thu 2 dung SimpleDateFormate de lay gia tri tu getTime => Date
		 */

		// ham getTime lay Object Date chuyen theo format dd MM yyyy
		String currentDateStr = dateformate.format(myCalendar.getTime());

		// Log.i("TAG",currentDateStr);

		return currentDateStr;
	}

	public String Covert_toThangNgayNam(Calendar myCalendar) {
		SimpleDateFormat dateThangNgayNam = new SimpleDateFormat("MM/dd/yyyy");
		String currentDateStr = dateThangNgayNam.format(myCalendar.getTime());
		return currentDateStr;
	}

	public String Covert_toNgayThangNam(Calendar myCalendar2) {
		//String curren2tDateStr = datetTextView2.format(myCalendar2.getTime());
		String currentDateStr = dateformate.format(myCalendar2.getTime());
		//Log.i("TAG",curren2tDateStr);
		return currentDateStr;
	}

	public String CovertString_toNgayThangNam(String myCalendar) {
		SimpleDateFormat dateNgayThangNam = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat oldFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			// chuyen doi sang oldformat roi Format lai ngay thang
			String currentDateStr = dateNgayThangNam.format(oldFormat
					.parse(myCalendar));
			return currentDateStr;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * * tim ben class ThuChiActivity : "Xu ly ngay update vao database SQLite"
	 * ham CovertToDate_NgayThangNam dung cho ThuChiActivity Update xu ly Ngay
	 * cần trả về Date cho Calendar
	 */
	public Date CovertToDate_NgayThangNam(String myCalendar) {
		SimpleDateFormat oldFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			// chuyen doi sang oldformat roi Format lai ngay thang
			Date currentDateStr = oldFormat.parse(myCalendar);
			return currentDateStr;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String NgayThangNamTomorrow() {
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.add(Calendar.DAY_OF_MONTH, 1);
		Date tomorrow = myCalendar.getTime();
		String currentDateStr = dateformate.format(tomorrow);
		return currentDateStr;
	}
	public Calendar Calendar_toStringDate(String YearMonthDate) {
		try {
			Date date1 = dateDatabase.parse(YearMonthDate);
			Calendar myCalendar = Calendar.getInstance();
			myCalendar.setTime(date1);
			return myCalendar;

		} catch (ParseException e) {
			//"LayDate_month_Year.class -> Calendar_toStringDate "+e.getMessage());
		}

		return null;
	}
	public Calendar getTommorrow() {
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.add(Calendar.DAY_OF_MONTH, 1);
		return myCalendar;
	}

	public String Covert_toInsertDataBase(Calendar myCalendar) {
		String currentDateStr = dateDatabase.format(myCalendar.getTime());
		return currentDateStr;
	}
	public String setInsertDataBase(String date) {
		try {
			// chuyen doi sang oldformat roi Format lai ngay thang
			String currentDateStr = dateDatabase.format(datetTextView2.parse(date));
			return currentDateStr;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public String NgayHienTai_THEO_InsertDataBase() {
		Calendar myCalendar = Calendar.getInstance();
		String currentDateStr = dateDatabase.format(myCalendar.getTime());
		return currentDateStr;
	}
	public List<String> selectNgay(int Ngaynext){
		/* Gia tri cua nextNgay
		 * Tra ve 2 gia tri string 
		 * array.get(0) : 28/4/2013
		 * array.get(1) : 2013-04-28 
		 * vi code ben ChiTieuActivity da select voi from...to
		 */
		Calendar myCalendar = Calendar.getInstance();
		myCalendar.add(Calendar.DAY_OF_MONTH,Ngaynext);
		Date date = myCalendar.getTime();
		String dayCurrent = datetTextView2.format(date);
		String dateFrom = dateDatabase.format(date);
		//Log.i("TAG","in LayDate so dem"+dateFrom);
		List<String> arrayDate = new ArrayList<String>();
		arrayDate.add(dayCurrent);
		arrayDate.add(dateFrom);
		return arrayDate;
	}
	/*
	 * public boolean MONTH_with_MonthCurrent(String date1){ Calendar cal =
	 * Calendar.getInstance(); Calendar cal2 = Calendar.getInstance(); //get
	 * Year //int year = cal.get(Calendar.YEAR); // nam hien tai
	 * 
	 * int month = cal.get(Calendar.MONTH); // nam hien tai try { Date date1S =
	 * dateDatabase.parse(date1); cal2.setTime(date1S); // nam trong Shared
	 * Prefered if(month == cal2.get(Calendar.MONTH)){ return true; } } catch
	 * (ParseException e) { Log.e("TAG","Loi method ktYear "+ e.getMessage()); }
	 * 
	 * return false; }
	 */
	public List<String> selectTuan(int tuannext){
		/* Gia tri cua tuannext = -7 or -14=> ++7
		 * cach nhau 6 ngay =>  7 ngay
		 * Tra ve 3 gia tri string 
		 * array.get(0) : tuan 01/04 - 7/04
		 * array.get(1) : 2013-04-01 ngay dau tien cua thang
		 * array.get(2) : 2013-04-07 ngay cuoi cung cua thang
		 * 
		 */
		if(tuannext!=0){
			tuannext*=7;
		}

		List<String> arrayDate = new ArrayList<String>();
		Calendar cal = Calendar.getInstance();

		String date_first_of_week=null;
		String date_last_of_week=null;
		//cal.setFirstDayOfWeek(Calendar.MONDAY);

		/*
		Kiem tra ngay hien tai neu la ngay chu nhat thi -1 ngay de 
		ngay chu nhat thuoc vao tuan truoc. 
		=> neu ko -1 ngay no se lay ngay gan nhat cua tuan ke tiep (t2 tuan ke tiep) 
		cach 1:*/
		if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
			cal.add(Calendar.DAY_OF_WEEK,-1);
		}
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.add(Calendar.DAY_OF_WEEK,tuannext);
		Date firstDayOfTheWeek = cal.getTime();
		date_first_of_week = dateDatabase.format(firstDayOfTheWeek);
		String currentDateStr = datetTextView.format(firstDayOfTheWeek);

		currentDateStr += " - ";

		// cong them 6 ngay => ngay coui cua tuan
		cal.add(Calendar.DAY_OF_WEEK,6);
		Date lastDayOfTheWeek = cal.getTime();

		currentDateStr += datetTextView.format(lastDayOfTheWeek);
		date_last_of_week = dateDatabase.format(lastDayOfTheWeek);

		arrayDate.add(currentDateStr);
		arrayDate.add(date_first_of_week);
		arrayDate.add(date_last_of_week);

		return arrayDate;
	}
	public List<String> selectThang(int thangnext) {
		/*
		 * Tra ve 3 gia tri string 
		 * array.get(0) : thang 01/04 - 30/04
		 * array.get(1) : 2013-04-01 ngay dau tien cua thang
		 * array.get(0) : 2013-04-30 ngay cuoi cung cua thang
		 * 
		 */

		List<String> arrayDate = new ArrayList<String>();
		Calendar myCalendar = Calendar.getInstance();
		String date_first_of_month=null;
		String date_last_of_month=null;

		String select_Thang = null;

		//first date of month
		myCalendar.set(Calendar.DAY_OF_MONTH,1);
		myCalendar.add(Calendar.MONTH,thangnext);//them 1 thang toi
		select_Thang = String.valueOf(datetTextView.format(myCalendar.getTime()));
		date_first_of_month = String.valueOf(dateDatabase.format(myCalendar.getTime()));
		select_Thang += " - ";

		//last date of month
		myCalendar.add(Calendar.MONTH,1);
		myCalendar.set(Calendar.DAY_OF_MONTH,1);
		myCalendar.add(Calendar.DATE,-1);


		date_last_of_month = String.valueOf(dateDatabase.format(myCalendar.getTime()));
		select_Thang += String.valueOf(datetTextView.format(myCalendar.getTime()));

		arrayDate.add(select_Thang);
		arrayDate.add(date_first_of_month);
		arrayDate.add(date_last_of_month);
		return arrayDate;
	}
	/*
	 * Select Quy (3 thang)
	 * next quarter phai dung 2 method: 1. next_quarter() and 2. layData_Qurater
	 * previous_quarter cung thuc hien nhu next
	 */
	//@QuarterCurrent luon thay doi voi goc la quy hien tai
	public int[] next_quarter(int Year, int QuarterCurrent){
		if(QuarterCurrent==4){ // truong hop vuot qua nam hien tai
			QuarterCurrent = 1;
			Year += 1;
			int[] quyHientai= {Year,QuarterCurrent};
			return quyHientai;
		}
		QuarterCurrent += 1;
		int[] quyHientai= {Year,QuarterCurrent};
		return quyHientai;

	}
	public int[] previous_quarter(int Year, int QuarterCurrent){
		// goi layData_Qurater 
		if(QuarterCurrent==1){ // truong hop sau nam hien tai
			QuarterCurrent = 4;
			Year -= 1;
			int[] quyHientai= {Year,QuarterCurrent};
			return quyHientai;
		}
		QuarterCurrent -= 1;
		int[] quyHientai= {Year,QuarterCurrent};
		return quyHientai;
	}

	public List<String> layData_Qurater(int year,int Currentquarter){
		/*
		 * tra ve 1 mang 3 gia tri lan luot
		 * 0: ngay de setTextView
		 * 1: first date of Quarter= year-mounth-day (select data base)
		 * 2: last date
		 * 
		 */
		String first_Quarter,last_QuaDate;
		List<String> arrayString= new ArrayList<String>();

		switch(Currentquarter){

		case 1: {
			first_Quarter = year+"-01-01";
			last_QuaDate = year+"-03-31";
			//luu vao array
			arrayString.add("01/01 - 31/03");
			arrayString.add(first_Quarter);
			arrayString.add(last_QuaDate);
			break;
		}
		case 2: {
			first_Quarter = year+"-04-01";
			last_QuaDate = year+"-06-30";

			arrayString.add("01/04 - 30/06");
			arrayString.add(first_Quarter);
			arrayString.add(last_QuaDate);
			break;
		}
		case 3: {
			first_Quarter = year+"-07-01";
			last_QuaDate = year+"-09-30";

			arrayString.add("01/07 - 30/09");
			arrayString.add(first_Quarter);
			arrayString.add(last_QuaDate);
			break;
		}
		case 4: {
			first_Quarter = year+"-10-01";
			last_QuaDate =year+"-12-31";

			arrayString.add("01/10 - 31/12");
			arrayString.add(first_Quarter);
			arrayString.add(last_QuaDate);
			break;
		}
		default: {
			arrayString =null;
			break;
		}
		}
		return arrayString;
	}
	//ham thuc hien dau hien if select quy
	public int[] Quarter_of_Current(){
		/*
		 * tra ve 1 mang int gom 2 gia tri
		 * 0: nam hien tai
		 * 1: dang thuoc Quy nao ? (1, 2, 3,4)
		 */
		Calendar cal = Calendar.getInstance();

		Date firstDayOfTheWeek = cal.getTime();
		int year = cal.get(Calendar.YEAR);  

		int tQr = (firstDayOfTheWeek.getMonth() / 3) + 1;
		int[] currentDateStr={year,tQr};

		return currentDateStr;
	}

	////////ket thuc select Quy//////////////////
	//Dung cho Thay Doi Thang ThongKe Pie
	//thang hien tai thangnext = 0
	public List<String> thongKe_ThangPie(int thangnext) {
		/*
		 * Tra ve 2 gia tri Calendar 
		 * array.get(0) : 2013-04-01 ngay dau tien cua thang
		 * array.get(1) : 2013-04-30 ngay cuoi cung cua thang
		 */
		List<String> arrayDate = new ArrayList<String>();
		Calendar myCalendar = Calendar.getInstance();

		myCalendar.set(Calendar.DAY_OF_MONTH,1);
		myCalendar.add(Calendar.MONTH,thangnext);//them 1 thang toi
		String date_first_of_month = String.valueOf(datetTextView2.format(myCalendar.getTime()));
		arrayDate.add(date_first_of_month);
		
		//last date of month
		myCalendar.add(Calendar.MONTH,1);
		myCalendar.set(Calendar.DAY_OF_MONTH,1);
		myCalendar.add(Calendar.DATE,-1);
		String date_last_of_month = String.valueOf(datetTextView2.format(myCalendar.getTime()));
		arrayDate.add(date_last_of_month);

		return arrayDate;
	}


}
