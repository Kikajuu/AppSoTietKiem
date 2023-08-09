package com.example.appsotietkiem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TEN_DATABASE= "SoTietKiem.db";

    public static final String TEN_BANG_SOTIETKIEM = "SoTietKiem";
    public static final String TEN_BANG_USER = "Users";
    public static final String TEN_BANG_TaiKhoanNganHang = "TaiKhoanNganHang";
    public static final String TEN_BANG_TienLai = "TienLai";

    //Cot bang user
    public static final String COT_USERID ="_userid";
    public static final String COT_USERNAME ="_username";
    public static final String COT_TENDANGNHAP ="_tendangnhap";
    public static final String COT_EMAIL = "_email";
    public static final String COT_PASSWORD ="_matkhau";
    public static final String COT_ISADMIN ="_isadmin";

    //Cot bang TaiKhoanNganHang
    public static final String COT_SOTAIKHOAN = "_sotaikhoan";
    public static final String COT_SODU = "_sodu";
    public static final String COT_UIDNH = "_uid";

    //Cot bang TienLai
    public static final String COT_TIENLAI_ID ="_tienlaiID";
    public static final String COT_MASOTL ="_masoTL";
    public static final String COT_KIHAN ="_kihan";
    public static final String COT_SOTIENLAI = "_sotienlai";
    public static final String COT_TISUAT ="_tisuat";

    //Cot bang so tiet kiem
    public static final String COT_MASO = "_maso";
    public static final String COT_UIDTK = "_uidtk";
    public static final String COT_TENSO = "_tenso";
    public static final String COT_SOTIENGUI = "_sotiengui";
    public static final String COT_NGAYBATDAU ="_ngaybatdau";
    public static final String COT_NGAYKETTHUC ="_ngayketthuc";


    public static final String TAO_BANG_USER = ""
            + "CREATE TABLE " + TEN_BANG_USER + " ( "
            + COT_USERID + " integer primary key autoincrement, "
            + COT_USERNAME + " text , "
            + COT_EMAIL + " text , "
            + COT_TENDANGNHAP + " text not null, "
            + COT_ISADMIN + " integer default 0, " // Thêm cột _isadmin vào bảng
            + COT_PASSWORD + " text not null );";


    public static final String TAO_BANG_SOTIETKIEM = ""
            + " create table " + TEN_BANG_SOTIETKIEM + " ( "
            + COT_MASO + " integer primary key autoincrement, "
            + COT_TENSO + " text, "
            + COT_UIDTK + " integer, "
            + COT_SOTIENGUI + " real, "
            + COT_NGAYBATDAU + " text, "
            + COT_NGAYKETTHUC + " text, "
            + " foreign key (" + COT_UIDTK + ") REFERENCES " + TEN_BANG_USER
            + "(" + COT_USERID + "));";

    public static final String TAO_BANG_TAIKHOAN = ""
            + " create table " + TEN_BANG_TaiKhoanNganHang + " ( "
            + COT_SOTAIKHOAN + " integer primary key autoincrement, "
            + COT_UIDNH + " integer, "
            + COT_SODU + " real, "
            + " foreign key (" + COT_UIDNH + ") REFERENCES " + TEN_BANG_USER
            + "(" + COT_USERID + "));";
    public static final String TAO_BANG_TIENLAI = ""
            + "CREATE TABLE " + TEN_BANG_TienLai + " ( "
            + COT_TIENLAI_ID + " integer primary key autoincrement, "
            + COT_MASOTL + " integer, "
            + COT_SOTIENLAI + " real, "
            + COT_KIHAN + " text not null, "
            + COT_TISUAT + " text not null, "
            + " foreign key (" + COT_MASOTL + ") REFERENCES " + TEN_BANG_SOTIETKIEM
            + "(" + COT_MASO + "));";
    public DBHelper(Context context) {
        super(context, TEN_DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL(TAO_BANG_USER);
        MyDB.execSQL(TAO_BANG_SOTIETKIEM);
        MyDB.execSQL(TAO_BANG_TAIKHOAN);
        MyDB.execSQL(TAO_BANG_TIENLAI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("DROP TABLE IF EXISTS " + TEN_BANG_USER);
        MyDB.execSQL("DROP TABLE IF EXISTS " + TEN_BANG_SOTIETKIEM);
        MyDB.execSQL("DROP TABLE IF EXISTS " + TEN_BANG_TaiKhoanNganHang);
        MyDB.execSQL("DROP TABLE IF EXISTS " + TEN_BANG_TaiKhoanNganHang);

    }

}