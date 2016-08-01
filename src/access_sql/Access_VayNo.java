package access_sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import object.QuaKhu_VayNo_Object;
import object.SoChiTieu_Obj;
import object.ThuChi;
import sochitieu.EntryItem;
import sochitieu.Item;
import sochitieu.SectionItem;
import util.LayDate_Month_Yeah;
import util.Variable;
import vayno_activity.SoNoObject;

public class Access_VayNo {

	private static final String[] String = null;
	private SQLiteDatabase db;
	private My_SQLiteOpenHelper dbHelper;

	// context dinh nghia vi tri luu giu
	public Access_VayNo(Context context) {
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

	public void insertVayNo(ThuChi vn) {
		try {
			open();
			ContentValues values = new ContentValues();
			// dua du lieu vao values
			values.put(My_SQLiteOpenHelper.columnVayNo_Ma, vn.getMa_id());
			values.put(My_SQLiteOpenHelper.columnVayNo_TenNguoi,vn.getTen_giao_dich());
			values.put(My_SQLiteOpenHelper.columnVayNo_SoTien, vn.getSo_tien());
			values.put(My_SQLiteOpenHelper.columnVayNo_NgayVay, vn.getNgay_thu_chi());
			values.put(My_SQLiteOpenHelper.columnVayNo_NgayTra, vn.getNgay_vay_no());
			values.put(My_SQLiteOpenHelper.columnVayNo_GhiChu, vn.getGhichu());
			db.insert(My_SQLiteOpenHelper.Table_VayNo, null, values);
			close();
		} catch (Exception e) {
			Log.e("TAG","Loi Access_vayNo.clas -> insertVayNo "+e.getMessage());
		}
	}
	public void insertVayNo_csv(ThuChi vn) {
		try {
			Log.e("TAG", Integer.toString(vn.getId()));
			open();
			ContentValues values = new ContentValues();
			
			// dua du lieu vao values
			values.put(My_SQLiteOpenHelper.columnVayNo_id, vn.getId());
			values.put(My_SQLiteOpenHelper.columnVayNo_Ma, vn.getMa_id());
			values.put(My_SQLiteOpenHelper.columnVayNo_TenNguoi,vn.getTen_giao_dich());
			values.put(My_SQLiteOpenHelper.columnVayNo_SoTien, vn.getSo_tien());
			values.put(My_SQLiteOpenHelper.columnVayNo_NgayVay, vn.getNgay_thu_chi());
			values.put(My_SQLiteOpenHelper.columnVayNo_NgayTra, vn.getNgay_vay_no());
			values.put(My_SQLiteOpenHelper.columnVayNo_GhiChu, vn.getGhichu());
			db.insert(My_SQLiteOpenHelper.Table_VayNo, null, values);
			close();
		} catch (Exception e) {
			Log.e("TAG","Loi Access_vayNo.clas -> insertVayNo "+e.getMessage());
		}
	}
	public void insertQuaKhu(QuaKhu_VayNo_Object vn) {
		try {
			open();
			ContentValues values = new ContentValues();
			// dua du lieu vao values
			values.put(My_SQLiteOpenHelper.columnQuaKhu_maVayNo, vn.getMa_vayno());  //vay ...no
			values.put(My_SQLiteOpenHelper.columnQuaKhu_maQuaKhu,vn.getMa_id());// 1.. .2..3 theo bangvayno
			values.put(My_SQLiteOpenHelper.columnQuaKhu_SoTien, vn.getSotien());
			values.put(My_SQLiteOpenHelper.columnQuaKhu_Ngay, vn.getNgay_tra_no());
			db.insert(My_SQLiteOpenHelper.Table_QuaKhu_ofVayNo, null, values);
			close();
		} catch (Exception e) {
			Log.e("TAG","Loi Access_vayNo.clas -> insertQuaKhu "+e.getMessage());
		}
	}
	/*
	 * tra tien vayno thuc hien: 2 gia tri dua vao(khi click item,Sotien user)
	 * B1: insert vao voi table BangVayNo ma_id = id cua vayno Goc
	 * 		sotien = sotien user tra edtitText
	 * B2: update lai sotien of ma_id goc
	 * su dung NgayVay de phan biet cac khoan vay cua cung 1 nguoi
	 * pay update NgayTra la ngay hien tai cua viec chi tra
	 * 
	 * if sotien of user(vay) = sotien of (pay)
	 * => update lai user(vay) voi so tien goc (so tien luc vay muon) 
	 * va ma_id vayx|| nox
	 * 
	 * dung id thuchi de phan biet cac khoan vay 
	 * id = ma_id insert vao 
	 */
	public void DaTraTienVayNo(ThuChi vn,float sotien) {
		//tao 1 bang luu vao
		//@vn da co san gia tri tu clickItem 
		//=> chinh sua, lay nhung gia tri can
		/*
		 * B1: ket thuc buoc 1 se dc @values chua gia tri insert
		 */
		float sotienUpdate = vn.getSo_tien() - sotien;
		ThuChi vnInsertDatra = new ThuChi();
		//lay id gan vao ma_id khi xem qua khu => load dk ma_id va ngay giao dich

		vnInsertDatra.setId(vn.getId());
		//maVayNo
		vnInsertDatra.setMa_id(vn.getMa_id());
		//so tien la so tien da tra
		vnInsertDatra.setSo_tien(sotien);
		//Ngay tra lay ngay hien tai
		vnInsertDatra.setNgay_vay_no(new LayDate_Month_Yeah().NgayHienTai_THEO_InsertDataBase());

		ContentValues values = new ContentValues();
		// dua du lieu vao values
		values.put(My_SQLiteOpenHelper.columnQuaKhu_maQuaKhu,vnInsertDatra.getId());
		values.put(My_SQLiteOpenHelper.columnQuaKhu_maVayNo, vnInsertDatra.getMa_id());
		values.put(My_SQLiteOpenHelper.columnQuaKhu_SoTien, vnInsertDatra.getSo_tien());
		values.put(My_SQLiteOpenHelper.columnQuaKhu_Ngay, vnInsertDatra.getNgay_vay_no());
		try {
			open();
			db.insert(My_SQLiteOpenHelper.Table_QuaKhu_ofVayNo, null, values);
			close();
		} catch (Exception e) {
			Log.e("TAG","Loi Access_vayNo.clas -> DaTraTienVayNo (Phan insert) "+e.getMessage());
		}
		/*
		 *B2: Update gia tri goc 
		 *so tien gia tri goc - so tien user da tra
		 */
		String[] dkUpdate = {Integer.toString(vn.getId())};
		ContentValues valueUpadte = new ContentValues();
		/*kt tra @sotienUpdate neu =0 thi update ma_id = pvay (=Variable.DaTraVay) 
		or pno (variable.DaTraNo)
		else @sotienUpdate > 0 chi update so iten
		 */
		if(sotienUpdate == 0){ // truong hop tra het tien no
			//update ma_id va so tien goc
			float TongSoTienDaTra =selectTongSoTienDaTra(vn.getId());
			if(TongSoTienDaTra!=0){
				String maUpdate;
				//kt ma_id la vay or no
				if(vn.getMa_id().equals("vay")){
					maUpdate = Variable.DaTraVay;
				}else{
					maUpdate = Variable.DaTraNo;
				}
				valueUpadte.put(My_SQLiteOpenHelper.columnVayNo_Ma,maUpdate);
				valueUpadte.put(My_SQLiteOpenHelper.columnVayNo_SoTien,TongSoTienDaTra);
			}
		}else{
			// @sotienUpdate so tien tru ra >0 chi update so tien 
			valueUpadte.put(My_SQLiteOpenHelper.columnVayNo_SoTien,sotienUpdate);
		}
		try {
			open();

			db.update(My_SQLiteOpenHelper.Table_VayNo,valueUpadte, My_SQLiteOpenHelper.columnVayNo_id +" = ?", dkUpdate);

			close();
		} catch (Exception e) {
			Log.e("TAG","Loi Access_vayNo.clas -> insertVayNo "+e.getMessage());
		}
	}



	/*public List<VayNo> getListVayNo() {
		open();
		List<VayNo> array = new ArrayList<VayNo>();

		Cursor cursor = db.query(My_SQLiteOpenHelper.Table_VayNo, Column_VayNo,
				null, null, null, null, null);

		cursor.moveToFirst();

		do {
			VayNo newVayNo = cursorToVayNo(cursor);
			array.add(newVayNo);
		} while (cursor.moveToNext());

		cursor.close();
		close();
		return array;
	}*/

	/*
	 * public List<ThuChi> getList_Timkiem(String timkiem) { List<ThuChi>
	 * MangThuChi = new ArrayList<ThuChi>(); open(); try { String a[] = {
	 * timkiem }; Cursor cursor = db .query(My_SQLiteOpenHelper.Table_VayNo,
	 * ColumnALL, My_SQLiteOpenHelper.column_TenGiaoDich, a, null, null, null);
	 * 
	 * cursor.moveToFirst();
	 * 
	 * do { ThuChi newThuChi = cursorToThuChi(cursor);
	 * MangThuChi.add(newThuChi); } while (cursor.moveToNext());
	 * 
	 * cursor.close(); } catch (SQLiteException e) {
	 * 
	 * } close(); return MangThuChi; }
	 */

	public float selectTongSoTienDaTra(int id) {
		float tongDatra;
		String chuoiID = Integer.toString(id);
		String sql = "select sum("+My_SQLiteOpenHelper.columnQuaKhu_SoTien+") from "+My_SQLiteOpenHelper.Table_QuaKhu_ofVayNo+
				" where "+ My_SQLiteOpenHelper.columnQuaKhu_maQuaKhu + " = '"+chuoiID+"'";
		try {
			open();
			Cursor cr = db.rawQuery(sql, null);
			cr.moveToFirst();
			tongDatra = cr.getFloat(0); //
			//Log.e("TAG","So Tong Tra la "+ tongDatra);
			close();
			return  tongDatra;
		} catch (Exception e) {
			Log.e("TAG","Loi Access_vayNo.clas -> selectTongSoTienDaTra "+e.getMessage());

		}
		return 0;
	}

	public boolean kiemtraVayNo() {

		/*
		 *  * // kiem tra ton tai database hay ko SQLiteDatabase checkDB = null;
		 * try { checkDB = SQLiteDatabase.openDatabase(
		 * "/data/data/com.moneylove/databases/moneylove", null,
		 * SQLiteDatabase.OPEN_READONLY); checkDB.close(); } catch
		 * (SQLiteException e) { // database doesn't exist yet. } // neu co du
		 * lieu thi true, ko co thi false return checkDB != null ? true : false;
		 */
		// kiem tra co du lieu trong database hay ko
		String stringSQL = "select " + My_SQLiteOpenHelper.columnVayNo_id
				+ " from " + My_SQLiteOpenHelper.Table_VayNo;
		open();
		Cursor cursor = db.rawQuery(stringSQL, null);

		if (cursor != null && cursor.getCount() > 0) { // => co du lieu
			cursor.close();
			close();
			return true;
		} else {
			// cursor(cr) ko can goi cr.close() vi cr la null
			close();// dong database
			return false;
		}
	}

	public void ClearMy_SQLiteOpenHelper() {
		try {
			open();
			db.execSQL(" Drop table " + My_SQLiteOpenHelper.Table_VayNo);
			close();
		} catch (Exception e) {
			Log.i("TAG",
					"Loi Database method ClearMy_SQLiteOpenHelper "
							+ e.getMessage());
		}
	}
	// cac ham delete
	public void Delete_OneRowDatabase(int id) {
		String s_id = Integer.toString(id);
		try {
			open();
			db.delete(My_SQLiteOpenHelper.Table_QuaKhu_ofVayNo,My_SQLiteOpenHelper.columnQuaKhu_maQuaKhu + " = " + s_id, null);
			// null mang cac dieu kien where
			db.delete(My_SQLiteOpenHelper.Table_VayNo,My_SQLiteOpenHelper.columnVayNo_id + " = " + s_id, null);
			close();
		} catch (Exception e) {
			Log.i("TAG","Loi Bang VayNo method Delete_OneRowDatabase "+ e.getMessage());
		} 
	}
	public boolean Delete_theoTenNguoi_VayNo(String tenNguoiGiaoDich) {
		//danh sach cac id bangvayno
		List<Integer> listID_VayNo = new ArrayList<Integer>();
		listID_VayNo = selectID_VayNo(tenNguoiGiaoDich);
		if(listID_VayNo==null){
			return false;
		}else{
			try {
				open();
				//thuc hien xoa ban qua khu
				for (int i = 0; i < listID_VayNo.size(); i++) {

					String sqlQuaKhu = "delete from "+My_SQLiteOpenHelper.Table_QuaKhu_ofVayNo + 		
							" where "+My_SQLiteOpenHelper.columnQuaKhu_maQuaKhu +" = '"+listID_VayNo.get(i)+"'";
					db.execSQL(sqlQuaKhu);
				}
				close();
			} catch (Exception e) {
				Log.e("TAG","Loi Access_VayNo.class->Delete_theoTenNguoi_VayNo (xoa list qua khu)"+ e.getMessage());
			}
			////ket thuc xoa bang qua khu chuyen qua xoa danh sach BangVayNo

			try {
				open();
				for (int i = 0; i < listID_VayNo.size(); i++) {
					String sqlVayNo = "delete from "+My_SQLiteOpenHelper.Table_VayNo + 		
							" where "+My_SQLiteOpenHelper.columnVayNo_id +" = '"+listID_VayNo.get(i)+"'";
					db.execSQL(sqlVayNo);
				}

				close();
				return true;
			} catch (Exception e) {
				Log.e("TAG","class.Access_vayNo->Delete_theoTenNguoi_VayNo (xoa list bang vayno) "+e.getMessage());
			}		
		}//close else
		return false;
	}
	/* selectID_VayNo dung cho Delete_theoTenNguoi_VayNo goi
	 * tra ve danh sach cac id can xoa
	 */
	private List<Integer> selectID_VayNo(String tenNguoi) {
		String sql = "select "+My_SQLiteOpenHelper.columnVayNo_id+" from "+My_SQLiteOpenHelper.Table_VayNo+
				" where "+ My_SQLiteOpenHelper.columnVayNo_TenNguoi + " = '"+tenNguoi+"'";
		try {
			open();
			Cursor cr = db.rawQuery(sql, null);
			if(cr.getCount()==0){
				return null;
			}else{
				List<Integer> listID = new ArrayList<Integer>();
				cr.moveToFirst();
				do{
					listID.add(cr.getInt(0)); //
				}while(cr.moveToNext());
				cr.close();
				close();
				return  listID;
			}
		} catch (Exception e) {
			Log.e("TAG","Loi Access_vayNo.class -> selectID_VayNo "+e.getMessage());
		}
		return null;
	}



	public void Update_OneRowDatabase(ThuChi vn) {
		// dua gia tri vao updateValues
		ContentValues updateValues = new ContentValues();
		updateValues.put(My_SQLiteOpenHelper.columnVayNo_Ma, vn.getMa_id());
		updateValues.put(My_SQLiteOpenHelper.columnVayNo_TenNguoi,vn.getTen_giao_dich());
		updateValues.put(My_SQLiteOpenHelper.columnVayNo_SoTien,vn.getSo_tien());
		updateValues.put(My_SQLiteOpenHelper.columnVayNo_NgayVay,vn.getNgay_thu_chi());
		updateValues.put(My_SQLiteOpenHelper.columnVayNo_NgayTra,vn.getNgay_vay_no());
		updateValues.put(My_SQLiteOpenHelper.columnVayNo_GhiChu, vn.getGhichu());
		// update Access_vayNo
		try {
			open();
			db.update(My_SQLiteOpenHelper.Table_VayNo, updateValues,
					My_SQLiteOpenHelper.columnVayNo_id + " = " + vn.getId(), null);
			close();
		} catch (Exception e) {
			Log.e("TAG","class.Access_vayNo->Update_OneRowDatabase "+e.getMessage());
		}
	}
	public void UpdateTenNguoiVayNo(String tenOld,String tenNew){
		String sql = "update "+My_SQLiteOpenHelper.Table_VayNo + " set "+My_SQLiteOpenHelper.columnVayNo_TenNguoi+
				" = '"+ tenNew +"' where "+My_SQLiteOpenHelper.columnVayNo_TenNguoi +" = '"+tenOld+"'";
		try {
			open();
			db.execSQL(sql);
			close();

		} catch (Exception e) {
			Log.e("TAG","class.Access_vayNo->UpdateTenNguoiVayNo "+e.getMessage());
		}
	}

	public ThuChi cursorToVayNo(Cursor cursor) {
		ThuChi vn = new ThuChi();
		vn.setId(cursor.getInt(0)); // 0
		vn.setMa_id(cursor.getString(1)); // 1
		vn.setTen_giao_dich(cursor.getString(2)); // 2
		vn.setSo_tien(cursor.getFloat(3)); // 3
		vn.setNgay_thu_chi(cursor.getString(4)); // 4
		vn.setNgay_vay_no(cursor.getString(5)); // 5
		vn.setGhichu(cursor.getString(6)); // 6
		return vn;
	}
	public ThuChi Find_id_VayNo_id(int id) {
		String[] Column_VayNo = { My_SQLiteOpenHelper.columnVayNo_id,
				My_SQLiteOpenHelper.columnVayNo_Ma,
				My_SQLiteOpenHelper.columnVayNo_TenNguoi,
				My_SQLiteOpenHelper.columnVayNo_SoTien,
				My_SQLiteOpenHelper.columnVayNo_NgayVay,
				My_SQLiteOpenHelper.columnVayNo_NgayTra,
				My_SQLiteOpenHelper.columnVayNo_GhiChu
		};
		String string_id = Integer.toString(id);
		try {
			open();
			Cursor c = db.query(My_SQLiteOpenHelper.Table_VayNo, Column_VayNo,
					My_SQLiteOpenHelper.columnVayNo_id + " = " + string_id, null,
					null, null, null);
			if (c.moveToNext()) {
				ThuChi tc = cursorToVayNo(c);
				c.close();
				close();
				// Log.i("TAG","Database ten giao dich"+tc.getTen_giao_dich());
				return tc;
			}
			close();
		} catch (Exception e) {
			Log.e("TAG","Loi class.Access_VayNo.Find_id_VayNo_id "+e.getMessage());
		}
		return null;
	}

	/*
	 * Du lieu SoNoObject theo thu tu gui di
	 * 0: listview
	 * 1: tongVay
	 * 2: tongNo
	 */
	public SoNoObject getAll_NguoiVayNo() {
		SoNoObject sono_obj;
		String[] columSoNo = {My_SQLiteOpenHelper.columnVayNo_Ma,//0
				My_SQLiteOpenHelper.columnVayNo_TenNguoi,//1
				"Sum("+ My_SQLiteOpenHelper.columnVayNo_SoTien+")"};//2

		ArrayList<String> items = new ArrayList<String>();
		float tongVay=0;
		float tongNo=0;
		try {
			open();
			String[] dk = {"vay","no",Variable.DaTraNo,Variable.DaTraVay};
			Cursor cursor = db.query(My_SQLiteOpenHelper.Table_VayNo,
					columSoNo, "(ma_id=? or ma_id=? or ma_id=?  or ma_id=?)",dk,
					My_SQLiteOpenHelper.columnVayNo_TenNguoi, null, My_SQLiteOpenHelper.columnVayNo_TenNguoi + " ASC");
			if (cursor.getCount() == 0) {
				close();
				return null;
			} else {
				sono_obj = new SoNoObject();
				cursor.moveToFirst();
				do {
					items.add(cursor.getString(1));
					if(cursor.getString(0).equals("vay")){
						tongVay+= cursor.getFloat(2);
					}else{
						tongNo+=cursor.getFloat(2);
					}

				} while (cursor.moveToNext());
				cursor.close();
				close();
				/*
				 * add du lieu vao Object SoNoObject
				 */
				sono_obj.setTvVay(tongVay);
				sono_obj.setTvNo(tongNo);
				sono_obj.setList(items);
				return sono_obj;
			}
		} catch (Exception e) {
			Log.e("TAG", "Loi class.Access_VayNo->getAll_NguoiVayNo");

		} finally {
			if (db.isOpen()) {
				close();
			}
		}
		return null;
	}

	//dua vao gia tri id goc cua vay (chi)
	// lay ra sotien da tra, ngay_tra
	// da lay id goc insert ma_id
	//chi co duy nhat 1 ma_id cua datraQuaKhu => ko bi trung
	public  SoChiTieu_Obj getQuaKhu_DaTra(int idSo) {
		SoChiTieu_Obj sochitieu = new SoChiTieu_Obj();

		String id = Integer.toString(idSo);
		String[] Column_VayNo = { My_SQLiteOpenHelper.columnQuaKhu_id,//0
				My_SQLiteOpenHelper.columnQuaKhu_maQuaKhu,//1
				My_SQLiteOpenHelper.columnQuaKhu_maVayNo,//2
				My_SQLiteOpenHelper.columnQuaKhu_SoTien,//3
				My_SQLiteOpenHelper.columnQuaKhu_Ngay,//4
		};
		ArrayList<Item> items = new ArrayList<Item>();
		try {
			open();
			//select dk trung ten, ma_id = vay or no
			String[] dk = { id };
			Cursor cursor = db.query(My_SQLiteOpenHelper.Table_QuaKhu_ofVayNo, Column_VayNo,
					My_SQLiteOpenHelper.columnQuaKhu_maQuaKhu + " = ? ", dk, null,
					null, My_SQLiteOpenHelper.columnQuaKhu_Ngay + " DESC");
			if (cursor.getCount() == 0) {
				close();
				return null;
			} 
			else{
				float tongtien =0;
				cursor.moveToFirst();
				String ngaytrunggian = null;
				boolean ktvay_or_no = false;// mac dinh false la vay
				do {
					ThuChi newVayNo = new ThuChi();
					newVayNo.setId(cursor.getInt(0)); // 0 

					//bat buot lay Ma_id de EnTryAdapter chay dc (Ma_id) gio la cac so cua id VayNo 
					newVayNo.setMa_id(Integer.toString(cursor.getInt(1))); // 1
					//ten giao dich luu ma_id goc Qua Khu
					newVayNo.setTen_giao_dich(cursor.getString(2)); // 2
					newVayNo.setSo_tien(cursor.getFloat(3)); // 3
					newVayNo.setNgay_vay_no(cursor.getString(4)); // 4
					//////////////////////////////////////////////////
					tongtien += newVayNo.getSo_tien();
					if(newVayNo.getTen_giao_dich().equals("no")){
						ktvay_or_no = true;
					}


					// thuc hien lan dau tien khoi tao bo Ngay vao array
					if (items.size() == 0 && ngaytrunggian == null) {
						// chuyen doi String thang ngay/thang/nam dung
						// LayDate_Moth_yeah
						items.add(new SectionItem(new LayDate_Month_Yeah()
						.CovertString_toNgayThangNam(newVayNo
								.getNgay_vay_no())));
						items.add(new EntryItem(newVayNo));
						// kt= false;
						ngaytrunggian = newVayNo.getNgay_vay_no();
						continue;
					}
					// thuc hien lan gia tri thu 2
					if (items.size() >= 2 && ngaytrunggian != null) {
						if (ngaytrunggian.equals(newVayNo.getNgay_vay_no())) {
							items.add(new EntryItem(newVayNo));
						} else {
							items.add(new SectionItem(new LayDate_Month_Yeah()
							.CovertString_toNgayThangNam(newVayNo
									.getNgay_vay_no())));
							items.add(new EntryItem(newVayNo));
							ngaytrunggian = newVayNo.getNgay_vay_no();
						}
					}
				} while (cursor.moveToNext());
				cursor.close();
				close();
				sochitieu.setArrayItem(items);
				if(ktvay_or_no){//neu la no 
					sochitieu.setTongNo(tongtien);
					sochitieu.setTongVay(0);
				}else{
					sochitieu.setTongVay(tongtien);
					sochitieu.setTongNo(0);
				}
				return sochitieu;
			}
		} catch (Exception e) {
			Log.e("TAG","class.Access_vayNo->getList_NguoiVayNo_chitiet "+e.getMessage());
		}
		return null;
	}




	/*
	 * select ma_id, Ten Nguoi, So Tien, NgayVay
	 */
	public SoChiTieu_Obj getList_NguoiVayNo_chitiet(String ten) {
		String[] Column_VayNo = { My_SQLiteOpenHelper.columnVayNo_id,//0
				My_SQLiteOpenHelper.columnVayNo_Ma,//1
				My_SQLiteOpenHelper.columnVayNo_TenNguoi, //2
				My_SQLiteOpenHelper.columnVayNo_SoTien,//3
				My_SQLiteOpenHelper.columnVayNo_NgayVay,//4
		};
		SoChiTieu_Obj sochitieu;
		ArrayList<Item> items = new ArrayList<Item>();
		try {
			open();
			//select dk trung ten, ma_id = vay or no
			String[] dk = { ten ,"vay","no",Variable.DaTraNo,Variable.DaTraVay};
			Cursor cursor = db.query(My_SQLiteOpenHelper.Table_VayNo, Column_VayNo,
					My_SQLiteOpenHelper.columnVayNo_TenNguoi + " = ? and ( ma_id = ? or ma_id = ? or ma_id = ?" +
							"or ma_id = ?)", dk, null,
							null, My_SQLiteOpenHelper.columnVayNo_NgayVay + " DESC");
			if (cursor.getCount() == 0) {
				close();
				return null;
			} 
			else{
				sochitieu = new SoChiTieu_Obj();
				cursor.moveToFirst();
				String ngaytrunggian = null;
				float tongVay=0;
				float tongNo=0;
				do {
					ThuChi newVayNo = new ThuChi();
					newVayNo.setId(cursor.getInt(0)); // 0
					newVayNo.setMa_id(cursor.getString(1)); // 1
					newVayNo.setTen_giao_dich(cursor.getString(2)); // 2
					newVayNo.setSo_tien(cursor.getFloat(3)); // 3
					newVayNo.setNgay_thu_chi(cursor.getString(4)); // 4
					/*
					 * Luu cac gia tri tong Vay No
					 */
					if(newVayNo.getMa_id().equals("vay")){
						tongVay+= newVayNo.getSo_tien();
					}else{
						tongNo+= newVayNo.getSo_tien();
					}
					//////////////////////////////////////////////////

					// thuc hien lan dau tien khoi tao bo Ngay vao array
					if (items.size() == 0 && ngaytrunggian == null) {
						// chuyen doi String thang ngay/thang/nam dung
						// LayDate_Moth_yeah
						items.add(new SectionItem(new LayDate_Month_Yeah()
						.CovertString_toNgayThangNam(newVayNo
								.getNgay_thu_chi())));
						items.add(new EntryItem(newVayNo));
						// kt= false;
						ngaytrunggian = newVayNo.getNgay_thu_chi();
						continue;
					}
					// thuc hien lan gia tri thu 2
					if (items.size() >= 2 && ngaytrunggian != null) {
						if (ngaytrunggian.equals(newVayNo.getNgay_thu_chi())) {
							items.add(new EntryItem(newVayNo));
						} else {
							items.add(new SectionItem(new LayDate_Month_Yeah()
							.CovertString_toNgayThangNam(newVayNo
									.getNgay_thu_chi())));
							items.add(new EntryItem(newVayNo));
							ngaytrunggian = newVayNo.getNgay_thu_chi();
						}
					}
				} while (cursor.moveToNext());
				cursor.close();
				close();
				/*
				 * add arrayItem vao sochitieu
				 */
				sochitieu.setArrayItem(items);
				sochitieu.setTongVay(tongVay);
				sochitieu.setTongNo(tongNo);
				return sochitieu;
			}
		} catch (Exception e) {
			Log.e("TAG","class.Access_vayNo->getList_NguoiVayNo_chitiet "+e.getMessage());
		}
		return null;
	}

}