package com.example.appsotietkiem.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.example.appsotietkiem.DBHelper;
import com.example.appsotietkiem.MyDatabase;
import com.example.appsotietkiem.R;


public class HomeFragment extends Fragment {
    TextView tvSoDu , tvSTK, tvTen;
    MyDatabase myDatabase;


    public HomeFragment() {
        // Required empty public constructor
    }





    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_home, container, false);
      tvSoDu = view.findViewById(R.id.tvSoDuUser);
      tvSTK = view.findViewById(R.id.tvStkUser);
        myDatabase = new MyDatabase(getContext());

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
                tvSTK.setText("Số tài khoản: " + sotaikhoan);
                tvSoDu.setText("Số dư: " + formattedSodu + " VND");

                //...
            }
            else {
                // UserID không hợp lệ, xử lý tương ứng (nếu cần)
            }
        }

      return view;
    }
    private String formatCurrency(long amount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("#,###,###");
        return decimalFormat.format(amount);
    }


}