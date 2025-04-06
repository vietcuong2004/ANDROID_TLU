package vn.edu.tlu.cse.dvc.menuapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.cse.dvc.menuapp.model.MonAn;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuanLyThucDon.db";
    private static final int DATABASE_VERSION = 2;

    // Bảng Nhân Viên
    private static final String TABLE_NHAN_VIEN = "nhanvien";
    private static final String COLUMN_NHAN_VIEN_ID = "id";
    private static final String COLUMN_TEN_DANG_NHAP = "ten_dang_nhap";
    private static final String COLUMN_MAT_KHAU = "mat_khau";

    // Bảng Thực Đơn
    private static final String TABLE_THUC_DON = "thucdon";
    private static final String COLUMN_THUC_DON_ID = "id";
    private static final String COLUMN_TEN_MON = "ten_mon";
    private static final String COLUMN_GIA = "gia";
    private static final String COLUMN_MO_TA = "mo_ta";
    private static final String COLUMN_ANH_MINH_HOA = "anh_minh_hoa";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Nhân Viên
        String createNhanVienTable = "CREATE TABLE " + TABLE_NHAN_VIEN + " (" +
                COLUMN_NHAN_VIEN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEN_DANG_NHAP + " TEXT, " +
                COLUMN_MAT_KHAU + " TEXT)";
        db.execSQL(createNhanVienTable);

        // Tạo bảng Thực Đơn
        String createThucDonTable = "CREATE TABLE " + TABLE_THUC_DON + " (" +
                COLUMN_THUC_DON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TEN_MON + " TEXT, " +
                COLUMN_GIA + " INTEGER, " +
                COLUMN_MO_TA + " TEXT, " +
                COLUMN_ANH_MINH_HOA + " INTEGER)"; // Thay đổi từ TEXT sang INTEGER
        db.execSQL(createThucDonTable);

        // Thêm dữ liệu mẫu cho bảng Nhân Viên
        themDuLieuMauNhanVien(db);
        // Thêm dữ liệu mẫu cho bảng Thực Đơn
        themDuLieuMauThucDon(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NHAN_VIEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THUC_DON);
        onCreate(db);
    }

    // Thêm dữ liệu mẫu cho bảng Nhân Viên
    private void themDuLieuMauNhanVien(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        // Nhân viên 1
        values.put(COLUMN_TEN_DANG_NHAP, "admin");
        values.put(COLUMN_MAT_KHAU, "123456");
        db.insert(TABLE_NHAN_VIEN, null, values);
        // Nhân viên 2
        values.clear();
        values.put(COLUMN_TEN_DANG_NHAP, "nhanvien1");
        values.put(COLUMN_MAT_KHAU, "abc123");
        db.insert(TABLE_NHAN_VIEN, null, values);
        // Nhân viên 3
        values.clear();
        values.put(COLUMN_TEN_DANG_NHAP, "nhanvien2");
        values.put(COLUMN_MAT_KHAU, "987654");
        db.insert(TABLE_NHAN_VIEN, null, values);
    }

    // Thêm dữ liệu mẫu cho bảng Thực Đơn
    private void themDuLieuMauThucDon(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        // Món ăn 1
        values.put(COLUMN_TEN_MON, "Phở bò");
        values.put(COLUMN_GIA, 50000);
        values.put(COLUMN_MO_TA, "Phở bò tái chính thơm ngon");
        values.put(COLUMN_ANH_MINH_HOA, R.drawable.phobo); // Lưu ID tài nguyên drawable
        db.insert(TABLE_THUC_DON, null, values);
        // Món ăn 2
        values.clear();
        values.put(COLUMN_TEN_MON, "Cơm gà xối mỡ");
        values.put(COLUMN_GIA, 45000);
        values.put(COLUMN_MO_TA, "Cơm gà giòn, nước sốt đặc biệt");
        values.put(COLUMN_ANH_MINH_HOA, R.drawable.comga);
        db.insert(TABLE_THUC_DON, null, values);
        // Món ăn 3
        values.clear();
        values.put(COLUMN_TEN_MON, "Bún chả Hà Nội");
        values.put(COLUMN_GIA, 40000);
        values.put(COLUMN_MO_TA, "Bún chả thịt nướng, nước mắm chua ngọt");
        values.put(COLUMN_ANH_MINH_HOA, R.drawable.buncha);
        db.insert(TABLE_THUC_DON, null, values);
        // Món ăn 4
        values.clear();
        values.put(COLUMN_TEN_MON, "Gỏi cuốn tôm thịt");
        values.put(COLUMN_GIA, 30000);
        values.put(COLUMN_MO_TA, "Cuốn tôm thịt, nước chấm đậm đà");
        values.put(COLUMN_ANH_MINH_HOA, R.drawable.goicuon);
        db.insert(TABLE_THUC_DON, null, values);
        // Món ăn 5
        values.clear();
        values.put(COLUMN_TEN_MON, "Bánh mì pate");
        values.put(COLUMN_GIA, 25000);
        values.put(COLUMN_MO_TA, "Bánh mì nóng giòn, pate béo ngậy");
        values.put(COLUMN_ANH_MINH_HOA, R.drawable.banhmi);
        db.insert(TABLE_THUC_DON, null, values);
    }

    // Thêm nhân viên mới
    public boolean themNhanVien(String tenDangNhap, String matKhau) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_DANG_NHAP, tenDangNhap);
        values.put(COLUMN_MAT_KHAU, matKhau);
        long result = db.insert(TABLE_NHAN_VIEN, null, values);
        return result != -1;
    }

    // Kiểm tra đăng nhập
    public boolean kiemTraDangNhap(String tenDangNhap, String matKhau) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NHAN_VIEN + " WHERE " +
                COLUMN_TEN_DANG_NHAP + "=? AND " + COLUMN_MAT_KHAU + "=?", new String[]{tenDangNhap, matKhau});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Thêm món ăn mới
    public boolean themMonAn(String tenMon, int gia, String moTa, int anhMinhHoa) { // Thay String thành int
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_MON, tenMon);
        values.put(COLUMN_GIA, gia);
        values.put(COLUMN_MO_TA, moTa);
        values.put(COLUMN_ANH_MINH_HOA, anhMinhHoa); // Lưu ID tài nguyên
        long result = db.insert(TABLE_THUC_DON, null, values);
        return result != -1;
    }

    // Sửa món ăn
    public boolean suaMonAn(int id, String tenMon, int gia, String moTa, int anhMinhHoa) { // Thay String thành int
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEN_MON, tenMon);
        values.put(COLUMN_GIA, gia);
        values.put(COLUMN_MO_TA, moTa);
        values.put(COLUMN_ANH_MINH_HOA, anhMinhHoa); // Lưu ID tài nguyên
        int result = db.update(TABLE_THUC_DON, values, COLUMN_THUC_DON_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Xóa món ăn
    public boolean xoaMonAn(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_THUC_DON, COLUMN_THUC_DON_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Lấy tất cả món ăn
    public List<MonAn> layTatCaMonAn() {
        List<MonAn> monAns = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_THUC_DON, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_THUC_DON_ID);
                int tenMonIndex = cursor.getColumnIndex(COLUMN_TEN_MON);
                int giaIndex = cursor.getColumnIndex(COLUMN_GIA);
                int moTaIndex = cursor.getColumnIndex(COLUMN_MO_TA);
                int anhMinhHoaIndex = cursor.getColumnIndex(COLUMN_ANH_MINH_HOA);

                // Kiểm tra xem các cột có tồn tại không
                if (idIndex == -1 || tenMonIndex == -1 || giaIndex == -1 || moTaIndex == -1 || anhMinhHoaIndex == -1) {
                    throw new IllegalStateException("Một hoặc nhiều cột không tồn tại trong bảng " + TABLE_THUC_DON +
                            ": idIndex=" + idIndex + ", tenMonIndex=" + tenMonIndex + ", giaIndex=" + giaIndex +
                            ", moTaIndex=" + moTaIndex + ", anhMinhHoaIndex=" + anhMinhHoaIndex);
                }

                int id = cursor.getInt(idIndex);
                String tenMon = cursor.getString(tenMonIndex);
                int gia = cursor.getInt(giaIndex);
                String moTa = cursor.getString(moTaIndex);
                int anhMinhHoa = cursor.getInt(anhMinhHoaIndex);

                monAns.add(new MonAn(id, tenMon, gia, moTa, anhMinhHoa));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return monAns;
    }
}