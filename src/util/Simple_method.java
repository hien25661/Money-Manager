package util;

import java.text.NumberFormat;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.TextView;

import com.boxtimer365.moneylove.R;

public class Simple_method {
	public String KiemtraSoFloat_Int(float sotien) {
		String _Sotien = String.valueOf(sotien);
		Double d= Double.parseDouble(_Sotien);
		String formated = NumberFormat.getInstance().format((d));
		return formated;
	}

	public String KiemtraSoString_toString(String sotien) {
		if(sotien.length()>3){
			String cleanString = sotien.replaceAll("[,]", "");
			Double d= Double.parseDouble(cleanString);
			String formated = NumberFormat.getInstance().format((d));
			return formated;	
		}
		return sotien;
	}
	public String KiemtraSoDouble(Double sotien) {
		String formated = NumberFormat.getInstance().format((sotien));
		return formated;
	}
	public String KiemtraSoInt(int sotien) {
		String _Sotien = String.valueOf(sotien);

		Double d= Double.parseDouble(_Sotien);
		String formated = NumberFormat.getInstance().format((d));
		// lam tron so
		/*if ((int) Math.round(sotien) == sotien) {
			_Sotien = String.valueOf((int) sotien);
		}*/

		return formated;
	}
	public String Chi_KiemtraSoFloat_Int(float sotien) {

		String _Sotien = String.valueOf(sotien);
		// lam tron so
		if(sotien == 0){
			return "0";
		}
		try {
			Double d= Double.parseDouble(_Sotien);
			String formated = NumberFormat.getInstance().format((d));

			/*	if ((int) Math.round(sotien) == sotien) {
				_Sotien = String.valueOf((int) sotien);
			}*/
			_Sotien = "-" +formated;
			return _Sotien;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	public String LamTronSoFloat(float sotien) {
		// lam tron so
		if ((int) Math.round(sotien) == sotien) {
			return  String.valueOf((int) sotien);
		}
		return String.valueOf(sotien);
	}
	public String LamTronSoFloat(double sotien) {
		// lam tron so
		if ((int) Math.round(sotien) == sotien) {
			return  String.valueOf((int) sotien);
		}
		String chuoi = String.format("%.2f", sotien);
		if(chuoi.length()>3){
			String cleanString = chuoi.replaceAll("[,]", "");
			Double d= Double.parseDouble(cleanString);
			String formated = NumberFormat.getInstance().format((d));
			return formated;	
		}
		return chuoi;
	}
	//nhap 4,200,3.21 => 42003.21
	public String ThayDoi_Phay(String sotien){ //ham tra ve chuoi 
		String cleanString = sotien.replaceAll("[,]", "");
		return cleanString;
	}
	
}
