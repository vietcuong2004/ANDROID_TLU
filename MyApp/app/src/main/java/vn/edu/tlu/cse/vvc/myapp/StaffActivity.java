package vn.edu.tlu.cse.vvc.myapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.*;
import Model.Product;
import Adapter.StaffProductAdapter;
import android.content.Intent;
import android.database.Cursor;

public class StaffActivity extends AppCompatActivity {

    RecyclerView recyclerProducts;
    Button btnAddProduct, btnLogout;
    DBHelper dbHelper;
    List<Product> productList;
    StaffProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        recyclerProducts = findViewById(R.id.recyclerProducts);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnLogout = findViewById(R.id.btnLogout);

        dbHelper = new DBHelper(this);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        loadProducts();

        btnAddProduct.setOnClickListener(v -> showAddProductDialog());

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(StaffActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        Cursor cursor = dbHelper.getAllProducts();
        while (cursor.moveToNext()) {
            productList.add(new Product(
                    cursor.getInt(0),          // ID
                    cursor.getString(1),       // Name
                    cursor.getString(2),       // Brand
                    cursor.getInt(3),          // Price
                    cursor.getInt(4),          // Stock
                    cursor.getString(5)        // Note
            ));
        }
        cursor.close();

        adapter = new StaffProductAdapter(this, productList, dbHelper, this::loadProducts);
        recyclerProducts.setAdapter(adapter);
    }

    private void showAddProductDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_product, null);
        EditText edtName = view.findViewById(R.id.edtProductName);
        EditText edtBrand = view.findViewById(R.id.edtProductBrand);
        EditText edtPrice = view.findViewById(R.id.edtProductPrice);
        EditText edtStock = view.findViewById(R.id.edtProductStock);
        EditText edtNote = view.findViewById(R.id.edtProductNote);
        Button btnConfirm = view.findViewById(R.id.btnConfirm);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        btnConfirm.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String brand = edtBrand.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String stockStr = edtStock.getText().toString().trim();
            String note = edtNote.getText().toString().trim();

            if (name.isEmpty() || brand.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int price = Integer.parseInt(priceStr);
            int stock = Integer.parseInt(stockStr);
            dbHelper.insertProduct(name, brand, price, stock, note);

            Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            loadProducts();
        });

        dialog.show();
    }
}
