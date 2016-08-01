package vayno_activity;

import java.util.ArrayList;

public class SoNoObject {
	private ArrayList<String> list;
	private float tvVay;
	private float tvNo;
	
	public ArrayList<String> getList() {
		return list;
	}
	public void setList(ArrayList<String> list) {
		this.list = list;
	}
	public float getTvVay() {
		return tvVay;
	}
	public void setTvVay(float tvVay) {
		this.tvVay = tvVay;
	}
	public float getTvNo() {
		return tvNo;
	}
	public void setTvNo(float tvNo) {
		this.tvNo = tvNo;
	}
	
	public SoNoObject() {
		super();
		this.list = null;
		this.tvVay = 0;
		this.tvNo = 0;
	}
}
