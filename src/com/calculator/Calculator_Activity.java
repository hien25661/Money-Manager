package com.calculator;


import util.Simple_method;

import com.moneylove.*;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.*;

public class Calculator_Activity extends Dialog implements OnClickListener {

	TextView txtInput;
	double result;
	/*double so1;
	double so2;*/

	String curResult = "";
	char pheptoan = ' ';
	boolean loiError = true;
	String inDigit;
	Simple_method simple = new Simple_method();
	//Button btn0,btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9;
	Button btnClear, btnDel, btnDiv, btnAdd, btnSub, btnMul, btnEqual, btnPoin,
	btnDone;

	public Calculator_Activity(Context context,String sotien) {
		super(context,R.style.customDialogStype);
		/** 'Window.FEATURE_NO_TITLE' - Used to hide the title */
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/** Design the dialog in main.xml file */
		setContentView(R.layout.calculator_layout);
		getWindow().setLayout(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		getWindow().setGravity(Gravity.CENTER);
		getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

		txtInput = (TextView) findViewById(R.id.txtInput);
		txtInput.setText(sotien);
		
		curResult = sotien;
		
		((Button) findViewById(R.id.btn0)).setOnClickListener(this);
		((Button) findViewById(R.id.btn1)).setOnClickListener(this);
		((Button) findViewById(R.id.btn2)).setOnClickListener(this);
		((Button) findViewById(R.id.btn3)).setOnClickListener(this);
		((Button) findViewById(R.id.btn4)).setOnClickListener(this);
		((Button) findViewById(R.id.btn5)).setOnClickListener(this);
		((Button) findViewById(R.id.btn6)).setOnClickListener(this);
		((Button) findViewById(R.id.btn7)).setOnClickListener(this);
		((Button) findViewById(R.id.btn8)).setOnClickListener(this);
		((Button) findViewById(R.id.btn9)).setOnClickListener(this);
		((Button) findViewById(R.id.btnAdd)).setOnClickListener(this);
		((Button) findViewById(R.id.btnSub)).setOnClickListener(this);
		((Button) findViewById(R.id.btnMul)).setOnClickListener(this);
		((Button) findViewById(R.id.btnDiv)).setOnClickListener(this);
		((Button) findViewById(R.id.btnClear)).setOnClickListener(this);
		((Button) findViewById(R.id.btnEqual)).setOnClickListener(this);
		((Button) findViewById(R.id.btnDel)).setOnClickListener(this);
		
		((Button) findViewById(R.id.btnPoin)).setOnClickListener(this);
		((Button) findViewById(R.id.btnDone)).setOnClickListener(new OKListener());
	}

	private void getdata(View v2){
		inDigit = ((Button) v2).getText().toString();
		if (curResult.equals("0")) {
			curResult = inDigit; // no leading zero
		} 
		else {
			curResult += inDigit;
		}
		txtInput.setText(curResult);
		//txtInput.setText(curResult);
		// Clear buffer if last operator is '='
		if (pheptoan == '=') {
			result = 0;
			pheptoan = ' ';
		}
	}

	//result la so dau tien, inNum la so thu 2
	public void tinhtoan() {
		/*so1= Double.parseDouble(txtInput.getText().toString());
		curResult="";*/
		try {
			
			Double inNum = Double.parseDouble(simple.ThayDoi_Phay(curResult));
			curResult = "0";
			if (pheptoan == ' ') {
				result = inNum; //nhan gia tri vao result
			} else if (pheptoan == '+') {
				result += inNum;
				//tinhtoan();
				//pheptoan = '+';
				//curResult="";
			} else if (pheptoan == '-') {
				
				result -= inNum;

			} else if (pheptoan == '*') {
				result *= inNum;

			} else if (pheptoan == '/') {
				if(result==0){

				}else{
					result /= inNum;
				}
			} else if (pheptoan == '=') {
			//	Toast.makeText(this.getContext(), "Press C to continue...", Toast.LENGTH_SHORT).show();
				// Keep the result for the next operation\
			}
			txtInput.setText(simple.LamTronSoFloat(result));
		} catch (Exception e) {
			Log.e("TAG","Loi o Calculator.class->tinhtoan" + e.getMessage());
		}
		

	}


	@Override
	public void onClick(View v) {
		boolean checkbtn = ((Button) v).isClickable();

		switch (v.getId()) {
		// Number buttons: '0' to '9'
		case R.id.btn0:{
			if(checkbtn){
				getdata(v);
			}
			break;
		}
		case R.id.btn1:if(checkbtn){
			getdata(v);
		}
		break;

		case R.id.btn2:{
			if(checkbtn){
				getdata(v);
			}
			break;
		}
		case R.id.btn3:{
			if(checkbtn){
				getdata(v);
			}
			break;
		}
		case R.id.btn4:{
			if(checkbtn){
				getdata(v);
			}
			break;
		}
		case R.id.btn5:{
			if(checkbtn){
				getdata(v);
			}
			break;
		}
		case R.id.btn6:{
			if(checkbtn){
				getdata(v);
			}
			break;
		}
		case R.id.btn7:{
			if(checkbtn){
				getdata(v);
			}
			break;
		}
		case R.id.btn8:{
			if(checkbtn){
				getdata(v);
			}
			break;
		}
		case R.id.btn9:{
			if(checkbtn){
				getdata(v);
			}
			break;
		}

		// Operator buttons: '+', '-', '*', '/' and '='
		case R.id.btnAdd:
			tinhtoan();
			pheptoan='+';
			break;
		case R.id.btnSub:
			tinhtoan();
			pheptoan = '-';
			break;
		case R.id.btnMul:
			tinhtoan();
			pheptoan = '*';
			break;
		case R.id.btnDiv:
			tinhtoan();
			pheptoan = '/';
			break;
		case R.id.btnEqual://khi an dau ==
			tinhtoan();
			pheptoan = '/';
			break;
			// Clear button
		case R.id.btnClear:
			result = 0;
			curResult = "0";
			pheptoan = ' ';
			txtInput.setText("0");
			loiError = true;
			break;
		case R.id.btnDone: {
		
			break;
		}
		case R.id.btnDel:
			if(curResult.length()>0){
				curResult = curResult.substring(0, (curResult.length()-1));
				txtInput.setText(simple.KiemtraSoString_toString(curResult));
			}
			else{
				curResult = "0";
				txtInput.setText(curResult);
			}
			break;
		case R.id.btnPoin: {
			if(!curResult.contains(".")){//ko co dau cham
				//Log.e("TAG","koco dau cham ok");
				curResult += ".";
				txtInput.setText(curResult);
			}
			break;
		}
		}		
	}
	///////////////////////////////////////////////
	/// Dinh dang kieu truyen du lieu tu Dialog ve Activiy
	///////////////////////////////////////////////
	
	 OnMyDialogResult mDialogResult; 
	public interface OnMyDialogResult{
		void finish(String result);
	}
	public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }
	private class OKListener implements android.view.View.OnClickListener {
        @Override
        public void onClick(View v) {
            if( mDialogResult != null ){
                mDialogResult.finish(new Simple_method().KiemtraSoString_toString(txtInput.getText().toString()));
            }
            Calculator_Activity.this.dismiss();
        }
    }


}
