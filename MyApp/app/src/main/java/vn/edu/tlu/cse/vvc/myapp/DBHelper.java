package vn.edu.tlu.cse.vvc.myapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ComputerStore.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (ID INTEGER PRIMARY KEY AUTOINCREMENT, Email TEXT UNIQUE, Password TEXT, Role TEXT, FullName TEXT)");
        db.execSQL("CREATE TABLE Products (ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Brand TEXT, Price INTEGER, Stock INTEGER, Note TEXT)");
        db.execSQL("CREATE TABLE Cart (ID INTEGER PRIMARY KEY AUTOINCREMENT, UserEmail TEXT, ProductID INTEGER, ProductName TEXT, Quantity INTEGER, Price INTEGER)");
        db.execSQL("CREATE TABLE Orders (ID INTEGER PRIMARY KEY AUTOINCREMENT, UserEmail TEXT, Date TEXT, Total INTEGER, Status TEXT)");

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        db.execSQL("INSERT INTO Users VALUES (1, 'staff@store.com', '123456', 'Staff', 'Nguyen Van A')");
        db.execSQL("INSERT INTO Users VALUES (2, 'admin1', '123456', 'Staff', 'Nguyen Van E')");
        db.execSQL("INSERT INTO Users VALUES (3, 'user1@email.com', 'abc123', 'Customer', 'Le Thi Mai')");
        db.execSQL("INSERT INTO Users VALUES (4, 'user2@email.com', '123abc', 'Customer', 'Tran Minh Quan')");
        db.execSQL("INSERT INTO Users VALUES (5, 'user1', '123456', 'Customer', 'Vuong Viet Cuong')");

        db.execSQL("INSERT INTO Products VALUES (1, 'Dell Inspiron 15', 'Dell', 18000000, 10, 'Laptop pho thong')");
        db.execSQL("INSERT INTO Products VALUES (2, 'MacBook Air M2', 'Apple', 28500000, 5, 'Chip Apple M2')");
        db.execSQL("INSERT INTO Products VALUES (3, 'HP Pavilion Gaming', 'HP', 22000000, 7, 'Dành cho chơi game')");
        db.execSQL("INSERT INTO Products VALUES (4, 'ASUS VivoBook X515', 'ASUS', 14000000, 12, 'SSD 512GB, RAM 8GB')");
        db.execSQL("INSERT INTO Products VALUES (5, 'Lenovo ThinkPad E14', 'Lenovo', 16500000, 6, 'Máy văn phòng bền bỉ')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Products");
        db.execSQL("DROP TABLE IF EXISTS Cart");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        onCreate(db);
    }

    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Email = ? AND Password = ?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Role FROM Users WHERE Email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return "";
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Products", null);
    }

    public void addToCart(String userEmail, int productId, String productName, int price) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Cart WHERE UserEmail = ? AND ProductID = ?", new String[]{userEmail, String.valueOf(productId)});
        if (cursor.moveToFirst()) {
            int currentQty = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
            ContentValues values = new ContentValues();
            values.put("Quantity", currentQty + 1);
            db.update("Cart", values, "UserEmail = ? AND ProductID = ?", new String[]{userEmail, String.valueOf(productId)});
        } else {
            ContentValues values = new ContentValues();
            values.put("UserEmail", userEmail);
            values.put("ProductID", productId);
            values.put("ProductName", productName);
            values.put("Quantity", 1);
            values.put("Price", price);
            db.insert("Cart", null, values);
        }
        cursor.close();
    }

    public Cursor getCartByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Cart WHERE UserEmail = ?", new String[]{email});
    }

    public void insertOrder(String userEmail, String date, int total, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserEmail", userEmail);
        values.put("Date", date);
        values.put("Total", total);
        values.put("Status", status);
        db.insert("Orders", null, values);
    }

    public void clearCart(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Cart", "UserEmail = ?", new String[]{email});
    }

    public String getBrandByProductName(String productName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Brand FROM Products WHERE Name = ?", new String[]{productName});
        if (cursor.moveToFirst()) {
            String brand = cursor.getString(0);
            cursor.close();
            return brand;
        }
        cursor.close();
        return "";
    }

    public void insertProduct(String name, String brand, int price, int stock, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Brand", brand);
        values.put("Price", price);
        values.put("Stock", stock);
        values.put("Note", note);
        db.insert("Products", null, values);
    }

    public void updateProduct(int id, String name, String brand, int price, int stock, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Brand", brand);
        values.put("Price", price);
        values.put("Stock", stock);
        values.put("Note", note);
        db.update("Products", values, "ID = ?", new String[]{String.valueOf(id)});
    }

    public void deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Products", "ID = ?", new String[]{String.valueOf(id)});
    }
}