package com.example.appsotietkiem;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.text.NumberFormat;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MyDatabase {
    SQLiteDatabase database;
    DBHelper dbHelper;
    private Context context;

    public MyDatabase(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        // Khởi tạo các thành phần cần thiết, ví dụ: DBHelper, SQLiteDatabase, ...
    }
//    public boolean addBankAccountInfo(long userId, String soTaiKhoan, double soDu) {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DBHelper.COT_UIDNH, userId);
//        contentValues.put(DBHelper.COT_SOTAIKHOAN, soTaiKhoan);
//        contentValues.put(DBHelper.COT_SODU, soDu);
//
//        long result = database.insert(DBHelper.TEN_BANG_TaiKhoanNganHang, null, contentValues);
//
//        return result != -1;
//    }
//
//
//    public boolean createAdminAccount() {
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(DBHelper.COT_USERNAME, "Admin");
//        contentValues.put(DBHelper.COT_EMAIL, "admin@example.com");
//        contentValues.put(DBHelper.COT_TENDANGNHAP, "admin");
//        contentValues.put(DBHelper.COT_ISADMIN, 1); // 1 là giá trị cho quyền admin
//        contentValues.put(DBHelper.COT_PASSWORD, "admin123");


//        long userId = database.insert(DBHelper.TEN_BANG_USER, null, contentValues);
//        if (userId != -1) {
//            // Nếu tạo tài khoản admin thành công, thêm thông tin tài khoản ngân hàng
//            String soTaiKhoan = "111111"; // Số tài khoản bạn muốn thêm cho admin
//            double soDu = 10000000; // Số dư bạn muốn thêm cho admin
//            return addBankAccountInfo(userId, soTaiKhoan, soDu);
//        }
//        return false;
//
//    }
// Trong lớp MyDatabase
public void updateAccountBalance(int userID, double newBalance) {
    SQLiteDatabase db = this.dbHelper.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(DBHelper.COT_SODU, newBalance);
    db.update(DBHelper.TEN_BANG_TaiKhoanNganHang, values, DBHelper.COT_UIDNH + " = ?", new String[]{String.valueOf(userID)});
    db.close();
}

    public boolean insertSavingsAccountInfo(String TenSo, int userID, double soTienGui, String kyHan, String ngayBatDau, String ngayKetThuc) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();

        try {
            // Bắt đầu transaction
            db.beginTransaction();

            // Thêm bản ghi mới vào bảng "SoTietKiem"
            ContentValues values1 = new ContentValues();
            values1.put(DBHelper.COT_TENSO, TenSo);
            values1.put(DBHelper.COT_UIDTK, userID);
            values1.put(DBHelper.COT_SOTIENGUI, soTienGui);
            values1.put(DBHelper.COT_NGAYBATDAU, ngayBatDau);
            values1.put(DBHelper.COT_NGAYKETTHUC, ngayKetThuc);

            long result1 = db.insert(DBHelper.TEN_BANG_SOTIETKIEM, null, values1);

            // Trừ số tiền gửi từ số dư trong tài khoản người dùng
            Cursor cursor = db.rawQuery("SELECT " + DBHelper.COT_SODU + " FROM " + DBHelper.TEN_BANG_TaiKhoanNganHang + " WHERE " + DBHelper.COT_UIDNH + " = " + userID, null);
            if (cursor != null && cursor.moveToFirst()) {
                double sodu = cursor.getDouble(cursor.getColumnIndex(DBHelper.COT_SODU));
                cursor.close();

                if (soTienGui > sodu) {
                    db.endTransaction();
                    Toast.makeText(context, "Số tiền gửi không được lớn hơn số dư", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    double soDuSauGiaoDich = sodu - soTienGui;
                    // Cập nhật số dư mới vào bảng "TaiKhoanNganHang"
                    ContentValues values2 = new ContentValues();
                    values2.put(DBHelper.COT_SODU, soDuSauGiaoDich);
                    int rowsAffected = db.update(DBHelper.TEN_BANG_TaiKhoanNganHang, values2, DBHelper.COT_UIDNH + " = ?", new String[]{String.valueOf(userID)});
                    if (rowsAffected <= 0) {
                        db.endTransaction();
                        Toast.makeText(context, "Đã xảy ra lỗi khi cập nhật số dư tài khoản", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            } else {
                db.endTransaction();
                Toast.makeText(context, "Không tìm thấy thông tin tài khoản", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Commit transaction nếu tất cả các thao tác thành công
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            // Kết thúc transaction sau khi thực hiện xong
            db.endTransaction();
        }
    }



    public String getCurrentDate() {
        // Hàm này trả về ngày hiện tại dưới dạng chuỗi, bạn có thể sử dụng SimpleDateFormat để định dạng ngày theo ý muốn
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    public String getEndDate(String startDate, String kiHan) {

        String[] parts = kiHan.split(" ");
        int duration = Integer.parseInt(parts[0]);

        // Định dạng ngày tháng
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Chuyển đổi chuỗi startDate sang đối tượng Date
            Date startDateObj = sdf.parse(startDate);

            // Sử dụng Calendar để tính ngày kết thúc
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDateObj);

            // Tăng thời gian của ngày bắt đầu bằng kỳ hạn
            calendar.add(Calendar.MONTH, duration);

            // Lấy ngày kết thúc dưới dạng Date
            Date endDateObj = calendar.getTime();

            // Chuyển đổi Date thành chuỗi và trả về kết quả
            String endDate = sdf.format(endDateObj);
            return endDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public double tinhTienLai(int maso, String kyHan) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        double tienLai = 0;

        Cursor cursor = db.rawQuery("SELECT " + DBHelper.COT_SOTIENGUI + ", " + DBHelper.COT_NGAYBATDAU + " FROM " + DBHelper.TEN_BANG_SOTIETKIEM + " WHERE " + DBHelper.COT_MASO + " = ?", new String[]{String.valueOf(maso)});

        if (cursor != null && cursor.moveToFirst()) {
            double soTienGui = cursor.getDouble(cursor.getColumnIndex(DBHelper.COT_SOTIENGUI));
            cursor.close();

            double tiSuat = 0;

            if (kyHan.equals("3 tháng")) {
                tiSuat = 0.05; // Lãi suất 5% cho kỳ hạn 3 tháng
            } else if (kyHan.equals("6 tháng")) {
                tiSuat = 0.06; // Lãi suất 6% cho kỳ hạn 6 tháng
            } else if (kyHan.equals("12 tháng")) {
                tiSuat = 0.07; // Lãi suất 7% cho kỳ hạn 9 tháng
            }

            tienLai = soTienGui * tiSuat;

            // Lưu thông tin tiền lãi vào bảng TienLai
            ContentValues values = new ContentValues();
            values.put(DBHelper.COT_MASOTL, maso);
            values.put(DBHelper.COT_SOTIENLAI, tienLai);
            values.put(DBHelper.COT_KIHAN, kyHan);
            values.put(DBHelper.COT_TISUAT, String.valueOf(tiSuat * 100) + "%");

            long newRowId = db.insert(DBHelper.TEN_BANG_TienLai, null, values);

            if (newRowId == -1) {
                // Xử lý lỗi nếu cần
            } else {
                // Thành công, có thể thực hiện các hành động sau khi lưu dữ liệu
            }
        }

        return tienLai;
    }

    public List<Saving> getAllSavings() {
        List<Saving> savingsList = new ArrayList<>();

        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT s.*, t." + DBHelper.COT_KIHAN + " FROM " + DBHelper.TEN_BANG_SOTIETKIEM + " s " +
                "INNER JOIN " + DBHelper.TEN_BANG_TienLai + " t ON s." + DBHelper.COT_MASO + " = t." + DBHelper.COT_MASOTL, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maSo = cursor.getInt(cursor.getColumnIndex(DBHelper.COT_MASO));
                String tenSo = cursor.getString(cursor.getColumnIndex(DBHelper.COT_TENSO));
                double soTienGui = cursor.getDouble(cursor.getColumnIndex(DBHelper.COT_SOTIENGUI));
                String ngayBatDau = cursor.getString(cursor.getColumnIndex(DBHelper.COT_NGAYBATDAU));
                String ngayKetThuc = cursor.getString(cursor.getColumnIndex(DBHelper.COT_NGAYKETTHUC));
                String kyHan = cursor.getString(cursor.getColumnIndex(DBHelper.COT_KIHAN)); // Lấy thông tin kỳ hạn từ cơ sở dữ liệu
                double soTienNhanDuoc = 0.0; // Tạm thời để 0

                Saving saving = new Saving(maSo, tenSo, soTienGui, ngayBatDau, ngayKetThuc, kyHan, soTienNhanDuoc);
                savingsList.add(saving);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return savingsList;
    }


    public Cursor getAccountInfo(int userID) {
    SQLiteDatabase database = dbHelper.getReadableDatabase();
    String[] columns = {DBHelper.COT_SOTAIKHOAN, DBHelper.COT_SODU};
    String selection = DBHelper.COT_UIDNH + " = ?";
    String[] selectionArgs = {String.valueOf(userID)};
    try {
        Cursor cursor = database.query(DBHelper.TEN_BANG_TaiKhoanNganHang, columns, selection, selectionArgs, null, null, null);
        return cursor;
    } catch (Exception e) {
        Log.e("DatabaseError", "Error querying database: " + e.getMessage());
        return null;
    }
}

    public int getUserIdByUsernameAndPassword(String tendangnhap, String password) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] columns = {DBHelper.COT_USERID};
        String selection = DBHelper.COT_TENDANGNHAP + " = ? AND " + DBHelper.COT_PASSWORD + " = ?";
        String[] selectionArgs = {tendangnhap, password};

        Cursor cursor = database.query(DBHelper.TEN_BANG_USER, columns, selection, selectionArgs, null, null, null);
        int userID = -1;

        if (cursor != null && cursor.moveToFirst()) {
            userID = cursor.getInt(cursor.getColumnIndex(DBHelper.COT_USERID));
            cursor.close();
        }

        return userID;
    }

    public int getUserIdByAccountNumber(String soTaiKhoan) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] columns = {DBHelper.COT_UIDNH};
        String selection = DBHelper.COT_SOTAIKHOAN + " = ?";
        String[] selectionArgs = {soTaiKhoan};

        Cursor cursor = database.query(DBHelper.TEN_BANG_TaiKhoanNganHang, columns, selection, selectionArgs, null, null, null);
        int userID = -1;

        if (cursor != null && cursor.moveToFirst()) {
            userID = cursor.getInt(cursor.getColumnIndex(DBHelper.COT_UIDNH));
            cursor.close();
        }

        return userID;
    }
    public boolean napTienChoTaiKhoan(int userID, double soTienNap) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        // Lấy số dư hiện tại của tài khoản
        Cursor cursor = database.rawQuery("SELECT " + DBHelper.COT_SODU + " FROM " + DBHelper.TEN_BANG_TaiKhoanNganHang + " WHERE " + DBHelper.COT_UIDNH + " = ?", new String[]{String.valueOf(userID)});
        double soDuHienTai = 0;
        if (cursor != null && cursor.moveToFirst()) {
            soDuHienTai = cursor.getDouble(cursor.getColumnIndex(DBHelper.COT_SODU));
            cursor.close();
        }

        // Cộng thêm số tiền nạp vào số dư hiện tại
        double soDuMoi = soDuHienTai + soTienNap;

        // Cập nhật số dư mới vào cơ sở dữ liệu
        ContentValues values = new ContentValues();
        values.put(DBHelper.COT_SODU, soDuMoi);

        int rowsUpdated = database.update(DBHelper.TEN_BANG_TaiKhoanNganHang, values, DBHelper.COT_UIDNH + " = ?", new String[]{String.valueOf(userID)});

        return rowsUpdated > 0;
    }
    public boolean checkAdminCredentials(String tendangnhap, String password) {
        database = dbHelper.getReadableDatabase();
        String[] columns = {DBHelper.COT_USERID};
        String selection = DBHelper.COT_TENDANGNHAP + " = ? AND " + DBHelper.COT_PASSWORD + " = ? AND " + DBHelper.COT_ISADMIN + " = ?";
        String[] selectionArgs = {tendangnhap, password, "1"}; // 1 là giá trị cho quyền admin
        Cursor cursor = database.query(DBHelper.TEN_BANG_USER, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }


    // Kiểm tra thông tin đăng nhập cho quyền user
    public boolean checkUserCredentials(String tendangnhap, String password) {
        database = dbHelper.getReadableDatabase();
        String[] columns = {DBHelper.COT_USERID};
        String selection = DBHelper.COT_TENDANGNHAP + " = ? AND " + DBHelper.COT_PASSWORD + " = ? AND " + DBHelper.COT_ISADMIN + " = 0";
        String[] selectionArgs = {tendangnhap, password}; // Không cần điền giá trị cho "COT_ISADMIN" vì đã có điều kiện "AND COT_ISADMIN = 0"
        Cursor cursor = database.query(DBHelper.TEN_BANG_USER, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
    private String generateRandomAccountNumber() {
        Random random = new Random();
        int maxNumber = 999999; // Tối đa 6 số có giá trị là 999999
        int randomNumber = random.nextInt(maxNumber + 1); // +1 để bao gồm cả maxNumber
        return String.format("%06d", randomNumber); // Chuyển số ngẫu nhiên thành chuỗi có tối đa 6 số
    }

    // phương thức lấy dữ liệu user
    public boolean insertData(String tendangnhap, String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.COT_TENDANGNHAP, tendangnhap);
        contentValues.put(DBHelper.COT_PASSWORD, password);
        long userId = database.insert(DBHelper.TEN_BANG_USER, null, contentValues); // Thêm user mới vào bảng Users

        if (userId == -1) {
            return false;
        } else {
            // Nếu user được tạo thành công, tiếp tục tạo số tài khoản ngẫu nhiên
            String sotaikhoan = generateRandomAccountNumber();

            ContentValues accountValues = new ContentValues();
            accountValues.put(DBHelper.COT_UIDNH, userId); // Gán userId vào cột uid của bảng TaiKhoanNganHang
            accountValues.put(DBHelper.COT_SOTAIKHOAN, sotaikhoan);
            accountValues.put(DBHelper.COT_SODU, 0); // Số dư ban đầu là 0

            long accountId = database.insert(DBHelper.TEN_BANG_TaiKhoanNganHang, null, accountValues);

            return accountId != -1;
        }
    }

    public boolean checkusername(String tendangnhap) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TEN_BANG_USER + " WHERE " + DBHelper.COT_TENDANGNHAP + " = ?", new String[]{tendangnhap});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public  boolean checkusernamepassword(String tendangnhap, String password) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TEN_BANG_USER + " WHERE " + DBHelper.COT_TENDANGNHAP + " = ? AND " + DBHelper.COT_PASSWORD + " = ?", new String[]{tendangnhap, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
    
}


