package object;

public class QuaKhu_VayNo_Object {
	private int ma_id;
	private String ma_vayno;
	
	public String getMa_vayno() {
		return ma_vayno;
	}
	public void setMa_vayno(String ma_vayno) {
		this.ma_vayno = ma_vayno;
	}
	private float sotien;
	private String ngay_tra_no;
	public int getMa_id() {
		return ma_id;
	}
	public void setMa_id(int ma_id) {
		this.ma_id = ma_id;
	}
	
	public float getSotien() {
		return sotien;
	}
	public void setSotien(float sotien) {
		this.sotien = sotien;
	}
	public String getNgay_tra_no() {
		return ngay_tra_no;
	}
	public void setNgay_tra_no(String ngay_tra_no) {
		this.ngay_tra_no = ngay_tra_no;
	}
	public QuaKhu_VayNo_Object(int ma_id, String ma_vayno, float sotien,
			String ngay_tra_no) {
		super();
		this.ma_id = ma_id;
		this.ma_vayno = ma_vayno;
		this.sotien = sotien;
		this.ngay_tra_no = ngay_tra_no;
	}
	
}
