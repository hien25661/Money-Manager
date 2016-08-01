package quickAction;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import activity_child.ThuChiActivity;
import activity_child.VayNo_Activity;
import activity_design.CustomizeDialog;
import activity_design.Dialog_DaTra;
import object.SoChiTieu_Obj;
import object.ThuChi;
import sochitieu.EntryAdapter;
import sochitieu.EntryItem;
import sochitieu.SelecTheLoaiAdapter;
import thongke.MonthAdapter;
import thongke.ThayDoi_ThongKe_PieActivity;
import util.CodeHeThong;
import util.LayDate_Month_Yeah;
import util.Simple_method;
import util.Variable;
import vayno_activity.QuaKhuDaTraActiviy;
import vayno_activity.SoNoChiTietActivity;

import com.moneylove.R;

import access_sql.Access_VayNo;
import access_sql.Database;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener; 
import android.widget.PopupWindow;
import application.MySharedPreference;

/**
 * QuickAction demo activity. 
 * 
 * This demo shows how to use quickaction, add items, setup listener for 
 * action item click and dismiss. It also shows how to implements quickaction on listview, get the
 * listview selected row and perform the action for each action item.
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class ChiTieuActivity extends Activity implements OnTabChangeListener, OnGestureListener {
	/*
	Code soChiTieu
	 */
	ListView listview,listTheloai;
	SoChiTieu_Obj values = null;
	Database db;
	Context context;
	/*
	 * @array_date mac dinh tra ve 3 gia tri theo quy uoc
	 * array.get(0) : thang 01/04 - 30/04
	 * array.get(1) : 2013-04-01 ngay dau tien cua thang
	 * array.get(0) : 2013-04-30 ngay cuoi cung cua thang
	 */
	List<String> array_date = new ArrayList<String>();
	TabHost tabs;
	private TextView tvEmptyData,tvEmptyDataTheLoai,tvDate_sothuchi,tvChi_sothuchi,tvVay_sothuchi,tvNo_sothuchi,tvThu_sothuchi,tvTong_sothuchi;
	private LinearLayout llayout_sochitieu;
	/*
	 * @checktLayoutTong dung cho @llayout_sochitieu 
	 * su kien ontouchListen
	 */
	private boolean checktLayoutTong = false;




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



	/**
	 * Listview selected row
	 */
	private int mSelectedRow = 0;

	/**
	 * Right arrow icon on each listview row
	 */
	private ImageView mMoreIv = null;

	private final int ID_Sua = 1;
	private final int ID_Xoa = 2;
	private final int ID_QuaKhu = 3;
	private final int ID_DaTra = 4;
	private final int ID_Call = 5;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sothuchi);
		intent = getIntent();

		//ConnectLayout lien ket code va xml
		//dung cho truot slide  vao connectLayout xem @gesturedetector
		ConnectLayout();

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

		/* ListView mList 			= (ListView) findViewById(R.id.l_list);

        NewQAAdapter adapter 	= new NewQAAdapter(this);

        final String[] data 	= {"Test 01", "Test 02", "Test 03", "Test 04", "Test 05", "Test 06", "Test 07", "Test 08",
        					   	  "Test 09", "Test 10"};

        adapter.setData(data);
        mList.setAdapter(adapter);*/
		/*
		 * Tao cac quickAction
		 */		
		//quickAction cho Thu,Chi
		ActionItem addItem 		= new ActionItem(ID_Sua, "Sửa", getResources().getDrawable(R.drawable.ic_quickaction_sua));
		ActionItem acceptItem 	= new ActionItem(ID_Xoa, "Xóa", getResources().getDrawable(R.drawable.ic_quickaction_remove));
		//quickAction cho VayNo
		ActionItem addVNdatra	= new ActionItem(ID_DaTra, "Đã Trả", getResources().getDrawable(R.drawable.ic_quickaction_datra));
		ActionItem suaVNquakhu 	= new ActionItem(ID_QuaKhu, "Quá Khứ", getResources().getDrawable(R.drawable.ic_quickaction_time));

		ActionItem Call = new ActionItem(ID_Call, "Gọi", getResources().getDrawable(R.drawable.ic_quickaction_call));
		/*
		 * Bien dung cho hien dialog QuickAction
		 * @mQuickAction bat buoc phai dc khoi tao
		 */
		final QuickAction mQuickAction 	= new QuickAction(this);
		final QuickAction mQuickAction2 	= new QuickAction(this);
		final QuickAction mQuickAction3 	= new QuickAction(this);

		//hien bang thuchi
		mQuickAction.addActionItem(addItem);
		mQuickAction.addActionItem(acceptItem);
		//hien bang vayno
		mQuickAction2.addActionItem(addVNdatra);
		mQuickAction2.addActionItem(suaVNquakhu);
		mQuickAction2.addActionItem(addItem);
		mQuickAction2.addActionItem(acceptItem);
		mQuickAction2.addActionItem(Call);
		//da tra hay da vay
		mQuickAction3.addActionItem(suaVNquakhu);
		mQuickAction3.addActionItem(acceptItem);


		//mQuickAction.addActionItem(addItem2);
		//setup the action item click listener
		//hien quickAction 2 nut thuc hien cac ham 
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				//ActionItem actionItem = quickAction.getActionItem(pos);
				//@mSelectedRow gan gia tri o #onItemClick tra ve vi tri row trong mang
				EntryItem item = (EntryItem) values.getArrayItem().get(mSelectedRow);
				if (actionId == ID_Sua) { //Truong hop Sua
					Intent intent2 = new Intent(ChiTieuActivity.this,ThuChiActivity.class);
					intent2.putExtra(Variable.request, Variable.requestcode_UpdateSua);
					intent2.putExtra(Variable.UPDATE_DATABASE + "id",item.getTc().getId());
					ChiTieuActivity.this.startActivityForResult(intent2, Variable.requestcode_UpdateSua);
					//Toast.makeText(getApplicationContext(), "Add item selected on row " + mSelectedRow, Toast.LENGTH_SHORT).show();
				}else{ // truong hop Xoa
					AlertDialog diaBox = makeAndShowDialogBox(item.getTc());
					diaBox.show();// mo diaBox
				}
			}
		});
		//hien quickAction 4 nut thuc hien cac ham sau
		mQuickAction2.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				//ActionItem actionItem = quickAction.getActionItem(pos);
				//@mSelectedRow gan gia tri o #onItemClick tra ve vi tri row trong mang
				EntryItem item = (EntryItem) values.getArrayItem().get(mSelectedRow);
				if (actionId == ID_Sua) { //Truong hop Sua
					Intent intent2 = new Intent(ChiTieuActivity.this,VayNo_Activity.class);
					intent2.putExtra(Variable.request, Variable.requestcode_UpdateSua);
					intent2.putExtra(Variable.UPDATE_DATABASE + "id",item.getTc().getId());
					ChiTieuActivity.this.startActivityForResult(intent2, Variable.requestcode_UpdateSua);
					//Toast.makeText(getApplicationContext(), "Add item selected on row " + mSelectedRow, Toast.LENGTH_SHORT).show();
				} else if(actionId == ID_Xoa){ //Truong hop Da tra
					AlertDialog diaBox = makeAndShowDialogBox(item.getTc());
					diaBox.show();// mo diaBox
					/*Toast.makeText(getApplicationContext(), actionItem.getTitle() + " item selected on row " 
							+ mSelectedRow, Toast.LENGTH_SHORT).show();*/
				} else if(actionId == ID_DaTra){ //Truong hop Da tra
					AlertDialog datraDialog = makeDialogDaTra(item.getTc());				
					datraDialog.show();
					/*Dialog dialogDaTra = new Dialog_DaTra(context,item.getTc());
					dialogDaTra.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
					//ham dung cho dialog ko bi nhay len tren khi co softkeyboard hien ra
					dialogDaTra.show();
						xuly_selectDatabase_theoTime();*/
					/*Toast.makeText(getApplicationContext(), actionItem.getTitle() + " item selected on row " 
							+ mSelectedRow, Toast.LENGTH_SHORT).show();*/
				}else if(actionId == ID_Call){
					CodeHeThong cd = new CodeHeThong();
					//Log.e("TAG", "So dien thoai la: "+ item.getTc().getTen_giao_dich().trim());
					String phone = cd.getSoDienThoai_fromNameContact(item.getTc().getTen_giao_dich(),ChiTieuActivity.this); 
					Log.e("TAG", "So dien thoai la: "+ phone );
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:"+phone));
					startActivity(callIntent);


				}
				else{ //Truong hop qua khu
					Intent intent2= new Intent(ChiTieuActivity.this,QuaKhuDaTraActiviy.class);
					intent2.putExtra("idQuaKhu", item.getTc().getId());
					ChiTieuActivity.this.startActivityForResult(intent2, Variable.requestcode_MoRong);
					/*	Toast.makeText(getApplicationContext(), actionItem.getTitle() + " item selected on row " 
							+ mSelectedRow, Toast.LENGTH_SHORT).show();*/
				}
			}
		});
		mQuickAction3.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				//ActionItem actionItem = quickAction.getActionItem(pos);
				//@mSelectedRow gan gia tri o #onItemClick tra ve vi tri row trong mang
				EntryItem item = (EntryItem) values.getArrayItem().get(mSelectedRow);
				if (actionId == ID_Xoa) { //Truong hop Sua
					AlertDialog diaBox = makeAndShowDialogBox(item.getTc());
					diaBox.show();// mo diaBox
				} else{ //Truong hop qua khu
					Intent intent2= new Intent(ChiTieuActivity.this,QuaKhuDaTraActiviy.class);
					intent2.putExtra("idQuaKhu", item.getTc().getId());
					ChiTieuActivity.this.startActivityForResult(intent2, Variable.requestcode_MoRong);
					/*	Toast.makeText(getApplicationContext(), actionItem.getTitle() + " item selected on row " 
							+ mSelectedRow, Toast.LENGTH_SHORT).show();*/
				}
			}
		});

		//setup on dismiss listener, set the icon back to normal
		mQuickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {			
			@Override
			public void onDismiss() {
				//mMoreIv.setImageResource(R.drawable.ic_list_more);
			}
		});
		mQuickAction2.setOnDismissListener(new PopupWindow.OnDismissListener() {			
			@Override
			public void onDismiss() {
				//mMoreIv.setImageResource(R.drawable.ic_list_more);
			}
		});
		mQuickAction3.setOnDismissListener(new PopupWindow.OnDismissListener() {			
			@Override
			public void onDismiss() {
				//mMoreIv.setImageResource(R.drawable.ic_list_more);
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				if(testSilde){ //co gia tri @values (ko co thi ko chay ham nay)
					if(selectedTab==1) {// kiem tra xem dang o Tab SoChiTieu:1 or Tab Theloai:2
						if (!values.getArrayItem().get(position).isSection()) {
							EntryItem item = (EntryItem) values.getArrayItem().get(position);
							//@mSelectedRow bo vao quickAction
							mSelectedRow = position; //set the selected row
							if(item.getTc().getMa_id().equals("thu") || item.getTc().getMa_id().equals("chi")){
								mQuickAction.show(view); 
							}else if(item.getTc().getMa_id().equals("vay") || item.getTc().getMa_id().equals("no")){
								mQuickAction2.show(view);
							}else{
								mQuickAction3.show(view);
							}
						}
					}				
				}
			}
		});
		listTheloai.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				mSelectedRow = position; //set the selected row
				if(testSilde){ //co gia tri @values (ko co thi ko chay ham nay)
					switch (selectedTab) {// kiem tra xem dang o Tab SoChiTieu:1 or Tab Theloai:2
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
					 * sau khi chay select_chitiet_TheLoai selectedTab =3 theloai chi co thu va chi => chi dung quickAction1
					 */
					case 3:{// truong hop dang o Tab 2 the loai chi tiet. Hien thong bao giong case 1
						if (!values.getArrayItem().get(position).isSection()) {
							mSelectedRow = position; //set the selected row
							mQuickAction.show(view); 
						}
						break;
					}
					}//close switch
				}//if silde
			}
		});	
	}//onCreate
	@Override
	public void onBackPressed() { // ham back
		super.onBackPressed();
		setResult(Activity.RESULT_CANCELED, intent);
		finish();
	}


	/*//Code chuyen mau
	 * 	//change the right arrow icon to selected state 
		mMoreIv = (ImageView) view.findViewById(R.id.i_more);
		mMoreIv.setImageResource(R.drawable.ic_list_more_selected);*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			/*Intent i = new Intent(this,ChiTieuActivity.class);
			startActivity(i);
			finish();*/
			ktTab_change_Date();
		}else{ //Activity.RESULT_CANCELED
			if(requestCode == Variable.requestcode_InsertThuChi){

			}else if(requestCode == Variable.requestcode_InsertVayNo){

			}else{

			}
		}
	}
	private AlertDialog makeAndShowDialogBox(final ThuChi vn) {
		AlertDialog myDialogBox = new AlertDialog.Builder(context)	
		// set message, title, and icon
		.setTitle("Warning").setMessage("Are you sure you want to delete \n [ "+ vn.getTen_giao_dich()+" ] ?")
		.setIcon(R.drawable.error)
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				if (vn.getMa_id().equals("thu") || vn.getMa_id().equals("chi")) {
					Database db = new Database(ChiTieuActivity.this);
					db.Delete_OneRowDatabase(vn.getId(),vn.getMa_id(),vn.getNgay_thu_chi(),vn.getSo_tien());
					/*
					 * Save lai variable Gobal
					 */
					SharedPreferences mySharedPreferences = context.getSharedPreferences(
							"SharedPreferences_MoneyLover", Activity.MODE_PRIVATE);
					new MySharedPreference().savePreferences(mySharedPreferences);

				} else {//vay no ko can save
					Access_VayNo dbVayNo = new Access_VayNo(context);
					dbVayNo.Delete_OneRowDatabase(vn.getId());
				}

				//goi lam sochitieu (ko can finish activity) chi can reset @values
				xuly_selectDatabase_theoTime();
				/*Intent intent2 = new Intent(context,Chi.class);
				context.startActivity(intent2);
				ChiTieuActivity.this.finish();*/
			}

		})// setPositiveButton

		.setNegativeButton("Không",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				// whatever should be done when answering "NO"
				// goes here
				// whichButton =-2
			}
		})// setNegativeButton

		.create();

		return myDialogBox;
	}
	private AlertDialog makeDialogDaTra(final ThuChi vn) {
		final EditText input = new EditText(this);
		input.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		input.setText(new Simple_method().KiemtraSoFloat_Int(vn.getSo_tien()));

		input.addTextChangedListener(new TextWatcher() {


			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					String chuoi = input.getText().toString().trim();
					//Toast.makeText(context,"editSotien"+ d,1).show();
					float sotienEdt = Float.valueOf(chuoi);
					if(sotienEdt>vn.getSo_tien()){
						//Toast.makeText(context,"Lỗi Nhập, Số tiền nhỏ hơn hoặc bằng " + new Simple_method().KiemtraSoFloat_Int(vn.getSo_tien()),1).show();
						input.setText(new Simple_method().KiemtraSoFloat_Int(vn.getSo_tien()));
					}
				} catch (Exception e) {
					//Toast.makeText(context,"Số tiền không được bỏ trống",1).show();
				}
			}
		});
		AlertDialog myDialogBox = new AlertDialog.Builder(context)	
		// set message, title, and icon
		.setMessage("Số tiền đã trả ")
		.setView(input)
		.setPositiveButton("Đồng ý",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				try {
					String d = input.getText().toString().trim();
					float sotienEdt = Float.valueOf(d);
					if(sotienEdt>0){
						new Access_VayNo(context).DaTraTienVayNo(vn, sotienEdt);
						/*
						 * Save lai variable Gobal

						SharedPreferences mySharedPreferences = context.getSharedPreferences(
								"SharedPreferences_MoneyLover", Activity.MODE_PRIVATE);
						new MySharedPreference().savePreferences(mySharedPreferences);*/
						//goi lam sochitieu (ko can finish activity) chi can reset @values
						xuly_selectDatabase_theoTime();
						/*Intent intent2 = new Intent(context,Chi.class);
						context.startActivity(intent2);
						ChiTieuActivity.this.finish();*/
					}
				} catch (Exception e) {
					Toast.makeText(context,"Số tiền không được bỏ trống",1).show();
				}
			}

		})// setPositiveButton

		.setNegativeButton("Không",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
			}
		})// setNegativeButton
		.create();
		return myDialogBox;
	}
	private void ConnectLayout(){
		context = this;
		gesturedetector = new GestureDetector(this,this);
		/*
		 * LinearLayout Tab
		 */
		// tab2 SoThuChi use slide left-->right and right-->left
		tab2 = (LinearLayout) findViewById(R.id.tab2);
		tab3 = (LinearLayout) findViewById(R.id.tab3);


		/*
		 * khoi tao textView, button
		 */
		tvTong_sothuchi  = (TextView) findViewById(R.id.tvTong_sothuchi);
		tvThu_sothuchi = (TextView) findViewById(R.id.tvThu_sothuchi);
		tvChi_sothuchi = (TextView) findViewById(R.id.tvChi_sothuchi);
		tvVay_sothuchi = (TextView) findViewById(R.id.tvVay_sothuchi);
		tvNo_sothuchi = (TextView) findViewById(R.id.tvNo_sothuchi);
		tvEmptyData = (TextView) findViewById(R.id.tvEmptyData);

		listview = (ListView) findViewById(R.id.listview_sothuchi);
		listTheloai = (ListView) findViewById(R.id.listTheLoai_sothuchi);
		tvEmptyDataTheLoai = (TextView) findViewById(R.id.tvEmptyDataTheLoai);


		llayout_sochitieu  = (LinearLayout) findViewById(R.id.llayout_sothuchi);
		llayout_sochitieu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(checktLayoutTong){
					tvTong_sothuchi.setVisibility(View.VISIBLE);// an list view
					checktLayoutTong = false;
				}else{
					tvTong_sothuchi.setVisibility(View.GONE);
					checktLayoutTong = true;
				}				
			}
		});

		tvDate_sothuchi = (TextView) findViewById(R.id.tvDate_sothuchi);
		btnThem_sochitieu = (Button) findViewById(R.id.btnThem_sochitieu);
		btnBack_sochitieu = (Button) findViewById(R.id.btnBack_sochitieu);
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


		/*
		 * Listen su kien click
		 */
		btnBack_sochitieu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED, intent);
				finish();
			}
		});
		btnThem_sochitieu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CustomizeDialog dialog = new CustomizeDialog(ChiTieuActivity.this);
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();			}
		});
		/*
		 * Khoi tao Tab
		 */
		tabs = (TabHost) findViewById(R.id.tabhost);
		tabs.setup();

		TabHost.TabSpec spec;
		// tab 1
		spec = tabs.newTabSpec("tag1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("Select time",getResources().getDrawable(R.drawable.sochitieu_thoigian));
		tabs.addTab(spec);
		// tab 2
		spec = tabs.newTabSpec("tag2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("Book Spending",getResources().getDrawable(R.drawable.sochitieu_center));
		tabs.addTab(spec);
		// tab 3
		spec = tabs.newTabSpec("tag3");
		spec.setContent(R.id.tab3);
		spec.setIndicator("Type",getResources().getDrawable(R.drawable.sochitieu_theloai));
		tabs.addTab(spec);
		// mac dinh tab giua => 1 (tag1)
		/*Tag
		 */
		tabs.setCurrentTab(1);
		tabs.setOnTabChangedListener(this);
		/*
		 * lang nghe su kien goi lai app khi su dung chuc nang goi doi tien (^-^)
		 * su dung code tai: http://www.mkyong.com/android/how-to-make-a-phone-call-in-android/
		 */
	/*	// add PhoneStateListener
		PhoneCallListener phoneListener = new PhoneCallListener();
		TelephonyManager telephonyManager = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);*/

	}
	/*
    Cac ham con phan tich
	 */
	//#ChonCheDoThoiGian khi co du 3 du lieu quay ve tra gia tri cho #xuly_selectDatabase_theoTime
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
	public void onTabChanged(String arg0) {
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
	Chon Tab1 Alert Dialog hien ra
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
						//chonCheDo_times = 4;
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
		//Toast.makeText(this, array_date.get(1), Toast.LENGTH_SHORT).show();
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
		tvTong_sothuchi.setText("0");
	}

	private void changeTextViewThuChiVayNo(SoChiTieu_Obj val) {
		Simple_method doi = new Simple_method();
		tvThu_sothuchi.setText(doi.KiemtraSoFloat_Int(val.getTongThu()));
		tvThu_sothuchi.setBackgroundColor(getResources().getColor(R.color.mauXanh_Thu));
		tvChi_sothuchi.setText(doi.Chi_KiemtraSoFloat_Int(val.getTongChi()));
		tvVay_sothuchi.setText(doi.KiemtraSoFloat_Int(val.getTongVay()));
		tvNo_sothuchi.setText(doi.KiemtraSoFloat_Int(val.getTongNo()));

		if(val.getTongThu() != 0 || val.getTongChi()!=0){
			float tong = val.getTongThu() - val.getTongChi();
			if(tong>0){
				tvTong_sothuchi.setText("Tổng tiền: "+doi.KiemtraSoFloat_Int(tong));
				tvTong_sothuchi.setBackgroundColor(getResources().getColor(R.color.mauXanh_Thu));
			}else{
				tvTong_sothuchi.setText("Tổng tiền: "+doi.KiemtraSoFloat_Int(tong));
				tvTong_sothuchi.setBackgroundColor(getResources().getColor(R.color.mauDo_Chi));
			}
		}else{
			tvTong_sothuchi.setText("Tổng tiền: 0");
			tvTong_sothuchi.setBackgroundColor(getResources().getColor(R.color.mauXanh_Thu));
		}




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


	/*
	 * Xu kien truot
	 */

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
	//chuc nang tuy chinh
	/*	
	 * Cac bien dung rieng cho dialog calendar

	List<String> mD_M_Y;
	List<String>chedoTuyChinhNgay;// lay ra tu day 2 gia tri tu ngay -> den ngay 
	boolean ktNgayTu_Den=true;
	private AlertDialog makeDialogCalendar() {

		LayoutInflater inflater = LayoutInflater.from(ChiTieuActivity.this);
		final View yourCustomView = inflater.inflate(R.layout.gridview_month, null);
		final GridView gridview = (GridView) yourCustomView.findViewById(R.id.gridview);
		final Button btn_pre = (Button) yourCustomView.findViewById(R.id.btn_pre);
		final Button btn_next = (Button) yourCustomView.findViewById(R.id.btn_next);
		final TextView tvDate = (TextView) yourCustomView.findViewById(R.id.tvDate);
		getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		final DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);

		btn_pre.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar dateHienTai = Calendar.getInstance();
				demNgay-=1;
				dateHienTai.add(Calendar.MONTH,demNgay);//them 1 thang toi
				MonthAdapter adapterCalendar = new MonthAdapter(getApplicationContext(),dateHienTai.get(Calendar.MONTH), dateHienTai.get(Calendar.YEAR), metrics);
				gridview.setAdapter(adapter);
				tvDate.setText(lay_date.dateformate.format(dateHienTai.getTime()));
				mD_M_Y = new ArrayList<String>();
				mD_M_Y = adapterCalendar.getmD_M_Y();
			}
		});

		btn_next.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar dateHienTai = Calendar.getInstance();
				demNgay+=1;
				dateHienTai.add(Calendar.MONTH,demNgay);//them 1 thang toi
				MonthAdapter adapter =new MonthAdapter(getApplicationContext(),dateHienTai.get(Calendar.MONTH), dateHienTai.get(Calendar.YEAR), metrics);
				gridview.setAdapter(adapter);
				tvDate.setText(lay_date.dateformate.format(dateHienTai.getTime()));
				mD_M_Y = new ArrayList<String>();
				mD_M_Y = adapter.getmD_M_Y();
			}
		});
		Calendar dateHienTai = Calendar.getInstance();
		tvDate.setText(lay_date.dateformate.format(dateHienTai.getTime()));
		MonthAdapter adapter =new MonthAdapter(getApplicationContext(),dateHienTai.get(Calendar.MONTH), dateHienTai.get(Calendar.YEAR), metrics);
		gridview.setAdapter(adapter);
		mD_M_Y = new ArrayList<String>();
		mD_M_Y = adapter.getmD_M_Y();

		gridview.setOnItemClickListener(new OnItemClickListener() {
			String tuNgay;
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//-7 title tu chu nhat den t7
				int chuyen = position -7;
				//String mItems = (String) a ;
				//Toast.makeText(getApplicationContext(), "Ngay chon la "+mItems ,Toast.LENGTH_LONG).show();

				if(ktNgayTu_Den){//@toiNgay
					tuNgay = mD_M_Y.get(chuyen);
					chedoTuyChinhNgay.add(tuNgay);
					Toast.makeText(getApplicationContext(), "Chọn từ ngày: "+mD_M_Y.get(chuyen)+" đến: ?",Toast.LENGTH_LONG).show();
					ktNgayTu_Den=false;
				}else{
					String date = mD_M_Y.get(chuyen);
					chedoTuyChinhNgay.add(date);
					Toast.makeText(getApplicationContext(), "",Toast.LENGTH_LONG).show();
					ktNgayTu_Den=true;
				}
			}
		});
		AlertDialog dialogbox = new AlertDialog.Builder(this)
		.setView(yourCustomView)
		//.setTitle("")
		.setPositiveButton("Xong", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		})
		.setNegativeButton("Hủy Bỏ",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

			}

		})
		.create();

		return dialogbox;
	}// close AlertDialog





	 * Alert Dialog Da tra 

	private AlertDialog VayNo_DialogUpdate_Datra() {

		 final EditText input = new EditText(context);
		 input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		AlertDialog myDialogBox = new AlertDialog.Builder(context)	
		// set message, title, and icon
		.setTitle("Lưu ý")
		.setView(input)
		.setPositiveButton(context.getString(R.string.luu),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				String tenNew =input.getEditableText().toString().trim();
				//Log.i("TAG","ten cu: " + tenNguoiGiaoDich + " " +tenNew);

				new Access_VayNo(context).UpdateTenNguoiVayNo(tenNguoiGiaoDich,tenNew);
				context.startActivity(intent);
				context.finish();
				dismiss();
			}
		})// setPositiveButton
		.setNegativeButton(context.getString(R.string.huybo),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
			dismiss();


			}
		})// setNegativeButton

		.create();

		return myDialogBox;
	}
	 */
	/*
	Ham goi lai app khi bat chuc nang goi => ket thuc viec goi se quay lai chuong trinh
	 */

	//monitor phone call activities
	/*private class PhoneCallListener extends PhoneStateListener {

		private boolean isPhoneCalling = false;

		String LOG_TAG = "LOGGING 123";

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {

			if (TelephonyManager.CALL_STATE_RINGING == state) {
				// phone ringing
				Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
			}

			if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
				// active
				Log.i(LOG_TAG, "OFFHOOK");

				isPhoneCalling = true;
			}

			if (TelephonyManager.CALL_STATE_IDLE == state) {
				// run when class initial and phone call ended, 
				// need detect flag from CALL_STATE_OFFHOOK
				Log.i(LOG_TAG, "IDLE");

				if (isPhoneCalling) {

					Log.i(LOG_TAG, "restart app");

					// restart app
					Intent i = getBaseContext().getPackageManager()
							.getLaunchIntentForPackage(
									getBaseContext().getPackageName());
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(i);

					isPhoneCalling = false;
				}

			}
		}
	}*/


}//close ChiTieuActiviy
