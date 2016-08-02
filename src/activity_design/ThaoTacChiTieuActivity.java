package activity_design;

import com.boxtimer365.moneylove.R;

import sochitieu.ChiTieuActivity_BanPhu;
import util.Variable;

import access_sql.Access_VayNo;
import access_sql.Database;
import activity_child.ThuChiActivity;
import activity_child.VayNo_Activity;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class ThaoTacChiTieuActivity extends Activity implements OnClickListener {

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	/*Button btnSua, btnXoa, btnDaTra, btnQuaKhu, btnHuy;
	LinearLayout llayout;
	private int id;
	private String ma_id;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.thaotac_chitieu);
		context = this;
		btnSua = (Button) findViewById(R.id.btnSua_thaotac);
		btnSua.setOnClickListener(this);

		btnXoa = (Button) findViewById(R.id.btnXoa_thaotac);
		btnXoa.setOnClickListener(this);

		btnHuy = (Button) findViewById(R.id.btnHuyBo_thaotac);
		btnHuy.setOnClickListener(this);

		llayout = (LinearLayout) findViewById(R.id.llayoutVayNo);

		setTitle("Các Thao Tác");

		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		// thu hay chi chua 1 trong 2 gia tri 1 la Thu, 2 la Chi
		id = bundle.getInt(Variable.CHITIEU_CALL + "id");
		ma_id = bundle.getString(Variable.CHITIEU_CALL + "ma_id");
		if (ma_id.equals("thu") || ma_id.equals("chi")) {
			
			 * Cac gia tri linearlayout LinearLayout.GONE ko hien khoang trang
			 * LinearLayout.Visible hien ra LinearLayout.Invisible hien khoang
			 * trang
			 
			llayout.setVisibility(LinearLayout.GONE);// an linearlayout di

		} else {
			llayout.setVisibility(LinearLayout.VISIBLE);
			btnDaTra = (Button) findViewById(R.id.btnXoa_thaotac);
			btnDaTra.setOnClickListener(this);
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Variable.requestcode_ThaoTac) {
			setResult(Variable.requestcode_ChiTieu);
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSua_thaotac: {
			Intent intent;
			if (ma_id.equals("thu") || ma_id.equals("chi")) {
				intent = new Intent(ThaoTacChiTieuActivity.this,
						ThuChiActivity.class);
			} else {
				intent = new Intent(ThaoTacChiTieuActivity.this,
						VayNo_Activity.class);
			}
			intent.putExtra(Variable.UPDATE_DATABASE + "string", "Sửa khoản ");
			intent.putExtra(Variable.UPDATE_DATABASE + "id", id);
			startActivityForResult(intent, Variable.requestcode_ThaoTac);
			finish();
			// startActivityForResult(intent, Variable.requestcode);
			break;
		}
		case R.id.btnXoa_thaotac: {
			AlertDialog diaBox = makeAndShowDialogBox();
			diaBox.show();
			// mo diaBox
			break;
		}
		case R.id.btnHuyBo_thaotac: {
			finish();
			break;
		}

		}// close switch
	}

	private AlertDialog makeAndShowDialogBox() {

		AlertDialog myDialogBox =

		new AlertDialog.Builder(this)
		// set message, title, and icon
				.setTitle("Lưu ý").setMessage("Bạn có chắc chắn muốn xóa ")
				// .setIcon(R.drawable.ic_menu_end_conversation)

				// set three option buttons
				.setPositiveButton("Đồng ý",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// whatever should be done when answering "YES"
								// goes here
								// whichButton =-1
								if (ma_id.equals("thu") || ma_id.equals("chi")) {
									Database db = new Database(context);
									db.Delete_OneRowDatabase(id);
								} else {
									Access_VayNo dbVayNo = new Access_VayNo(
											context);
									dbVayNo.Delete_OneRowDatabase(id);

								}

								Intent intent = new Intent(
										ThaoTacChiTieuActivity.this,
										ChiTieuActivity.class);
								setResult(Variable.requestcode_ChiTieu);
								startActivity(intent);
								finish();

							}
						})// setPositiveButton

				.setNegativeButton("Không",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// whatever should be done when answering "NO"
								// goes here
								// whichButton =-2
								finish();
							}
						})// setNegativeButton

				.create();

		return myDialogBox;
	}
*/
}
