package activity_child;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.doi_tien_te.Doi_Tien_Te;
import com.doi_tien_te.TienTeHandler;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.moneylove.R;

import object.ob_chart;
import thongke.ThayDoi_ThongKe_PieActivity;
import util.LayDate_Month_Yeah;
import util.Variable;
import vayno_activity.TinhChart;


import access_sql.Database;
import adapter.Mucluc_thongkeAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import chart.PieDetailsItem;
import chart.View_PieChart;

public class ThongKe_Activity extends Activity{
	List<PieDetailsItem> piedata = new ArrayList<PieDetailsItem>();
	TinhChart TinhToan = new TinhChart();
	private TextView tvTitel_chart,tvEmptyData_chart;
	private Button btnThayDoi_chart,Back_chart;
	private ListView listTheLoai_chart;
	private Intent intent;
	private LinearLayout llayout_chart;
	private Mucluc_thongkeAdapter adapter;

	//lay ngay va thang hien tai
 List<String> selectNgayThang = new ArrayList<String>();
	private LayDate_Month_Yeah lay_ngay = new LayDate_Month_Yeah();
	private String thu_or_chi = "chi";
	Database db = new Database(this);
	ImageView mImageView;
	View_PieChart piechart;
	List<Integer> theloais;
	List<Integer> tongtiens;
	Bitmap mBaggroundImage;
	LinearLayout finalLayout; 
	String tungay;
	String denngay;
	private void ConnetLayout() {
		tvTitel_chart = (TextView) findViewById(R.id.tvTitel_chart);
		tvEmptyData_chart = (TextView) findViewById(R.id.tvEmptyData_chart);
		llayout_chart = (LinearLayout) findViewById(R.id.llayout_chart);
		listTheLoai_chart = (ListView) findViewById(R.id.listTheLoai_chart);




		Back_chart = (Button) findViewById(R.id.Back_chart);
		btnThayDoi_chart = (Button) findViewById(R.id.btnThayDoi_chart);
		/*
		 * Listen su kien click
		 */
		btnThayDoi_chart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplication(),ThayDoi_ThongKe_PieActivity.class);
				startActivityForResult(intent, Variable.requestcode_MoRong);
			}
		});
		Back_chart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(Variable.requestcode_MoRong, intent);
				finish();
			}
		});
		/*listTheLoai_chart.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int positon,
					long arg3) {

			}
		});*/
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == Variable.requestcode_MoRong){
				Bundle bundle = data.getExtras();
				thu_or_chi= bundle.getString(Variable.THU_or_CHI);
				 tungay = bundle.getString("tuNgay");
				 denngay = bundle.getString("toiNgay");
				SimpleDateFormat dateTv = new SimpleDateFormat("dd/MM/yyyy");
				String trunggia;
				/*
				 *kiem tra tu ngay va ngay toi < hay > de dua du lieu vao dung
				 **/				try {
					Date dateTu = dateTv.parse(tungay);
					Date dateDen = dateTv.parse(denngay);
					if(dateTu.before(dateDen)){
						tvTitel_chart.setText(tungay+" - "+denngay);
						call_chartPie(thu_or_chi,lay_ngay.setInsertDataBase(tungay),lay_ngay.setInsertDataBase(denngay));
					}else{
						tvTitel_chart.setText(denngay+" - "+tungay);
						call_chartPie(thu_or_chi,lay_ngay.setInsertDataBase(denngay),lay_ngay.setInsertDataBase(tungay));
					}


				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


			}
		}

	}
	AdView adView;
	InterstitialAd interstitial;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart_pie);
		adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		// Prepare the Interstitial Ad
		interstitial = new InterstitialAd(this);
		// Insert the Ad Unit ID
		interstitial.setAdUnitId(getResources().getString(R.string.ads_id_interstis));
		// Request for Ads
		adRequest = new AdRequest.Builder().build();
		// Load ads into Interstitial Ads
		interstitial.loadAd(adRequest);
		interstitial.setAdListener(new AdListener() {
			// Listen for when user closes ad
			public void onAdClosed() {
			}
		});
		intent = getIntent();
		finalLayout = (LinearLayout) findViewById(R.id.pie_container);
		ConnetLayout();

		selectNgayThang = lay_ngay.selectThang(0);
		/*
		 * @selectNgayThang Tra ve 3 gia tri string 
		 * array.get(0) : thang 01/04 - 30/04
		 * array.get(1) : 2013-04-01 ngay dau tien cua thang
		 * array.get(2) : 2013-04-30 ngay cuoi cung cua thang
		 * 
		 */
		tvTitel_chart.setText(selectNgayThang.get(0));

		call_chartPie(thu_or_chi,selectNgayThang.get(1),selectNgayThang.get(2));

	}

	private void call_chartPie(String thu_or_chi2, String dateFrom, String dateTo) {
		/*
		 * get du lieu tu database
		 */
		ob_chart Chart = db.getdata_thongke(thu_or_chi2,dateFrom,dateTo);
		int maxCount = 0;
		int itemCount = 0;

		if (Chart == null) {// ko co du lieu set layout empty
			tvEmptyData_chart.setVisibility(View.VISIBLE);
			llayout_chart.setVisibility(View.GONE);
			// Log.i("TAG","out doi muc thong ke ");
		} else {// co du lieu
			tvEmptyData_chart.setVisibility(View.GONE);
			llayout_chart.setVisibility(View.VISIBLE);
			theloais = new ArrayList<Integer>();
			tongtiens = new ArrayList<Integer>();
			theloais = Chart.getKQTheloai();
			tongtiens = Chart.getKQTong();


			//int list_phanTram[] = TinhToan.TinhPhanTram(tongtiens);
			int colors[] = TinhToan.list_RandomColor(theloais.size());

			adapter = new Mucluc_thongkeAdapter(this,R.id.tv_list_TenGiaoDich,theloais,tongtiens,colors,thu_or_chi2);
			listTheLoai_chart.setAdapter(adapter);
			// -----------------------------------------
			PieDetailsItem item;
			for (int i = 0; i < tongtiens.size(); i++) {
				itemCount = tongtiens.get(i);
				item = new PieDetailsItem();
				item.count = itemCount;
				// item.label=itemslabel[i];
				item.color = colors[i];
				piedata.add(item);
				maxCount = maxCount + itemCount;
			}
			int size = 300;
			int BgColor = Color.WHITE;

			mBaggroundImage = Bitmap.createBitmap(size, size,
					Bitmap.Config.ARGB_8888);

			piechart = new View_PieChart(this);

			piechart.setLayoutParams(new LayoutParams(size, size));
			piechart.setGeometry(size, size, 2, 2, 2, 2, 2130837504);
			piechart.setSkinparams(BgColor);

			piechart.setData(piedata, maxCount);

			piechart.invalidate();

			piechart.draw(new Canvas(mBaggroundImage));
			piechart = null;
			mImageView = new ImageView(this);
			mImageView.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			mImageView.setBackgroundColor(BgColor);

			mImageView.setImageBitmap(mBaggroundImage);
			finalLayout.removeAllViews();
			finalLayout.addView(mImageView);
		}		
	}

	/*public int RandomColor() {
		return Color.rgb((int) (Math.random() * 256),
				(int) (Math.random() * 256), (int) (Math.random() * 256));
	}*/
	



}
