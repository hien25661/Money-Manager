package congcu_chia_tien;

import util.Simple_method;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.boxtimer365.moneylove.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChiaTienActivity extends Activity {
	final int CUSTOM_DIALOG_CHIA_TIEN_OK = 0;
	int tile = 0;
	int songuoi = 1;
	Intent intent;
	EditText edTyLeTip, edSoNguoi, edSoTien;
	AdView adView;
	InterstitialAd interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		intent = getIntent();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chia_tien_activity);
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
		Button btnTinhTien = (Button) ChiaTienActivity.this.findViewById(R.id.btnChiaTien);
		edSoTien = (EditText) ChiaTienActivity.this.findViewById(R.id.edSoTienChia);
		edSoNguoi = (EditText) ChiaTienActivity.this.findViewById(R.id.edSoNguoi);
		Button tileCong = (Button) findViewById(R.id.tile_cong);
		edTyLeTip = (EditText) findViewById(R.id.edTyLeTip);
		Button tile_tru = (Button) findViewById(R.id.tile_tru);
		Button Back_chiatien = (Button) findViewById(R.id.Back_chiatien);
		Back_chiatien.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		});
		tileCong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				tile += 5;
				edTyLeTip.setText(String.valueOf(tile));

			}
		});
		tile_tru.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tile >= 5) {
					tile -= 5;
					edTyLeTip.setText(String.valueOf(tile));
				}
			}
		});

		Button songuoi_cong = (Button) findViewById(R.id.songuoi_cong);
		Button songuoi_tru = (Button) findViewById(R.id.songuoi_tru);
		songuoi_tru.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (songuoi >= 2) {
					songuoi -= 1;
					edSoNguoi.setText(String.valueOf(songuoi));
				}
			}
		});
		songuoi_cong.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				songuoi += 1;
				edSoNguoi.setText(String.valueOf(songuoi));
			}
		});

		btnTinhTien.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edSoTien.getText().toString().length() == 0 || Integer.valueOf(edSoTien.getText().toString()) < 1) {// chi
																														// kiem
																														// tra
																														// so
																														// nguoi
																														// va
																														// so
																														// tien
																														// ti
																														// le
																														// tiep
																														// bo
																														// qua
					AlertDialog buider1 = new AlertDialog.Builder(ChiaTienActivity.this).create();
					buider1.setTitle("An error occurred!");
					buider1.setIcon(R.drawable.warning);
					buider1.setMessage("The amount must be greater than 0");
					buider1.setButton("OK", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					buider1.show();
				} else if (edSoNguoi.getText().toString().length() == 0
						|| Integer.valueOf(edSoNguoi.getText().toString()) < 1) {
					AlertDialog buider2 = new AlertDialog.Builder(ChiaTienActivity.this).create();
					buider2.setTitle("An error occurred!");
					buider2.setIcon(R.drawable.warning);
					buider2.setMessage("People must be greater than 0");
					buider2.setButton("OK", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});
					buider2.show();
				} else {
					showDialog(CUSTOM_DIALOG_CHIA_TIEN_OK);
				}

			}
		});
	}

	protected void setResult(Intent intent2) {
		// TODO Auto-generated method stub

	}

	// Tạo Dialog khi thuc hien tinh toan ma không xay ra loi
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub.
		final Dialog dialog;
		switch (id) {
		case CUSTOM_DIALOG_CHIA_TIEN_OK:
			dialog = new Dialog(this);
			dialog.setContentView(R.layout.dialog_ketqua_chiatien);
			dialog.setTitle("Result");
			// dialog.setIcon(R.drawable.money_pig);
			final EditText edSoTien = (EditText) ChiaTienActivity.this.findViewById(R.id.edSoTienChia);
			final EditText edTyLeTip = (EditText) ChiaTienActivity.this.findViewById(R.id.edTyLeTip);
			final EditText edSoNguoi = (EditText) ChiaTienActivity.this.findViewById(R.id.edSoNguoi);
			TextView tvTienTip = (TextView) dialog.findViewById(R.id.tvSoTienTip);
			TextView tvTongTien = (TextView) dialog.findViewById(R.id.tvTongTien);
			TextView tvSoTienMoiNguoi = (TextView) dialog.findViewById(R.id.tvSoTienMoiNguoi);
			int soTien = Integer.valueOf(edSoTien.getText().toString().trim());
			int tip = Integer.valueOf(edTyLeTip.getText().toString().trim());
			int soNguoi = Integer.valueOf(edSoNguoi.getText().toString().trim());

			Simple_method simple = new Simple_method();
			// tinh toan
			float tienTip = Float.valueOf((tip * soTien) / 100);
			tvTienTip.setText(simple.LamTronSoFloat(tienTip));
			float tongTien = soTien + tienTip;
			tvTongTien.setText(simple.LamTronSoFloat(tongTien));

			float tb = tongTien / soNguoi;

			tvSoTienMoiNguoi.setText(simple.LamTronSoFloat(tb));
			Button btnDong = (Button) dialog.findViewById(R.id.btnExitChiaTien);
			btnDong.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dismissDialog(CUSTOM_DIALOG_CHIA_TIEN_OK);
				}
			});
			return dialog;
		}
		return super.onCreateDialog(id);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(intent);
		overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
		finish();
	}
}
