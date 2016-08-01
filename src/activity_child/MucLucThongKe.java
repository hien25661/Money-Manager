package activity_child;

import java.util.Calendar;

import object.ThuChi;
import util.LayDate_Month_Yeah;
import util.Variable;
import access_sql.Database;
import adapter.Mucluc_thongkeAdapter;
import adapter.TheLoaiAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moneylove.GridView_main;
import com.moneylove.R;

public class MucLucThongKe extends ListActivity {

	Mucluc_thongkeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.muclucthongke);
		/*
		 * adapter = new Mucluc_thongkeAdapter(this,
		 * R.id.tvTheLoai_muclucthongke,
		 * ThongKe_Activity.ob_list_chart.getList_theloai(),
		 * ThongKe_Activity.ob_list_chart.getList_color());
		 * setListAdapter(adapter);
		 */

	}// onCrear

}// updataActivity

