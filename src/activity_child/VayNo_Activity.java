package activity_child;

import java.util.Calendar;

import object.ThuChi;

import com.boxtimer365.moneylove.GridView_main;
import com.calculator.Calculator_Activity;
import com.calculator.Calculator_Activity.OnMyDialogResult;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.boxtimer365.moneylove.R;

import sochitieu.ChiTieuActivity_BanPhu;
import util.CodeHeThong;
import util.LayDate_Month_Yeah;
import util.Simple_method;
import util.Variable;

import access_sql.Access_VayNo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import application.MyApplication;
import application.MySharedPreference;

public class VayNo_Activity extends Activity implements OnClickListener {

	private Access_VayNo db;
	EditText  so_tien, ghichu,NgayGiaoDich, NgayTra;
	AutoCompleteTextView ten_giao_dich;
	TextView  tvNguoiVayNo,tvTitel_vayno;
	Button btnLuu,Back_vayno;
	private String vay_or_no;
	//de xem nguoi dung click @NgayGiaoDich or @NgayTra
	private boolean ktDateVay_or_Tra = false; 
	LayDate_Month_Yeah lay_ngay = new LayDate_Month_Yeah();


	String ktTen_gd = null;
	// cac bien dung cho ThaoTacChiTieu
	//private boolean insert_or_update = false;
	private ThuChi Update_tc; // dung cho ThaoTacCHiTieu goi Update
	private Intent intent;
	private int requestCode;

	Calendar myCalendar = Calendar.getInstance(); // khoi tao calenadar @NgayGiaoDich
	Calendar tommorrow;//@NgayTra
	/*
	 * truoc moi click se gan gia tri true or false
	 * ktDateVay_or_Tra = true thi xu ly tommorrow
	 * else myCaledar
	 */	
	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			if (ktDateVay_or_Tra) {
				tommorrow.set(Calendar.YEAR, year);
				tommorrow.set(Calendar.MONTH, monthOfYear);
				tommorrow.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				NgayTra.setText(lay_ngay.Covert_toNgayThangNam(tommorrow));
			} else {
				myCalendar.set(Calendar.YEAR, year);
				myCalendar.set(Calendar.MONTH, monthOfYear);
				myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				NgayGiaoDich.setText(lay_ngay.Covert_toNgayThangNam(myCalendar));
			}
		}
	};// use: xu ly Ngay
	/*
	 * Ham lay danh sach contact name
	*/
	private String[] getContacts() {
		// Run query
		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME };
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + ("1") + "'";
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME+ " COLLATE LOCALIZED ASC";
		Cursor cursor = managedQuery(uri, projection, selection, selectionArgs, sortOrder);
		int g =cursor.getCount();
		String[] contact = new String[g];
		int i=0;
		while (cursor.moveToNext()) {
			String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
			contact[i]=displayName;
			i++;
		}
		return contact;

	}
	private void ConnectLayoutVayNo() {
		btnLuu = (Button) findViewById(R.id.btnAdd_vayno);
		btnLuu.setOnClickListener(this);
		Back_vayno = (Button) findViewById(R.id.Back_vayno);
		Back_vayno.setOnClickListener(this);
		tvTitel_vayno  = (TextView) findViewById(R.id.tvTitel_vayno);

		tvNguoiVayNo = (TextView) findViewById(R.id.tvNguoiVayNo_vayno);
		ten_giao_dich = (AutoCompleteTextView) findViewById(R.id.autoCplTenNguoiVayNo);
		//lay du lieu trong contact
		 String[] contact = getContacts();
			ArrayAdapter<String> adapter = 
		         new ArrayAdapter<String>(this,R.layout.autocomplete_textview,contact);

		ten_giao_dich.setThreshold(1);
		ten_giao_dich.setAdapter(adapter);
		
		so_tien = (EditText) findViewById(R.id.edtSoTien_vayno);
		so_tien.setOnClickListener(this);
		so_tien.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					Calculator_Activity dialog =  new Calculator_Activity(VayNo_Activity.this, so_tien.getText().toString());
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
		NgayGiaoDich = (EditText) findViewById(R.id.tvDateVay_vayno);
		NgayGiaoDich.setOnClickListener(this);
		NgayTra = (EditText) findViewById(R.id.tvDateTra_vayno);
		NgayTra.setOnClickListener(this);
		ghichu = (EditText) findViewById(R.id.edtGhiChu_vayno);

	}
	AdView adView;
	InterstitialAd interstitial;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vay_no);
		adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		// Prepare the Interstitial Ad
		interstitial = new InterstitialAd(this);
		// Insert the Ad Unit ID
		interstitial.setAdUnitId(getResources().getString(R.string.ads_id_interstis));
		// Request for Ads
		adRequest = new AdRequest.Builder().build();
		// Load ads into Interstitial Ads
		interstitial.loadAd(adRequest);
		interstitial.setAdListener(new AdListener() {
			// Listen for when user closes ad
			public void onAdClosed() {
			}
		});
		// khoi tao Access_VayNo
		db = new Access_VayNo(this);

		ConnectLayoutVayNo();
		//Log.i("TAG", "Qua dc setContent");
		/*
		 * Nhan cac gia tri GiaoDichActivity Send (vay or no)
		 */
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		// thu hay chi chua 1 trong 2 gia tri 1 la vay, 2 la no
		requestCode = bundle.getInt(Variable.request);
		//updateData = bundle.getString(Variable.UPDATE_DATABASE + "string");
		if (requestCode == Variable.requestcode_InsertVayNo) {
			/*
			 * 
			 */
			vay_or_no = bundle.getString(Variable.VAY_or_NO);
			tvTitel_vayno.setText("Add Item");
			setTextTvNguoiVayNo();	//tv Nguoi Vay or Nguoi Cho Vay

			tommorrow = lay_ngay.getTommorrow();
			NgayGiaoDich.setText(lay_ngay.NgayThangNamCurrent());

			NgayTra.setText(lay_ngay.NgayThangNamTomorrow());

			NgayGiaoDich.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						ktDateVay_or_Tra = false;
						new DatePickerDialog(VayNo_Activity.this, d, myCalendar
								.get(Calendar.YEAR), myCalendar
								.get(Calendar.MONTH), myCalendar
								.get(Calendar.DAY_OF_MONTH)).show();

					}
				}
			});

			NgayTra.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						ktDateVay_or_Tra = true;
						new DatePickerDialog(VayNo_Activity.this, d, tommorrow
								.get(Calendar.YEAR), tommorrow
								.get(Calendar.MONTH), tommorrow
								.get(Calendar.DAY_OF_MONTH)).show();
					}
				}
			});
			// Xu Ly Ngay

			// lay ngay tu he thong lan dau khoi tao


			if(vay_or_no.equals("no")){
				tvTitel_vayno.setText("Add debts");
			}else{
				tvTitel_vayno.setText("Add loan");
			}
		}
		else{
			Update_tc = new ThuChi();
			// tra ve so id
			int id_data = bundle.getInt(Variable.UPDATE_DATABASE + "id");
			// tim kiem theo id

			Update_tc = db.Find_id_VayNo_id(id_data);
			// da luu id va ma id
			vay_or_no = Update_tc.getMa_id();

			setTextTvNguoiVayNo();

			if (vay_or_no.equals("no")) {
				setTitle("Edit Debts");
				tvTitel_vayno.setText("Edit Debts");

			} else {
				setTitle("Edit Loan");
				tvTitel_vayno.setText("Edit Loan");
			}

			ten_giao_dich.setText(Update_tc.getTen_giao_dich());

			so_tien.setText(new Simple_method().KiemtraSoFloat_Int(Update_tc.getSo_tien()));
			ghichu.setText(Update_tc.getGhichu());

			String _ngay = lay_ngay.CovertString_toNgayThangNam(Update_tc.getNgay_thu_chi());
			NgayGiaoDich.setText(_ngay);
			// dat lai @myCalendar khi click vao dialog hien dung ngay theo database
			myCalendar = lay_ngay.Calendar_toStringDate(Update_tc.getNgay_thu_chi());
			//dung lai bien @_ngay
			_ngay = new String();
			_ngay = lay_ngay.CovertString_toNgayThangNam(Update_tc.getNgay_vay_no());
			NgayTra.setText(_ngay);
			tommorrow = lay_ngay.Calendar_toStringDate(Update_tc.getNgay_vay_no());
		}

	}// close OnCreate

	private void setTextTvNguoiVayNo() {
		if (vay_or_no.equalsIgnoreCase("vay")) {
			ktTen_gd = "Borrower";
			tvNguoiVayNo.setText(ktTen_gd);
		} else {
			ktTen_gd = "Lender";
			tvNguoiVayNo.setText(ktTen_gd);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}// onActivityResult

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edtSoTien_vayno: {
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
		case R.id.tvDateVay_vayno: {
			ktDateVay_or_Tra = false;
			new DatePickerDialog(VayNo_Activity.this, d,
					myCalendar.get(Calendar.YEAR),
					myCalendar.get(Calendar.MONTH),
					myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			break;
		}
		case R.id.tvDateTra_vayno: {
			ktDateVay_or_Tra = true;
			new DatePickerDialog(VayNo_Activity.this, d,
					tommorrow.get(Calendar.YEAR),
					tommorrow.get(Calendar.MONTH),
					tommorrow.get(Calendar.DAY_OF_MONTH)).show();

			break;
		}
		case R.id.btnAdd_vayno: {
			//tat ban phim ao 
			new CodeHeThong().hideSoftKeyBoard(this);

			boolean ktLoiUserNhap=false;
			// neu ko co loi nguoi Nhap = > tra ve true
			if (requestCode == Variable.requestcode_InsertVayNo) {
				ktLoiUserNhap = insertVayNoActivity();

			} else {
				ktLoiUserNhap = updateVAyNoActivity();
			}
			if(ktLoiUserNhap){
				/*	SharedPreferences mySharedPreferences = getSharedPreferences(
						"SharedPreferences_MoneyLover", Activity.MODE_PRIVATE);
				new MySharedPreference().savePreferences(mySharedPreferences);
				 */setResult(Activity.RESULT_OK,intent);
				 finish();
			}
			break;
		}
		case R.id.Back_vayno: {
			onBackPressed();
			break;
		}


		}//swich

	}// onClick

	private AlertDialog makeAndShowDialogBox() {
		AlertDialog dialogbox = new AlertDialog.Builder(this)
		.setMessage("Cannot be empty \n " + ktTen_gd + "\n Amount of money")
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			}
		}).create();
		return dialogbox;
	}// close AlertDialog
	// ///////////////////////////////////////////////////////////////////////

	private boolean insertVayNoActivity() {
		String ten_gd;
		float sotien;
		ten_gd = ten_giao_dich.getText().toString().trim();
		if (ten_gd.length() == 0) {
			AlertDialog diaBox = makeAndShowDialogBox();
			diaBox.show();
			return false;
		}else{ 
			try {
				sotien = Float.valueOf(so_tien.getText().toString().trim().replace(",", ""));
				ThuChi vn = new ThuChi();
				// Lay cac ky tu T:Thu , C : Chi , N :no ,V : Vay
				vn.setMa_id(vay_or_no);
				vn.setTen_giao_dich(ten_gd);
				vn.setSo_tien(sotien);
				// Xu ly ngay luu vao database SQLite
				vn.setNgay_thu_chi(lay_ngay.setInsertDataBase(NgayGiaoDich.getText().toString()));
				vn.setNgay_vay_no(lay_ngay.setInsertDataBase(NgayTra.getText().toString()));
				// Log.i("TAG","Ngay Thu Chi: "+ thuchi.getNgay_thu_chi() +
				// " Ngay vay mp: "+ thuchi.getNgay_vay_no());
				// //////////////////////
				vn.setGhichu(ghichu.getText().toString().trim());
				db.insertVayNo(vn);

				return true;
			} catch (Exception e) {
				// Toast.makeText(this, "Bạn chưa nhập đầy đủ " +e.getMessage(),
				// Toast.LENGTH_LONG).show();
				if(vay_or_no.equals("no")){
					Toast.makeText(this,"Amount of debt is not allowed vacated",
							Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(this,"Amount not allowed vacated",
							Toast.LENGTH_LONG).show();
				}
			}
		}
		return false;
	}
	private boolean updateVAyNoActivity() {
		float sotien;
		String ten_gd;
		ten_gd = ten_giao_dich.getText().toString().trim();
		if (ten_gd.length() == 0) {
			AlertDialog diaBox = makeAndShowDialogBox();
			diaBox.show();
			return false;
		}else{ 
			try {
				sotien = Float.valueOf(so_tien.getText().toString().trim().replace(",", ""));
				//select tong tien da tra neu sotien nhap vao < hon so tien trong QuaKhu
				//=> bao loi
				float sotienQuaKhu = db.selectTongSoTienDaTra(Update_tc.getId());
				if(sotien < sotienQuaKhu){//so tien phai lon hon so tien da nhan

					Toast.makeText(getApplicationContext(),"Amount must be greater than the proceeds \n Total:"+ 
							new Simple_method().KiemtraSoFloat_Int(sotienQuaKhu)+" ", Toast.LENGTH_LONG).show();
					return false;
				}
				//Update_tc.getSo_tien() van dang luu gia tri trong database
				// onCreat da luu ma Id : Update_tc.setMa_id
				Update_tc.setTen_giao_dich(ten_gd);
				Update_tc.setSo_tien(sotien);

				// Xu ly ngay update vao database SQLite
				myCalendar.setTime(lay_ngay.CovertToDate_NgayThangNam(NgayGiaoDich
						.getText().toString()));
				tommorrow.setTime(lay_ngay.CovertToDate_NgayThangNam(NgayGiaoDich
						.getText().toString()));

				Update_tc.setNgay_thu_chi(lay_ngay.setInsertDataBase(NgayGiaoDich.getText().toString()));
				Update_tc.setNgay_vay_no(lay_ngay.setInsertDataBase(NgayTra.getText().toString()));

				// //////////////////////
				Update_tc.setGhichu(ghichu.getText().toString().trim());
				db.Update_OneRowDatabase(Update_tc);

				return true;
			} catch (Exception e) {
				if(vay_or_no.equals("no")){
					Toast.makeText(this,"Amount of debt is not allowed vacated",
							Toast.LENGTH_LONG).show();
				}
				else{
					Toast.makeText(this,"Amount not allowed vacated",
							Toast.LENGTH_LONG).show();
				}
			}
		}
		return false;
	}// close updateThuChiActivity
	/*
	 * muc dich ham suadulieu_beforeUpdateVayNo
	 * neu so tien trong database va edit giong nhau thi ko lam gi ca
	 * nguoi lai so tien database != sotienEdi 
	 * thuc hien lay bien @Gobal - so tien ra truoc khi xulitinhtoan_VayNo
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		new CodeHeThong().hideSoftKeyBoard(this);
		setResult(Activity.RESULT_CANCELED,intent);
		overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
		finish();
	}
}// VayNo_Activity

