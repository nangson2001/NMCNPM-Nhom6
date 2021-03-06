package com.example.nhahangamthuc.do_choi;

import java.util.HashMap;
import java.util.Map;

public class DoChoi {

    private int id;
    private int soLuong;
    private String ten;
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public DoChoi() {
    }

    public DoChoi(int id, int soLuong, String ten) {
        this.id = id;
        this.soLuong = soLuong;
        this.ten = ten;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    @Override
    public String toString() {
        return "DoChoi{" +
                ", id=" + id +
                ", soLuong=" + soLuong +
                ", ten='" + ten + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("ten", ten);
        map.put("soLuong", soLuong);
        return map;
    }
}
