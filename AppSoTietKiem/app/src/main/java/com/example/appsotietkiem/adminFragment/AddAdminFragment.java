package com.example.appsotietkiem.adminFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appsotietkiem.MyDatabase;
import com.example.appsotietkiem.R;


public class AddAdminFragment extends Fragment {
    View mview;
    EditText edtSoTaiKhoan, edtSoTienNap;
    Button btnNapTien;
    MyDatabase myDatabase;

    public AddAdminFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_add_admin, container, false);
        edtSoTaiKhoan = mview.findViewById(R.id.edtSoTaiKhoanGui);
        edtSoTienNap = mview.findViewById(R.id.edtSOtiengui);
        btnNapTien = mview.findViewById(R.id.btnNapTien);

        btnNapTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String soTaiKhoan = edtSoTaiKhoan.getText().toString();
                String soTienNap = edtSoTienNap.getText().toString();
                myDatabase = new MyDatabase(getContext()); // Khởi tạo đối tượng MyDatabase

                if (soTaiKhoan.isEmpty() || soTienNap.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập số tài khoản hoặc số tiền", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra xem tài khoản người dùng có tồn tại không
                int userID = myDatabase.getUserIdByAccountNumber(soTaiKhoan);
                if (userID != -1) {
                    double tienNap = Double.parseDouble(soTienNap);

                    // Nạp tiền vào tài khoản người dùng
                    if (myDatabase.napTienChoTaiKhoan(userID, tienNap)) {
                        Toast.makeText(getContext(), "Nạp tiền thành công vào tài khoản người dùng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Đã xảy ra lỗi khi nạp tiền", Toast.LENGTH_SHORT).
                        show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy tài khoản người dùng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return mview;
    }
}