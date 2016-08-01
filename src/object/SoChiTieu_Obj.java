package object;

import java.util.ArrayList;

import sochitieu.Item;

public class SoChiTieu_Obj {
	private ArrayList<Item> arrayItem;
	private float tongThu;
	private float tongChi;
	private float tongVay;
	private float tongNo;
	public ArrayList<Item> getArrayItem() {
		return arrayItem;
	}
	public float getTongThu() {
		return tongThu;
	}
	public float getTongChi() {
		return tongChi;
	}
	public void setArrayItem(ArrayList<Item> arrayItem) {
		this.arrayItem = arrayItem;
	}
	public void setTongThu(float tongThu) {
		this.tongThu = tongThu;
	}
	public void setTongChi(float tongChi) {
		this.tongChi = tongChi;
	}
	public void setTongVay(float tongVay) {
		this.tongVay = tongVay;
	}
	public void setTongNo(float tongNo) {
		this.tongNo = tongNo;
	}
	public float getTongVay() {
		return tongVay;
	}
	public float getTongNo() {
		return tongNo;
	}
	public SoChiTieu_Obj() {
		tongThu = 0;
		tongChi =0;
		tongNo = 0; 
		tongVay = 0;
		arrayItem =null;
	}
	
	
}
