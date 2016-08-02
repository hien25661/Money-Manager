package import_cvs;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import object.QuaKhu_VayNo_Object;
import object.ThuChi;
import util.Variable;



import access_sql.Access_VayNo;
import access_sql.Database;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import application.MyApplication;

import com.boxtimer365.moneylove.R;


public class ImportActivity extends Activity{
	File file ;
	TextView tv;
	StringBuilder chuoi =  new StringBuilder();
	List<ThuChi> thuchi =  new ArrayList<ThuChi>();//dung bang thu chi
	List<ThuChi> vayno =  new ArrayList<ThuChi>();//dung bang vay no
	List<QuaKhu_VayNo_Object> quakhu = new ArrayList<QuaKhu_VayNo_Object>(); //dung bang quakhu
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		tv = (TextView ) findViewById(R.id.tvImport);
		try
		{
			new ImportDatabaseCSVTask().execute();
		}

		catch(Exception ex)
		{
			Log.e("Error in MainActivity ",ex.getMessage());
		}
	}

	private class ImportDatabaseCSVTask extends AsyncTask<Void, Void, Boolean> {
		private final ProgressDialog dialog = new ProgressDialog(ImportActivity.this); 
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Import database...");
			this.dialog.show();
		}
		@Override
		protected void onProgressUpdate(Void... values) {
		}
		@Override
		protected Boolean doInBackground(Void... s) {
			String SDcardPath = Environment.getExternalStorageDirectory().getPath();
			String fileName = SDcardPath+"/MoneyManager.csv";
			try {
				//List<String> thuchi =  new ArrayList<String>();
				FileReader reader = new FileReader(fileName);
				CSVReader csvReader = new CSVReader(reader);
				
				boolean ktBangQuaKhu=false;
				String[] row = null;
				while((row = csvReader.readNext()) != null) {
					//bang thuchi
				    if(row[1].equals("thu")||row[1].equals("chi")){ //ma_id la thu hay chi
						// lay gia tri tu ma_id 
				    	ThuChi tc =  new ThuChi(row[1],row[2],Float.parseFloat(row[3]),row[4],row[5],Integer.parseInt(row[6]));
						thuchi.add(tc);
					}//bang vayno
				    else if(row[1].equals("vay")||row[1].equals("no")||row[1].equals(Variable.DaTraNo)||row[1].equals(Variable.DaTraVay)){
						ThuChi vn =  new ThuChi(Integer.parseInt(row[0]),row[1],row[2],Float.parseFloat(row[3]),row[4],row[5],row[7]);
						vayno.add(vn);
					}//bang QuaKhu_VayNo check colum tiengiaodich
				    else if(ktBangQuaKhu){
				    	if(row[2].equals("vay")||row[2].equals("no")){
				    		QuaKhu_VayNo_Object qk = new QuaKhu_VayNo_Object(Integer.parseInt(row[1]),row[2],Float.parseFloat(row[3]),row[4]);
							quakhu.add(qk);
				    	}
				    }else if(row[0].equals("BangQuaKhu")){
				    	//sau BangQuaKhu so lieu cua bang qua khu
				    	ktBangQuaKhu = true;
				    }else if(row[0].equals("TongTien")){
				    	MyApplication.tongtien = Float.parseFloat(row[3]);
				    }
				}
				csvReader.close(); //dong ket noi
				reader.close();
				//ket thuc viec doc csv/////////
				/////////////////////
				//thuc hien viec insert vao database
				//insert bangThuChi
				Database db = new Database(getApplicationContext());
				Access_VayNo dbVayNo = new Access_VayNo(getApplicationContext());
				for (int i = 0; i < thuchi.size(); i++) {
					db.insertThuChi(thuchi.get(i));
				}
				for (int i = 0; i < vayno.size(); i++) {
					dbVayNo.insertVayNo_csv(vayno.get(i));
				}
				for (int i = 0; i < quakhu.size(); i++) {
					dbVayNo.insertQuaKhu(quakhu.get(i));
				}
				
				
				
				return true;
				
			}catch (Exception e) {
				Log.e("TAG","Loi ImportActivity "+e.getMessage());

			}
			return false;
		}
		@Override
		protected void onPostExecute(Boolean result) {
			if(this.dialog.isShowing()){
				this.dialog.dismiss();
			}		
			if (result) 
			{
				/*AlertDialog dilg =  makeDilog_SendMail();
				dilg.show();*/
				Toast.makeText(getApplicationContext(), "Import successful! ", Toast.LENGTH_LONG).show();
			} 
			else 
			{
				Toast.makeText(getApplicationContext(), "Import failed", Toast.LENGTH_LONG).show();
			}
			finish();



		}
	}


	/*	private AlertDialog makeDilog_SendMail() {
		final EditText input = new EditText(this);
		input.setHint("Nhập địa chỉ email");
		AlertDialog myDialogBox = new AlertDialog.Builder(this)	
		// set message, title, and icon
		.setTitle("Export Thành Công").setMessage("Bạn có muốn gửi file qua email ko ?")
		.setView(input)
		.setPositiveButton("Gửi email",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				Uri u1  =   null;
				u1  =   Uri.fromFile(file);
				Log.e("TAG", "Dia chi dng dan" + u1.toString());
				Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
				// sendIntent.setType("text/html");
				sendIntent.setType("application/csv");
				sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { input.getText().toString() });
				sendIntent.putExtra(Intent.EXTRA_SUBJECT, "MoneyManager");
				sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
				startActivity(Intent.createChooser(sendIntent, "Send Mail"));

			}

		})// setPositiveButton

		.setNegativeButton("Không",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {

			}
		})// setNegativeButton

		.create();

		return myDialogBox;
	}*/

}
