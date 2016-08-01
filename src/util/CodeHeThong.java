package util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class CodeHeThong {
	public void hideSoftKeyBoard(Activity activity){
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()){
			imm.hideSoftInputFromWindow (activity.getWindow().getCurrentFocus().getWindowToken(), 0);
		}
	}
	
	
	public boolean checkInternetMobile(Context con){
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);   
            if(wifiInfo.isConnected()|| mobileInfo.isConnected())
            {
                return true;
            }
        }
        catch(Exception e){
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
        }
         
        return false;
    }   
	
	public String getSoDienThoai_fromNameContact(String name,Context context){
		try {
			String ret = null;
			String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
			String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
			Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
			        projection, selection, null, null);
			if (c.moveToFirst()) {
			    ret = c.getString(0);
			}
			c.close();
			
			return ret;
		} catch (Exception e) {
			Log.e("TAG","Loig CodeHeThong.class->getSoDienThoai_fromNameContact "+e.getMessage());
		}
		return null;
	}
}//close codeHeThong
