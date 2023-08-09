package com.example.appsotietkiem;

public class LaiSuat {
    private String kyHan;
    private double laiSuat;

    public LaiSuat(String kyHan, double laiSuat) {
        this.kyHan = kyHan;
        this.laiSuat = laiSuat;
    }

    public String getKyHan() {
        return kyHan;
    }

    public double getLaiSuat() {
        return laiSuat;
    }

    @Override
    public String toString() {
        return kyHan + " - Lãi suất: " + laiSuat + "%";
    }
}
