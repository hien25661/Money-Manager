package com.tim_kiem;

import java.util.List;

import com.moneylove.R;

import util.Variable;
import access_sql.Database;
import access_sql.My_SQLiteOpenHelper;
import object.ThuChi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class TimKiemActivity extends Activity implements OnClickListener,
		OnItemSelectedListener {

	private EditText edTen, edNguoi_dung, edSoTienTu, edSoTienToi, edTuNgay,
			edToiNgay;
	private TextView tvketqua;
	private Database db;
	private Spinner spLoai;
	private Button btntimkiem;
	private String[] items = { "Tất cả", "Thu", "Chi", "Nợ", "Vay" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tim_kiem_layout);
		db = new Database(this);
		tvketqua = (EditText) findViewById(R.id.tvLoai);
		edTen = (EditText) findViewById(R.id.edTen);
		/*
		 * edSoTienTu = (EditText) findViewById(R.id.edSoTienTu); edSoTienToi =
		 * (EditText) findViewById(R.id.edSoTienToi); edTuNgay = (EditText)
		 * findViewById(R.id.edTuNgay); edToiNgay = (EditText)
		 * findViewById(R.id.edToiNgay);
		 * 
		 * spLoai = (Spinner) findViewById(R.id.spLoai);
		 */

		btntimkiem = (Button) findViewById(R.id.btnTimKiem);
		btntimkiem.setOnClickListener(this);

		spLoai.setOnItemSelectedListener(this);
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		// provide a particular design for the drop-down lines
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// associate GUI spinner and adapter
		spLoai.setAdapter(aa);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTimKiem: {
			StringBuilder dk_hoi = new StringBuilder();
			if (edTen.getText().toString() != null) {
				/*
				 * List<ThuChi> tc = db
				 * .getList_Timkiem(edTen.getText().toString()); for (int i = 0;
				 * i < tc.size(); i++) { dk_hoi.append("Ma " +
				 * tc.get(i).getMa_id() + " Ten giao dich " +
				 * tc.get(i).getTen_giao_dich()); dk_hoi.append("\nSo tien " +
				 * tc.get(i).getSo_tien() + " Ngay Giao Dich " +
				 * tc.get(i).getNgay_thu_chi());
				 */}
			tvketqua.setText(dk_hoi);

		}
			

			break;
		}
	}

	// }

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
