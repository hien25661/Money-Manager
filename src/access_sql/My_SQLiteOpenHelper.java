package access_sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class My_SQLiteOpenHelper extends SQLiteOpenHelper {

	private static final int Database_version = 1;
	private static final String Database_Name = "moneylove.db";
	/*
	 * Ten cac Bang
	 */
	public static final String Table_ThuChi = "BangThuChi";
	public static final String Table_VayNo = "BangVayNo";
	public static final String Table_QuaKhu_ofVayNo = "BangQuaKhuVayNo";
	public static final String Table_Login = "Login";

	
	
	/*
	 * Table_ThuChi
	 */
	public static final String column_user = "tendn"; // 0
	public static final String column_pass = "matkhau"; // 1
	/*
	 * Table_ThuChi
	 */
	public static final String column_id = "id"; // 0
	public static final String column_Ma = "ma_id"; // 1
	public static final String column_TenGiaoDich = "ten_giao_dich"; // 2
	public static final String column_SoTien = "so_tien"; // 3
	public static final String column_date = "ngay_thu_chi"; // 4
	public static final String column_GhiChu = "ghi_chu"; // 5
	public static final String column_TheLoai = "the_loai"; // 6
	// public static final String column_IdQuanLy = "maid_quanly";
	/*
	 * Table_VayNo
	 */
	public static final String columnVayNo_id = "id"; // 0
	public static final String columnVayNo_Ma = "ma_id"; // 1
	public static final String columnVayNo_TenNguoi = "ten_nguoi_vayno"; // 2
	public static final String columnVayNo_SoTien = "so_tien"; // 3
	public static final String columnVayNo_NgayVay = "ngay_vay"; // 4
	public static final String columnVayNo_NgayTra = "ngay_tra"; // 5
	public static final String columnVayNo_GhiChu = "ghi_chu"; // 6

	/*
	 * BangQuaKhuVayNo
	 */
	public static final String columnQuaKhu_id = "id"; // 0
	public static final String columnQuaKhu_maQuaKhu = "ma_quakhu"; // 1
	public static final String columnQuaKhu_maVayNo = "ma_vayno"; // 1
	public static final String columnQuaKhu_SoTien = "sotien_quakhu"; // 2
	public static final String columnQuaKhu_Ngay = "ngay_giao_dich"; // 3

	private final String Database_QuaKhu ="CREATE TABLE [BangQuaKhuVayNo] ("+
			"[id] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,"+
			"[ma_quakhu] INTEGER  NULL,"+
			"[ma_vayno] vaRCHAR(4)  NULL,"+
			"[sotien_quakhu] fLOAT  NOT NULL,"+
			"[ngay_giao_dich] date  NOT NULL)"; // 4

	/*
	 * noi dung cac table
	 */
	
	private final String Database_create = "create table " + Table_ThuChi
			+ " (" + column_id//0
			+ " integer primary key autoincrement NOT NULL, " + column_Ma
			+ " VARCHAR(3) NOT NULL, " // 1
			+ column_TenGiaoDich + " text NOT NULL, " // 2
			+ column_SoTien + " float NOT NULL, " // 3
			+ column_date + " date NOT NULL, " // 4
			+ column_GhiChu + " text NULL, " // 5
			+ column_TheLoai + " int NULL);"; // 6

	private final String DatabaseVayNo_create = "create table " + Table_VayNo
			+ " (" + columnVayNo_id
			+ " integer primary key autoincrement NOT NULL, " 
			+ columnVayNo_Ma+ " VARCHAR(4) NOT NULL, " 
			+ columnVayNo_TenNguoi+ " text NOT NULL, " + columnVayNo_SoTien + " float NOT NULL, "
			+ columnVayNo_NgayVay + " date NOT NULL, " + columnVayNo_NgayTra
			+ " date NULL , " + columnVayNo_GhiChu + " text NULL);";

	private final String DatabaseLogin_create = "create table " + Table_Login
			+ " (" + column_user
			+ " integer primary key autoincrement NOT NULL, " 
			+ column_pass + " VARCHAR(4) NOT NULL)";

	
	public My_SQLiteOpenHelper(Context context) {
		super(context, Database_Name, null, Database_version);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL(Database_create);
			db.execSQL(DatabaseVayNo_create);
			db.execSQL(Database_QuaKhu);
			db.execSQL(DatabaseLogin_create);
		} catch (SQLiteException e) {
			Log.i("TAG", "Loi onCreate class.MySQLOpenHelper " + e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + Table_ThuChi);
		db.execSQL("DROP TABLE IF EXISTS " + Table_VayNo);
		db.execSQL("DROP TABLE IF EXISTS " + Table_QuaKhu_ofVayNo);
		db.execSQL("DROP TABLE IF EXISTS " + Table_Login);
		onCreate(db);
	}

}
