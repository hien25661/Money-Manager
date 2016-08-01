package object;

import java.util.Date;

public class ThuChi {

	private int id;//0
	private String ma_id;//1
	private String ten_giao_dich;//2
	private float so_tien;//3
	private String ngay_thu_chi;//4
	private String ghichu;//5
	private int theloai;//6
	private String ngay_vay_no;//7

	public ThuChi(int id, String ma_id, String ten_giao_dich, float so_tien,
			String ngay_thu_chi, String ghichu, int theloai) {
		super();
		this.id = id;
		this.ma_id = ma_id;
		this.ten_giao_dich = ten_giao_dich;
		this.so_tien = so_tien;
		this.ngay_thu_chi = ngay_thu_chi;
		this.ghichu = ghichu;
		this.theloai = theloai;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getSo_tien() {
		return so_tien;
	}

	public ThuChi() {

	}

	public ThuChi(int id, String ma_id, String ten_giao_dich, float so_tien,
			String ngay_thu_chi, String ghichu, int theloai, String ngay_vay_no) {
		super();
		this.id = id;
		this.ma_id = ma_id;
		this.ten_giao_dich = ten_giao_dich;
		this.so_tien = so_tien;
		this.ngay_thu_chi = ngay_thu_chi;
		this.ghichu = ghichu;
		this.theloai = theloai;
		this.ngay_vay_no = ngay_vay_no;
	}
	

	public ThuChi(String ma_id, String ten_giao_dich, float so_tien,
			String ngay_thu_chi, String ghichu, String ngay_vay_no) {
		super();
		this.ma_id = ma_id;
		this.ten_giao_dich = ten_giao_dich;
		this.so_tien = so_tien;
		this.ngay_thu_chi = ngay_thu_chi;
		this.ghichu = ghichu;
		this.ngay_vay_no = ngay_vay_no;
	}
	public ThuChi(String ma_id, String ten_giao_dich, float so_tien,
			String ngay_thu_chi, String ghichu, int theloai) {
		super();
		this.ma_id = ma_id;
		this.ten_giao_dich = ten_giao_dich;
		this.so_tien = so_tien;
		this.ngay_thu_chi = ngay_thu_chi;
		this.ghichu = ghichu;
		this.theloai = theloai;
	}
	
	public ThuChi(int id, String ma_id, String ten_giao_dich, float so_tien,
			String ngay_thu_chi, String ghichu, String ngay_vay_no) {
		super();
		this.id = id;
		this.ma_id = ma_id;
		this.ten_giao_dich = ten_giao_dich;
		this.so_tien = so_tien;
		this.ngay_thu_chi = ngay_thu_chi;
		this.ghichu = ghichu;
		this.ngay_vay_no = ngay_vay_no;
	}

	public void setSo_tien(float so_tien) {
		this.so_tien = so_tien;
	}

	public String getMa_id() {
		return ma_id;
	}

	public void setMa_id(String ma_id) {
		this.ma_id = ma_id;
	}

	public String getTen_giao_dich() {
		return ten_giao_dich;
	}

	public void setTen_giao_dich(String ten_giao_dich) {
		this.ten_giao_dich = ten_giao_dich;
	}

	public String getGhichu() {
		return ghichu;
	}

	public void setGhichu(String ghichu) {
		this.ghichu = ghichu;
	}

	public String getNgay_thu_chi() {
		return ngay_thu_chi;
	}

	public void setNgay_thu_chi(String ngay_thu_chi) {
		this.ngay_thu_chi = ngay_thu_chi;
	}

	public String getNgay_vay_no() {
		return ngay_vay_no;
	}

	public void setNgay_vay_no(String ngay_vay_no) {
		this.ngay_vay_no = ngay_vay_no;
	}

	public int getTheloai() {
		return theloai;
	}

	public void setTheloai(int theloai) {
		this.theloai = theloai;
	}

}
