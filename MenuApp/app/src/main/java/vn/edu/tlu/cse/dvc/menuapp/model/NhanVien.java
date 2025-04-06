package vn.edu.tlu.cse.dvc.menuapp.model;
public class NhanVien {
    private int id;
    private String tenDangNhap;
    private String matKhau;

    public NhanVien(int id, String tenDangNhap, String matKhau) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    public int getId() {
        return id;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }
}
