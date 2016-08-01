package sochitieu;

import java.util.ArrayList;

import util.Simple_method;
import util.Variable;

import com.moneylove.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EntryAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

	public EntryAdapter(Context context, ArrayList<Item> items) {
		super(context, 0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/* private view holder class */
	private class ViewHolder {
		ImageView ivIcon;
		TextView tvSoTien;
		TextView tvTenGiaoDich;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;

		final Item i = items.get(position);
		if (i != null) {
			if (i.isSection()) {
				SectionItem si = (SectionItem) i;
				v = vi.inflate(R.layout.list_item_section, null);

				v.setOnClickListener(null);
				v.setOnLongClickListener(null);
				v.setLongClickable(false);

				final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
				sectionView.setText(si.getTitle());
			} else {
				EntryItem ei = (EntryItem) i;
				v = vi.inflate(R.layout.list_so_thu_chi, null);
				holder = new ViewHolder();

				holder = new ViewHolder();
				holder.tvTenGiaoDich = (TextView) v.findViewById(R.id.tv_list_TenGiaoDich);
				holder.tvSoTien = (TextView) v.findViewById(R.id.tv_list_Sotien);
				holder.ivIcon = (ImageView) v.findViewById(R.id.ivlist_Icon);

				//if (holder.tvTenGiaoDich != null)
				if (holder.tvSoTien != null)
					holder.tvSoTien.setText(new Simple_method()
					.KiemtraSoFloat_Int(ei.getTc().getSo_tien()));
				/*
				 * Xet Icon
				 */
				if (ei.getTc().getMa_id().equals("thu")) {
					holder.tvTenGiaoDich.setText(ei.getTc().getTen_giao_dich());
					holder.ivIcon
					.setImageResource(Variable.ICONS_INCOME_THELOAI[ei
					                                                .getTc().getTheloai()]);
					holder.tvSoTien.setBackgroundColor(context.getResources()
							.getColor(R.color.mauXanh_Thu));
				} else if (ei.getTc().getMa_id().equals("chi")) {
					holder.tvTenGiaoDich.setText(ei.getTc().getTen_giao_dich());
					holder.tvSoTien.setBackgroundColor(context.getResources().getColor(R.color.mauDo_Chi));
					holder.ivIcon.setImageResource(Variable.ICONS_Expense_THELOAI[ei
					                                                 .getTc().getTheloai()]);
					holder.tvSoTien.setText(new Simple_method().Chi_KiemtraSoFloat_Int(ei.getTc().getSo_tien()));
				} else if (ei.getTc().getMa_id().equals("no")) {
					holder.ivIcon.setImageResource(Variable.ICON_NO);

					holder.tvSoTien.setBackgroundColor(context.getResources()
							.getColor(R.color.mauDen_No));
					holder.tvTenGiaoDich.setText("Nợ "
							+ ei.getTc().getTen_giao_dich());
				} else if (ei.getTc().getMa_id().equals("vay")) {
					holder.ivIcon.setImageResource(Variable.ICON_Vay);
					holder.tvSoTien.setBackgroundColor(context.getResources()
							.getColor(R.color.mauCam_Vay));
					holder.tvTenGiaoDich.setText("Cho vay "
							+ ei.getTc().getTen_giao_dich());
				}else if (ei.getTc().getMa_id().equals(Variable.DaTraVay)) {
					holder.ivIcon.setImageResource(Variable.ICON_Vay);
					holder.tvSoTien.setBackgroundColor(context.getResources()
							.getColor(R.color.mauCam_Vay));
					holder.tvTenGiaoDich.setText("Cho vay "
							+ ei.getTc().getTen_giao_dich());
					holder.tvTenGiaoDich.setPaintFlags( holder.tvTenGiaoDich.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				}else if (ei.getTc().getMa_id().equals(Variable.DaTraNo)) {
					holder.ivIcon.setImageResource(Variable.ICON_NO);
					holder.tvSoTien.setBackgroundColor(context.getResources().getColor(R.color.mauDen_No));
					holder.tvTenGiaoDich.setText("Nợ "
							+ ei.getTc().getTen_giao_dich());
					holder.tvTenGiaoDich.setPaintFlags( holder.tvTenGiaoDich.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
					
				}else {//truong hop qua khu tra no
					holder.ivIcon.setVisibility(View.GONE);
					if(ei.getTc().getTen_giao_dich().equals("no")){
						holder.tvSoTien.setBackgroundColor(context.getResources()
								.getColor(R.color.mauDen_No));
					}else{
						holder.tvSoTien.setBackgroundColor(context.getResources()
								.getColor(R.color.mauCam_Vay));
					}
					holder.tvTenGiaoDich.setText(R.string.sotiendatra);
				}
			}
		}
		return v;
	}

}
