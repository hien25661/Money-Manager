package thongke;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.boxtimer365.moneylove.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class CalendarGridView extends Dialog implements OnItemClickListener{
	MonthAdapter adapter;
	List<String> mD_M_Y;
	Activity activity;
	
	public CalendarGridView(Context context2,Activity context) {
		super(context2);
		activity = context;
		//requset phai tren context
		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.gridview_month);
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
		
		
		
		final DisplayMetrics metrics = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		Calendar mCalendar = Calendar.getInstance();
		// set adapter
		GridView mGridView = (GridView)findViewById(R.id.gridview);
		 adapter =new MonthAdapter(context, mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.YEAR), metrics);
		mGridView.setAdapter(adapter);
		
		mGridView.setOnItemClickListener(this);
		//adapter.get
		mD_M_Y = new ArrayList<String>();
		mD_M_Y = adapter.getmD_M_Y();
	
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		Object a = new Object();
		a = adapter.getItem(position);
		//-7 title tu chu nhat den t7
		int chuyen = position -7;
				
		String mItems = (String) a ;
		//Toast.makeText(getApplicationContext(), "Ngay chon la "+mItems ,Toast.LENGTH_LONG).show();
		Toast.makeText(activity, "Ngay thang nam "+mD_M_Y.get(chuyen) ,Toast.LENGTH_LONG).show();
				
	}

}
