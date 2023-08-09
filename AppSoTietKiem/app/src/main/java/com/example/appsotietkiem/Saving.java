package com.example.appsotietkiem;

public class Saving {
    private int maso;
    private String tenSo;
    private int uidtk;
    private double soTienGui;
    private String ngayBatDau;
    private String ngayKetThuc;
    private double soTienNhanDuoc;
    private String kyHan;


    public Saving(int maso, String tenSo, double soTienGui, String ngayBatDau, String ngayKetThuc, String kyHan, double soTienNhanDuoc)
    {
        this.maso = maso;
        this.tenSo = tenSo;
        this.soTienGui = soTienGui;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.soTienNhanDuoc = soTienNhanDuoc;
    }

    public int getMaso() {
        return maso;
    }

    public String getTenSo() {
        return tenSo;
    }

    public int getUidtk() {
        return uidtk;
    }
    public String getKyHan() {
        return kyHan;
    }
    public double getSoTienGui() {
        return soTienGui;
    }

    public String getNgayBatDau() {
        return ngayBatDau;
    }

    public String getNgayKetThuc() {
        return ngayKetThuc;
    }
    public void setSoTienNhanDuoc(double soTienNhanDuoc) {
        this.soTienNhanDuoc = soTienNhanDuoc;
    }
    public double getSoTienNhanDuoc() {
        return soTienNhanDuoc;
    }
}
