package activity_child;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.boxtimer365.moneylove.R;

import adapter.SoChiTieuAdapter;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class EmptyDataseActivity extends Activity {

	// ////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.paper_emty);
	}
	/*
	 * @Override protected void onListItemClick(ListView l, View v, int
	 * position, long id) {
	 * 
	 * super.onListItemClick(l, v, position, id);
	 * 
	 * Intent intent = new Intent(this,SoNoChiTietActivity.class);
	 * 
	 * intent.putExtra(Variable.VAY_or_NO,
	 * values.get(position).getTen_giao_dich());
	 * 
	 * startActivity(intent); }
	 */
}
