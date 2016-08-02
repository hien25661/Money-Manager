package util;

import com.boxtimer365.moneylove.R;

public class Variable {

	public static final String THELOAI = "theloai";
	public static final String THU_or_CHI = "thuchi";
	public static final String VAY_or_NO = "vayno";
	public static final String CHITIEU_CALL = "chitieu";
	public static final String UPDATE_DATABASE = "update";
	public static final String Delete_DATABASE = "delete";
	public static final String NameUser_GiaoDichVayNo = "user_giao_dich";
	/*
	bien dung cho da tra Access_vayNo
	 */
	public static final String DaTraVay = "pvay";
	public static final String DaTraNo = "pno";


	public static final int ICON_NO = R.drawable.notien;
	public static final int ICON_Vay = R.drawable.vaytien_sochitieu;

	public static final int ICON_GridView_Main[] = { R.drawable.add_item,//0
		R.drawable.icon_gridview_wallet, //1
		R.drawable.sono,//2
		R.drawable.icon_gridview_chart, //3
		R.drawable.doitien_a,//4
		R.drawable.icon_gridview_csv, //5
		R.drawable.icon_gridview_backup,//6
		R.drawable.database_remove,////7
		R.drawable.ic_grv_thongkethang,//8
		R.drawable.icon_gridview_chiatien};//9

	/*
	 * Requestcode Intent
	 */
	public static final String request = "requestCode";
	public static final int requestcode = 101;
	public static final int requestcode_InsertThuChi = 104; // gridView goi thuchi
	public static final int requestcode_InsertVayNo = 107; // gridView goi vayno
	public static final int requestcode_UpdateSua = 105;
	public static final int requestcode_SoChiTieu =106;
	public static final int requestcode_MoRong =107;

	// Thu
	public static final String[] INCOME_THELOAI = { "Salary", "Reward","sell" }; // chuoi
	// hinh icon
	public static final int[] ICONS_INCOME_THELOAI = {
		R.drawable.moneypic_icon, R.drawable.moneycoin_icon, R.drawable.ic_theloai_tienbando };

	// Chi
	public static final String[] Expense_THELOAI = { "Eating", "Friend",
		"Family", "Travel", "Study", "Entertainment", "Health",
		"Sport", "Love" }; // chuoi 9
	// hinh icon
	public static final int[] ICONS_Expense_THELOAI = { R.drawable.steakicon,
		R.drawable.friend, R.drawable.famil, R.drawable.travel_icon,
		R.drawable.case_leather, R.drawable.games, R.drawable.hospital,
		R.drawable.soccer, R.drawable.heart };


}
