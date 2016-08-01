package vayno_activity;

import java.util.ArrayList;

import object.SoChiTieu_Obj;

import sochitieu.EntryAdapter;
import sochitieu.Item;
import util.Simple_method;
import util.Variable;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.moneylove.R;

import access_sql.Access_VayNo;
import adapter.SoNoAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class QuaKhuDaTraActiviy extends Activity implements OnItemClickListener{
	private TextView emtydata,tvTenNguoiVayNo_sono,tvVay_sono,tvNo_sono;
	private Button btnBack_sono;
	private ListView listview_sono;
	private Intent intent;
	private Access_VayNo db;
	private SoChiTieu_Obj values;
	private EntryAdapter adapter;
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
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		int id = bundle.getInt("idQuaKhu");
		ConnetLayout();
		tvTenNguoiVayNo_sono.setText("");
		db = new Access_VayNo(this);
		values = db.getQuaKhu_DaTra(id); // do du lieu vao value
		if(values == null){
			listview_sono.setVisibility(View.GONE); //an listview
			emtydata.setVisibility(View.VISIBLE);
		}else{
			emtydata.setVisibility(View.GONE);// an textview
			listview_sono.setVisibility(View.VISIBLE);
			adapter =  new EntryAdapter(this, values.getArrayItem());
			listview_sono.setAdapter(adapter);
			Simple_method simple = new Simple_method();
			if(values.getTongNo()==0){
				tvNo_sono.setVisibility(View.GONE);
				tvVay_sono.setText(simple.KiemtraSoFloat_Int(values.getTongVay()));
			}else{
				tvVay_sono.setVisibility(View.GONE);
				tvNo_sono.setText(simple.KiemtraSoFloat_Int(values.getTongNo()));
			}
		}		
	}
	private void ConnetLayout() {
		
		tvVay_sono  = (TextView) findViewById(R.id.tvVay_sono);
		tvNo_sono  = (TextView) findViewById(R.id.tvNo_sono);
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
				setResult(Variable.requestcode_MoRong, intent);
				finish();
			}
		});
		listview_sono.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
}
