package vn.edu.tlu.cse.vvc.cuahangtienloi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import Model.Product;
import Adapter.CustomerProductAdapter;

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

        // Ánh xạ view
        recyclerProducts = findViewById(R.id.recyclerProducts);
        btnViewCart = findViewById(R.id.btnViewCart);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DBHelper(this);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        // Nhận email từ Intent
        userEmail = getIntent().getStringExtra("email");

        // Load danh sách sản phẩm
        loadProductList();

        // Xem giỏ hàng
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
        Cursor cursor = dbHelper.getAllProducts();
        while (cursor.moveToNext()) {
            productList.add(new Product(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4)
            ));
        }
        cursor.close();

        adapter = new CustomerProductAdapter(this, productList, userEmail);
        recyclerProducts.setAdapter(adapter);
    }
}
