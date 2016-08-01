package access_sql;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import object.QuanLyTienObject;
import object.SoChiTieu_Obj;
import object.ThuChi;
import object.ob_chart;
import sochitieu.EntryItem;
import sochitieu.Item;
import sochitieu.SectionItem;
import util.LayDate_Month_Yeah;
import util.SuaOrXoaBefore;
import util.Variable;
import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;
import application.MyApplication;

public class Database {

	private SQLiteDatabase db;
	private My_SQLiteOpenHelper dbHelper;

	public My_SQLiteOpenHelper getDbHelper() {
		return dbHelper;
	}

	private String[] ColumnALL = { My_SQLiteOpenHelper.column_id,
			My_SQLiteOpenHelper.column_Ma,
			My_SQLiteOpenHelper.column_TenGiaoDich,
			My_SQLiteOpenHelper.column_SoTien, My_SQLiteOpenHelper.column_date,
			My_SQLiteOpenHelper.column_GhiChu,
			My_SQLiteOpenHelper.column_TheLoai };
	
	private String[] Column_VayNo = { My_SQLiteOpenHelper.columnVayNo_id,
			My_SQLiteOpenHelper.columnVayNo_Ma,
			My_SQLiteOpenHelper.columnVayNo_TenNguoi,
			My_SQLiteOpenHelper.columnVayNo_SoTien,
			My_SQLiteOpenHelper.columnVayNo_NgayVay };

	// context dinh nghia vi tri luu giu
	//dung cho cvs
	public Database(Context context) {
		dbHelper = new My_SQLiteOpenHelper(context);
	}

	// ---opens the database---
	public void open() {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			Log.i("TAG", "Loi Open" + e.getMessage());
		}
	}
	// ---closes the database---
	public void close() {
		dbHelper.close();
	}
	public List<String[]> exportCSV() {
		List<String[]> mang = new ArrayList<String[]>();
		try {
			open();
			Cursor curCSV = db.rawQuery("SELECT * FROM "+ My_SQLiteOpenHelper.Table_ThuChi,null);
			//7 column bat dau tu 0 den 7
			mang.add(new String[]{"id","ma_id","ten_giao_dich","so_tien","ngay_thu_chi","ghichu","theloai","ngay_vay_no"});
			//mang.add(curCSV.getColumnNames());
			if(curCSV.getCount()>0){
				while(curCSV.moveToNext())
				{
					String arrStr[] ={String.valueOf(curCSV.getInt(0)),curCSV.getString(1),

							curCSV.getString(2),String.valueOf(curCSV.getFloat(3)),curCSV.getString(4)

							,curCSV.getString(5),String.valueOf(curCSV.getInt(6)),null};
					mang.add(arrStr);
				}
				curCSV.close();
			}
			mang.add(new String[]{"TongTien_ThuChi",null,null,String.valueOf(MyApplication.tongtien),null,null,null,null});
			//xu ly bang VayNo
			Cursor curVayNo = db.rawQuery("SELECT * FROM "+ My_SQLiteOpenHelper.Table_VayNo,null);
			//in bang vayno cach 1 hang
			mang.add(new String[]{"BangVayNo",null,null,null,null,null,null});
			if(curVayNo.getCount()>0){
				while(curVayNo.moveToNext())
				{
					String arrStr[] ={String.valueOf(curVayNo.getInt(0)),curVayNo.getString(1),

							curVayNo.getString(2),String.valueOf(curVayNo.getFloat(3)),curVayNo.getString(4)

							,curVayNo.getString(6),null,curVayNo.getString(5)};
					//vi tri 6 la ghi chu   5 la ngay tra
					mang.add(arrStr);
				}
				curVayNo.close();
			}
			mang.add(new String[]{"BangQuaKhu",null,null,null,null,null,null});
			Cursor curQuaKhu = db.rawQuery("SELECT * FROM "+ My_SQLiteOpenHelper.Table_QuaKhu_ofVayNo,null);
			if(curQuaKhu.getCount()>0){
				while(curQuaKhu.moveToNext())
				{
					String arrStr[] ={String.valueOf(curQuaKhu.getInt(0)),curQuaKhu.getString(1),

							curQuaKhu.getString(2),String.valueOf(curQuaKhu.getFloat(3)),curQuaKhu.getString(4)

							,null,null,null};
					mang.add(arrStr);
				}
				curQuaKhu.close();
			}
			close();
			/*for (int i = 0; i < mang.size(); i++) {
				Log.e("TAG",mang.get(i)[1]+"  "+mang.get(i)[0]);
			}*/
			return mang;

		} catch (Exception e) {
			Log.e("exportCSV ", e.getMessage());
		}	
		return null;
	}
	// ---insert a contact into the database---
	public void insertThuChi(ThuChi thuchi) {
		ContentValues values = new ContentValues();
		// dua du lieu vao values
		values.put(My_SQLiteOpenHelper.column_Ma, thuchi.getMa_id());
		values.put(My_SQLiteOpenHelper.column_TenGiaoDich,
				thuchi.getTen_giao_dich());
		values.put(My_SQLiteOpenHelper.column_SoTien, thuchi.getSo_tien());
		values.put(My_SQLiteOpenHelper.column_date, thuchi.getNgay_thu_chi());
		values.put(My_SQLiteOpenHelper.column_GhiChu, thuchi.getGhichu());
		values.put(My_SQLiteOpenHelper.column_TheLoai, thuchi.getTheloai());
		try {
			open();
			// insert vao database tra ve tham so la long
			db.insert(My_SQLiteOpenHelper.Table_ThuChi, null, values);
			close();
		} catch (SQLiteException e) {
			Log.i("TAG", "Loi method insertThuChi_TH_HomNay " + e.getMessage());
		}
	}

	public ThuChi cursorToThuChi(Cursor cursor) {
		ThuChi tc = new ThuChi();
		tc.setId(cursor.getInt(0)); // 0
		tc.setMa_id(cursor.getString(1)); // 1
		tc.setTen_giao_dich(cursor.getString(2)); // 2
		tc.setSo_tien(cursor.getFloat(3)); // 3
		tc.setNgay_thu_chi(cursor.getString(4)); // 4
		tc.setGhichu(cursor.getString(5)); // 5
		tc.setTheloai(cursor.getInt(6)); // 6

		return tc;
	}

	public ThuChi cursorToVayNo(Cursor cursor) {
		ThuChi vn = new ThuChi();
		vn.setId(cursor.getInt(0)); // 0
		vn.setMa_id(cursor.getString(1)); // 1
		vn.setTen_giao_dich(cursor.getString(2)); // 2
		vn.setSo_tien(cursor.getFloat(3)); // 3
		vn.setNgay_thu_chi(cursor.getString(4)); // 4
		return vn;
	}
	public String[] select_TenGiaoDich_autocomplet(String ma_id) {
		String[] Column_VayNo = { 
				My_SQLiteOpenHelper.column_TenGiaoDich,
		};
		try {
			open();
			Cursor c = db.query(My_SQLiteOpenHelper.Table_ThuChi, Column_VayNo,
					My_SQLiteOpenHelper.column_Ma+ " = '"+ma_id+"'", null,My_SQLiteOpenHelper.column_TenGiaoDich, null, null);
			if(c.getCount() == 0 ){
				close();
				return null;
			}else{
				String[] giaodich = new String[c.getCount()];
				c.moveToFirst();
				int i=0;
				do {
					giaodich[i] = c.getString(0);
					i++;

				} while (c.moveToNext());
				c.close();
				close();
				return giaodich;
			}
		} catch (Exception e) {
			Log.e("TAG","Loi class.Database. select_TenGiaoDich_autocomplet "+e.getMessage());
		}

		return null;
	}
	public SoChiTieu_Obj select_List_AllThuChi_VayNo(String firstdate,String lastdate) {
		SoChiTieu_Obj sochitieu;
		/*
		 * Cau sql gop 2 bang thanh 1 bang. The Loai with VayNo ko gia tri 0 select
		 * id,ma_id,ten_giao_dich,so_tien,ngay_thu_chi,the_loai from BangThuChi
		 * union all select id,ma_id,ten_nguoi_vayno,so_tien,ngay_vay,0 from
		 * BangVayNo ORDER BY ngay_thu_chi desc
		 */
		// List<ThuChi> MangThuChi = new ArrayList<ThuChi>();
		// gom 2 bang thanh 1 mac dinh VayNo co theloai = 0 
		ArrayList<Item> items = new ArrayList<Item>();
		try {
			open();
			String sql = "select id,ma_id,ten_giao_dich,so_tien,ngay_thu_chi,the_loai from BangThuChi where ngay_thu_chi >= '"+firstdate
					+ "' and ngay_thu_chi <= '" + lastdate
					+ "' union all " // close 'firstdate' and 'lastdate'
					+ " select id,ma_id,ten_nguoi_vayno,so_tien,ngay_vay,0 from BangVayNo where ngay_vay >= '"+firstdate
					+ "' and ngay_vay <= '" + lastdate
					+ "' and (ma_id ='vay' or ma_id = 'no' or ma_id = 'pvay' or ma_id = 'pno')" +
					" ORDER BY ngay_thu_chi desc ";  // close 'lastdate'
			Cursor cr = db.rawQuery(sql, null);
			// gia tri cr.getCount = 0 neu ko co gia tri
			// Log.e("TAG","Cursor"+ cr.getCount()+
			// " class.Database getList_AllThuChi_VayNo");
			if (cr.getCount() == 0) {
				close();
				return null;
			} else {
				sochitieu = new SoChiTieu_Obj();
				cr.moveToFirst();
				// boolean kt = true; // kt khoi tao lan dau
				String ngaytrunggian = null;
				float tongThu=0;
				float tongChi=0;
				float tongNo=0;
				float tongVay=0;
				do {

					ThuChi newThuChi = new ThuChi();
					newThuChi.setId(cr.getInt(0)); // 0
					newThuChi.setMa_id(cr.getString(1)); // 1
					newThuChi.setTen_giao_dich(cr.getString(2)); // 2
					newThuChi.setSo_tien(cr.getFloat(3)); // 3
					newThuChi.setNgay_thu_chi(cr.getString(4)); // 4
					newThuChi.setTheloai(cr.getInt(5)); // 5
					/*
					 * Luu cac gia tri tong Thu Chi vay No
					 */
					if(newThuChi.getMa_id().equals("thu")){
						tongThu+=newThuChi.getSo_tien();
					}else if(newThuChi.getMa_id().equals("chi")){
						tongChi+=newThuChi.getSo_tien();
					}else if(newThuChi.getMa_id().equals("vay")){
						tongVay+= newThuChi.getSo_tien();
					}else{
						tongNo+= newThuChi.getSo_tien();
					}
					/////////////////////insert vao List View/////////////////////////////
					//ngay -> tengiaodich theo ngay	
					// thuc hien lan dau tien khoi tao bo Ngay vao array
					if (items.size() == 0 && ngaytrunggian == null) {
						// chuyen doi String thang ngay/thang/nam dung
						// LayDate_Moth_yeah
						items.add(new SectionItem(new LayDate_Month_Yeah()
						.CovertString_toNgayThangNam(newThuChi
								.getNgay_thu_chi())));
						items.add(new EntryItem(newThuChi));
						// kt= false;
						ngaytrunggian = newThuChi.getNgay_thu_chi();
						continue;
					}
					// thuc hien lan gia tri thu 2
					if (items.size() >= 2 && ngaytrunggian != null) {
						if (ngaytrunggian.equals(newThuChi.getNgay_thu_chi())) {
							items.add(new EntryItem(newThuChi));
						} else {
							items.add(new SectionItem(new LayDate_Month_Yeah()
							.CovertString_toNgayThangNam(newThuChi
									.getNgay_thu_chi())));
							items.add(new EntryItem(newThuChi));
							ngaytrunggian = newThuChi.getNgay_thu_chi();
						}
					}
				} while (cr.moveToNext());
				// ket thuc cursor
				cr.close();
				close();
				/*
				 * add arrayItem vao sochitieu
				 */
				sochitieu.setArrayItem(items);
				sochitieu.setTongThu(tongThu);
				sochitieu.setTongChi(tongChi);
				sochitieu.setTongVay(tongVay);
				sochitieu.setTongNo(tongNo);
				return sochitieu;
			}

		} catch (Exception e) {
			Log.e("TAG", "Loi class.Database getList_AllThuChi_VayNo");

		} finally {
			if (db.isOpen()) {
				close();
			}
		}
		return null;
	}
	/*
	 * Ngay_select_AllTable giong code hoan toan 
		Thang_select_List_AllThuChi_VayNo
		chi khac cau lenh SQL (select ngay 1 gia tri dua vao) 
		va ngay dung = ngay hien tai
	 */
	public SoChiTieu_Obj Ngay_select_AllTable(String date) {
		SoChiTieu_Obj sochitieu;
		/*
		 * Cau sql gop 2 bang thanh 1 bang. The Loai with VayNo ko gia tri 0 select
		 * id,ma_id,ten_giao_dich,so_tien,ngay_thu_chi,the_loai from BangThuChi
		 * union all select id,ma_id,ten_nguoi_vayno,so_tien,ngay_vay,0 from
		 * BangVayNo ORDER BY ngay_thu_chi desc
		 */
		// List<ThuChi> MangThuChi = new ArrayList<ThuChi>();
		// gom 2 bang thanh 1 mac dinh VayNo co theloai = 0 
		ArrayList<Item> items = new ArrayList<Item>();
		open();
		try {
			String sql = "select id,ma_id,ten_giao_dich,so_tien,ngay_thu_chi,the_loai from BangThuChi where ngay_thu_chi = '"+date
					+ "' union all " // close 'date' 
					+ " select id,ma_id,ten_nguoi_vayno,so_tien,ngay_vay,0 from BangVayNo where ngay_vay = '"+date
					+ "' and (ma_id ='vay' or ma_id = 'no' or ma_id = 'pvay' or ma_id = 'pno') " +
					"ORDER BY ngay_thu_chi desc ";  // close 'lastdate'
			Cursor cr = db.rawQuery(sql, null);
			// gia tri cr.getCount = 0 neu ko co gia tri
			// Log.e("TAG","Cursor"+ cr.getCount()+
			// " class.Database getList_AllThuChi_VayNo");
			if (cr.getCount() == 0) {
				close();
				return null;
			} else {
				sochitieu = new SoChiTieu_Obj();
				cr.moveToFirst();
				// boolean kt = true; // kt khoi tao lan dau
				String ngaytrunggian = null;
				float tongThu=0;
				float tongChi=0;
				float tongNo=0;
				float tongVay=0;
				do {

					ThuChi newThuChi = new ThuChi();
					newThuChi.setId(cr.getInt(0)); // 0
					newThuChi.setMa_id(cr.getString(1)); // 1
					newThuChi.setTen_giao_dich(cr.getString(2)); // 2
					newThuChi.setSo_tien(cr.getFloat(3)); // 3
					newThuChi.setNgay_thu_chi(cr.getString(4)); // 4
					newThuChi.setTheloai(cr.getInt(5)); // 5
					/*
					 * Luu cac gia tri tong Thu Chi vay No
					 */
					if(newThuChi.getMa_id().equals("thu")){
						tongThu+=newThuChi.getSo_tien();
					}else if(newThuChi.getMa_id().equals("chi")){
						tongChi+=newThuChi.getSo_tien();
					}else if(newThuChi.getMa_id().equals("vay")){
						tongVay+= newThuChi.getSo_tien();
					}else{
						tongNo+= newThuChi.getSo_tien();
					}
					//////////////////////////////////////////////////

					// thuc hien lan dau tien khoi tao bo Ngay vao array
					if (items.size() == 0 && ngaytrunggian == null) {
						// chuyen doi String thang ngay/thang/nam dung
						// LayDate_Moth_yeah
						items.add(new SectionItem(new LayDate_Month_Yeah()
						.CovertString_toNgayThangNam(newThuChi
								.getNgay_thu_chi())));
						items.add(new EntryItem(newThuChi));
						// kt= false;
						ngaytrunggian = newThuChi.getNgay_thu_chi();
						continue;
					}
					// thuc hien lan gia tri thu 2
					if (items.size() >= 2 && ngaytrunggian != null) {
						if (ngaytrunggian.equals(newThuChi.getNgay_thu_chi())) {
							items.add(new EntryItem(newThuChi));
						} else {
							items.add(new SectionItem(new LayDate_Month_Yeah()
							.CovertString_toNgayThangNam(newThuChi
									.getNgay_thu_chi())));
							items.add(new EntryItem(newThuChi));
							ngaytrunggian = newThuChi.getNgay_thu_chi();
						}
					}
				} while (cr.moveToNext());
				// ket thuc cursor
				cr.close();
				close();
				/*
				 * add arrayItem vao sochitieu
				 */
				sochitieu.setArrayItem(items);
				sochitieu.setTongThu(tongThu);
				sochitieu.setTongChi(tongChi);
				sochitieu.setTongVay(tongVay);
				sochitieu.setTongNo(tongNo);
				return sochitieu;
			}

		} catch (Exception e) {
			Log.e("TAG", "Loi class.Database getList_AllThuChi_VayNo "+e.getMessage());

		} finally {
			if (db.isOpen()) {
				close();
			}
		}
		return null;
	}

	////////Select theo The Loai////////////////////////////////////////////
	public SoChiTieu_Obj select_TheLoai(String dateFrom, String dateTo) {
		SoChiTieu_Obj sochitieu;
		/*
		 *Tra ve 3 gia tri
		 *0.ListView
		 *1.TOng Thu
		 *2.Tong Chi
		 */
		// List<ThuChi> MangThuChi = new ArrayList<ThuChi>();
		// gom 2 bang thanh 1 mac dinh VayNo co theloai = 0 
		ArrayList<Item> items = new ArrayList<Item>();
		open();
		try {// select tinh tong so tien dua theo The Loai, select 2 Thu va Chi
			//cach khac select 2 lan select thu, select chi (select vay de tranh viec thu va chi cung ma theloai
			// dung cach 1: GROUP BY the_loai,ma_id => bo union all
			String sql = "select ma_id,ten_giao_dich,SUM(so_tien),the_loai from BangThuChi where ngay_thu_chi >= '"+dateFrom
					+ "' and ngay_thu_chi <= '" + dateTo
					+ "' GROUP BY the_loai,ma_id " 
					+ " order by ma_id desc";
			Cursor cr = db.rawQuery(sql, null);
			// gia tri cr.getCount = 0 neu ko co gia tri
			// Log.e("TAG","Cursor"+ cr.getCount()+
			// " class.Database getList_AllThuChi_VayNo");
			if (cr.getCount() == 0) {
				close();
				return null;
			} else {
				sochitieu = new SoChiTieu_Obj();
				cr.moveToFirst();
				// boolean kt = true; // kt khoi tao lan dau
				String theloaitrunggian = null;
				float tongThu=0;
				float tongChi=0;

				do {

					ThuChi newThuChi = new ThuChi();
					newThuChi.setMa_id(cr.getString(0)); // 0
					newThuChi.setTen_giao_dich(cr.getString(1)); // 1
					newThuChi.setSo_tien(cr.getFloat(2)); // 2
					newThuChi.setTheloai(cr.getInt(3)); // 3
					/*
					 * Luu tinh tong cac gia tri tong Thu Chi
					 */
					if(newThuChi.getMa_id().equals("thu")){
						tongThu+=newThuChi.getSo_tien();
					}else{
						tongChi+=newThuChi.getSo_tien();
					}
					//////////////////////////////////////////////////
					//--------------Chi thuc hien khoi tao lan dau tien----------------------
					// thuc hien lan dau tien khoi tao add Income Truong hop co Thu 
					if (items.size() == 0 && newThuChi.getMa_id().equals("thu")) {
						items.add(new SectionItem("Income"));
						items.add(new EntryItem(newThuChi));
						// kt= false;
						theloaitrunggian = "thu";
						continue;
					}
					// thuc hien lan dau tien khoi tao add Expense Truong hop co Chi (neu ko co thu)
					if (items.size() == 0 && newThuChi.getMa_id().equals("chi")) {
						items.add(new SectionItem("Expense"));
						items.add(new EntryItem(newThuChi));
						// kt= false;
						theloaitrunggian = "chi";
						continue;
					}
					//------------------------------------------------------------
					// thuc hien lan gia tri thu 2
					if (items.size() >= 2 && theloaitrunggian != null) {
						if (theloaitrunggian.equals(newThuChi.getMa_id())) {
							items.add(new EntryItem(newThuChi));
						} else {
							items.add(new SectionItem("Expense"));
							items.add(new EntryItem(newThuChi));
							theloaitrunggian = newThuChi.getMa_id();
						}
					}
				} while (cr.moveToNext());
				// ket thuc cursor
				cr.close();
				close();
				/*
				 * add arrayItem vao sochitieu
				 */
				sochitieu.setArrayItem(items);
				sochitieu.setTongThu(tongThu);
				sochitieu.setTongChi(tongChi);
				sochitieu.setTongNo(0);
				sochitieu.setTongVay(0);

				return sochitieu;
			}

		} catch (Exception e) {
			Log.e("TAG", "(ko co data) Loi class.Database getList_AllThuChi_VayNo "+ e.getMessage());

		} finally {
			if (db.isOpen()) {
				close();
			}
		}
		return null;
	}

	
	public SoChiTieu_Obj select_TheLoai_TheoNgay(String dateFrom) {
		SoChiTieu_Obj sochitieu;
		/*
		 *Tra ve 3 gia tri
		 *0.ListView
		 *1.TOng Thu
		 *2.Tong Chi
		 */
		// List<ThuChi> MangThuChi = new ArrayList<ThuChi>();
		// gom 2 bang thanh 1 mac dinh VayNo co theloai = 0 
		ArrayList<Item> items = new ArrayList<Item>();
		open();
		try {// select tinh tong so tien dua theo The Loai, select 2 Thu va Chi
			//cach khac select 2 lan select thu, select chi (select vay de tranh viec thu va chi cung ma theloai
			// dung cach 1: GROUP BY the_loai,ma_id => bo union all
			String sql = "select ma_id,ten_giao_dich,SUM(so_tien),the_loai from BangThuChi where ngay_thu_chi == '"+dateFrom
					+ "'GROUP BY the_loai,ma_id " 
					+ " order by ma_id desc";
			Cursor cr = db.rawQuery(sql, null);
			// gia tri cr.getCount = 0 neu ko co gia tri
			// Log.e("TAG","Cursor"+ cr.getCount()+
			// " class.Database getList_AllThuChi_VayNo");
			if (cr.getCount() == 0) {
				close();
				return null;
			} else {
				sochitieu = new SoChiTieu_Obj();
				cr.moveToFirst();
				// boolean kt = true; // kt khoi tao lan dau
				String theloaitrunggian = null;
				float tongThu=0;
				float tongChi=0;
				do {

					ThuChi newThuChi = new ThuChi();
					newThuChi.setMa_id(cr.getString(0)); // 0
					newThuChi.setTen_giao_dich(cr.getString(1)); // 1
					newThuChi.setSo_tien(cr.getFloat(2)); // 2
					newThuChi.setTheloai(cr.getInt(3)); // 3
					/*
					 * Luu tinh tong cac gia tri tong Thu Chi
					 */
					if(newThuChi.getMa_id().equals("thu")){
						tongThu+=newThuChi.getSo_tien();
					}else{
						tongChi+=newThuChi.getSo_tien();
					}
					//////////////////////////////////////////////////
					//--------------Chi thuc hien khoi tao lan dau tien----------------------
					// thuc hien lan dau tien khoi tao add Income Truong hop co Thu 
					if (items.size() == 0 && newThuChi.getMa_id().equals("thu")) {
						items.add(new SectionItem("Income"));
						items.add(new EntryItem(newThuChi));
						// kt= false;
						theloaitrunggian = "thu";
						continue;
					}
					// thuc hien lan dau tien khoi tao add Expense Truong hop co Chi (neu ko co thu)
					if (items.size() == 0 && newThuChi.getMa_id().equals("chi")) {
						items.add(new SectionItem("Expense"));
						items.add(new EntryItem(newThuChi));
						// kt= false;
						theloaitrunggian = "chi";
						continue;
					}
					//------------------------------------------------------------
					// thuc hien lan gia tri thu 2
					if (items.size() >= 2 && theloaitrunggian != null) {
						if (theloaitrunggian.equals(newThuChi.getMa_id())) {
							items.add(new EntryItem(newThuChi));
						} else {
							items.add(new SectionItem("Expense"));
							items.add(new EntryItem(newThuChi));
							theloaitrunggian = newThuChi.getMa_id();
						}
					}
				} while (cr.moveToNext());
				// ket thuc cursor
				cr.close();
				close();
				/*
				 * add arrayItem vao sochitieu
				 */
				sochitieu.setArrayItem(items);
				sochitieu.setTongThu(tongThu);
				sochitieu.setTongChi(tongChi);
				sochitieu.setTongNo(0);
				sochitieu.setTongVay(0);
				return sochitieu;
			}

		} catch (Exception e) {
			Log.e("TAG", "(ko co data) Loi class.Database getList_AllThuChi_VayNo "+ e.getMessage());

		} finally {
			if (db.isOpen()) {
				close();
			}
		}
		return null;
	}

	/////////End Select theo The Loai//////////////////////////////////////////////
	/*
	 * ham dung SoThuChi->Tab2(the loai)->onItemClick-> chitiet theloai
	 */
	public SoChiTieu_Obj select_chitiet_TheLoai(String dateFrom,String dateTo,String ma_id,int theloai){
		SoChiTieu_Obj sochitieu;
		/*
		 *Tra ve 3 gia tri
		 *0.ListView
		 *1.TOng Thu or CHi
		 */

		// List<ThuChi> MangThuChi = new ArrayList<ThuChi>();
		/*
		 * Dieu kien
		 */
		String[] args = {dateFrom,dateTo,ma_id,Integer.toString(theloai)};

		ArrayList<Item> items = new ArrayList<Item>();
		open();
		try {// select tinh tong so tien dua theo The Loai, select 2 Thu va Chi
			//cach khac select 2 lan select thu, select chi (select vay de tranh viec thu va chi cung ma theloai
			// dung cach 1: GROUP BY the_loai,ma_id => bo union all
			String sql = "select id,ma_id,ten_giao_dich,so_tien,ngay_thu_chi,the_loai from BangThuChi where ngay_thu_chi >= ?"
					+ " and ngay_thu_chi <= ?"
					+ " and ma_id = ?"
					+ " and the_loai= ?" 
					+ " order by ngay_thu_chi desc";
			Cursor cr = db.rawQuery(sql, args);
			// gia tri cr.getCount = 0 neu ko co gia tri
			// Log.e("TAG","Cursor"+ cr.getCount()+
			// " class.Database getList_AllThuChi_VayNo");
			if (cr.getCount() == 0) {
				close();
				return null;
			} else {
				sochitieu = new SoChiTieu_Obj();
				cr.moveToFirst();
				// boolean kt = true; // kt khoi tao lan dau
				String ngaytrunggian = null;
				float tongGiaTri=0;
				do {

					ThuChi newThuChi = new ThuChi();
					newThuChi.setId(cr.getInt(0)); // 0
					newThuChi.setMa_id(cr.getString(1)); // 1
					newThuChi.setTen_giao_dich(cr.getString(2)); // 2
					newThuChi.setSo_tien(cr.getFloat(3)); // 3
					newThuChi.setNgay_thu_chi(cr.getString(4)); // 4
					newThuChi.setTheloai(cr.getInt(5)); // 5
					/*
					 * Luu cac gia tri tong Thu or Chi
					 */
					tongGiaTri+=newThuChi.getSo_tien();
					//////////////////////////////////////////////////

					// thuc hien lan dau tien khoi tao bo Ngay vao array
					if (items.size() == 0 && ngaytrunggian == null) {
						// chuyen doi String thang ngay/thang/nam dung
						// LayDate_Moth_yeah
						items.add(new SectionItem(new LayDate_Month_Yeah()
						.CovertString_toNgayThangNam(newThuChi
								.getNgay_thu_chi())));
						items.add(new EntryItem(newThuChi));
						// kt= false;
						ngaytrunggian = newThuChi.getNgay_thu_chi();
						continue;
					}
					// thuc hien lan gia tri thu 2
					if (items.size() >= 2 && ngaytrunggian != null) {
						if (ngaytrunggian.equals(newThuChi.getNgay_thu_chi())) {
							items.add(new EntryItem(newThuChi));
						} else {
							items.add(new SectionItem(new LayDate_Month_Yeah()
							.CovertString_toNgayThangNam(newThuChi
									.getNgay_thu_chi())));
							items.add(new EntryItem(newThuChi));
							ngaytrunggian = newThuChi.getNgay_thu_chi();
						}
					}
				} while (cr.moveToNext());
				// ket thuc cursor
				cr.close();
				close();
				/*
				 * add arrayItem vao sochitieu
				 */
				sochitieu.setArrayItem(items);
				if(ma_id.equals("thu")){
					sochitieu.setTongThu(tongGiaTri);
				}else{
					sochitieu.setTongChi(tongGiaTri);
				}
				return sochitieu;
			}

		} catch (Exception e) {
			Log.e("TAG", "Loi class.Database select_chitiet_TheLoai");

		} finally {
			if (db.isOpen()) {
				close();
			}
		}
		return null;
	}
	public SoChiTieu_Obj select_chitiet_TheLoaiTheoNgay(String dateFrom,String ma_id,int theloai){
		SoChiTieu_Obj sochitieu;
		/*
		 *Tra ve 3 gia tri
		 *0.ListView
		 *1.TOng Thu or CHi
		 */

		// List<ThuChi> MangThuChi = new ArrayList<ThuChi>();
		/*
		 * Dieu kien
		 */
		String[] args = {dateFrom,ma_id,Integer.toString(theloai)};

		ArrayList<Item> items = new ArrayList<Item>();
		open();
		try {// select tinh tong so tien dua theo The Loai, select 2 Thu va Chi
			//cach khac select 2 lan select thu, select chi (select vay de tranh viec thu va chi cung ma theloai
			// dung cach 1: GROUP BY the_loai,ma_id => bo union all
			String sql = "select id,ma_id,ten_giao_dich,so_tien,ngay_thu_chi,the_loai from BangThuChi where ngay_thu_chi = ?"
					+ " and ma_id = ?"
					+ " and the_loai= ?" 
					+ " order by ngay_thu_chi desc";
			Cursor cr = db.rawQuery(sql, args);
			// gia tri cr.getCount = 0 neu ko co gia tri
			// Log.e("TAG","Cursor"+ cr.getCount()+
			// " class.Database getList_AllThuChi_VayNo");
			if (cr.getCount() == 0) {
				close();
				return null;
			} else {
				sochitieu = new SoChiTieu_Obj();
				cr.moveToFirst();
				// boolean kt = true; // kt khoi tao lan dau
				String ngaytrunggian = null;
				float tongGiaTri=0;
				do {

					ThuChi newThuChi = new ThuChi();
					newThuChi.setId(cr.getInt(0)); // 0
					newThuChi.setMa_id(cr.getString(1)); // 1
					newThuChi.setTen_giao_dich(cr.getString(2)); // 2
					newThuChi.setSo_tien(cr.getFloat(3)); // 3
					newThuChi.setNgay_thu_chi(cr.getString(4)); // 4
					newThuChi.setTheloai(cr.getInt(5)); // 5
					/*
					 * Luu cac gia tri tong Thu or Chi
					 */
					tongGiaTri+=newThuChi.getSo_tien();
					//////////////////////////////////////////////////

					// thuc hien lan dau tien khoi tao bo Ngay vao array
					if (items.size() == 0 && ngaytrunggian == null) {
						// chuyen doi String thang ngay/thang/nam dung
						// LayDate_Moth_yeah
						items.add(new SectionItem(new LayDate_Month_Yeah()
						.CovertString_toNgayThangNam(newThuChi
								.getNgay_thu_chi())));
						items.add(new EntryItem(newThuChi));
						// kt= false;
						ngaytrunggian = newThuChi.getNgay_thu_chi();
						continue;
					}
					// thuc hien lan gia tri thu 2
					if (items.size() >= 2 && ngaytrunggian != null) {
						if (ngaytrunggian.equals(newThuChi.getNgay_thu_chi())) {
							items.add(new EntryItem(newThuChi));
						} else {
							items.add(new SectionItem(new LayDate_Month_Yeah()
							.CovertString_toNgayThangNam(newThuChi
									.getNgay_thu_chi())));
							items.add(new EntryItem(newThuChi));
							ngaytrunggian = newThuChi.getNgay_thu_chi();
						}
					}
				} while (cr.moveToNext());
				// ket thuc cursor
				cr.close();
				close();
				/*
				 * add arrayItem vao sochitieu
				 */
				sochitieu.setArrayItem(items);
				if(ma_id.equals("thu")){
					sochitieu.setTongThu(tongGiaTri);
					sochitieu.setTongChi(0);
				}else{
					sochitieu.setTongChi(tongGiaTri);
					sochitieu.setTongThu(0);
				}

				sochitieu.setTongNo(0);
				sochitieu.setTongVay(0);
				return sochitieu;
			}

		} catch (Exception e) {
			Log.e("TAG", "Loi class.Database select_chitiet_TheLoai");

		} finally {
			if (db.isOpen()) {
				close();
			}
		}
		return null;
	}	
	
	public boolean kiemtraDatabase(String NameTable) {
		// Tra ve true co du lieu
		/*
		 * // kiem tra ton tai database hay ko SQLiteDatabase checkDB = null;
		 * try { checkDB = SQLiteDatabase.openDatabase(
		 * "/data/data/com.moneylove/databases/moneylove", null,
		 * SQLiteDatabase.OPEN_READONLY); checkDB.close(); } catch
		 * (SQLiteException e) { // database doesn't exist yet. } // neu co du
		 * lieu thi true, ko co thi false return checkDB != null ? true : false;
		 */
		// kiem tra co du lieu trong database hay ko
		open();
		Cursor cursor = db.rawQuery("select count(id) from " + NameTable, null);
		// Log.i("TAG", "KiemtraDatabase getCurson "+ cursor.getCount());

		if (cursor != null) {
			cursor.moveToFirst();
			if (cursor.getInt(0) == 0) { // => khong co du lieu
				cursor.close();
				close();
				return false;
			}
		}
		cursor.close(); // => co du lieu
		close();
		return true;
	}

	public void ClearDataBase() {
		try {
			open();
			dbHelper.onUpgrade(db, 0, 1);
			close();

		} catch (Exception e) {
			Log.e("TAG", "Loi Database method ClearDatabase " + e.getMessage());
		}
	}

	public void Delete_OneRowDatabase(int id,String ma_id,String ngayDatabase,float so_tienDatabase) {
		String s_id = Integer.toString(id);
		/*
		 * Thuc hien chinh sua du lieu varible truoc khi xoa 
		 */
		try {
			new SuaOrXoaBefore().Xoa_suadulieu_BeforeRemove(ma_id,ngayDatabase,so_tienDatabase);
			open();
			db.delete(My_SQLiteOpenHelper.Table_ThuChi,
					My_SQLiteOpenHelper.column_id + " = " + s_id, null);

			close();
		} catch (Exception e) {
			close();
			Log.e("TAG","class.Databse.Delete_OneRowDatabase "+ e.getLocalizedMessage());
		}

	}

	public void Update_OneRowDatabase(ThuChi tc) {
		try {
			open();

			ContentValues updateValues = new ContentValues();
			updateValues.put(My_SQLiteOpenHelper.column_Ma, tc.getMa_id());
			updateValues.put(My_SQLiteOpenHelper.column_TenGiaoDich,
					tc.getTen_giao_dich());
			updateValues.put(My_SQLiteOpenHelper.column_SoTien, tc.getSo_tien());
			updateValues.put(My_SQLiteOpenHelper.column_date, tc.getNgay_thu_chi());
			updateValues.put(My_SQLiteOpenHelper.column_GhiChu, tc.getGhichu());

			updateValues.put(My_SQLiteOpenHelper.column_TheLoai, tc.getTheloai());

			db.update(My_SQLiteOpenHelper.Table_ThuChi, updateValues,
					My_SQLiteOpenHelper.column_id + " = " + tc.getId(), null);

			close();
		} catch (Exception e) {
			Log.i("TAG","class.Databse.Update_OneRowDatabase "+e.getMessage());
		}

	}

	public ThuChi FindThuChi_id(int id) {
		String string_id = Integer.toString(id);
		open();
		Cursor c = db.query(My_SQLiteOpenHelper.Table_ThuChi, ColumnALL,
				My_SQLiteOpenHelper.column_id + " = " + string_id, null, null,
				null, null);
		if (c.moveToNext()) {
			ThuChi tc = cursorToThuChi(c);
			c.close();
			close();
			// Log.i("TAG","Database ten giao dich"+tc.getTen_giao_dich());
			return tc;
		}
		close();
		return null;
	}

	// thong ke chi tieu
	public ob_chart getdata_thongke(String ma_id,String dateForm,String dateTo) {
		ob_chart Chart = new ob_chart();
		String[] dk = { ma_id,dateForm,dateTo};
		String[] columnThongKe_Chart_TheLoai = { My_SQLiteOpenHelper.column_Ma,// 0
				My_SQLiteOpenHelper.column_TheLoai };// 1
		open();
		try {
			// dieu kien la chi or thu
			/*
			 * select lan 1 => co bao nhieu the loai
			 */
			// group by theo TheLoai
			Cursor cursor2 = db.query(My_SQLiteOpenHelper.Table_ThuChi,
					columnThongKe_Chart_TheLoai, My_SQLiteOpenHelper.column_Ma
					+ "=? and ngay_thu_chi >= ? and ngay_thu_chi <= ?",dk, My_SQLiteOpenHelper.column_TheLoai, null,
					null);
			if (cursor2.getCount() == 0) {
				close();
				return null;
			}else{
				cursor2.moveToFirst();
				//int sotheloai = cursor2.getCount(); //lay tong so luong theloai
				List<Integer> theloai = new ArrayList<Integer>();
				do {
					theloai.add(cursor2.getInt(1));
				} while (cursor2.moveToNext());
				cursor2.close();

				/*
				 * Thuc hien select lan thu 2 de tinh tong cac the loai
				 */
				// select lan 2
				String[] columnThongKe_Chart_TheLoai3 = {
						"Sum("+My_SQLiteOpenHelper.column_SoTien+")"};

				Cursor cursor3 = db.query(My_SQLiteOpenHelper.Table_ThuChi,columnThongKe_Chart_TheLoai3, 
						My_SQLiteOpenHelper.column_Ma+ "=? and ngay_thu_chi >= ? and ngay_thu_chi <= ?", dk,My_SQLiteOpenHelper.column_TheLoai, null, null);
				cursor3.moveToFirst();
				List<Integer> tongtien = new ArrayList<Integer>();
				do {
					tongtien.add((int)cursor3.getFloat(0));
				} while (cursor3.moveToNext());
				cursor3.close();

				Chart.setKQTheloai(theloai);
				Chart.setKQTong(tongtien);
				cursor3.close();
				close();
				return Chart;
			}//close else
		} catch (Exception e) {
			Log.e("TAG", "Loi class.Database->getdata_thongke"+ e.getMessage());
		}
		close();
		return null;
	}
	public float select_TongTien_Thang(String ma_id) {
		LayDate_Month_Yeah lay_ngay = new LayDate_Month_Yeah();
		List<String> thangHienTai = new ArrayList<String>();
		thangHienTai = lay_ngay.selectThang(0);
		try {
			open();
			Cursor cursor = db.rawQuery("select sum(so_tien) from BangThuChi where ma_id = ? and (ngay_thu_chi>= ? and ngay_thu_chi<= ?)",
					new String[]{ma_id,thangHienTai.get(1),thangHienTai.get(2)});
			if (cursor.moveToFirst()) {//co du lieu
				float sotien = cursor.getFloat(0);
				close();
				return sotien;
			} 
			else{
				close();
				return 0;
			}
		} catch (Exception e) {
			Log.i("TAG","Database.class->select_TongTien_Thang " + e.getMessage());
		}
		
		return 0;
	}
	public List<String> select_thuchi_trongThang(String ma_id) {
		LayDate_Month_Yeah lay_ngay = new LayDate_Month_Yeah();
		List<String> thangHienTai = new ArrayList<String>();
		thangHienTai = lay_ngay.selectThang(0);
		try {
			open();
			Cursor cursor = db.rawQuery("select ten_giao_dich,so_tien from BangThuChi where ma_id = ? and so_tien = max(so_tien) and (ngay_thu_chi>= ? and ngay_thu_chi<= ?)",
					new String[]{ma_id,thangHienTai.get(1),thangHienTai.get(2)});
			if (cursor.moveToFirst()) {//co du lieu
				float sotien = cursor.getFloat(0);
				close();
				//return sotien;
			} 
			else{
				close();
				return null;
			}
		} catch (Exception e) {
			Log.i("TAG","Database.class->select_TongTien_Thang " + e.getMessage());
		}
		
		return null;
	}
}
