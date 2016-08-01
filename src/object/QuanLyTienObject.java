package object;

import java.io.Serializable;

public class QuanLyTienObject implements Serializable {
	/**
	 * 
	 */
	// chuc nang Serializable giup chuyen giua cac Activity de dang hon
	private static final long serialVersionUID = 1L;
	private int id;
	private String date;
	private float Total_Thang_THU;
	private float Total_Thang_CHI;

	public float getTongtien() {
		return tongtien;
	}

	public void setTongtien(float tongtien) {
		this.tongtien = tongtien;
	}

	private float tongtien;

	public float getTotal_Thang_THU() {
		return Total_Thang_THU;
	}

	public void setTotal_Thang_THU(float total_Thang_THU) {
		Total_Thang_THU = total_Thang_THU;
	}

	public float getTotal_Thang_CHI() {
		return Total_Thang_CHI;
	}

	public void setTotal_Thang_CHI(float total_Thang_CHI) {
		Total_Thang_CHI = total_Thang_CHI;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
