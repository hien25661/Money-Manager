package activity_child;

import util.Variable;
import adapter.TheLoaiAdapter;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.boxtimer365.moneylove.R;

public class TheLoaiActivity extends ListActivity {

	private String thu_or_chi;
	private TheLoaiAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		Bundle bundle = getIntent().getExtras();
		thu_or_chi = bundle.getString(Variable.THU_or_CHI);

		if (thu_or_chi.equalsIgnoreCase("thu")) {
			adapter = new TheLoaiAdapter(this, R.id.tvTheLoai_theloai,
					Variable.INCOME_THELOAI, Variable.ICONS_INCOME_THELOAI);
		} else {
			adapter = new TheLoaiAdapter(this, R.id.tvTheLoai_theloai,
					Variable.Expense_THELOAI, Variable.ICONS_Expense_THELOAI);
		}
		setListAdapter(adapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Intent intent = new Intent(TheLoaiActivity.this, ThuChiActivity.class);
		intent.putExtra(Variable.THELOAI, position);
		setResult(Activity.RESULT_OK, intent);

		// thoat kho the loai
		finish();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
		finish();
	}

}
