package com.doi_tien_te;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import util.CodeHeThong;
import util.Simple_method;

import com.boxtimer365.moneylove.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class Doi_Tien_Te extends Activity implements OnItemSelectedListener, OnTabChangeListener {
	TextView name, code, buy, transfer, sell, Kq;
	EditText tfTien;
	Spinner spList;
	Button ok;

	Intent intent;
	float giaTrans;
	TienTeHandler handler =null;
	TabHost tabHost;
	List<String> listItemChangedMoney = new ArrayList<String>();
	private Button btnBack_tiente;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		intent = getIntent();
		/*if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = 
					new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}		*/		
		setContentView(R.layout.doi_tien_te);

		tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();
		TabHost.TabSpec spec;
		// -------------TAB
		// 1--------------------------------------------------------------------

		spec = tabHost.newTabSpec("tag1");
		spec.setContent(R.id.tab1_tiente);
		spec.setIndicator("Currency List");
		tabHost.addTab(spec);
		currencyList();
		
			new RssLoadingTask().execute();

		// -----------------TAB
		// 2----------------------------------------------------------------

		spec = tabHost.newTabSpec("tag2");
		spec.setIndicator("Calculate Exchange");
		spec.setContent(R.id.tab2_tiente);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
		tabHost.setOnTabChangedListener(this);
	}
	private class RssLoadingTask extends AsyncTask<Void, Void, Void> {
		private final ProgressDialog dialog = new ProgressDialog(Doi_Tien_Te.this); 
		StringBuilder a,b,c,d,e =null;
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Wait \n Connet Internet");
			this.dialog.show();
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
		protected Void doInBackground(Void... s) {
			//check internet
			readRss();
			return null;
		}
		private void readRss() {
			String nameURL= "http://www.vietcombank.com.vn/exchangerates/ExrateXML.aspx";
			try {
				URL url =null;
				url = new URL(nameURL);

				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser =null;
				saxParser = factory.newSAXParser();

				XMLReader reader = null;

				reader = saxParser.getXMLReader();

				handler = new TienTeHandler();
				reader.setContentHandler(handler);

				reader.parse(new InputSource(url.openStream()));

			} catch (Exception e) {
				Log.e("TAG","loi url DoiTienTe TienTeHandler "+ e.getMessage());
			}
			a = new StringBuilder();
			for (int i = 0; i < handler.getNameMoney().size(); i++) {

				a.append(" " + String.valueOf(handler.getNameMoney().get(i))
						+ " \n\n");
			}
			b = new StringBuilder();
			for (int i = 0; i < handler.getCodeMoney().size(); i++) {

				b.append(" " + String.valueOf(handler.getCodeMoney().get(i))
						+ "\n\n");
			}
			c = new StringBuilder();
			for (int i = 0; i < handler.getBuyMoney().size(); i++) {

				c.append(" " + String.valueOf(handler.getBuyMoney().get(i))
						+ "\n\n ");
			}
			d = new StringBuilder();
			for (int i = 0; i < handler.getTransferMoney().size(); i++) {

				d.append(" "
						+ String.valueOf(handler.getTransferMoney().get(i))
						+ "\n\n ");
			}
			e = new StringBuilder();
			for (int i = 0; i < handler.getSellMoney().size(); i++) {

				e.append(" " + String.valueOf(handler.getSellMoney().get(i))
						+ " \n\n");
			}
			for (int i = 0; i < handler.getCodeMoney().size(); i++) {

				listItemChangedMoney.add("   "
						+ handler.getCodeMoney().get(i) + " - "
						+ handler.getNameMoney().get(i));
			}


		}


		@Override
		protected void onPostExecute(Void result) {
			if(this.dialog.isShowing()){
				this.dialog.dismiss();
			}		
			name.setText(a);
			code.setText(b);
			buy.setText(c);
			transfer.setText(d);
			sell.setText(e);
		}
	}

	public void currencyList() 
	{	

		name = (TextView) findViewById(R.id.txtName);
		code = (TextView) findViewById(R.id.txtCode);
		buy = (TextView) findViewById(R.id.txtBuy);
		transfer = (TextView) findViewById(R.id.txtTransfer);
		sell = (TextView) findViewById(R.id.txtSell);
		btnBack_tiente = (Button) findViewById(R.id.back_tiente);
		btnBack_tiente.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(Activity.RESULT_CANCELED, intent);
				finish();
			}
		});


	}

	// -----------------TAB
	// 2----------------------------------------------------------------

	private void addItemsOnListCurr() {
		spList = (Spinner) findViewById(R.id.spinCurr);
		//List<String> list = new ArrayList<String>();

		spList.setSelection(-1);
		tfTien = (EditText) findViewById(R.id.editTien);
		Kq = (TextView) findViewById(R.id.txtKq);


		//	list.addAll(listItem);

		ArrayAdapter dataAdapter = new ArrayAdapter(this,
				android.R.layout.simple_spinner_item, listItemChangedMoney);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spList.setAdapter(dataAdapter);


		spList.setOnItemSelectedListener(this);

		ok = (Button) findViewById(R.id.btnExchange);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String text;
				int giatri_nhap;
				float tong;

				text = tfTien.getText().toString();
				if (text.equals("")) {
					AlertDialog diaBox = makeAndShowDialogBox();
					diaBox.show();
				} else {
					giatri_nhap = Integer.parseInt(text);
					tong = (giatri_nhap * giaTrans);
					Kq.setText(new Simple_method().KiemtraSoFloat_Int(tong));
				}
			}
		});


	}
	
	private AlertDialog makeAndShowDialogBox() {

		AlertDialog myDialog = new AlertDialog.Builder(this)
		// set message, title, and icon
		.setTitle("An error occurred")
		.setMessage("You have not entered enough information!")
		.setIcon(R.drawable.error)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		}).create();
		return myDialog;
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		giaTrans = Float.valueOf(handler.getTransferMoney().get(position));
	/*
	 * Code thay doi mau khi chua an spinter (ko co doan duoi spinter do mau den => chuyen mau trang cho de nhin
	*/	
		 ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
	}

	@Override
	public void onTabChanged(String arg0) {
		switch (tabHost.getCurrentTab()) {
		case 0:{
			
			break;
		}
		case 1:{
			addItemsOnListCurr();

			break;
		}
		}//close switch		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {


	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
		finish();
	}

}