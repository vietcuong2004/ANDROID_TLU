package vn.edu.tlu.cse.vvc.myapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import Adapter.CustomerProductAdapter;
import Model.Product;

public class CustomerActivity extends AppCompatActivity {

    RecyclerView recyclerProducts;
    Button btnViewCart, btnLogout;
    DBHelper dbHelper;
    List<Product> productList;
    CustomerProductAdapter adapter;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        recyclerProducts = findViewById(R.id.recyclerProducts);
        btnViewCart = findViewById(R.id.btnViewCart);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DBHelper(this);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        // Nhận email từ Intent
        userEmail = getIntent().getStringExtra("email");

        // Load danh sách sản phẩm
        loadProductList();

        // Mở giỏ hàng
        btnViewCart.setOnClickListener(v -> {
            Intent intent = new Intent(CustomerActivity.this, CartActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(CustomerActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadProductList() {
        productList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Products", null);
        while (cursor.moveToNext()) {
            productList.add(new Product(
                    cursor.getInt(0),     // ID
                    cursor.getString(1),  // Tên
                    cursor.getString(2),  // Hãng
                    cursor.getInt(3),     // Giá
                    cursor.getInt(4),     // Tồn kho
                    cursor.getString(5)   // Ghi chú
            ));
        }
        cursor.close();

        adapter = new CustomerProductAdapter(this, productList, userEmail);
        recyclerProducts.setAdapter(adapter);
    }
}
