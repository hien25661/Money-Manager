package export_cvs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import util.LayDate_Month_Yeah;

import com.moneylove.R;

import access_sql.Access_VayNo;
import access_sql.Database;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import application.MySharedPreference;

public class ExportActivity extends Activity{
	File file ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.thongke_theo_thang);
		try
		{
			new ExportDatabaseCSVTask().execute();
		}

		catch(Exception ex)
		{
			Log.e("Error in MainActivity ",ex.getMessage());
		}
		
		
		
	}

	private class ExportDatabaseCSVTask extends AsyncTask<String, Void, Boolean> 
	{
		private final ProgressDialog dialog = new ProgressDialog(ExportActivity.this); 
		// can use UI thread here
		@Override
		protected void onPreExecute() 
		{
			this.dialog.setMessage("Exporting database...");
			this.dialog.show();
		}   
		// automatically done on worker thread (separate from UI thread)
		protected Boolean doInBackground(final String... args) 
		{
			//File dbFile=getDatabasePath("excerDB.db");

			File exportDir = new File(Environment.getExternalStorageDirectory(), "");        
			if (!exportDir.exists()) 
			{
				exportDir.mkdirs();
			}
			file = new File(exportDir, "MoneyManager.csv");
			try 
			{
				file.createNewFile();                
				CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
				Database db = new Database(ExportActivity.this);
				List<String[]> mang = new ArrayList<String[]>();
				mang = db.exportCSV();
				int i =0;
				if(mang != null){
					while(i!=mang.size())
					{
						csvWrite.writeNext(mang.get(i));
						i++;
					}
					csvWrite.close();
					//Log.e("TAG",file.toString());	
					/*
					 * get account dia chi email
					*/
					/*GetAccount getacc= new GetAccount();
					String email = getacc.getEmail(getApplicationContext());
					if (email!=null) {*/
					/*	Uri u1  =   null;
						u1  =   Uri.fromFile(file);
						Intent sendIntent = new Intent(Intent.ACTION_SEND);
						sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Money manager"+new LayDate_Month_Yeah().NgayThangNamCurrent());
						sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"phuchau265@gmail.com"});
						sendIntent.putExtra(Intent.EXTRA_STREAM, u1);
						sendIntent.setType("text/html");
						startActivity(Intent.createChooser(sendIntent,"Check email"));    */
						//	}
					return true;
				}
			}
			catch(SQLException sqlEx)
			{
				Log.e("ExportDatabase", sqlEx.getMessage(), sqlEx);
				return false;                
			}
			catch (IOException e) 
			{
				Log.e("ExportDatabase", e.getMessage(), e);
			}
			return false;
		}

		// can use UI thread here
		@Override
		protected void onPostExecute(final Boolean success) 
		{
			if (this.dialog.isShowing()) 
			{
				this.dialog.dismiss();
			}
			if (success) 
			{
				AlertDialog dilg =  makeDilog_SendMail();
				dilg.show();
				Toast.makeText(getApplicationContext(), "Export successful! check SD card: "+file.toString(), Toast.LENGTH_LONG).show();
			} 
			else 
			{
				Toast.makeText(getApplicationContext(), "Export failed", Toast.LENGTH_SHORT).show();
			}

		}

	}
	
	
	private AlertDialog makeDilog_SendMail() {
		final EditText input = new EditText(this);
		input.setHint("Enter your email");
		AlertDialog myDialogBox = new AlertDialog.Builder(this)	
		// set message, title, and icon
		.setTitle("Export Success").setMessage("Do you want to send the file via email?"+file.toString())
		 .setView(input)
		.setPositiveButton("Send email",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				if(input.getText().toString().length()!=0){
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
				else{
					Toast.makeText(getApplicationContext(), "Error send mail", Toast.LENGTH_LONG).show();
				}
				
			}

		})// setPositiveButton

		.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
			
			}
		})// setNegativeButton

		.create();

		return myDialogBox;
	}
	
}
