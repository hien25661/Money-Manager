package thongke;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MonthAdapter extends BaseAdapter {
	private GregorianCalendar mCalendar;
	private Calendar mCalendarToday;
	private Context mContext;
	private DisplayMetrics mDisplayMetrics;
	private List<String> mItems;
	private List<String> mD_M_Y;
	private int mMonth;
	private int mYear;
	private int mDaysShown;
	private int mDaysLastMonth;
	private int mDaysNextMonth;
	private int mTitleHeight, mDayHeight;
	private final String[] mDays = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
	private final int[] mDaysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
	
	public MonthAdapter(Context c, int month, int year, DisplayMetrics metrics) {
		mContext = c;
		mMonth = month;
		mYear = year;
		mCalendar = new GregorianCalendar(mYear, mMonth, 1);
		mCalendarToday = Calendar.getInstance();
		mDisplayMetrics = metrics;
		populateMonth();
	}
	
	/**
	 * @param date - null if day title (0 - dd / 1 - mm / 2 - yy)
	 * @param position - position in item list
	 * @param item - view for date
	 */
	protected void onDate(int[] date, int position, View item) {
	}
	private void datmonthyear(int day,int month,int year){
		mD_M_Y.add(String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year));
	}
	
	private void populateMonth() {
		mItems = new ArrayList<String>();	
		mD_M_Y = new ArrayList<String>();
		for (String day : mDays) {
			mItems.add(day);
			mDaysShown++;
		}
		int firstDay = getDay(mCalendar.get(Calendar.DAY_OF_WEEK));
		int prevDay;
		if (mMonth == 0)//neu thang 1 => lay thang 12
			prevDay = daysInMonth(11) - firstDay + 1;
		else
			prevDay = daysInMonth(mMonth - 1) - firstDay + 1;
		for (int i = 0; i < firstDay; i++) {
			mItems.add(String.valueOf(prevDay + i));
			mDaysLastMonth++;
			mDaysShown++;
		} 
		
		int daysInMonth = daysInMonth(mMonth);	
		for (int i = 1; i <= daysInMonth; i++) {
			mItems.add(String.valueOf(i));
			mDaysShown++;
		}
		
		mDaysNextMonth = 1;
		while (mDaysShown % 7 != 0) {
			mItems.add(String.valueOf(mDaysNextMonth));
			mDaysShown++;
			mDaysNextMonth++;
		}
		
		mTitleHeight = 50;
		int rows = (mDaysShown / 7);
		mDayHeight = (mDisplayMetrics.heightPixels - mTitleHeight 
				- (rows * 8) - getBarHeight()) / (rows - 1);
		//mDayHeight-= 60;
		
	}
	
	private int daysInMonth(int month) {
		int daysInMonth = mDaysInMonth[month];
		if (month == 1 && mCalendar.isLeapYear(mYear))
			daysInMonth++;
		return daysInMonth;
	}
	
	private int getBarHeight() {
		switch (mDisplayMetrics.densityDpi) {
		case DisplayMetrics.DENSITY_HIGH:
			return 48;
		case DisplayMetrics.DENSITY_MEDIUM:
			return 32;
		case DisplayMetrics.DENSITY_LOW:
			return 24;
		default:
			return 48;
		}
	}
	
	private int getDay(int day) {
		switch (day) {
		case Calendar.MONDAY:
			return 0;
		case Calendar.TUESDAY:
			return 1;
		case Calendar.WEDNESDAY:
			return 2;
		case Calendar.THURSDAY:
			return 3;
		case Calendar.FRIDAY:
			return 4;
		case Calendar.SATURDAY:
			return 5;
		case Calendar.SUNDAY:
			return 6;
		default:
			return 0;
		}
	}
	
	private boolean isToday(int day, int month, int year) {
		if (mCalendarToday.get(Calendar.MONTH) == month
				&& mCalendarToday.get(Calendar.YEAR) == year
				&& mCalendarToday.get(Calendar.DAY_OF_MONTH) == day) {
			return true;
		}
		return false;
	}
	
	private int[] getDate(int position) {
		int date[] = new int[3];
		if (position <= 6) {
			return null; // day names
		} else if (position <= mDaysLastMonth + 6) {
			// previous month
			date[0] = Integer.parseInt(mItems.get(position));
			if (mMonth == 0) {
				date[1] = 11;	
				date[2] = mYear - 1;
				datmonthyear(date[0],12,date[2]);
				
			} else {
				date[1] = mMonth - 1;
				date[2] = mYear;
				datmonthyear(date[0],(date[1]+1),date[2]);
			}
		} else if (position <= mDaysShown - mDaysNextMonth  ) {
			// current month
			date[0] = position - (mDaysLastMonth + 6);
			date[1] = mMonth;
			date[2] = mYear;
			datmonthyear(date[0],(date[1]+1),date[2]);
			
		} else {
			// next month
			date[0] = Integer.parseInt(mItems.get(position));
			if (mMonth == 11) {
				date[1] = 0;
				date[2] = mYear + 1;
				datmonthyear(date[0],(date[1]+1),date[2]);
			} else {
				date[1] = mMonth + 1;
				date[2] = mYear;
				datmonthyear(date[0],(date[1]+1),date[2]);
			}
		}
		return date;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final TextView view = new TextView(mContext);
		view.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		view.setText(mItems.get(position));
		view.setTextColor(Color.BLACK);
		
		int[] date = getDate(position);
		if (date != null) {
			view.setHeight(mDayHeight);
			if (date[1] != mMonth) {
				// previous or next month
				//view.setBackgroundColor(Color.rgb(234, 234, 250));
				view.setBackgroundColor(Color.rgb(205,197,191));
				view.setOnClickListener(null);
			} else {
				// current month
				view.setBackgroundColor(Color.rgb(244, 244, 244));
				if (isToday(date[0], date[1], date[2] )) {
					view.setTextColor(Color.RED);
				}
			}
		} else {
			//title  date Thus,Fine
			view.setOnClickListener(null);
			//view.setBackgroundColor(Color.argb(100, 10, 80, 255));
			view.setBackgroundColor(Color.rgb(50, 205, 50));
			view.setHeight(mTitleHeight);
			view.setTextColor(Color.WHITE);
		}
		
		onDate(date, position, view);
		return view;
	}
	
	public List<String> getmD_M_Y() {
		return mD_M_Y;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}

