package sochitieu;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import object.SoChiTieu_Obj;
import android.view.GestureDetector;
import android.view.Window;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import util.LayDate_Month_Yeah;
import util.Simple_method;
import util.Variable;
import access_sql.Access_VayNo;
import access_sql.Database;
import activity_child.ThuChiActivity;
import activity_child.VayNo_Activity;
import activity_design.CustomizeDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import application.MySharedPreference;

import com.moneylove.R;
/*
 * Khoi dau code: doc onTabChanged()
 * @selectedTab = 2,3 cung tab the loai
 * @selectedTab =1 load all 2 table
 * createDialogBox khung chuan
 * 
 */
public class ChiTieuActivity_BanPhu extends Activity implements OnGestureListener,OnItemClickListener, OnClickListener, OnTabChangeListener {
	ListView listview,listTheloai;
	SoChiTieu_Obj values = null;
	Database db;
	/*
	 * @array_date mac dinh tra ve 3 gia tri theo quy uoc
	 * array.get(0) : thang 01/04 - 30/04
	 * array.get(1) : 2013-04-01 ngay dau tien cua thang
	 * array.get(0) : 2013-04-30 ngay cuoi cung cua thang
	 */
	List<String> array_date = new ArrayList<String>();
	TabHost tabs;
	private TextView tvEmptyData,tvEmptyDataTheLoai,tvDate_sothuchi,tvChi_sothuchi,tvVay_sothuchi,tvNo_sothuchi,tvThu_sothuchi;
	private LayDate_Month_Yeah lay_date = new LayDate_Month_Yeah();
	Button  btnBack_sochitieu,btnThem_sochitieu;
	private EntryAdapter adapter;
	private SelecTheLoaiAdapter adapterTheloai;
	LinearLayout tab2,tab3; //tab use listen OnGestureListener
	Intent intent; // luu intent quay ve Gridview
	/*
	 * @quyHienTai dc dung khi @chonCheDo_times len 3
	 * tra ve 1 mang int gom 2 gia tri
	 * 0: nam hien tai
	 * 1: dang thuoc Quy nao ? (1, 2, 3,4)
	 */
	private int[] quy_QuarterHienTai;
	/*
	@chon_thoi gian cac gia tri 
			0: ngay ;1:tuan
			2:thang ; 3:quy
	 */	
	private int chonCheDo_times=2; // mac dinh la thang
	/*@selectedTab 
	 * kiem tra xem dang chon Tab nao
	 * use onChangeTab
	 */
	private int selectedTab=1; // mac dinh khoi dau la Tab SoChiTieu

	int demNgay = 0; // khi an next day => cong 7 ngay vao
	private boolean testSilde=true; //use @testSilde kt co du lieu-> onItemClick (position)
	//ko co du lieu bo onItemClick
	/*
	 * chi dung khi o Tab 2
	 * @ma_idTab2_chitiet
	 * @theloai_Tab2_chitiet
	 * 2 bien use khi dang o Tab2 click vao chitiet 
	 * => luu 2 bien do de chay method select_chitiet_TheLoai() khi next or prevous
	 */
	private String ma_idTab2_chitiet;
	private int theloai_Tab2_chitiet;

	private GestureDetector gesturedetector = null;


	private void ConnectLayout(){
		/*
		 * LinearLayout Tab
		 */
		// tab2 SoThuChi use slide left-->right and right-->left
		tab2 = (LinearLayout) findViewById(R.id.tab2);
		tab3 = (LinearLayout) findViewById(R.id.tab3);


		/*
		 * khoi tao textView, button
		 */
		tvThu_sothuchi = (TextView) findViewById(R.id.tvThu_sothuchi);
		tvChi_sothuchi = (TextView) findViewById(R.id.tvChi_sothuchi);
		tvVay_sothuchi = (TextView) findViewById(R.id.tvVay_sothuchi);
		tvNo_sothuchi = (TextView) findViewById(R.id.tvNo_sothuchi);
		tvEmptyData = (TextView) findViewById(R.id.tvEmptyData);

		listview = (ListView) findViewById(R.id.listview_sothuchi);
		listTheloai = (ListView) findViewById(R.id.listTheLoai_sothuchi);
		tvEmptyDataTheLoai = (TextView) findViewById(R.id.tvEmptyDataTheLoai);

		tvDate_sothuchi = (TextView) findViewById(R.id.tvDate_sothuchi);
		btnThem_sochitieu = (Button) findViewById(R.id.btnThem_sochitieu);
		btnBack_sochitieu = (Button) findViewById(R.id.btnBack_sochitieu);

		/*
		 * Listen su kien click
		 */
		btnBack_sochitieu.setOnClickListener(this);
		btnThem_sochitieu.setOnClickListener(this);
		listview.setOnItemClickListener(this);
		listTheloai.setOnItemClickListener(this);		/*
		 * Khoi tao Tab
		 */
		tabs = (TabHost) findViewById(R.id.tabhost);
		tabs.setup();

		TabHost.TabSpec spec;
		// tab 1
		spec = tabs.newTabSpec("tag1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Chọn thời gian",getResources().getDrawable(R.drawable.sochitieu_thoigian));
		tabs.addTab(spec);
		// tab 2
		spec = tabs.newTabSpec("tag2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Sổ Chi Tiêu",getResources().getDrawable(R.drawable.sochitieu_center));
		tabs.addTab(spec);
		// tab 3
		spec = tabs.newTabSpec("tag3");
		spec.setContent(R.id.tab3);
		spec.setIndicator("Theo Thể Loại",getResources().getDrawable(R.drawable.sochitieu_theloai));
		tabs.addTab(spec);
		// mac dinh tab giua => 1 (tag1)
		/*Tag
		 */
		tabs.setCurrentTab(1);
		tabs.setOnTabChangedListener(this);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sothuchi);
		//ket noi giua textView, Button, LinearLayout
		intent = getIntent();
		ConnectLayout();

		//dung cho truot slide
		gesturedetector = new GestureDetector(this,this);

		tab2.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (gesturedetector.onTouchEvent(event)) { 
					//Log.i("TAG","Truot Tab slide");
					return true;
				} else { 
					//Log.i("TAG","Truot Tab slide 2");
					return false;
				}			
			}
		});		
		tab3.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (gesturedetector.onTouchEvent(event)) { 
					//Log.i("TAG","Truot Tab slide");
					return true;
				} else { 
					//Log.i("TAG","Truot Tab slide 2");
					return false;
				}			
			}
		});		

		listview.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (gesturedetector.onTouchEvent(event)) {
					//Log.i("TAG","Truot slide");
					//testSilde = true;
					return true;
				} else {
					//testSilde = false;
					//Log.i("TAG","Truot slide 2");
					return false;
				}
			}
		});

		listTheloai.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (gesturedetector.onTouchEvent(event)) {
					//Log.i("TAG","Truot slide");
					//testSilde = true;
					return true;
				} else {
					//testSilde = false;
					//Log.i("TAG","Truot slide 2");
					return false;
				}
			}
		});


		//array_date = lay_date.selectThang();
		array_date = ChonCheDoThoiGian(chonCheDo_times);
		/* 
		 * Tra ve 3 gia tri string 
		 * array.get(0) : thang 01/04 - 30/04
		 * array.get(1) : 2013-04-01 ngay dau tien cua thang
		 * array.get(0) : 2013-04-30 ngay cuoi cung cua thang
		 */
		//in ra man hinh 
		tvDate_sothuchi.setText(array_date.get(0));

		/*
		 * Dua du lieu tu database vao list view, bat su kien chon position
		 */
		db = new Database(this);
		xuly_selectDatabase_theoTime();
	}

	private List<String> ChonCheDoThoiGian(int i) {
		switch (i) {
		case 0:{
			return lay_date.selectNgay(demNgay);
		}
		case 1:{
			return lay_date.selectTuan(demNgay); 	//set tuan hien tai
		}
		case 2:{
			return lay_date.selectThang(demNgay);
		}
		case 3:{	//Quy 3 thang
			return lay_date.layData_Qurater(quy_QuarterHienTai[0], quy_QuarterHienTai[1]); // Quy hien tai
		}
		case 4:{ // tuy chinh ngay

		}
		}
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){

			Intent i = new Intent(this,ChiTieuActivity_BanPhu.class);
			startActivity(i);
			finish();
		}else{ //Activity.RESULT_CANCELED
			if(requestCode == Variable.requestcode_InsertThuChi){

			}else if(requestCode == Variable.requestcode_InsertVayNo){

			}else{

			}
		}
	}

	@Override
	protected void onResume() {
		//Log.i("TAG","onResume ChiTieuActivity dang cho AlertDialog");
		super.onResume();
	}

	@Override
	public void onBackPressed() { // ham back
		super.onBackPressed();
		setResult(Activity.RESULT_CANCELED, intent);
		finish();
	}

	// Chon positon trong list view
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,long id) {
		if(testSilde){ //co gia tri @values (ko co thi ko chay ham nay)
			switch (selectedTab) {// kiem tra xem dang o Tab SoChiTieu:1 or Tab Theloai:2
			case 1:{
				if (!values.getArrayItem().get(position).isSection()) {
					EntryItem item = (EntryItem) values.getArrayItem().get(position);
					CallDialog dialog = new CallDialog(this,this);
					dialog.setCanceledOnTouchOutside(true);
					dialog.setVariable_inDialog(item.getTc().getId(), item.getTc().getMa_id(),item.getTc().getSo_tien(),item.getTc().getNgay_thu_chi());
					dialog.show();
				}
				break;
			}
			case 2:{
				if (!values.getArrayItem().get(position).isSection()) {
					EntryItem item = (EntryItem) values.getArrayItem().get(position);
					ma_idTab2_chitiet = item.getTc().getMa_id();
					theloai_Tab2_chitiet = item.getTc().getTheloai();
					select_chitiet_TheLoai(ma_idTab2_chitiet,theloai_Tab2_chitiet);
				}
				break;
			}
			/*
			 * sau khi chay select_chitiet_TheLoai selectedTab =3
			 */
			case 3:{// truong hop dang o Tab 2 the loai chi tiet. Hien thong bao giong case 1
				if (!values.getArrayItem().get(position).isSection()) {
					EntryItem item = (EntryItem) values.getArrayItem().get(position);
					CallDialog dialog = new CallDialog(this,this);
					dialog.setCanceledOnTouchOutside(true);
					dialog.setVariable_inDialog(item.getTc().getId(), item.getTc().getMa_id(),item.getTc().getSo_tien(),item.getTc().getNgay_thu_chi());
					dialog.show();		
				}
				break;
			}
			}//close switch
		}// test if
	}
	private void select_chitiet_TheLoai(String ma_id,int theloai) {
		values = new SoChiTieu_Obj();
		switch (chonCheDo_times) {	// chay het vong switch => dc @array_date, values
		case 0:{
			array_date.clear(); //lam sach mang luu 3 gia tri ngay
			//lay du lieu ngay hien tai ra @array_date
			array_date =  ChonCheDoThoiGian(chonCheDo_times);
			//==0 la che do thoi gian: ngay
			values = db.select_chitiet_TheLoaiTheoNgay(array_date.get(1),ma_id,theloai);
			break;
		}
		case 3:{//Quy
			//array_date.clear ko dc dung 
			/*
			 * da xu ly o method nexView and previousView, AlerDialog
			 */
			values = db.select_chitiet_TheLoai(array_date.get(1),array_date.get(2),ma_id,theloai);
			break;
		}
		default:{
			array_date.clear(); //lam sach mang luu 3 gia tri ngay
			// khac che do theo Quy (Quy da xu lay @array_date) or Ngay
			//ChonCheDoThoiGian su dung  @demNgay
			array_date =  ChonCheDoThoiGian(chonCheDo_times);
			//Log.i("TAG","in Chitieu do du lieu "+array_date.get(1));
			// neu la che thang tuan quy tuyChinh
			values = db.select_chitiet_TheLoai(array_date.get(1),array_date.get(2),ma_id,theloai);
			break;
		}
		}//close switch


		tvDate_sothuchi.setText(array_date.get(0));// in textView

		//Log.i("TAG","in ChiTieuActivity so dem"+demTuan+ " " +array_date.get(1)+ " "+array_date.get(2));
		if (values == null) {
			listTheloai.setVisibility(View.GONE);// an list view
			tvEmptyDataTheLoai.setVisibility(LinearLayout.VISIBLE);
			//Log.i("TAG","in empty ChiTieuActivity");
			testSilde = false;	 //khoa onItemClick -> off
			changTextViewNull(); // textView hien Thu,Chi,Vay,No = 0
		} else {
			//Log.i("TAG","in ChiTieuActivity co du lieu" );
			tvEmptyDataTheLoai.setVisibility(LinearLayout.GONE);
			listTheloai.setVisibility(View.VISIBLE);// list view
			adapter =  new EntryAdapter(this, values.getArrayItem());
			adapter.notifyDataSetChanged();
			listTheloai.setAdapter(adapter);
			testSilde = true; //mo: onItemClick-> bac on
			/*
			 * thay doi textView
			 */
			changeTextViewThuChiVayNo(values);
			selectedTab =3; // @selectedTab =3 dang o Tab 2 (chi tiet The Loai)
		}		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBack_sochitieu: {
			//			Intent intent = new Intent(this,GridView_main.class);
			//			startActivity(intent);
			setResult(Activity.RESULT_CANCELED, intent);
			finish();
			break;
		}
		case R.id.btnThem_sochitieu: {
			CustomizeDialog dialog = new CustomizeDialog(this);
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
			break;
		}	

		}//switch
	}

	private void xuly_selectDatabase_theoTime() {
		values = new SoChiTieu_Obj();
		switch (chonCheDo_times) {	// chay het vong switch => dc @array_date, values
		case 0:{
			array_date.clear(); //lam sach mang luu 3 gia tri ngay
			//lay du lieu ngay hien tai ra @array_date
			array_date =  ChonCheDoThoiGian(chonCheDo_times);
			//==0 la che do thoi gian: ngay
			values = db.Ngay_select_AllTable(array_date.get(1));
			break;
		}
		case 3:{//Quy
			//array_date.clear ko dc dung 
			/*
			 * da xu ly o method nexView and previousView, AlerDialog
			 */
			values = db.select_List_AllThuChi_VayNo(array_date.get(1),array_date.get(2));
			break;
		}
		default:{
			array_date.clear(); //lam sach mang luu 3 gia tri ngay
			// khac che do theo Quy (Quy da xu lay @array_date) or Ngay
			//ChonCheDoThoiGian su dung  @demNgay
			array_date =  ChonCheDoThoiGian(chonCheDo_times);
			//Log.i("TAG","in Chitieu do du lieu "+array_date.get(1));
			// neu la che thang tuan quy tuyChinh
			values = db.select_List_AllThuChi_VayNo(array_date.get(1),array_date.get(2));
			break;
		}
		}//close switch
		tvDate_sothuchi.setText(array_date.get(0));// in textView

		//Log.i("TAG","in ChiTieuActivity so dem"+demTuan+ " " +array_date.get(1)+ " "+array_date.get(2));
		if (values == null) {
			listview.setVisibility(View.GONE);// an list view
			tvEmptyData.setVisibility(View.VISIBLE);
			//Log.i("TAG","in empty ChiTieuActivity");
			testSilde = false;	 //khoa onItemClick -> off
			changTextViewNull(); // textView hien Thu,Chi,Vay,No = 0
		} else {
			//Log.i("TAG","in ChiTieuActivity co du lieu" );
			tvEmptyData.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);// list view
			adapter =  new EntryAdapter(this, values.getArrayItem());
			adapter.notifyDataSetChanged();
			listview.setAdapter(adapter);
			testSilde = true; //mo: onItemClick-> bac on
			/*
			 * thay doi textView
			 */
			changeTextViewThuChiVayNo(values);
		}		
	}

	private void changTextViewNull() {
		tvThu_sothuchi.setText("0");
		tvChi_sothuchi.setText("0");
		tvVay_sothuchi.setText("0");
		tvNo_sothuchi.setText("0");

	}

	private void changeTextViewThuChiVayNo(SoChiTieu_Obj val) {
		Simple_method doi = new Simple_method();
		tvThu_sothuchi.setText(doi.KiemtraSoFloat_Int(val.getTongThu()));
		tvChi_sothuchi.setText(doi.Chi_KiemtraSoFloat_Int(val.getTongChi()));
		tvVay_sothuchi.setText(doi.KiemtraSoFloat_Int(val.getTongVay()));
		tvNo_sothuchi.setText(doi.KiemtraSoFloat_Int(val.getTongNo()));

	}
	/*
	 * Chon Tab
	 */
	@Override
	public void onTabChanged(String tagId) {
		//selectedTab = tabs.getCurrentTab(); 
		switch (tabs.getCurrentTab()) {
		case 0:{
			AlertDialog dialBox = createDialogBox();
			dialBox.show();
			//ket thuc dialog.show() load lai du lieu
			tabs.setCurrentTab(selectedTab);
			break;
		}
		case 1:{
			//code thay doi listTheloai => data load vao listview 
			xuly_selectDatabase_theoTime();
			selectedTab=1;
			break;
		}
		case 2:{
			//Log.i("TAG","Dang trong Tab 2");
			//code thay doi listTheloai => data load vao listview 
			selectTheoTheLoai();
			tabs.getCurrentTabView().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selectTheoTheLoai();
					tabs.setCurrentTab(2);
					//bat buoc gan gia tri @selectedTab =2 o day
					selectedTab =2; // an lai nhieu lan tab 2 

				}
			});
			selectedTab =2; //dieu chinh dang giong Tab1
			break;
		}
		}//close switch
	}
	/*
	 * Giong code Tab1 SoThuChi chi khac select theo TheLoai value = db.(THEO The Loai)
	 */
	private void selectTheoTheLoai() {
		values = new SoChiTieu_Obj();
		switch (chonCheDo_times) {	// chay het vong switch => dc @array_date, values
		case 0:{// select theo ngay
			array_date.clear(); //lam sach mang luu 3 gia tri ngay
			//lay du lieu ngay hien tai ra @array_date
			array_date =  ChonCheDoThoiGian(chonCheDo_times);
			//==0 la che do thoi gian: ngay
			values = db.select_TheLoai_TheoNgay(array_date.get(1));
			break;
		}
		case 3:{//Quy
			//array_date.clear ko dc dung 
			/*
			 * da xu ly o method nexView and previousView, AlerDialog
			 */
			values = db.select_TheLoai(array_date.get(1),array_date.get(2));
			break;
		}
		default:{
			array_date.clear(); //lam sach mang luu 3 gia tri ngay
			// khac che do theo Quy (Quy da xu lay @array_date) or Ngay
			//ChonCheDoThoiGian su dung  @demNgay
			array_date =  ChonCheDoThoiGian(chonCheDo_times);
			//Log.i("TAG","in Chitieu do du lieu "+array_date.get(1));
			// neu la che thang tuan quy tuyChinh
			values = db.select_TheLoai(array_date.get(1),array_date.get(2));
			break;
		}
		}//close switch


		tvDate_sothuchi.setText(array_date.get(0));// in textView

		//Log.i("TAG","in ChiTieuActivity so dem"+demTuan+ " " +array_date.get(1)+ " "+array_date.get(2));
		if (values == null) {
			listTheloai.setVisibility(View.GONE);// an list view
			tvEmptyDataTheLoai.setVisibility(View.VISIBLE);
			//Log.i("TAG","in selectTheoTheLoai empty ChiTieuActivity");
			testSilde = false;	 //khoa onItemClick -> off
			changTextViewNull(); // textView hien Thu,Chi,Vay,No = 0
		} else {
			//Log.i("TAG","in selectTheoTheLoai ChiTieuActivity co du lieu" );
			tvEmptyDataTheLoai.setVisibility(View.GONE);
			listTheloai.setVisibility(View.VISIBLE);// list view
			adapterTheloai =  new SelecTheLoaiAdapter(this, values.getArrayItem());
			adapterTheloai.notifyDataSetChanged();
			listTheloai.setAdapter(adapterTheloai);
			testSilde = true; //mo: onItemClick-> bac on
			/*
			 * thay doi textView
			 */
			changeTextViewThuChiVayNo(values);
		}		
	}
	/*
	Xay dung list view 1 AlertDialog Chon che do thoi gian
	 */

	private AlertDialog createDialogBox() {
		final String itemsTime[]= this.getResources().getStringArray(R.array.chedoTimes);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle(this.getString(R.string.chonchedoTime));
		dialog.setSingleChoiceItems(itemsTime,chonCheDo_times,new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int item) {
				switch (item) {
				case 0:{
					if(chonCheDo_times!=0){
						chonCheDo_times= 0;
						load_lai_thechedo();
					}
					break;
				}
				case 1:{
					if(chonCheDo_times!=1){
						chonCheDo_times= 1;
						//load lai gia tri 
						load_lai_thechedo();
					}
					break;
				}
				case 2:{
					if(chonCheDo_times!=2){
						chonCheDo_times=2;
						//load lai gia tri 
						load_lai_thechedo();
					}
					break;
				}
				case 3:{
					if(chonCheDo_times!=3){
						//@demNgay ko dc dung tiep => ve 0  use
						chonCheDo_times= 3;
						//@quy_QuarterHienTai dung lam goc de tinh toan
						quy_QuarterHienTai = lay_date.Quarter_of_Current();
						array_date.clear();
						//tra ve quy ke tiep or neu vuot qua Q4 thi cong year len
						array_date= lay_date.layData_Qurater(quy_QuarterHienTai[0], quy_QuarterHienTai[1]);
						//load lai gia tri 
						load_lai_thechedo();
					}
					break;
				}
				case 4:{
					if(chonCheDo_times!=4){
						chonCheDo_times = 4;
						/*	String dateTo;
						final boolean ktDate= true;
						final Calendar date_from = Calendar.getInstance(); // khoi tao calenadar @NgayGiaoDich
						final Calendar date_to=lay_date.getTommorrow();//@Ngayto
						DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int year, int monthOfYear,
									int dayOfMonth) {

								if (ktDate) {
									date_from.set(Calendar.YEAR, year);
									date_from.set(Calendar.MONTH, monthOfYear);
									date_from.set(Calendar.DAY_OF_MONTH, dayOfMonth);
									dateFrom = new LayDate_Month_Yeah().Covert_toInsertDataBase(date_from);
								} else {
									date_to.set(Calendar.YEAR, year);
									date_to.set(Calendar.MONTH, monthOfYear);
									date_to.set(Calendar.DAY_OF_MONTH, dayOfMonth);
								}
							}
						};// use: xu ly Ngay
						 */
					}
					break;
				}
				}//close switch
				dialog.dismiss();
			}//close onClick

			private void load_lai_thechedo() {
				// //load lai gia tri 
				if(selectedTab == 1){
					xuly_selectDatabase_theoTime();
				}else{// selectedTab = 3 or selectedTab =2 thi load lai 2
					selectTheoTheLoai();
					selectedTab =2;
				}

			}
		});
		//dialog.setItems(itemsTime, 
		dialog.setPositiveButton(this.getString(R.string.huybo), new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		return dialog.create();
	}


	/*
	 * su kien truot OnGestureListener
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (gesturedetector.onTouchEvent(event)) {
			testSilde = false;
			return true;
		} else {
			testSilde = true;
			return false;
		}
	}
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	private final int SWIPE_MIN_DISTANCE = 30;
	private  final int SWIPE_MAX_OFF_PATH = 250;
	private  final int SWIPE_THRESHOLD_VELOCITY = 200;
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		try {
			if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
				return false;
			// right to left swipe
			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				nextView();
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				previousView();
			}
		} catch (Exception e) {
			// nothing
		}
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
	/*
	 * thuc hien prevous tuan thang ke tiep, kiem tra @chonCheDo_times !=3 t
	 * kt tiep @selectedTab dang tab 1 => xuly_selectDatabase_theoTime() => nguoc lai
	 */
	private void previousView() {
		demNgay-=1; //cong dem len 7 => tuan ke tiep

		if(chonCheDo_times!=3){ // ko phai chon theo Quy 3 month
			ktTab_change_Date();

		}else{ //@chonCheDo_times==3    //@demNgay da ve 0 => next thi @demNgay + 1 +QuarterCurrent >4 thi tang nam quay lai Quy1
			array_date.clear();
			//tra ve quy ke tiep or neu vuot qua Q4 thi cong year len
			quy_QuarterHienTai = lay_date.previous_quarter(quy_QuarterHienTai[0], quy_QuarterHienTai[1]);
			array_date= lay_date.layData_Qurater(quy_QuarterHienTai[0], quy_QuarterHienTai[1]);
			ktTab_change_Date();
		}
	}
	/*
	 * thuc hien next tuan thang ke tiep, kiem tra @chonCheDo_times !=3 t
	 * kt tiep @selectedTab dang tab 1 => xuly_selectDatabase_theoTime() => nguoc lai
	 */
	private void nextView() {
		demNgay+=1; //cong dem len 7 => tuan ke tiep
		if(chonCheDo_times!=3){ // ko phai chon theo Quy 3 month
			ktTab_change_Date();
		}else{ //@chonCheDo_times==3    //@demNgay da ve 0 => next thi @demNgay + 1 +QuarterCurrent >4 thi tang nam quay lai Quy1
			array_date.clear();
			//tra ve quy ke tiep or neu vuot qua Q4 thi cong year len
			quy_QuarterHienTai = lay_date.next_quarter(quy_QuarterHienTai[0], quy_QuarterHienTai[1]);
			array_date= lay_date.layData_Qurater(quy_QuarterHienTai[0], quy_QuarterHienTai[1]);
			ktTab_change_Date();
		}//else
	}//close nextView

	private void ktTab_change_Date(){
		if(selectedTab==1){ //kiem tra xem dang o tab nao ? Tab1
			xuly_selectDatabase_theoTime();
		}else if(selectedTab ==2){// dang o Tab2 
			selectTheoTheLoai();
		}else{	// o Tab 2 phan chitiet
			select_chitiet_TheLoai(ma_idTab2_chitiet, theloai_Tab2_chitiet);
		}
	}

	//////////ket thuc su kien truot////////////////////////////
}//close ChiTieuActivity
class CallDialog extends Dialog implements OnClickListener {

	private Button btnSua, btnXoa, btnDaTra, btnQuaKhu, btnHuy;
	LinearLayout llayout;
	private Context context;
	private Activity activity2;
	/*
	 * cac bien id sotienInDatabase dateDatabase ma_id
	 * dc dung cho viec xoa
	 */
	private int id;
	private float sotienInDatabase;
	private String dateDatabase;
	private String ma_id;
	final Timer t = new Timer();
	public CallDialog(Activity activity,Context context) {
		//ko hien dang sau black
		super(context,R.style.customDialogStype);
		//@R.layout.thaotac_chitieu ko ngan cach giua dialog va man hinh
		this.activity2 = activity;
		this.context = context;

		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.thaotac_chitieu);

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

		btnSua = (Button) findViewById(R.id.btnSua_thaotac);
		btnSua.setOnClickListener(this);

		btnXoa = (Button) findViewById(R.id.btnXoa_thaotac);
		btnXoa.setOnClickListener(this);

		btnHuy = (Button) findViewById(R.id.btnHuyBo_thaotac);
		btnHuy.setOnClickListener(this);

		llayout = (LinearLayout) findViewById(R.id.llayoutVayNo);
		
		
       
		t.schedule(new TimerTask() {
            public void run() {
            	dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 2000); // after 2 second (or 2000 miliseconds), the task will be active.


	}

	public void setVariable_inDialog(int id,String ma_id,float sotienDatabase,String dateDatabase) {
		this.id =id;				
		this.ma_id =ma_id;
		this.sotienInDatabase =sotienDatabase;
		this.dateDatabase = dateDatabase;
		//makeAndShowDialogBox
		if (ma_id.equals("thu") || ma_id.equals("chi")) {
			/*
			 * Cac gia tri linearlayout LinearLayout.GONE ko hien khoang trang
			 * LinearLayout.Visible hien ra LinearLayout.Invisible hien khoang
			 * trang
			 */
			llayout.setVisibility(LinearLayout.GONE);// an linearlayout di

		} else {
			//	llayout.setVisibility(LinearLayout.VISIBLE);
			btnDaTra = (Button) findViewById(R.id.btnDaTra_giaodich);
			btnDaTra.setOnClickListener(this);
			btnQuaKhu = (Button) findViewById(R.id.btnQuaKhu_giaodich);
			btnQuaKhu.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		/** When OK Button is clicked, dismiss the dialog */
		switch (v.getId()) {
		case R.id.btnSua_thaotac: {
			Intent intent2;
			if (ma_id.equals("thu") || ma_id.equals("chi")) {
				intent2 = new Intent(context,ThuChiActivity.class);
			} else {
				intent2 = new Intent(context,VayNo_Activity.class);
			}

			intent2.putExtra(Variable.request, Variable.requestcode_UpdateSua);
			intent2.putExtra(Variable.UPDATE_DATABASE + "id", id);
			activity2.startActivityForResult(intent2, Variable.requestcode_UpdateSua);
			dismiss();
			break;
		}
		case R.id.btnXoa_thaotac: {
			AlertDialog diaBox = makeAndShowDialogBox();
			diaBox.show();// mo diaBox
			dismiss();
			break;
		}

		case R.id.btnHuyBo_thaotac: {
			cancel();
			break;
		}

		}// close switch

	}
	private AlertDialog makeAndShowDialogBox() {
		AlertDialog myDialogBox = new AlertDialog.Builder(context)	
		// set message, title, and icon
		.setTitle("Warning").setMessage("Are you sure you want to delete ")
		.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				// whatever should be done when answering "YES"
				// goes here
				// whichButton =-1
				if (ma_id.equals("thu") || ma_id.equals("chi")) {
					Database db = new Database(context);
					db.Delete_OneRowDatabase(id,ma_id,dateDatabase,sotienInDatabase);

				} else {
					Access_VayNo dbVayNo = new Access_VayNo(context);
					dbVayNo.Delete_OneRowDatabase(id);

				}
				/*
				 * Save lai variable Gobal
				 */
				SharedPreferences mySharedPreferences = context.getSharedPreferences(
						"SharedPreferences_MoneyLover", Activity.MODE_PRIVATE);
				new MySharedPreference().savePreferences(mySharedPreferences);

				Intent intent2 = new Intent(context,ChiTieuActivity_BanPhu.class);
				context.startActivity(intent2);
				dismiss();

			}

		})// setPositiveButton

		.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				// whatever should be done when answering "NO"
				// goes here
				// whichButton =-2
				dismiss();
			}
		})// setNegativeButton

		.create();

		return myDialogBox;
	}
	
}