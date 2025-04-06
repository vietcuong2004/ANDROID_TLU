package vn.edu.tlu.cse.vvc.myapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.*;
import Model.Product;
import Adapter.CartAdapter;

public class CartActivity extends AppCompatActivity {

    RecyclerView recyclerCart;
    TextView txtTotal;
    Button btnCheckout, btnBack;
    DBHelper dbHelper;
    List<Product> cartList;
    CartAdapter adapter;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerCart = findViewById(R.id.recyclerCart);
        txtTotal = findViewById(R.id.txtTotal);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);

        dbHelper = new DBHelper(this);
        userEmail = getIntent().getStringExtra("email");

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        loadCart();

        btnCheckout.setOnClickListener(v -> {
            if (cartList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống!", Toast.LENGTH_SHORT).show();
                return;
            }

            int totalAmount = 0;
            for (Product p : cartList) {
                totalAmount += p.getPrice() * p.getQuantity();
            }

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            dbHelper.insertOrder(userEmail, date, totalAmount, "Đã thanh toán");
            dbHelper.clearCart(userEmail);

            Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
            finish();
        });

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CustomerActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
            finish();
        });
    }

    private void loadCart() {
        cartList = new ArrayList<>();
        Cursor cursor = dbHelper.getCartByEmail(userEmail);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("ProductID"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("ProductName"));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("Price"));
            String brand = dbHelper.getBrandByProductName(name);

            Product p = new Product(id, name, brand, price, 0, "");
            p.setQuantity(quantity);

            cartList.add(p);
        }
        cursor.close();

        adapter = new CartAdapter(this, cartList, this::updateTotal);
        recyclerCart.setAdapter(adapter);
        updateTotal();
    }

    private void updateTotal() {
        int total = 0;
        for (Product p : cartList) {
            total += p.getPrice() * p.getQuantity();
        }
        txtTotal.setText("Tổng tiền: " + total + " VNĐ");
    }
}
