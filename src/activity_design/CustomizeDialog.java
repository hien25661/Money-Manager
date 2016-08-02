package activity_design;

import util.Variable;
import activity_child.ThuChiActivity;
import activity_child.VayNo_Activity;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;

import com.moneylove.R;

public class CustomizeDialog extends Dialog implements OnClickListener {

	private Button btnThu, btnNo, btnChi, btnChoVay;
	private ImageButton btnHuy;
	private Activity activity2;

	public CustomizeDialog(Activity activity) {
		super(activity,R.style.customDialogStype);
		this.activity2 = activity;
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.giaodich);

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

		btnThu = (Button) findViewById(R.id.btnThu_giaodich);
		btnThu.setOnClickListener(this);

		btnNo = (Button) findViewById(R.id.btnNo_giaodich);
		btnNo.setOnClickListener(this);

		btnChi = (Button) findViewById(R.id.btnChi_giaodich);
		btnChi.setOnClickListener(this);

		btnChoVay = (Button) findViewById(R.id.btnChoVay_giaodich);
		btnChoVay.setOnClickListener(this);

		btnHuy = (ImageButton) findViewById(R.id.btnHuyBo_giaodich);
		btnHuy.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		/** When OK Button is clicked, dismiss the dialog */
		switch (v.getId()) {
		case R.id.btnThu_giaodich: {
			Intent intent = new Intent(activity2, ThuChiActivity.class);
			intent.putExtra(Variable.request, Variable.requestcode_InsertThuChi);
			intent.putExtra(Variable.THU_or_CHI, "thu");
			activity2.startActivityForResult(intent,Variable.requestcode_InsertThuChi);
			activity2.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
			dismiss();
			break;
		}
		case R.id.btnChi_giaodich: {
			Intent intent = new Intent(activity2, ThuChiActivity.class);
			intent.putExtra(Variable.request, Variable.requestcode_InsertThuChi);
			intent.putExtra(Variable.THU_or_CHI, "chi");
			activity2.startActivityForResult(intent,Variable.requestcode_InsertThuChi);
			activity2.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
			dismiss();
			break;
		}
		case R.id.btnChoVay_giaodich: {
			 Intent intent = new Intent(activity2,VayNo_Activity.class);
			 intent.putExtra(Variable.request, Variable.requestcode_InsertVayNo);
			 intent.putExtra(Variable.VAY_or_NO, "vay"); // vay
			 activity2.startActivityForResult(intent,Variable.requestcode_InsertVayNo);
			 activity2.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
			 dismiss();
			break;
		}
		case R.id.btnNo_giaodich: {
			 Intent intent = new Intent(activity2,VayNo_Activity.class);
			 intent.putExtra(Variable.request, Variable.requestcode_InsertVayNo);
			 intent.putExtra(Variable.VAY_or_NO, "no"); // no
			 activity2.startActivityForResult(intent,Variable.requestcode_InsertVayNo);
			 activity2.overridePendingTransition(R.anim.anim_slide_in_left,R.anim.anim_slide_out_left);
			 dismiss();
			break;
		}
		case R.id.btnHuyBo_giaodich: {
			dismiss();
			break;
		}

		}// close switch

	}

}