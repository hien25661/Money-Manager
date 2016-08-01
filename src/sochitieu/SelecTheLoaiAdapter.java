package sochitieu;

import java.util.ArrayList;

import util.Simple_method;
import util.Variable;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moneylove.R;

public class SelecTheLoaiAdapter extends ArrayAdapter<Item> {

	private Context context;
	private ArrayList<Item> items;
	private LayoutInflater vi;

	public SelecTheLoaiAdapter(Context context, ArrayList<Item> items) {
		super(context, 0, items);
		this.context = context;
		this.items = items;
		vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

				if (holder.tvTenGiaoDich != null){
					if (ei.getTc().getMa_id().equals("thu")) {
						holder.tvTenGiaoDich.setText(Variable.INCOME_THELOAI[ei.getTc().getTheloai()]);
					} else {
						holder.tvTenGiaoDich.setText(Variable.Expense_THELOAI[ei.getTc().getTheloai()]);
					} 
				}
				/*
				 * Xet Icon
				 */
				if (ei.getTc().getMa_id().equals("thu")) {
					holder.ivIcon.setImageResource(Variable.ICONS_INCOME_THELOAI[ei.getTc().getTheloai()]);
					holder.tvSoTien.setBackgroundColor(context.getResources().getColor(R.color.mauXanh_Thu));
					holder.tvSoTien.setText(new Simple_method().KiemtraSoFloat_Int(ei.getTc().getSo_tien()));
				} else {
					holder.tvSoTien.setBackgroundColor(context.getResources().getColor(R.color.mauDo_Chi));
					holder.ivIcon.setImageResource(Variable.ICONS_Expense_THELOAI[ei.getTc().getTheloai()]);
					holder.tvSoTien.setText(new Simple_method().Chi_KiemtraSoFloat_Int(ei.getTc().getSo_tien()));

				} 
			}
		}
		return v;
	}

}
