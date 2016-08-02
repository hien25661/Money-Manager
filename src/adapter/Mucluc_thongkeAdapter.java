package adapter;

import java.util.List;

import util.Simple_method;
import util.Variable;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.boxtimer365.moneylove.R;

public class Mucluc_thongkeAdapter extends ArrayAdapter<Integer> {

	private Context context;
	private List<Integer> tenMucluc;
	private int[] mauBackground;
	private List<Integer> tongtien;

	private String kttheloai;

	public Mucluc_thongkeAdapter(Context context,int textViewResourceId,List<Integer> list_theloai,List<Integer> tongtien, int[] colors, String thu_or_chi2) {
		super(context,textViewResourceId,list_theloai);
		this.context = context;
		this.tenMucluc = list_theloai;
		this.kttheloai = thu_or_chi2;
		this.tongtien = tongtien;
		this.mauBackground=colors;
	}
	/* private view holder class */
	private class ViewHolder {
		TextView tvTenTheLoai;
		TextView tvSoTien;
		ImageView ivIcon;
	}
	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_so_thu_chi, parent, false);
			holder = new ViewHolder();
			holder.tvTenTheLoai =  (TextView) rowView.findViewById(R.id.tv_list_TenGiaoDich);
			holder.tvSoTien =  (TextView) rowView.findViewById(R.id.tv_list_Sotien);
			holder.ivIcon =  (ImageView) rowView.findViewById(R.id.ivlist_Icon);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		//thuc hien gan vao listview
		if(kttheloai.equals("chi")){//true la chi 
			holder.tvTenTheLoai.setText(Variable.Expense_THELOAI[tenMucluc.get(position)]);
			holder.ivIcon.setImageResource(Variable.ICONS_Expense_THELOAI[tenMucluc.get(position)]);
			holder.tvSoTien.setText(new Simple_method().KiemtraSoInt(tongtien.get(position)));
			holder.tvSoTien.setBackgroundColor(mauBackground[position]);
		}else{//false la thu
			holder.tvTenTheLoai.setText(Variable.INCOME_THELOAI[tenMucluc.get(position)]);
			holder.ivIcon.setImageResource(Variable.ICONS_INCOME_THELOAI[tenMucluc.get(position)]);
			holder.tvSoTien.setText(new Simple_method().KiemtraSoInt(tongtien.get(position)));
			holder.tvSoTien.setBackgroundColor(mauBackground[position]);
		}
		// tv.setBackgroundColor(iconMau[position]);

		return rowView;

	}
}
