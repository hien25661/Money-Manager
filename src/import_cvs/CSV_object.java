package import_cvs;

public class CSV_object {
	private String ma_id;//1
	private String ten_giao_dich;//2
	private String so_tien;//3
	private String ngay_thu_chi;//4
	private String ghichu;//5
	private String theloai;//6
	private String ngay_tra_no;//6
	public CSV_object(String ma_id, String ten_giao_dich,
			String so_tien, String ngay_thu_chi, String ghichu, String theloai) {
		super();
		this.ma_id = ma_id;
		this.ten_giao_dich = ten_giao_dich;
		this.so_tien = so_tien;
		this.ngay_thu_chi = ngay_thu_chi;
		this.ghichu = ghichu;
		this.theloai = theloai;
	}
	public String getNgay_tra_no() {
		return ngay_tra_no;
	}
	public void setNgay_tra_no(String ngay_tra_no) {
		this.ngay_tra_no = ngay_tra_no;
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
	public String getSo_tien() {
		return so_tien;
	}
	public void setSo_tien(String so_tien) {
		this.so_tien = so_tien;
	}
	public String getNgay_thu_chi() {
		return ngay_thu_chi;
	}
	public void setNgay_thu_chi(String ngay_thu_chi) {
		this.ngay_thu_chi = ngay_thu_chi;
	}
	public String getGhichu() {
		return ghichu;
	}
	public void setGhichu(String ghichu) {
		this.ghichu = ghichu;
	}
	public String getTheloai() {
		return theloai;
	}
	public void setTheloai(String theloai) {
		this.theloai = theloai;
	}
}
