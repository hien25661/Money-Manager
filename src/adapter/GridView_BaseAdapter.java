package adapter;

import com.moneylove.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridView_BaseAdapter extends BaseAdapter {

	private Context context;
	private final String[] quanlyValues;
	private final int[] iv_gridview;

	public GridView_BaseAdapter(Context context, String[] quanlyValues, int[] iv) {
		this.context = context;
		this.quanlyValues = quanlyValues;
		this.iv_gridview = iv;
	}

	@Override
	public int getCount() {

		return quanlyValues.length;
	}

	@Override
	public Object getItem(int arg0) {

		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	private class ViewHolder {
		ImageView imageView;
		TextView textView;
	}

	@Override
	public View getView(int position, View rowView, ViewGroup arg2) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;
		if (rowView == null) {
			gridView = new View(context);
			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.items_gridview, null);
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.tv_quanly);
			textView.setText(quanlyValues[position]);

			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.iv_quanly);
			imageView.setImageResource(iv_gridview[position]);

		} else {
			gridView = (View) rowView;
		}

		return gridView;
	}

}
