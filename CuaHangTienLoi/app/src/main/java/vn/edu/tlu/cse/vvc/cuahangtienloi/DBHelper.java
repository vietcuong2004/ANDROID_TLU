package vn.edu.tlu.cse.vvc.cuahangtienloi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "store.db";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Bảng người dùng
        db.execSQL("CREATE TABLE Users(ID INTEGER PRIMARY KEY AUTOINCREMENT, Email TEXT, Password TEXT, Role TEXT, FullName TEXT)");

        // Bảng sản phẩm
        db.execSQL("CREATE TABLE Products(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Category TEXT, Price INTEGER, Stock INTEGER)");

        // Bảng giỏ hàng
        db.execSQL("CREATE TABLE Cart(ID INTEGER PRIMARY KEY AUTOINCREMENT, CustomerEmail TEXT, ProductName TEXT, Quantity INTEGER, TotalPrice INTEGER)");

        // Bảng đơn hàng
        db.execSQL("CREATE TABLE Orders(ID INTEGER PRIMARY KEY AUTOINCREMENT, CustomerEmail TEXT, OrderDate TEXT, TotalAmount INTEGER, Status TEXT)");

        // Dữ liệu mẫu
        db.execSQL("INSERT INTO Users VALUES (1, 'staff@store.com', '123456', 'Nhân viên', 'Nguyễn Văn A')," +
                "(2, 'user1@email.com', 'abc123', 'Khách hàng', 'Trần Thị B')," +
                "(3, 'admin1', '123456', 'Nhân viên', 'Trần Thị B')," +
                "(4, 'user1', '123456', 'Khách hàng', 'Trần Thị C')," +
                "(5, 'user2@email.com', 'qwerty', 'Khách hàng', 'Lê Minh C')");

        db.execSQL("INSERT INTO Products VALUES " +
                "(1, 'Mì Hảo Hảo', 'Thực phẩm', 5000, 100)," +
                "(2, 'Sữa Vinamilk', 'Đồ uống', 25000, 50)," +
                "(3, 'Bánh Oreo', 'Bánh kẹo', 20000, 30)," +
                "(4, 'Coca-Cola lon', 'Đồ uống', 10000, 80)");

//        db.execSQL("INSERT INTO Cart VALUES " +
//                "(1, 'user1@email.com', 'Mì Hảo Hảo', 5, 25000)," +
//                "(2, 'user2@email.com', 'Sữa Vinamilk', 2, 50000)," +
//                "(3, 'user1@email.com', 'Coca-Cola lon', 3, 30000)");

        db.execSQL("INSERT INTO Orders VALUES " +
                "(1, 'user1@email.com', '2000-01-01', 55000, 'Đã thanh toán')," +
                "(2, 'user2@email.com', '2000-01-02', 50000, 'Chưa thanh toán')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Products");
        db.execSQL("DROP TABLE IF EXISTS Cart");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        onCreate(db);
    }

    // ============================== USERS ==============================
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Email=? AND Password=?", new String[]{email, password});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Role FROM Users WHERE Email=?", new String[]{email});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return "Khách hàng"; // Mặc định
    }


    // ============================== PRODUCTS ==============================
    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Products", null);
    }

    public boolean insertProduct(String name, String category, int price, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Category", category);
        values.put("Price", price);
        values.put("Stock", stock);
        long result = db.insert("Products", null, values);
        return result != -1;
    }

    public boolean updateProduct(int id, String name, String category, int price, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Category", category);
        values.put("Price", price);
        values.put("Stock", stock);
        long result = db.update("Products", values, "ID=?", new String[]{String.valueOf(id)});
        return result != -1;
    }

    public boolean deleteProduct(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Products", "ID=?", new String[]{String.valueOf(id)});
        return result != -1;
    }

    // ============================== CART ==============================
    public boolean addToCart(String email, String productName, int quantityToAdd, int unitPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Kiểm tra sản phẩm đã có trong giỏ chưa
        Cursor cursor = db.rawQuery("SELECT Quantity, TotalPrice FROM Cart WHERE CustomerEmail=? AND ProductName=?",
                new String[]{email, productName});

        if (cursor.moveToFirst()) {
            // Nếu đã có → cập nhật số lượng và tổng tiền
            int currentQty = cursor.getInt(0);
            int newQty = currentQty + quantityToAdd;
            int newTotal = newQty * unitPrice;

            ContentValues values = new ContentValues();
            values.put("Quantity", newQty);
            values.put("TotalPrice", newTotal);
            int result = db.update("Cart", values, "CustomerEmail=? AND ProductName=?", new String[]{email, productName});
            cursor.close();
            return result != -1;
        }

        else {
            // Nếu chưa có → thêm mới
            ContentValues values = new ContentValues();
            values.put("CustomerEmail", email);
            values.put("ProductName", productName);
            values.put("Quantity", quantityToAdd);
            values.put("TotalPrice", unitPrice * quantityToAdd);
            long result = db.insert("Cart", null, values);
            cursor.close();
            return result != -1;
        }
    }


    public Cursor getCartByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM Cart WHERE CustomerEmail=?", new String[]{email});
    }

    public boolean clearCart(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("Cart", "CustomerEmail=?", new String[]{email});
        return result != -1;
    }

    public String getCategoryByProductName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Category FROM Products WHERE Name = ?", new String[]{name});
        if (cursor.moveToFirst()) {
            String category = cursor.getString(0);
            cursor.close();
            return category;
        }
        cursor.close();
        return "Không rõ";
    }

    // ============================== ORDERS ==============================
    public boolean insertOrder(String email, String date, int totalAmount, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("CustomerEmail", email);
        values.put("OrderDate", date);
        values.put("TotalAmount", totalAmount);
        values.put("Status", status);
        long result = db.insert("Orders", null, values);
        return result != -1;
    }

    public Cursor getOrdersByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (email.equals("%")) {
            return db.rawQuery("SELECT * FROM Orders", null);
        } else {
            return db.rawQuery("SELECT * FROM Orders WHERE CustomerEmail=?", new String[]{email});
        }
    }
}