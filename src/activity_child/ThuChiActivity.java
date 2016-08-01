package activity_child;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import object.ThuChi;
import util.CodeHeThong;
import util.LayDate_Month_Yeah;
import util.Simple_method;
import util.SuaOrXoaBefore;
import util.Variable;
import access_sql.Database;
import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import application.MyApplication;
import application.MySharedPreference;

import com.calculator.Calculator_Activity;
import com.calculator.Calculator_Activity.OnMyDialogResult;
import com.moneylove.GridView_main;
import com.moneylove.R;

public class ThuChiActivity extends Activity implements OnClickListener {

	private Database db;
	private EditText so_tien, ghichu, ngay;
	private AutoCompleteTextView ten_giao_dich;
	private TextView tvTheLoai_thuchi,tvTitel_thuchi;
	private Button btnKetqua,Back_thuchi;
	private ImageView ivtheloai_Icon_thuchi;
	private LinearLayout linear;

	// xu ly insert or update
	private String thu_or_chi = null;
	private String updateData = null;
	private int requestCode;
	private Intent intent;

	private int POSITION_TheLoai;
	/*
	 * @kt_tenGD_auto dc dung khi chi chon the loai
	 * ko go vao ten_giao_dich
	 */
	private boolean kt_tenGD_auto= false;

	private boolean kt_InsertTheLoaichon = false; // kiem tra phan loai co dc
	// chon khi Insert (Chưa
	// Phan Loai)

	private ThuChi Update_tc; // dung cho ThaoTacCHiTieu goi Upadte

	LayDate_Month_Yeah lay_ngay = new LayDate_Month_Yeah();

	Calendar myCalendar = Calendar.getInstance();

	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			myCalendar.set(Calendar.YEAR, year);
			myCalendar.set(Calendar.MONTH, monthOfYear);
			myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			ngay.setText(lay_ngay.Covert_toNgayThangNam(myCalendar));
		}
	};// use: xu ly Ngay
	private void autoCompleteText(String thuhaychi){
		/*
		Lay danh sach giao dich, cho autoComplete
		*/
		 String[] contact = db.select_TenGiaoDich_autocomplet(thuhaychi);
		 if(contact == null){
			 return;
		 }else{
			 ArrayAdapter<String> adapter = 
			         new ArrayAdapter<String>(this,R.layout.autocomplete_textview,contact);

			ten_giao_dich.setThreshold(1);
			ten_giao_dich.setAdapter(adapter);
		 }
	}
	
	private void ConnectLayout() {
		Back_thuchi = (Button) findViewById(R.id.Back_thuchi);
		Back_thuchi.setOnClickListener(this);
		btnKetqua = (Button) findViewById(R.id.btnKetqua);
		ten_giao_dich = (AutoCompleteTextView) findViewById(R.id.autoCpleditTenGiaoDich);
		
		so_tien = (EditText) findViewById(R.id.edtSoTien);
		so_tien.setOnClickListener(this);
		so_tien.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					Calculator_Activity dialog =  new Calculator_Activity(ThuChiActivity.this, so_tien.getText().toString());
					dialog.setDialogResult(new OnMyDialogResult(){
					    public void finish(String result){
					    	so_tien.setText(result);
					    	//Log.e("TAG", "ket qua tra ve tu dialog calulator "+result);
					    }
					});
					dialog.setCanceledOnTouchOutside(true);
					dialog.show();
				}
			}
		});
		
		
		ngay = (EditText) findViewById(R.id.edtDate);
		tvTitel_thuchi = (TextView) findViewById(R.id.tvTitel_thuchi);
		ghichu = (EditText) findViewById(R.id.edtGhiChu);
		// theloai = (EditText) findViewById(R.id.edtTheLoai);

		tvTheLoai_thuchi = (TextView) findViewById(R.id.tvTheLoai_thuchi);
		ivtheloai_Icon_thuchi = (ImageView) findViewById(R.id.ivtheloai_Icon_thuchi);

		linear = (LinearLayout) findViewById(R.id.linearlayout_thuchi);
		/*
		 * lang nghe bat su kien
		 */
		linear.setOnClickListener(this);
		// Xu Ly edit Ngay

		ngay.setOnClickListener(this);
		btnKetqua.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thuchi);

		// khoi tao database
		db = new Database(this);

		ConnectLayout(); // dinh nghia cac textView, Button, bat su kien

		/*
		 * Nhan cac gia tri tu: GridView_main or ThaoTacChitieuActivity
		 */
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		requestCode = bundle.getInt(Variable.request);
		// thu hay chi chua 1 trong 2 gia tri 1 la Thu, 2 la Chi
		//insert
		if (requestCode == Variable.requestcode_InsertThuChi) {
			/*
			 * 
			 */
			thu_or_chi = bundle.getString(Variable.THU_or_CHI);
			autoCompleteText(thu_or_chi);
			tvTitel_thuchi.setText("Add Item");
			setTitle("Add Item");
			ngay.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						new DatePickerDialog(ThuChiActivity.this, d, myCalendar
								.get(Calendar.YEAR), myCalendar
								.get(Calendar.MONTH), myCalendar
								.get(Calendar.DAY_OF_MONTH)).show();
					}
				}
			});
			
			
			
			
			
			SimpleDateFormat dateformate = new SimpleDateFormat("dd/MM/yyyy");
			// lay ngay tu he thong => in ngay hien tai
			ngay.setText(dateformate.format(myCalendar.getTime()));
		} else {//update
			Update_tc = new ThuChi();
			int id_data = bundle.getInt(Variable.UPDATE_DATABASE + "id");

			Update_tc = db.FindThuChi_id(id_data);

			thu_or_chi = Update_tc.getMa_id(); // gan thu or chi vao bien
			autoCompleteText(thu_or_chi);
			tvTitel_thuchi.setText("Sửa khoản "+thu_or_chi);
			ten_giao_dich.setText(Update_tc.getTen_giao_dich());

			so_tien.setText(new Simple_method().KiemtraSoFloat_Int(Update_tc.getSo_tien()));
			ghichu.setText(Update_tc.getGhichu());

			String _ngay = lay_ngay.CovertString_toNgayThangNam(Update_tc.getNgay_thu_chi());
			ngay.setText(_ngay);
			// Log.i("TAG","UpdataActivity :"+ _ngay);

			POSITION_TheLoai = Update_tc.getTheloai();

			String _tvTheLoai;
			int _ivTheLoai;
			if (thu_or_chi.equals("thu")) {
				_tvTheLoai = Variable.INCOME_THELOAI[POSITION_TheLoai];
				_ivTheLoai = Variable.ICONS_INCOME_THELOAI[POSITION_TheLoai];
			} else {
				_tvTheLoai = Variable.Expense_THELOAI[POSITION_TheLoai];
				_ivTheLoai = Variable.ICONS_Expense_THELOAI[POSITION_TheLoai];
			}

			tvTheLoai_thuchi.setText(_tvTheLoai);
			ivtheloai_Icon_thuchi.setImageResource(_ivTheLoai);

		}

	}// close OnCreate

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String theloai_choose;
		try {

			if ((requestCode == Variable.requestcode)
					&& (resultCode == Activity.RESULT_OK)) {
				Bundle bundle = data.getExtras();
				POSITION_TheLoai = bundle.getInt(Variable.THELOAI);
				// thu
				if (thu_or_chi.equalsIgnoreCase("thu")) {
					ivtheloai_Icon_thuchi
					.setImageResource(Variable.ICONS_INCOME_THELOAI[POSITION_TheLoai]);
					theloai_choose = Variable.INCOME_THELOAI[POSITION_TheLoai];
				} else { // nguoc lai: CHI
					ivtheloai_Icon_thuchi
					.setImageResource(Variable.ICONS_Expense_THELOAI[POSITION_TheLoai]);
					theloai_choose = Variable.Expense_THELOAI[POSITION_TheLoai];
				}
				// thu or chi thi cung in ra tvTheLoai
				tvTheLoai_thuchi.setText(theloai_choose);
				// neu ten giao dich null thi setTen_Giao_Dich = the loai
				if(kt_tenGD_auto){
					ten_giao_dich.setText(theloai_choose);
				}
				if(ten_giao_dich.getText().toString().isEmpty()){
					ten_giao_dich.setText(theloai_choose);
					kt_tenGD_auto =true;
				}
				
				/*
				 * Insert btnKetQua kiem tra da chon Phan Loai Chua
				 */
				kt_InsertTheLoaichon = true;
			}
		} catch (Exception e) {
			tvTheLoai_thuchi.setText("Co van de" + requestCode + " "
					+ resultCode);

		}
	}// onActivityResult

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edtSoTien: {
			Calculator_Activity dialog =  new Calculator_Activity(this, so_tien.getText().toString());
			dialog.setDialogResult(new OnMyDialogResult(){
			    public void finish(String result){
			    	so_tien.setText(result);
			    	//Log.e("TAG", "ket qua tra ve tu dialog calulator "+result);
			    }
			});
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			break;
		}
		case R.id.edtDate: {
			new DatePickerDialog(ThuChiActivity.this, d,
					myCalendar.get(Calendar.YEAR),
					myCalendar.get(Calendar.MONTH),
					myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			break;
		}
		case R.id.linearlayout_thuchi: {
			Intent intentTheLoai = new Intent(ThuChiActivity.this,
					TheLoaiActivity.class);
			intentTheLoai.putExtra(Variable.THU_or_CHI, thu_or_chi);

			startActivityForResult(intentTheLoai, Variable.requestcode);
			break;
		}
		case R.id.Back_thuchi:{
			new CodeHeThong().hideSoftKeyBoard(this);
			setResult(Activity.RESULT_CANCELED, intent);
			finish();
			break;
		}
		case R.id.btnKetqua: {
			new CodeHeThong().hideSoftKeyBoard(this);
			boolean ktLoiUserNhap=false;
			// neu ko co loi nguoi Nhap = > tra ve true

			if (requestCode == Variable.requestcode_InsertThuChi) { 
				// ham insert
				ktLoiUserNhap =	insertThuChiActivity();
			} else { // ham update
				ktLoiUserNhap = updateThuChiActivity();
			}
			/*
			 * save sharedPreferences lai
			 */
			if(ktLoiUserNhap){
				SharedPreferences mySharedPreferences = getSharedPreferences(
						"SharedPreferences_MoneyLover", Activity.MODE_PRIVATE);
				new MySharedPreference().savePreferences(mySharedPreferences);

				//thoat quay ve activiy cu
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
			break;
		}// close case btnKetqua

		}// switch case

	}// onClick

	private AlertDialog makeAndShowDialogBox() {
		AlertDialog dialogbox = new AlertDialog.Builder(this)
		.setTitle("Error")
		.setMessage("Category forced to choose")
		.setPositiveButton("Select Type",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				Intent intentTheLoai = new Intent(
						ThuChiActivity.this,
						TheLoaiActivity.class);
				intentTheLoai.putExtra(Variable.THU_or_CHI,
						thu_or_chi);
				startActivityForResult(intentTheLoai,
						Variable.requestcode);
			}// onClick
		})// setPositiveButton

		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				// whatever should be done when answering "NO"
				// goes here
			}
		})// setNegativeButton

		.create();
		return dialogbox;
	}// close AlertDialog

	private boolean insertThuChiActivity() {
		float sotien;
		String ten_gd;
		ThuChi thuchi = new ThuChi();
		if (!kt_InsertTheLoaichon) {
			AlertDialog diaBox = makeAndShowDialogBox();
			diaBox.show();
			return false;
		} else {
			try {
				sotien = Float.valueOf(so_tien.getText().toString().trim());
				ten_gd = ten_giao_dich.getText().toString().trim();

				if(ten_gd.length()==0){
					Toast.makeText(getApplicationContext(), "Tên Giao Dịch không được bỏ trống", Toast.LENGTH_LONG).show();
					return false;
				}
				// Ma_id la cac: thu , chi
				thuchi.setMa_id(thu_or_chi);
				thuchi.setTen_giao_dich(ten_gd);
				thuchi.setSo_tien(sotien);

				// Xu ly ngay luu vao database SQLite
				// Dua vao Covert_toInsertDataBase 1 Calendar de chuyen doi hon
				// so voi dua vao String
				thuchi.setNgay_thu_chi(lay_ngay
						.Covert_toInsertDataBase(myCalendar));
				// //////////////////////
				thuchi.setGhichu(ghichu.getText().toString().trim());

				thuchi.setTheloai(POSITION_TheLoai);

				/*
				 * Log.i("TAG","Ngay giao dich: "+thuchi.getNgay_thu_chi());
				 * Log.i("TAG","The loai: "+thuchi.getTheloai());
				 */
				xulytinhtoan_THUCHI(thuchi.getMa_id(),
						thuchi.getNgay_thu_chi(), thuchi.getSo_tien());
				db.insertThuChi(thuchi);
				return true;
			} catch (Exception e) {
				Toast.makeText(this,"Khoản " + thu_or_chi
						+ " và số tiền không được phép bỏ trống",
						Toast.LENGTH_LONG).show();
			}
		}
		return false;
	}

	private void xulytinhtoan_THUCHI(String ma_id, String ngay_thu_chi,float so_tien2) {
		if (ma_id.equals("thu")) {
			if (ngay_thu_chi.equals(lay_ngay.NgayHienTai_THEO_InsertDataBase())) {
				// Log.i("TAG","Cung Ngay Insert" + MyApplication.homnay + " " +
				// MyApplication.tongtien);
				MyApplication.homnay += so_tien2;
				MyApplication.tongtien += so_tien2;
				MyApplication.thang += so_tien2;
				MyApplication.tuan += so_tien2;
				// Log.i("TAG","Cung Ngay Insert luc sau" + MyApplication.homnay
				// + " " + MyApplication.tongtien);

			} else { // khac nam thang ngay
				SimpleDateFormat dateDatabase = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance(); // thang nam hien tai
				// cal.get(Calendar.MONTH) = thang
				Calendar cal2 = Calendar.getInstance(); // thang nam cua
				// ngay_thu_chi
				/*
				 * int month = cal.get(Calendar.MONTH); // thang hien tai int
				 * year = cal.get(Calendar.YEAR); // int week_of_month =
				 * cal.get(Calendar.WEEK_OF_MONTH);
				 */
				try {
					cal2.setTime(dateDatabase.parse(ngay_thu_chi)); // chuyen
					// cal2
					// thanh
					// Date
					cal.setFirstDayOfWeek(Calendar.MONDAY); 
					
					// khac ngay hien tai => ko +voi MyApplication.homnay
					// luon + voi MyApplication.tongtien bat buoc
					MyApplication.tongtien += so_tien2;
					if (cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
						MyApplication.thang += so_tien2; // cung thang => +
						if (cal.get(Calendar.WEEK_OF_MONTH) == cal2
								.get(Calendar.WEEK_OF_MONTH)) {
							MyApplication.tuan += so_tien2;
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
			if (ngay_thu_chi.equals(lay_ngay.NgayHienTai_THEO_InsertDataBase())) {
				// Log.i("TAG","Cung Ngay Insert" + MyApplication.homnay + " " +
				// MyApplication.tongtien);
				MyApplication.homnay -= so_tien2;
				MyApplication.tongtien -= so_tien2;
				MyApplication.thang -= so_tien2;
				MyApplication.tuan -= so_tien2;
				// Log.i("TAG","Cung Ngay Insert luc sau" + MyApplication.homnay
				// + " " + MyApplication.tongtien);

			} else { // khac nam thang ngay
				SimpleDateFormat dateDatabase = new SimpleDateFormat(
						"yyyy-MM-dd");
				Calendar cal = Calendar.getInstance(); // thang nam hien tai
				// cal.get(Calendar.MONTH) = thang
				Calendar cal2 = Calendar.getInstance(); // thang nam cua
				// ngay_thu_chi
				/*
				 * int month = cal.get(Calendar.MONTH); // thang hien tai int
				 * year = cal.get(Calendar.YEAR); // int week_of_month =
				 * cal.get(Calendar.WEEK_OF_MONTH);
				 */
				try {
					cal2.setTime(dateDatabase.parse(ngay_thu_chi)); // chuyen
					// cal2
					// thanh
					// Date
					// khac ngay hien tai => ko +voi MyApplication.homnay
					// luon + voi MyApplication.tongtien bat buoc
					MyApplication.tongtien -= so_tien2;
					if (cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
						MyApplication.thang -= so_tien2; // cung thang => +
						if (cal.get(Calendar.WEEK_OF_MONTH) == cal2
								.get(Calendar.WEEK_OF_MONTH)) {
							MyApplication.tuan -= so_tien2;
						}
					}

				} catch (ParseException e) {
					Log.e("TAG", "Loi method xulytinhtoan_THUCHI ThuChiAcitviy"
							+ e.getMessage());
					cal2 = null;
				}

			}
		}

	}

	private boolean updateThuChiActivity() {


		float sotien_edit; //so tien trong editText
		String ten_gd,date;
		ten_gd = ten_giao_dich.getText().toString();
		if(ten_gd.length() == 0){
			Toast.makeText(this,"Khoản " + thu_or_chi
					+ " không được phép bỏ trống",
					Toast.LENGTH_LONG).show();
			return false;
		}else{
			try {
				sotien_edit = Float.valueOf(so_tien.getText().toString().trim());

				ten_gd = ten_giao_dich.getText().toString().trim();
				date = ngay.getText().toString();
				//neu sotien va ngay thang thay doi =>#suadulieu_BeforeUpdate
				//Log.e("TAG","Loi truoc suadulieu_BeforeUpdate");
				new SuaOrXoaBefore().suadulieu_BeforeUpdate(Update_tc.getMa_id(),Update_tc.getNgay_thu_chi(),Update_tc.getSo_tien(),
						date,sotien_edit);
				//Log.e("TAG","Loi sau suadulieu_BeforeUpdate");
				// ThuChi Update_tc nay da luu id phia tren
				Update_tc.setTen_giao_dich(ten_gd);
				Update_tc.setSo_tien(sotien_edit);

				// Xu ly ngay update vao database SQLite

				//Log.i("TAG","updateThuChiActivity ngay "+lay_ngay.setInsertDataBase(ngay.getText().toString()));
				Update_tc.setNgay_thu_chi(lay_ngay.setInsertDataBase(date));

				// //////////////////////
				Update_tc.setGhichu(ghichu.getText().toString().trim());
				Update_tc.setTheloai(POSITION_TheLoai);


				// thuc hien cong lai giong insert binh thuong danh Gobal
				xulytinhtoan_THUCHI(Update_tc.getMa_id(), Update_tc.getNgay_thu_chi(), Update_tc.getSo_tien());
				//Log.e("TAG","Loi cuoi suadulieu_BeforeUpdate");
				db.Update_OneRowDatabase(Update_tc);
				return true;
			} catch (Exception e) {
				Toast.makeText(this,"Khoản " + thu_or_chi
						+ " và số tiền không được phép bỏ trống",
						Toast.LENGTH_LONG).show();
				Log.e("TAG", "class.ThuChiActivity.updateThuChiActivity "+ e.getMessage());
			}
			return false;
		}


	}// close updateThuChiActivity

}// ThuChiActivity

