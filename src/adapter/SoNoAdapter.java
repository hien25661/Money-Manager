package adapter;

import java.util.ArrayList;
import java.util.List;

import com.moneylove.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SoNoAdapter extends ArrayAdapter<String> {

	private Context context;
	private ArrayList<String> vayno;
	
	public SoNoAdapter(Context context, int ResourceId,ArrayList<String> vn) {
		super(context, ResourceId, vn);
		this.context = context;
		this.vayno = vn;
	}

	/* private view holder class */
	private class ViewHolder {
		TextView tvTenGiaoDich;
	}

	@Override
	public View getView(int position, View rowView, ViewGroup parent) {
		ViewHolder holder;
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		
		String vnS = getItem(position);
			if (rowView == null) {
				rowView = inflater.inflate(R.layout.adapter_so_no, null);
				holder = new ViewHolder();
				holder.tvTenGiaoDich = (TextView) rowView.findViewById(R.id.tvlist_SoNo);
				rowView.setTag(holder);
			} else {
				// getTag lay gia tri holder phia tren
				holder = (ViewHolder) rowView.getTag();
			}
			//thuc hien gan vao listview
			holder.tvTenGiaoDich.setText(vnS);
		
		return rowView;
	}

}
