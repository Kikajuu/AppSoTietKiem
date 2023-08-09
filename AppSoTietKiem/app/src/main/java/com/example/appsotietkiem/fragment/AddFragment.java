package com.example.appsotietkiem.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.appsotietkiem.DBHelper;
import com.example.appsotietkiem.LaiSuat;
import com.example.appsotietkiem.MyDatabase;
import com.example.appsotietkiem.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {
    View mView;
    TextView tvSoDu,tvSTK;
    MyDatabase myDatabase;
    EditText edtTenSo, edtSoTien;
    Spinner spinnerKyHan;
    ArrayList<LaiSuat> laiSuat;
    Button btnThemSoTietKiem;

    public AddFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add, container, false);
        tvSTK = mView.findViewById(R.id.tvSTKNGUON);
        tvSoDu = mView.findViewById(R.id.tvSoduNGUON);
        spinnerKyHan = mView.findViewById(R.id.spinnerKyHan);
        edtTenSo = mView.findViewById(R.id.edtTenSo);
        edtSoTien = mView.findViewById(R.id.edtSoTienGui1);
        btnThemSoTietKiem = mView.findViewById(R.id.btnThemSoTietKiem);

        myDatabase = new MyDatabase(getContext());

        btnThemSoTietKiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lấy dữ liệu từ EditText và Spinner
                String tenSoTietKiem = edtTenSo.getText().toString();
                String kyHan = spinnerKyHan.getSelectedItem().toString();
                String soTienNap = edtSoTien.getText().toString();

                // Kiểm tra xem các thông tin cần thiết đã được cung cấp chưa
                if (tenSoTietKiem.isEmpty() || kyHan.isEmpty() || soTienNap.isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Chuyển đổi số tiền từ chuỗi sang dạng số thực
                double soTienGui = Double.parseDouble(soTienNap);

                // Kiểm tra xem số tiền gửi có lớn hơn số dư hay không
                SharedPreferences preferences = getActivity().getSharedPreferences("MyPrefs", getActivity().MODE_PRIVATE);
                int userID = preferences.getInt("userID", -1);

                if (userID != -1) {
                    Cursor cursor = myDatabase.getAccountInfo(userID);
                    if (cursor != null && cursor.moveToFirst()) {
                        // Lấy thông tin số dư từ Cursor
                        double sodu = cursor.getDouble(cursor.getColumnIndex(DBHelper.COT_SODU));
                        cursor.close();

                        if (soTienGui > sodu) {
                            Toast.makeText(getActivity(), "Số tiền gửi không được lớn hơn số dư", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            // Trừ số tiền gửi từ số dư trong tài khoản người dùng
                            double soDuSauGiaoDich = sodu - soTienGui;

                            // Cập nhật số dư mới vào cơ sở dữ liệu
                            myDatabase.updateAccountBalance(userID, soDuSauGiaoDich);

                            // Thêm bản ghi mới vào bảng "SoTietKiem"
                            String ngayBatDau = myDatabase.getCurrentDate();
                            String ngayKetThuc = myDatabase.getEndDate(ngayBatDau, kyHan);
                            boolean isInserted = myDatabase.insertSavingsAccountInfo(tenSoTietKiem, userID, soTienGui, kyHan, ngayBatDau, ngayKetThuc);
                            Log.d("InsertInfo", "isInserted: " + isInserted);
                            if (isInserted) {
                                Toast.makeText(getActivity(), "Thêm sổ tiết kiệm thành công", Toast.LENGTH_SHORT).show();
                                // Đã thêm thành công, có thể chuyển tới màn hình xác nhận hoặc làm gì đó khác
                            } else {
                                Toast.makeText(getActivity(), "Đã xảy ra lỗi khi thêm sổ tiết kiệm", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(getActivity(), "Không tìm thấy thông tin tài khoản", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }
        });




        SharedPreferences preferences = getContext().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE);
        int userID = preferences.getInt("userID", -1);

        if (userID != -1) {
            // Nếu UserID hợp lệ, lấy thông tin số tài khoản và số dư từ SQLite
            Cursor cursor = myDatabase.getAccountInfo(userID);
            if (cursor != null && cursor.moveToFirst()) {
                // Lấy thông tin số tài khoản và số dư từ Cursor
                String sotaikhoan = cursor.getString(cursor.getColumnIndex(DBHelper.COT_SOTAIKHOAN));
                @SuppressLint("Range") long sodu = cursor.getLong(cursor.getColumnIndex(DBHelper.COT_SODU));

                // Định dạng số dư với dấu chấm ngăn cách hàng nghìn
                String formattedSodu = formatCurrency(sodu);

                // Hiển thị thông tin lên giao diện
                tvSTK.setText("Tài Khoản Nguồn: " + sotaikhoan );
                tvSoDu.setText("Số dư: " + formatCurrency(sodu) + " VND ");

                // Đóng Cursor khi không sử dụng nữa
                cursor.close();
                // Xử lý Cursor và hiển thị thông tin lên TextView tương tự như trong ví dụ trước đó
                // ...
            } else {
                // UserID không hợp lệ, xử lý tương ứng (nếu cần)
            }
        }
        spinnerKyHan = mView.findViewById(R.id.spinnerKyHan);

        // Tạo danh sách các đối tượng InterestRate với kì hạn và lãi suất tương ứng
        laiSuat = new ArrayList<>();
        laiSuat.add(new LaiSuat("3 tháng", 5.0));
        laiSuat.add(new LaiSuat("6 tháng", 6.5));
        laiSuat.add(new LaiSuat("12 tháng", 7.0));

        // Thiết lập ArrayAdapter để hiển thị các kì hạn và lãi suất từ danh sách trong Spinner
        ArrayAdapter<LaiSuat> adapter = new ArrayAdapter<LaiSuat>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                laiSuat
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKyHan.setAdapter(adapter);

        spinnerKyHan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy đối tượng InterestRate được chọn từ Spinner
                LaiSuat selectedInterestRate = (LaiSuat) parent.getItemAtPosition(position);
                String selectedKyHan = selectedInterestRate.getKyHan();
                double selectedLaiSuat = selectedInterestRate.getLaiSuat();
                // Ở đây, bạn có thể lưu giá trị kì hạn và lãi suất vào biến hoặc làm bất kỳ xử lý nào bạn muốn.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có kì hạn nào được chọn (nếu cần)
            }
        });


        return mView;
    }
    private String formatCurrency(long amount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("#,###,###");
        return decimalFormat.format(amount);
    }

}