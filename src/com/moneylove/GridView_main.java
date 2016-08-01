package com.moneylove;

import import_cvs.ImportActivity;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import map.GoogleMap;

import object.QuanLyTienObject;
import object.ThuChi;
import quickAction.ChiTieuActivity;

import com.calculator.Calculator_Activity;
import com.doi_tien_te.Doi_Tien_Te;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import congcu_chia_tien.ChiaTienActivity;

import export_cvs.ExportActivity;

import sochitieu.ChiTieuActivity_BanPhu;
import thongke.ThayDoi_ThongKe_PieActivity;
import util.CodeHeThong;
import util.LayDate_Month_Yeah;
import util.Simple_method;
import util.Variable;
import vayno_activity.SoNoActivity;
import access_sql.Access_VayNo;
import access_sql.Database;
import access_sql.My_SQLiteOpenHelper;
import activity_child.EmptyDataseActivity;
import activity_child.ThongKe_Activity;
import activity_child.ThuChiActivity;
import activity_child.VayNo_Activity;
import activity_design.CustomizeDialog;
import android.R.bool;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import application.MyApplication;
import application.MySharedPreference;

public class GridView_main extends Activity implements OnItemClickListener {
	private final String[] quanlyValues = { "Add Item", "Book Spending", "Paybook", "Chart", "Currency exchange",
			"Export CSV file", "Restore data", "Erase all data", "Statistics Month", "Disunite Money" };
	private Database db;
	private TextView tvMoneyToday, tvMoneyTotal, tvMoneyTuan, tvMoneyThang;
	private LayDate_Month_Yeah date = new LayDate_Month_Yeah();
	private boolean loadSharedPreferece = true;
	// NameSharedPreference luu TongHomNay, TongTuan, TongThang
	private final String NameSharedPreference = "SharedPreferences_MoneyLover";
	private SharedPreferences mySharedPreferences;
	private int mode = Activity.MODE_PRIVATE;

	// private QuanLyTienObject qltDB; // lay du lieu Hom nay, Thang, Tuan (gui
	// qua ThuChiActivity , VayNoActivity xu ly
	AdView adView;
	InterstitialAd interstitial;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.main);
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
		 * khoi tao cac TextView So lieu Today, Total, Tuan, Thang
		 */
		tvMoneyToday = (TextView) findViewById(R.id.tvMoneyToday);
		tvMoneyTotal = (TextView) findViewById(R.id.tvMoneyTotal);
		tvMoneyTuan = (TextView) findViewById(R.id.tvMoneyTuan);
		tvMoneyThang = (TextView) findViewById(R.id.tvMoneyThang);

		/*
		 * create a preference to the shared preferences object
		 */

		mySharedPreferences = getSharedPreferences(NameSharedPreference, mode);
		// is there an existing Preferences from previous executions of this
		// app?
		if (mySharedPreferences != null && mySharedPreferences.contains("currentDate")) {
			new MySharedPreference().showPreferences(mySharedPreferences);
			callTextView();
		} else {// load lan dau tien
			loadSharedPreferece = false;
		}

		// Log.i("TAG","Create GridView insert shared Preferece");
		/*
		 * khoi tao grid View
		 */
		GridView gv = (GridView) findViewById(R.id.grid);
		gv.setAdapter(new adapter.GridView_BaseAdapter(this, quanlyValues, Variable.ICON_GridView_Main));
		gv.setOnItemClickListener(this);
		/*
		 * Kiem tra database table QuanLyTien => True lay gia tri in else mac
		 * dinh (insert dl vao QuanLyTien)
		 */

	}// close onCreate

	private void callTextView() {
		Simple_method doi = new Simple_method();
		tvMoneyToday.setText(doi.KiemtraSoFloat_Int(MyApplication.homnay));
		tvMoneyTotal.setText(doi.KiemtraSoFloat_Int(MyApplication.tongtien));
		tvMoneyTuan.setText(doi.KiemtraSoFloat_Int(MyApplication.tuan));
		tvMoneyThang.setText(doi.KiemtraSoFloat_Int(MyApplication.thang));
		if (MyApplication.homnay < 0) {
			tvMoneyToday.setBackgroundColor(this.getResources().getColor(R.color.mauDo_Chi));
		} else {
			tvMoneyToday.setBackgroundColor(this.getResources().getColor(R.color.mauXanh_Thu));
		}
		if (MyApplication.tongtien < 0) {
			tvMoneyTotal.setBackgroundColor(this.getResources().getColor(R.color.mauDo_Chi));
		} else {
			tvMoneyTotal.setBackgroundColor(this.getResources().getColor(R.color.mauXanh_Thu));
		}

		if (MyApplication.tuan < 0) {
			tvMoneyTuan.setBackgroundColor(this.getResources().getColor(R.color.mauDo_Chi));
		} else {
			tvMoneyTuan.setBackgroundColor(this.getResources().getColor(R.color.mauXanh_Thu));
		}
		if (MyApplication.thang < 0) {
			tvMoneyThang.setBackgroundColor(this.getResources().getColor(R.color.mauDo_Chi));
		} else {
			tvMoneyThang.setBackgroundColor(this.getResources().getColor(R.color.mauXanh_Thu));
		}
	}

	@Override
	public void onBackPressed() { // ham back
		super.onBackPressed();
		finish();

	}

	@Override
	protected void onPause() {
		// Log.i("TAG","Pause GridView insert shared Preferece");
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Log.i("TAG", "onDestroy GridView");
		super.onDestroy();
	}

	@Override
	protected void onStop() {
		super.onStop();
		loadSharedPreferece = true;
		// Log.i("TAG","Stop GridView");
	}

	@Override
	protected void onResume() {
		if (mySharedPreferences != null && loadSharedPreferece) {
			new MySharedPreference().showPreferences(mySharedPreferences);
			Log.i("TAG", "onResume mySharedPreferences ");
			callTextView();
		}
		// TODO Auto-generated method stub
		super.onResume();
	}

	// ///////////////////////////////////////////////////////////////////
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		switch (position) {
		case 0: {

			// qltDB: dua cac du lieu HomNay, Thang, Tong qua cac Activity
			// ThuChiAcity
			CustomizeDialog dialog = new CustomizeDialog(this);
			// ham khi an ngoai dialog se thoat dialog
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		}
			break;
		case 1: { // so chi tieu
			Intent intent = new Intent(GridView_main.this, ChiTieuActivity.class);
			startActivityForResult(intent, Variable.requestcode_SoChiTieu);
			break;
		}
		case 2: {
			Intent intent = new Intent(GridView_main.this, SoNoActivity.class);
			startActivityForResult(intent, Variable.requestcode_MoRong);
			break;
		}
		case 3: { // thong ke
			Intent intent = new Intent(GridView_main.this, ThongKe_Activity.class);
			startActivity(intent);
			break;
		}
		case 4: { // doi tien
			CodeHeThong check = new CodeHeThong();
			boolean checkInternet = check.checkInternetMobile(getApplicationContext());
			if (checkInternet == false) {
				// Log.e("TAG","Chay dialog");
				AlertDialog dialog2 = makeDialogInternet();
				dialog2.show();
			} else {
				Intent intent = new Intent(GridView_main.this, Doi_Tien_Te.class);
				startActivityForResult(intent, Variable.requestcode_MoRong);
			}
			break;
		}
		case 5: { // tim kiem
			// xuat file cvs
			Intent intent = new Intent(GridView_main.this, ExportActivity.class);
			startActivity(intent);

			break;
		}
		case 6: {
			/*
			 * // tim kiem Intent intent = new Intent(GridView_main.this,
			 * ChiTieuActivity.class); startActivity(intent); break;
			 */
			AlertDialog dialogImport = makeDialog_ImportData();
			dialogImport.show();
			break;
		}
		case 7: { // Xoa sach du lieu
			AlertDialog dialog = makeDialog_RemoveData();
			dialog.show();
			break;
		}
		case 8: {
			break;
		}
		case 9: { // chia tien
			Intent intent = new Intent(GridView_main.this, ChiaTienActivity.class);
			startActivityForResult(intent, Variable.requestcode_MoRong);
			break;
		}
		}// close switch

	}// close switch case

	private AlertDialog makeDialogInternet() {

		AlertDialog myDialog = new AlertDialog.Builder(this)
				// set message, title, and icon
				.setTitle("Error Connect").setMessage("Please Connect Internet").setIcon(R.drawable.error)
				.setNeutralButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				}).create();
		return myDialog;
	}

	private AlertDialog makeDialog_RemoveData() {
		AlertDialog myDialogBox = new AlertDialog.Builder(this)
				// set message, title, and icon
				.setTitle("Note").setMessage("Are you sure you want to delete all data ?").setIcon(R.drawable.error)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Database db = new Database(getApplicationContext());
						db.ClearDataBase();
						MyApplication.homnay = 0;
						MyApplication.tongtien = 0;
						MyApplication.thang = 0;
						MyApplication.tuan = 0;
						SharedPreferences mySharedPreferences = GridView_main.this
								.getSharedPreferences("SharedPreferences_MoneyLover", Activity.MODE_PRIVATE);
						new MySharedPreference().savePreferences(mySharedPreferences);
						callTextView();

					}

				})// setPositiveButton

				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// whatever should be done when answering "NO"
						// goes here
						// whichButton =-2
					}
				})// setNegativeButton

				.create();

		return myDialogBox;
	}

	private AlertDialog makeDialog_ImportData() {
		AlertDialog myDialogBox = new AlertDialog.Builder(this)
				// set message, title, and icon
				.setTitle("Warning").setMessage("Are you sure you want to restore data ?").setIcon(R.drawable.error)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Database db = new Database(getApplicationContext());
						db.ClearDataBase();
						MyApplication.homnay = 0;
						MyApplication.tongtien = 0;
						MyApplication.thang = 0;
						MyApplication.tuan = 0;
						SharedPreferences mySharedPreferences = GridView_main.this
								.getSharedPreferences("SharedPreferences_MoneyLover", Activity.MODE_PRIVATE);
						new MySharedPreference().savePreferences(mySharedPreferences);
						callTextView();
						dialog.dismiss();
						Intent intent = new Intent(GridView_main.this, ImportActivity.class);
						startActivityForResult(intent, Variable.requestcode_MoRong);
					}

				})// setPositiveButton

				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// whatever should be done when answering "NO"
						// goes here
						// whichButton =-2
					}
				})// setNegativeButton

				.create();

		return myDialogBox;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == Activity.RESULT_OK) {
				if (requestCode == Variable.requestcode_InsertThuChi) {
					// Log.i("TAG","onActivityResult setResult goi
					// callTextView");
					loadSharedPreferece = true; // thuc hien load shareRefece
				} else if (requestCode == Variable.requestcode_InsertVayNo) {
					// ko can load share
					loadSharedPreferece = false; // khoa ko load share
				} else {
					loadSharedPreferece = false;
				}
			} else {// truong hop Activity.Cacecl
					// ngoai tru SoChieuTieu phai load lai vi
					// SoChitieu Co chuc nang them
				if (requestCode == Variable.requestcode_SoChiTieu) {
					loadSharedPreferece = true; // thuc hien load shareRefece
				} else {
					loadSharedPreferece = false; // => ko load lai sharePrefece
				}
			}

		} catch (Exception e) {
			Log.i("TAG", "Loi AcitiyResult GridView");
		}

	}
}// close GridView_main
