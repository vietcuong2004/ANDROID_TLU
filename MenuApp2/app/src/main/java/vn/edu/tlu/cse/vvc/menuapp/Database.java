package vn.edu.tlu.cse.vvc.menuapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

import java.util.*;

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "menu.db";
    public static final int DB_VERSION = 1;

    private final String[] sampleImages = {"im1", "im2", "im3", "im4", "im5"};
    private Context context;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng Users
        db.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE," +
                "password TEXT)");

        // Tạo bảng Menu
        db.execSQL("CREATE TABLE Menu (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "price INTEGER," +
                "description TEXT," +
                "image TEXT)");

        // Thêm dữ liệu mẫu người dùng
        db.execSQL("INSERT INTO Users (username, password) VALUES " +
                "('admin', '123456')," +
                "('nhanvien1', 'abc123')");

        // Thêm sẵn 5 món ăn mẫu
        insertSampleMenu(db, "Phở bò", 50000, "Phở bò tái chín thơm ngon", "im1");
        insertSampleMenu(db, "Cơm gà xối mỡ", 45000, "Cơm gà giòn, nước sốt đặc biệt", "im2");
        insertSampleMenu(db, "Bún chả Hà Nội", 40000, "Bún chả thịt nướng, nước mắm chua ngọt", "im3");
        insertSampleMenu(db, "Gỏi cuốn tôm thịt", 30000, "Cuốn tôm thịt, nước chấm đậm đà", "im4");
        insertSampleMenu(db, "Bánh mì pate", 25000, "Bánh mì nóng giòn, pate béo ngậy", "im5");
    }

    // Thêm món ăn mẫu trực tiếp bằng SQLiteDatabase
    private void insertSampleMenu(SQLiteDatabase db, String name, int price, String description, String image) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("description", description);
        cv.put("image", image);
        db.insert("Menu", null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Menu");
        onCreate(db);
    }

    // Đăng ký
    public boolean register(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("password", password);
        long result = db.insert("Users", null, cv);
        return result != -1;
    }

    // Đăng nhập
    public boolean login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username=? AND password=?",
                new String[]{username, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Thêm món ăn mới (dùng sau này)
    public boolean addMenuItem(String name, int price, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        Random random = new Random();
        String imageName = sampleImages[random.nextInt(sampleImages.length)];

        cv.put("name", name);
        cv.put("price", price);
        cv.put("description", description);
        cv.put("image", imageName);

        long result = db.insert("Menu", null, cv);
        return result != -1;
    }

    // Lấy tất cả món ăn
    public List<Food> getAllMenuItems() {
        List<Food> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Menu", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String image = cursor.getString(cursor.getColumnIndexOrThrow("image"));
                list.add(new Food(id, name, price, desc, image));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }

    // Xoá món ăn theo ID
    public boolean deleteMenuItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete("Menu", "id=?", new String[]{String.valueOf(id)});
        return rowsDeleted > 0;
    }

    //Sửa món ăn
    public boolean updateMenuItem(int id, String name, int price, String desc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("description", desc);
        int rows = db.update("Menu", cv, "id=?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

}