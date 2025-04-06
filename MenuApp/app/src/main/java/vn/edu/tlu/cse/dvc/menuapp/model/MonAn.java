package vn.edu.tlu.cse.dvc.menuapp.model;

public class MonAn {
    private int id;
    private String tenMon;
    private int gia;
    private String moTa;
    private int anhMinhHoa; // Thay đổi từ String sang int

    public MonAn(int id, String tenMon, int gia, String moTa, int anhMinhHoa) {
        this.id = id;
        this.tenMon = tenMon;
        this.gia = gia;
        this.moTa = moTa;
        this.anhMinhHoa = anhMinhHoa;
    }

    public int getId() {
        return id;
    }

    public String getTenMon() {
        return tenMon;
    }

    public int getGia() {
        return gia;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getAnhMinhHoa() {
        return anhMinhHoa;
    }
}