package thongke;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.moneylove.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import access_sql.Database;
import util.LayDate_Month_Yeah;
import util.Variable;

public class ThayDoi_ThongKe_PieActivity  extends Activity implements OnClickListener{
	private Database db;
	private EditText editToi_Ngay, editTu_Ngay;
	private Button Back_thaydoi;
	private LayDate_Month_Yeah lay_ngay = new LayDate_Month_Yeah();
	// xu ly insert or update
	private Intent intent;
	boolean ktDateVay_or_Tra  = false; 
	String tuNgay,toiNgay,thu_Or_chi="chi";
	SimpleDateFormat dateTv = new SimpleDateFormat("dd/MM/yyyy");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thaydoi_thongke_pie);
		intent = getIntent();

		ConnectLayout();
		/*CalendarView calendarView=(CalendarView) findViewById(R.id.calendarView1);
		calendarView.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year, int month,
					int dayOfMonth) {
				Toast.makeText(getApplicationContext(), ""+dayOfMonth, 0).show();// TODO Auto-generated method stub

			}
		});*/
	}
	public void onRadioButtonClicked(View view) {

		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch(view.getId()) {
		case R.id.radioThu:
			if (checked){
				thu_Or_chi = "thu";
				//Toast.makeText(this, "You've selected: Thu", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.radioChi:
			if (checked){
				thu_Or_chi = "chi";
				//Toast.makeText(this, "You've selected: Chi", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
	private void ConnectLayout() {

		Back_thaydoi = (Button) findViewById(R.id.Back_thaydoi);
		Back_thaydoi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.putExtra(Variable.THU_or_CHI, thu_Or_chi);
				intent.putExtra("tuNgay",editTu_Ngay.getText().toString());
				intent.putExtra("toiNgay",editToi_Ngay.getText().toString());
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
		//lay du lieu ngay
		List<String> arrayDate = new ArrayList<String>();
		arrayDate = lay_ngay.thongKe_ThangPie(0); //lay thang hien tai
		/*fromCalendar = arrayDate.get(0);
		toCalendar = arrayDate.get(1);*/
		editToi_Ngay = (EditText) findViewById(R.id.editToi_Ngay);
		editTu_Ngay = (EditText) findViewById(R.id.editTu_Ngay);

		editTu_Ngay.setText(arrayDate.get(0));
		editToi_Ngay.setText(arrayDate.get(1));
		tuNgay= arrayDate.get(0);
		toiNgay = arrayDate.get(1);
		editTu_Ngay.setOnClickListener(this);
		editToi_Ngay.setOnClickListener(this);

		editTu_Ngay.setOnFocusChangeListener(null);
		editToi_Ngay.setOnFocusChangeListener(null);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.editTu_Ngay: {
			ktDateVay_or_Tra = false;
			AlertDialog dialog = makeAndShowDialogBox();
			dialog.setTitle("Từ Ngày");
			dialog.show();
			break;
		}
		case R.id.editToi_Ngay: {
			ktDateVay_or_Tra = true;
			AlertDialog dialog = makeAndShowDialogBox();
			dialog.setTitle("Tới Ngày");
			dialog.show();
			break;
		}
		}
	}
	int pressNext_Previous = 0;
	List<String> mD_M_Y;
	MonthAdapter adapter;
	private AlertDialog makeAndShowDialogBox() {
		LayoutInflater inflater = LayoutInflater.from(ThayDoi_ThongKe_PieActivity.this);
		final View yourCustomView = inflater.inflate(R.layout.gridview_month, null);
		final GridView gridview = (GridView) yourCustomView.findViewById(R.id.gridview);
		final Button btn_pre = (Button) yourCustomView.findViewById(R.id.btn_pre);
		final Button btn_next = (Button) yourCustomView.findViewById(R.id.btn_next);
		final TextView tvDate = (TextView) yourCustomView.findViewById(R.id.tvDate);
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		final DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
        //getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		final AlertDialog dialogbox = new AlertDialog.Builder(this)
		.setView(yourCustomView)
		//.setTitle("")
		.setNegativeButton("Hủy Bỏ",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}

		})
		.setPositiveButton("Done",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(ktDateVay_or_Tra){//@kt = true chay editToiNgay
					toiNgay = tvDate.getText().toString();
					editToi_Ngay.setText(toiNgay);
				}else{
					tuNgay = tvDate.getText().toString();
					editTu_Ngay.setText(tuNgay);
				}
			}

		}).create();			

		btn_pre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar dateHienTai = Calendar.getInstance();
				pressNext_Previous-=1;
				dateHienTai.add(Calendar.MONTH,pressNext_Previous);//them 1 thang toi
				adapter =new MonthAdapter(getApplicationContext(),dateHienTai.get(Calendar.MONTH), dateHienTai.get(Calendar.YEAR), metrics);
				gridview.setAdapter(adapter);
				tvDate.setText(dateTv.format(dateHienTai.getTime()));
				mD_M_Y = new ArrayList<String>();
				mD_M_Y = adapter.getmD_M_Y();
			}
		});

		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar dateHienTai = Calendar.getInstance();
				pressNext_Previous+=1;
				dateHienTai.add(Calendar.MONTH,pressNext_Previous);//them 1 thang toi
				adapter =new MonthAdapter(getApplicationContext(),dateHienTai.get(Calendar.MONTH), dateHienTai.get(Calendar.YEAR), metrics);
				gridview.setAdapter(adapter);
				tvDate.setText(dateTv.format(dateHienTai.getTime()));
				mD_M_Y = new ArrayList<String>();
				mD_M_Y = adapter.getmD_M_Y();
			}
		});
		Calendar dateHienTai = Calendar.getInstance();
		tvDate.setText(dateTv.format(dateHienTai.getTime()));
		adapter =new MonthAdapter(getApplicationContext(),dateHienTai.get(Calendar.MONTH), dateHienTai.get(Calendar.YEAR), metrics);
		gridview.setAdapter(adapter);
		mD_M_Y = new ArrayList<String>();
		mD_M_Y = adapter.getmD_M_Y();

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long arg3) {
				//Object a = new Object();
				//a = adapter.getItem(position);
				//-7 title tu chu nhat den t7
				int chuyen = position -7;
				((TextView) parent.getChildAt(chuyen)).setTextColor(Color.GREEN);
				//String mItems = (String) a ;
				//Toast.makeText(getApplicationContext(), "Ngay chon la "+mItems ,Toast.LENGTH_LONG).show();
				tvDate.setText(mD_M_Y.get(chuyen));
				if(ktDateVay_or_Tra){//@toiNgay
					String date = mD_M_Y.get(chuyen);
					toiNgay = tvDate.getText().toString();
					editToi_Ngay.setText(toiNgay);
					try {
						Date date1 = dateTv.parse(tuNgay);
						Date date2 = dateTv.parse(date);
						if(date2.before(date1)|| date2.equals(date1)){
							Toast.makeText(getApplicationContext(), "Error date To must after date From "+tuNgay,Toast.LENGTH_LONG).show();
							tvDate.setText(toiNgay);
							return;
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					tuNgay = tvDate.getText().toString();
					editTu_Ngay.setText(tuNgay);
				}
				dialogbox.dismiss();

			}
		});

		return dialogbox;
	}// close AlertDialog

}
