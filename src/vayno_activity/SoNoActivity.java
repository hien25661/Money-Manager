package vayno_activity;

import java.util.ArrayList;
import java.util.List;

import com.boxtimer365.moneylove.GridView_main;
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
import sochitieu.ChiTieuActivity_BanPhu;
import sochitieu.EntryItem;
import util.Simple_method;
import util.Variable;

import access_sql.Access_VayNo;
import access_sql.Database;
import activity_child.ThuChiActivity;
import activity_child.VayNo_Activity;
import adapter.SoNoAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import application.MyApplication;
import application.MySharedPreference;

public class SoNoActivity extends Activity {

	private TextView tvVay_sono, tvNo_sono, emtydata;
	private Button btnBack_sono;
	private ListView listview_sono;
	private Access_VayNo db;
	private SoNoAdapter adapter;
	private SoNoObject values;
	private Intent intent;
	private final int ID_Sua = 1;
	private final int ID_Xoa = 2;
	private final int ID_Xem = 3;
	private int mSelectedRow;
	private QuickAction mQuickAction;
	AdView adView;
	InterstitialAd interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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
		intent = getIntent();
		/*
		 * Tao cac quickAction
		 */
		mQuickAction = new QuickAction(this);
		ActionItem addItem = new ActionItem(ID_Sua, "Edit", getResources().getDrawable(R.drawable.ic_quickaction_sua));
		ActionItem acceptItem = new ActionItem(ID_Xoa, "Delete",
				getResources().getDrawable(R.drawable.ic_quickaction_remove));
		// quickAction cho VayNo
		ActionItem xem = new ActionItem(ID_Xem, "Show", getResources().getDrawable(R.drawable.ic_quickaction_datra));
		mQuickAction.addActionItem(xem);
		mQuickAction.addActionItem(addItem);
		mQuickAction.addActionItem(acceptItem);

		// hien bang vayno
		ConnetLayout();
		db = new Access_VayNo(this);

		// mQuickAction.addActionItem(addItem2);
		// setup the action item click listener
		// hien quickAction 2 nut thuc hien cac ham
		mQuickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
			@Override
			public void onItemClick(QuickAction quickAction, int pos, int actionId) {
				// ActionItem actionItem = quickAction.getActionItem(pos);
				// @mSelectedRow gan gia tri o #onItemClick tra ve vi tri row
				// trong mang
				// String ten_nguoi_vayno = values.getList().get(mSelectedRow);
				if (actionId == ID_Xem) { // Truong hop Xem
					Intent intent = new Intent(getApplicationContext(), SoNoChiTietActivity.class);
					intent.putExtra(Variable.NameUser_GiaoDichVayNo, values.getList().get(mSelectedRow));
					SoNoActivity.this.startActivityForResult(intent, Variable.requestcode_MoRong);
					overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
				} else if (actionId == ID_Xoa) { // truong hop Xoa
					AlertDialog diaBox = makeDialogButton_Remove();
					diaBox.show();// mo diaBox
				} else {// SUA chi update lai tenGiaoDich
					AlertDialog diaBox = makeDialog_Update();
					diaBox.show();
				}
			}
		});
		// setup on dismiss listener, set the icon back to normal
		mQuickAction.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				// mMoreIv.setImageResource(R.drawable.ic_list_more);
			}
		});
		// do di lieu vao @value, sau do goi truyxuatDatabase
		truyxuatDatabase();
	}

	private void truyxuatDatabase() {
		values = db.getAll_NguoiVayNo();
		if (values == null) {
			listview_sono.setVisibility(View.GONE); // an listview
			emtydata.setVisibility(View.VISIBLE);
			changTextViewNull();
		} else {
			emtydata.setVisibility(View.GONE);// an textview
			listview_sono.setVisibility(View.VISIBLE);
			adapter = new SoNoAdapter(this, R.id.tvlist_SoNo, values.getList());
			listview_sono.setAdapter(adapter);
			changeTextViewThuChiVayNo(values);
		}
	}

	private void changTextViewNull() {
		tvVay_sono.setText("0");
		tvNo_sono.setText("0");
	}

	private void changeTextViewThuChiVayNo(SoNoObject val) {
		Simple_method doi = new Simple_method();
		tvVay_sono.setText(doi.KiemtraSoFloat_Int(val.getTvVay()));
		tvNo_sono.setText(doi.KiemtraSoFloat_Int(val.getTvNo()));

	}

	private void ConnetLayout() {
		tvVay_sono = (TextView) findViewById(R.id.tvVay_sono);
		tvNo_sono = (TextView) findViewById(R.id.tvNo_sono);
		emtydata = (TextView) findViewById(R.id.tvEmpty_sono);

		listview_sono = (ListView) findViewById(R.id.listview_sono);

		btnBack_sono = (Button) findViewById(R.id.btnBack_sono);

		/*
		 * Listen su kien click
		 */
		btnBack_sono.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(Variable.requestcode_MoRong, intent);
				finish();
				overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
			}
		});
		listview_sono.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int positon, long arg3) {
				mSelectedRow = positon;// set the selected row
				mQuickAction.show(view);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(Variable.requestcode_MoRong, intent);
		finish();
		overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Variable.requestcode_MoRong) {
			truyxuatDatabase();
		}

	}

	/*
	 * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
	 * position, long arg3) { if(values!= null){
	 * 
	 * CallDialog dialog = new CallDialog(this,intent,ten_nguoi_vayno);
	 * dialog.setCanceledOnTouchOutside(true); dialog.show(); } }
	 */
	private AlertDialog makeDialogButton_Remove() {
		final String tengiaodich = values.getList().get(mSelectedRow);
		AlertDialog myDialogBox = new AlertDialog.Builder(this)
				// set message, title, and icon
				.setTitle("Warning").setMessage("Are you sure you want to delete \n[" + tengiaodich + "]")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						new Access_VayNo(getApplicationContext()).Delete_theoTenNguoi_VayNo(tengiaodich);
						truyxuatDatabase();

						/*
						 * startActivity(intent); finish();
						 */
					}
				})// setPositiveButton

				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})// setNegativeButton

				.create();

		return myDialogBox;
	}

	private AlertDialog makeDialog_Update() {

		final EditText input = new EditText(getApplicationContext());
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		input.setText(values.getList().get(mSelectedRow));
		AlertDialog myDialogBox = new AlertDialog.Builder(this)
				// set message, title, and icon
				.setTitle("Warning").setView(input)
				.setPositiveButton(this.getString(R.string.luu), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						String tenNew = input.getEditableText().toString().trim();
						// Log.i("TAG","ten cu: " + tenNguoiGiaoDich + " "
						// +tenNew);

						new Access_VayNo(getApplicationContext())
								.UpdateTenNguoiVayNo(values.getList().get(mSelectedRow), tenNew);
						truyxuatDatabase();

						/*
						 * startActivity(intent); finish();
						 */
					}
				})// setPositiveButton
				.setNegativeButton(this.getString(R.string.huybo), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				})// setNegativeButton

				.create();

		return myDialogBox;
	}

}// close SoNoActivity
/*
 * class CallDialog extends Dialog implements OnClickListener {
 * 
 * private Button btnSua, btnXoa, btnHuy; LinearLayout llayout; private Activity
 * context; private String tenNguoiGiaoDich; private Intent intent;
 * 
 * Intent luu lai Gridview gui qua
 * 
 * public CallDialog(Activity context,Intent intent,String tenNguoiGiaoDich) {
 * super(context,R.style.customDialogStype); this.context = context;
 * this.tenNguoiGiaoDich = tenNguoiGiaoDich; this.intent = intent;
 * 
 *//** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
/*
 * requestWindowFeature(Window.FEATURE_NO_TITLE);
 *//** Design the dialog in main.xml file */
/*
 * setContentView(R.layout.thaotac_chitieu);
 * 
 * getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
 * getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
 * 
 * btnSua = (Button) findViewById(R.id.btnSua_thaotac);
 * btnSua.setOnClickListener(this);
 * 
 * btnXoa = (Button) findViewById(R.id.btnXoa_thaotac);
 * btnXoa.setOnClickListener(this);
 * 
 * btnHuy = (Button) findViewById(R.id.btnHuyBo_thaotac);
 * btnHuy.setOnClickListener(this);
 * 
 * llayout = (LinearLayout) findViewById(R.id.llayoutVayNo);
 * 
 * llayout.setVisibility(LinearLayout.GONE);// an linearlayout di
 * 
 * //setText lai button Huy btnHuy.setText("Xem");
 * 
 * }
 * 
 * @Override public void onClick(View v) {
 *//** When OK Button is clicked, dismiss the dialog *//*
														 * switch (v.getId()) {
														 * case
														 * R.id.btnSua_thaotac:
														 * { // dismiss(); da
														 * thuc hien xoa dialog
														 * o
														 * makeAndShowDialogBox(
														 * ) dismiss();
														 * AlertDialog diaBox =
														 * makeAndShowDialogBox(
														 * ); diaBox.show();//
														 * mo diaBox break; }
														 * case
														 * R.id.btnXoa_thaotac:
														 * { dismiss();
														 * AlertDialog diaBox =
														 * makeDialogButton_Remove
														 * (); diaBox.show();//
														 * mo diaBox break; }
														 * //btn huy bo la xem
														 * case R.id.
														 * btnHuyBo_thaotac: {
														 * Intent intent = new
														 * Intent(context,
														 * SoNoChiTietActivity.
														 * class);
														 * intent.putExtra(
														 * Variable.
														 * NameUser_GiaoDichVayNo,
														 * tenNguoiGiaoDich);
														 * context.
														 * startActivityForResult
														 * (intent, Variable.
														 * requestcode_MoRong);
														 * 
														 * dismiss(); break; }
														 * 
														 * }// close switch
														 * 
														 * } private AlertDialog
														 * makeDialogButton_Remove
														 * () { AlertDialog
														 * myDialogBox = new
														 * AlertDialog.Builder(
														 * context) // set
														 * message, title, and
														 * icon
														 * .setTitle("Lưu ý").
														 * setMessage("Bạn có chắc chắn muốn xóa "
														 * )
														 * .setPositiveButton("Đồng ý"
														 * , new
														 * DialogInterface.
														 * OnClickListener() {
														 * public void onClick(
														 * DialogInterface
														 * dialog, int
														 * whichButton) { new
														 * Access_VayNo(context)
														 * .
														 * Delete_theoTenNguoi_VayNo
														 * (tenNguoiGiaoDich);
														 * context.startActivity
														 * (intent);
														 * context.finish();
														 * dismiss();
														 * 
														 * 
														 * }
														 * 
														 * })//
														 * setPositiveButton
														 * 
														 * .setNegativeButton(
														 * "Không", new
														 * DialogInterface.
														 * OnClickListener() {
														 * public void onClick(
														 * DialogInterface
														 * dialog, int
														 * whichButton) {
														 * dismiss();
														 * 
														 * } })//
														 * setNegativeButton
														 * 
														 * .create();
														 * 
														 * return myDialogBox; }
														 * private AlertDialog
														 * makeAndShowDialogBox(
														 * ) {
														 * 
														 * final EditText input
														 * = new
														 * EditText(context);
														 * input.setInputType(
														 * InputType.
														 * TYPE_NUMBER_FLAG_DECIMAL
														 * ); AlertDialog
														 * myDialogBox = new
														 * AlertDialog.Builder(
														 * context) // set
														 * message, title, and
														 * icon
														 * .setTitle("Lưu ý")
														 * .setView(input)
														 * .setPositiveButton(
														 * context.getString(R.
														 * string.luu), new
														 * DialogInterface.
														 * OnClickListener() {
														 * public void onClick(
														 * DialogInterface
														 * dialog, int
														 * whichButton) { String
														 * tenNew =input.
														 * getEditableText().
														 * toString().trim();
														 * //Log.i(
														 * "TAG","ten cu: " +
														 * tenNguoiGiaoDich +
														 * " " +tenNew);
														 * 
														 * new
														 * Access_VayNo(context)
														 * .UpdateTenNguoiVayNo(
														 * tenNguoiGiaoDich,
														 * tenNew);
														 * context.startActivity
														 * (intent);
														 * context.finish();
														 * dismiss(); } })//
														 * setPositiveButton
														 * .setNegativeButton(
														 * context.getString(R.
														 * string.huybo), new
														 * DialogInterface.
														 * OnClickListener() {
														 * public void onClick(
														 * DialogInterface
														 * dialog, int
														 * whichButton) {
														 * dismiss();
														 * 
														 * 
														 * } })//
														 * setNegativeButton
														 * 
														 * .create();
														 * 
														 * return myDialogBox; }
														 * }
														 */