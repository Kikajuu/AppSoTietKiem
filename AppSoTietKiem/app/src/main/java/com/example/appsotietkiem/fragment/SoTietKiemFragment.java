package com.example.appsotietkiem.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.appsotietkiem.MyDatabase;
import com.example.appsotietkiem.R;
import com.example.appsotietkiem.Saving;
import com.example.appsotietkiem.SavingAdapter;

import java.util.List;

public class SoTietKiemFragment extends Fragment {
    ListView listViewSavings;
    MyDatabase database;

    public SoTietKiemFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sotietkiem, container, false);

        // Ánh xạ ListView
        listViewSavings = v.findViewById(R.id.ListViewSoTietKiem);

        // Khởi tạo MyDatabase
        database = new MyDatabase(getActivity());

        // Lấy danh sách thông tin sổ tiết kiệm từ cơ sở dữ liệu
        List<Saving> savingsList = database.getAllSavings();

        // Tính và cập nhật Số tiền sẽ nhận được cho mỗi đối tượng Saving
        for (Saving saving : savingsList) {
            double tienLai = database.tinhTienLai(saving.getMaso(), saving.getKyHan());
            double soTienNhanDuoc = saving.getSoTienGui() + tienLai;
            saving.setSoTienNhanDuoc(soTienNhanDuoc);
        }

        // Khởi tạo adapter và gán cho ListView
        SavingAdapter savingsAdapter = new SavingAdapter(getActivity(), savingsList);
        listViewSavings.setAdapter(savingsAdapter);

        return v;
    }
}



