package thongke;

import java.util.ArrayList;

import sochitieu.EntryAdapter;
import sochitieu.Item;
import util.Variable;
import access_sql.Access_VayNo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.boxtimer365.moneylove.R;

public class ThongKe_ThangActivity extends Activity {
	private TextView tvdate_thongkethang;
	private Button btnBack_thongkethang;
	private ListView listview_thongkethang;
	private Intent intent;
	private Access_VayNo db;
	private ArrayList<Item> values;
	private EntryAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.so_no);
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		ConnetLayout();
		
		
		
		db = new Access_VayNo(this);
		//values = db.getQuaKhu_DaTra(id); // do du lieu vao value
		if(values == null){
			listview_thongkethang.setVisibility(View.GONE); //an listview
		}else{
			listview_thongkethang.setVisibility(View.VISIBLE);
			adapter =  new EntryAdapter(this, values);
			listview_thongkethang.setAdapter(adapter);
		}		
	}
	private void ConnetLayout() {
		tvdate_thongkethang=(TextView) findViewById(R.id.tvdate_thongkethang);
		listview_thongkethang = (ListView) findViewById(R.id.listview_thongkethang);
		btnBack_thongkethang = (Button) findViewById(R.id.btnBack_thongkethang);

		/*
		 * Listen su kien click
		 */
		btnBack_thongkethang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(Variable.requestcode_MoRong, intent);
				finish();
			}
		});
	}
}
