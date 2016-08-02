package vayno_activity;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.boxtimer365.moneylove.R;

import object.SoChiTieu_Obj;
import object.ThuChi;
import quickAction.ActionItem;
import quickAction.ChiTieuActivity;
import quickAction.QuickAction;

import sochitieu.EntryAdapter;
import sochitieu.EntryItem;
import util.Simple_method;
import util.Variable;
import access_sql.Access_VayNo;
import access_sql.Database;
import activity_child.VayNo_Activity;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import application.MySharedPreference;

public class SoNoChiTietActivity extends Activity{

	private TextView tvVay_sono,tvNo_sono,emtydata,tvTenNguoiVayNo_sono;
	private Access_VayNo db;
	private EntryAdapter adapter;
	private Intent intent;
	private ListView listview_sono;
	private Button btnBack_sono;
	private SoChiTieu_Obj values;
	private Activity activity;
	private String tengiaodich;
	private QuickAction mQuickAction;
	private final int ID_Sua = 1;
	private final int ID_Xoa = 2;
	private final int ID_QuaKhu = 3;
	private final int ID_DaTra = 4;
	private int mSelectedRow; // bien dung nhan gia tri position when click

	private void ConnetLayout() {
		activity = this;
		tvVay_sono = (TextView) findViewById(R.id.tvVay_sono);
		tvNo_sono = (TextView) findViewById(R.id.tvNo_sono);
		emtydata = (TextView) findViewById(R.id.tvEmpty_sono);
		tvTenNguoiVayNo_sono=(TextView) findViewById(R.id.tvTenNguoiVayNo_sono);
		listview_sono = (ListView) findViewById(R.id.listview_sono);

		btnBack_sono = (Button) findViewById(R.id.btnBack_sono);

		/*
		 * Listen su kien click
		 */
		btnBack_sono.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		listview_sono.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				mSelectedRow = position; //set the selected row	
				mQuickAction.show(view);
			}
		});
	}
	private void changTextViewNull() {
		tvVay_sono.setText("0");
		tvNo_sono.setText("0");
	}
	private void changeTextViewThuChiVayNo(SoChiTieu_Obj val) {
		Simple_method doi = new Simple_method();
		tvVay_sono.setText(doi.KiemtraSoFloat_Int(val.getTongVay()));
		tvNo_sono.setText(doi.KiemtraSoFloat_Int(val.getTongNo()));

	}
	private void truyxuatDatabase() {
		values = db.getList_NguoiVayNo_chitiet(tengiaodich);

		if(values == null){
			listview_sono.setVisibility(View.GONE); //an listview
			emtydata.setVisibility(View.VISIBLE);
			changTextViewNull();
		}else{
			emtydata.setVisibility(View.GONE);// an textview
			listview_sono.setVisibility(View.VISIBLE);
			adapter =  new EntryAdapter(this, values.getArrayItem());
			adapter.notifyDataSetChanged();
			listview_sono.setAdapter(adapter);
			changeTextViewThuChiVayNo(values);
		}		
	}
	AdView adView;
	InterstitialAd interstitial;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.so_no);
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
		/*
		 * Tao cac quickAction
		 */		
		mQuickAction 	= new QuickAction(this);
		ActionItem addItem 		= new ActionItem(ID_Sua, "Edit", getResources().getDrawable(R.drawable.ic_quickaction_sua));
		ActionItem acceptItem 	= new ActionItem(ID_Xoa, "Delete", getResources().getDrawable(R.drawable.ic_quickaction_remove));
		//quickAction cho VayNo
		ActionItem datra	= new ActionItem(ID_DaTra, "Paid", getResources().getDrawable(R.drawable.ic_quickaction_datra));
		ActionItem quakhu 	= new ActionItem(ID_QuaKhu, "Past", getResources().getDrawable(R.drawable.ic_quickaction_time));

		mQuickAction.addActionItem(datra);
		mQuickAction.addActionItem(quakhu);
		mQuickAction.addActionItem(addItem);
		mQuickAction.addActionItem(acceptItem);

		ConnetLayout();
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				//ActionItem actionItem = quickAction.getActionItem(pos);
				//@mSelectedRow gan gia tri o #onItemClick tra ve vi tri row trong mang
				EntryItem item = (EntryItem) values.getArrayItem().get(mSelectedRow);
				if (actionId == ID_Sua) { //Truong hop Sua
					Intent intent2 = new Intent(SoNoChiTietActivity.this,VayNo_Activity.class);
					intent2.putExtra(Variable.request, Variable.requestcode_UpdateSua);
					intent2.putExtra(Variable.UPDATE_DATABASE + "id",item.getTc().getId());
					SoNoChiTietActivity.this.startActivityForResult(intent2, Variable.requestcode_UpdateSua);
					overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
					//Toast.makeText(getApplicationContext(), "Add item selected on row " + mSelectedRow, Toast.LENGTH_SHORT).show();
				} else if(actionId == ID_Xoa){ //Truong hop Da tra
					AlertDialog diaBox = makeDialog_Delete(item.getTc());
					diaBox.show();// mo diaBox
					/*Toast.makeText(getApplicationContext(), actionItem.getTitle() + " item selected on row " 
							+ mSelectedRow, Toast.LENGTH_SHORT).show();*/
				} else if(actionId == ID_DaTra){ //Truong hop Da tra
					AlertDialog datraDialog = makeDialog_DaTra(item.getTc());				
					datraDialog.show();
					/*Dialog dialogDaTra = new Dialog_DaTra(context,item.getTc());
					dialogDaTra.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
					//ham dung cho dialog ko bi nhay len tren khi co softkeyboard hien ra
					dialogDaTra.show();
						xuly_selectDatabase_theoTime();*/
					/*Toast.makeText(getApplicationContext(), actionItem.getTitle() + " item selected on row " 
							+ mSelectedRow, Toast.LENGTH_SHORT).show();*/
				}else{ //Truong hop qua khu
					Intent intent2= new Intent(SoNoChiTietActivity.this,QuaKhuDaTraActiviy.class);
					intent2.putExtra("idQuaKhu", item.getTc().getId());
					SoNoChiTietActivity.this.startActivityForResult(intent2, Variable.requestcode_MoRong);
					overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
					/*	Toast.makeText(getApplicationContext(), actionItem.getTitle() + " item selected on row " 
							+ mSelectedRow, Toast.LENGTH_SHORT).show();*/
				}
			}
		});
		mQuickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {			
			@Override
			public void onDismiss() {
				//mMoreIv.setImageResource(R.drawable.ic_list_more);
			}
		});
		db = new Access_VayNo(this);
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		tengiaodich = bundle.getString(Variable.NameUser_GiaoDichVayNo);
		//in UserName len TextView
		tvTenNguoiVayNo_sono.setText(tengiaodich);
		// 5 gia tri id,ma_id,tennguoi,sotien,ngayvay

		truyxuatDatabase();
	}
	/*@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if (!values.getArrayItem().get(position).isSection()) {
			EntryItem item = (EntryItem) values.getArrayItem().get(position);
			mSelectedRow = position; //set the selected row
			Call_makeDialog dialog = new Call_makeDialog(activity,this,item.getTc());
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		}

	}*/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == Variable.requestcode_MoRong){
			truyxuatDatabase();
		}
		if(requestCode== Variable.requestcode_UpdateSua){
			truyxuatDatabase();
		}
	}
	private AlertDialog makeDialog_Delete(final ThuChi vn) {
		AlertDialog myDialogBox = new AlertDialog.Builder(this)	
		// set message, title, and icon
		.setTitle("Warning").setMessage("Are you sure you want to delete \n [ "+ vn.getTen_giao_dich()+" ] ?")
		.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				Database db = new Database(SoNoChiTietActivity.this);
				Access_VayNo dbVayNo = new Access_VayNo(getApplicationContext());
				dbVayNo.Delete_OneRowDatabase(vn.getId());

				/*
				 * Save lai variable Gobal (vayno ko can save Gobal
				 */
				//truy xuat lai database
				truyxuatDatabase();
				/*Intent intent2 = new Intent(context,Chi.class);
				context.startActivity(intent2);
				ChiTieuActivity.this.finish();*/
			}
		})// setPositiveButton
		.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
			}
		})// setNegativeButton

		.create();

		return myDialogBox;
	}
	private AlertDialog makeDialog_DaTra(final ThuChi vn) {
		final EditText input = new EditText(this);
		input.setRawInputType(InputType.TYPE_CLASS_NUMBER);
		input.setText(new Simple_method().KiemtraSoFloat_Int(vn.getSo_tien()));
		//bat nhap dung so tien muon
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
		AlertDialog myDialogBox = new AlertDialog.Builder(this)	
		// set message, title, and icon
		.setMessage("Amount paid ")
		.setView(input)
		.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				try {
					String d = input.getText().toString().trim();
					float sotienEdt = Float.valueOf(d);
					if(sotienEdt>0){
						db.DaTraTienVayNo(vn,sotienEdt);
					truyxuatDatabase();
						/*Intent intent2 = new Intent(context,Chi.class);
						context.startActivity(intent2);
						ChiTieuActivity.this.finish();*/
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),"Money was not be empty",1).show();
				}
			}

		})// setPositiveButton

		.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
			}
		})// setNegativeButton
		.create();
		return myDialogBox;
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(Variable.requestcode_MoRong, intent);
		overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
		finish();
	}
}//close SoNoChiTietActivity
/*class Call_makeDialog extends Dialog implements OnClickListener {

	private Button btnSua, btnXoa, btnHuy,btnQuaKhu_giaodich,btnDaTra_giaodich;
	LinearLayout llayout;
	private Context context;
	private String tenNguoiGiaoDich;
	private Activity activity;
	private ThuChi thuchi;  //id of BangVayNo

	
	 
	public Call_makeDialog(Activity activity,Context context,ThuChi thuchi) {
		super(context,R.style.customDialogStype);
		this.context = context;
		this.thuchi = thuchi;
		this.activity = activity;

		*//** 'Window.FEATURE_NO_TITLE' - Used to hide the title *//*
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		*//** Design the dialog in main.xml file *//*
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

		btnDaTra_giaodich = (Button) findViewById(R.id.btnDaTra_giaodich);
		btnDaTra_giaodich.setOnClickListener(this);

		btnQuaKhu_giaodich = (Button) findViewById(R.id.btnQuaKhu_giaodich);
		btnQuaKhu_giaodich.setOnClickListener(this);

		//setText lai button Huy
		btnHuy.setVisibility(View.GONE);

	}

	@Override
	public void onClick(View v) {
		*//** When OK Button is clicked, dismiss the dialog *//*
		switch (v.getId()) {
		case R.id.btnSua_thaotac: {
			//	dismiss(); da thuc hien xoa dialog o makeAndShowDialogBox()
			dismiss();
			AlertDialog diaBox = makeAndShowDialogBox();
			diaBox.show();// mo diaBox
			 			break;
		}
		case R.id.btnXoa_thaotac: {
			AlertDialog diaBox = makeDialogButton_Remove();
			diaBox.show();// mo diaBox
			 			break;
		}
		case R.id.btnDaTra_giaodich: {
			DialogDaTra dialogDatra = new DialogDaTra(context,thuchi);
			dialogDatra.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			dialogDatra.show();
			dismiss();
			break;
		}
		case R.id.btnQuaKhu_giaodich: {
			Intent intent2= new Intent(activity,QuaKhuDaTraActiviy.class);
			intent2.putExtra("idQuaKhu", thuchi.getId());
			activity.startActivityForResult(intent2, Variable.requestcode_MoRong);
			dismiss();
			break;
		}

		}// close switch

	}
	private AlertDialog makeDialogButton_Remove() {
		AlertDialog myDialogBox = new AlertDialog.Builder(context)	
		// set message, title, and icon
		.setTitle(context.getText(R.string.luu)).setMessage(context.getText(R.string.messageDialogRemve))
		.setPositiveButton(context.getString(R.string.dongy),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				new Access_VayNo(context).Delete_theoTenNguoi_VayNo(tenNguoiGiaoDich);
				dismiss();


			}

		})// setPositiveButton

		.setNegativeButton("Không",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				dismiss();

			}
		})// setNegativeButton

		.create();

		return myDialogBox;
	}
}
class DialogDaTra extends Dialog implements android.view.View.OnClickListener {
	private ThuChi vn ;
	private Context context;
	EditText edtSotien_dialogpay;
	Button btnDatra_dialogpayed,btnDong_dialogpayed;

	public DialogDaTra(Context context,ThuChi vn) {
		super(context);
		//this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_sotien_payed);
		this.getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


		this.vn =vn;
		this.setTitle(null);
		this.context = context;

		btnDatra_dialogpayed = (Button) findViewById(R.id.btnDatra_dialogpayed);
		btnDong_dialogpayed = (Button) findViewById(R.id.btnDong_dialogpayed);
		edtSotien_dialogpay = (EditText) findViewById(R.id.edtSotien_dialogpay);

		btnDatra_dialogpayed.setOnClickListener(this);

		btnDong_dialogpayed.setOnClickListener(this);
		edtSotien_dialogpay.setText(new Simple_method().KiemtraSoFloat_Int(vn.getSo_tien()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDatra_dialogpayed:{
			try {
				String d = edtSotien_dialogpay.getText().toString().trim();
				//Toast.makeText(context,"editSotien"+ d,1).show();
				float sotienEdt = Float.valueOf(d);
				if(sotienEdt>vn.getSo_tien()){
					edtSotien_dialogpay.setText("");
					Toast.makeText(context,"Lỗi Nhập, Số tiền nhỏ hơn hoặc bằng " + new Simple_method().KiemtraSoFloat_Int(vn.getSo_tien()),1).show();
				}else{
					new Access_VayNo(context).DaTraTienVayNo(vn, sotienEdt);
					dismiss();
				}
			} catch (Exception e) {
				Toast.makeText(context,"Số tiền không được bỏ trống",1).show();
			}
		}
		break;

		case R.id.btnDong_dialogpayed:{
			dismiss();
		}
		break;
		}
	}
}*/