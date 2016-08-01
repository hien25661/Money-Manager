package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.moneylove.R;

public class TheLoaiAdapter extends ArrayAdapter<String> {

	private Context context;
	private String[] theloai;
	private int[] iconTheLoai;

	public TheLoaiAdapter(Context context, int textViewResourceId,
			String[] theloai, int[] iconTheloai) {
		super(context, textViewResourceId, theloai);
		this.context = context;
		this.theloai = theloai;
		this.iconTheLoai = iconTheloai;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.theloai, parent, false);

		ImageView iv = (ImageView) rowView
				.findViewById(R.id.ivtheloai_Icon_theloai);
		TextView tv = (TextView) rowView.findViewById(R.id.tvTheLoai_theloai);

		iv.setImageResource(iconTheLoai[position]);
		tv.setText(theloai[position]);

		return rowView;

	}

}
