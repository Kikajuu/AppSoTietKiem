package com.example.appsotietkiem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SavingAdapter extends ArrayAdapter<Saving> {
    private Context context;
    private List<Saving> savings;

    public SavingAdapter(Context context, List<Saving> savings) {
        super(context, R.layout.customlistviewsotietkiem, savings);
        this.context = context;
        this.savings = savings;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.customlistviewsotietkiem, parent, false);

        TextView tvTenSo = itemView.findViewById(R.id.tvTenSo);
        TextView tvSoTienGui = itemView.findViewById(R.id.tvSoTienGui);
        TextView tvNgayBatDau = itemView.findViewById(R.id.tvNgayBatDau);
        TextView tvNgayKetThuc = itemView.findViewById(R.id.tvNgayKetThuc);
        TextView tvSoTienNhanDuoc = itemView.findViewById(R.id.tvSoTienNhanDuoc);

        Saving saving = savings.get(position);

        tvTenSo.setText(saving.getTenSo());
        tvSoTienGui.setText(String.valueOf(saving.getSoTienGui()));
        tvNgayBatDau.setText(saving.getNgayBatDau());
        tvNgayKetThuc.setText(saving.getNgayKetThuc());
        tvSoTienNhanDuoc.setText(String.valueOf(saving.getSoTienNhanDuoc()));

        return itemView;
    }
}
