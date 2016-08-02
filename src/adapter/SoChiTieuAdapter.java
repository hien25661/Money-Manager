package adapter;

import java.util.ArrayList;
import java.util.List;

import com.moneylove.R;

import object.ThuChi;

import util.LayDate_Month_Yeah;
import util.Simple_method;
import util.Variable;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SoChiTieuAdapter extends ArrayAdapter<ThuChi> {

	private Context context;
	private List<ThuChi> thuchi;

	public SoChiTieuAdapter(Context context, int ResourceId, List<ThuChi> tc) {
		super(context, ResourceId, tc);
		this.context = context;
		this.thuchi = tc;
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView ivIcon;
		TextView tvSoTien;
		TextView tvTenGiaoDich;
		TextView tvNgayGiaoDich;
	}

	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		ViewHolder holder = null;
		ThuChi tc = getItem(position);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_so_thu_chi, null);
			holder = new ViewHolder();
			holder.tvTenGiaoDich = (TextView) rowView
					.findViewById(R.id.tv_list_TenGiaoDich);
			holder.tvSoTien = (TextView) rowView
					.findViewById(R.id.tv_list_Sotien);
			holder.ivIcon = (ImageView) rowView.findViewById(R.id.ivlist_Icon);
			// holder.tvNgayGiaoDich = (TextView) rowView
			// .findViewById(R.id.tv_list_NgayGiaoDich);

			rowView.setTag(holder);
		} else {
			// getTag lay gia tri holder phia tren
			holder = (ViewHolder) rowView.getTag();
		}

		holder.tvNgayGiaoDich.setText(new LayDate_Month_Yeah()
				.CovertString_toNgayThangNam(tc.getNgay_thu_chi()));

		holder.tvTenGiaoDich.setText(tc.getTen_giao_dich());
		float sotien = tc.getSo_tien();
		String _Sotien = String.valueOf(sotien);

		if ((int) Math.round(sotien) == sotien) {
			_Sotien = String.valueOf((int) sotien);
		}

		holder.tvSoTien.setText(_Sotien);

		if (tc.getMa_id().equals("thu")) {
			holder.ivIcon.setImageResource(Variable.ICONS_INCOME_THELOAI[tc
					.getTheloai()]);
			holder.tvSoTien.setBackgroundColor(Color.parseColor("#0099CC"));
		} else if (tc.getMa_id().equals("chi")) {
			holder.tvSoTien.setBackgroundColor(Color.parseColor("#CC3300"));
			holder.ivIcon.setImageResource(Variable.ICONS_Expense_THELOAI[tc
					.getTheloai()]);
			String noichuoiChi = "-" + _Sotien;
			holder.tvSoTien.setText(noichuoiChi);
		} else if (tc.getMa_id().equals("no")) {
			holder.ivIcon.setImageResource(Variable.ICON_NO);

			holder.tvSoTien.setBackgroundColor(Color.parseColor("#000000"));
			holder.tvTenGiaoDich.setText("Nợ " + tc.getTen_giao_dich());
		} else {
			holder.ivIcon.setImageResource(Variable.ICON_Vay);
			holder.tvSoTien.setBackgroundColor(Color.parseColor("#996633"));
			holder.tvTenGiaoDich.setText("Loan " + tc.getTen_giao_dich());
		}

		return rowView;
	}

}
