package activity_design;

import com.moneylove.R;

import object.ThuChi;
import util.Simple_method;
import access_sql.Access_VayNo;
import android.R.bool;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Dialog_DaTra extends Dialog implements android.view.View.OnClickListener{
	private ThuChi vn ;
	private Context context;
	EditText edtSotien_dialogpay;
	Button btnDatra_dialogpayed,btnDong_dialogpayed;

	
	public Dialog_DaTra(Context context,ThuChi vn) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_sotien_payed);
		this.getWindow().setLayout(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		this.vn =vn;
		this.setTitle(null);
		this.context = context;

		btnDatra_dialogpayed = (Button) findViewById(R.id.btnDatra_dialogpayed);
		btnDong_dialogpayed = (Button) findViewById(R.id.btnDong_dialogpayed);
		edtSotien_dialogpay = (EditText) findViewById(R.id.edtSotien_dialogpay);

		btnDatra_dialogpayed.setOnClickListener(this);

		btnDong_dialogpayed.setOnClickListener(this);
		edtSotien_dialogpay.setText(new Simple_method().KiemtraSoFloat_Int(vn.getSo_tien()));

		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDatra_dialogpayed:{
			try {
				String d = edtSotien_dialogpay.getText().toString().trim();
				//Toast.makeText(context,"editSotien"+ d,1).show();
				float sotienEdt = Float.valueOf(d);
				if(sotienEdt>vn.getSo_tien()){
					edtSotien_dialogpay.setText("");
					Toast.makeText(context,"Lỗi Nhập, Số tiền nhỏ hơn hoặc bằng " + new Simple_method().KiemtraSoFloat_Int(vn.getSo_tien()),1).show();
				}else{
					new Access_VayNo(context).DaTraTienVayNo(vn, sotienEdt);
					dismiss();
				}
			} catch (Exception e) {
				Toast.makeText(context,"Số tiền không được bỏ trống",1).show();
			}
		}
		break;

		case R.id.btnDong_dialogpayed:{
			dismiss();
		}
		break;
		}
	}

}
