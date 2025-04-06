package vn.edu.tlu.cse.vvc.cuahangtienloi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import Model.Product;
import Adapter.StaffProductAdapter;

public class StaffActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioProducts, radioOrders;
    RecyclerView recyclerProducts;
    TableLayout tableOrders;
    Button btnAddProduct, btnLogout;

    DBHelper dbHelper;
    List<Product> productList;
    StaffProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        radioGroup = findViewById(R.id.radioGroup);
        radioProducts = findViewById(R.id.radioProducts);
        radioOrders = findViewById(R.id.radioOrders);
        recyclerProducts = findViewById(R.id.recyclerProducts);
        tableOrders = findViewById(R.id.tableOrders);
        btnAddProduct = findViewById(R.id.btnAddProduct);
        btnLogout = findViewById(R.id.btnLogout);
        dbHelper = new DBHelper(this);

        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        loadProductRecycler();

        //Staff chỉ 1 giao diện nhưng có 2 radio button, chọn button nào thì sẽ ẩn bớt nút đi để hiển thị giao diện mới.
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioProducts) {
                recyclerProducts.setVisibility(View.VISIBLE);
                tableOrders.setVisibility(View.GONE);
                btnAddProduct.setVisibility(View.VISIBLE);
                loadProductRecycler();
            }
            else {
                recyclerProducts.setVisibility(View.GONE);
                tableOrders.setVisibility(View.VISIBLE);
                btnAddProduct.setVisibility(View.GONE);
                loadOrderTable();
            }
        });

        btnAddProduct.setOnClickListener(v -> showAddProductDialog());

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(StaffActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void loadProductRecycler() {
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
        adapter = new StaffProductAdapter(this, productList, this::loadProductRecycler);
        recyclerProducts.setAdapter(adapter);
    }

    private void loadOrderTable() {
        tableOrders.removeAllViews();

        // Header
        TableRow header = new TableRow(this);
        String[] titles = {"Khách hàng", "Ngày đặt", "Tổng tiền", "Trạng thái"};
        for (String title : titles) {
            TextView tv = new TextView(this);
            tv.setText(title);
            tv.setPadding(16, 8, 16, 8);
            tv.setTypeface(null, android.graphics.Typeface.BOLD);
            header.addView(tv);
        }
        tableOrders.addView(header);

        Cursor cursor = dbHelper.getOrdersByEmail("%");
        while (cursor.moveToNext()) {
            TableRow row = new TableRow(this);
            row.addView(makeTextView(cursor.getString(1)));
            row.addView(makeTextView(cursor.getString(2)));
            row.addView(makeTextView(String.valueOf(cursor.getInt(3))));
            row.addView(makeTextView(cursor.getString(4)));
            tableOrders.addView(row);
        }
        cursor.close();
    }

    private TextView makeTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(16, 8, 16, 8);
        return tv;
    }

    public void showEditProductDialog(int id) {
        Cursor cursor = dbHelper.getAllProducts();
        String name = "", category = "";
        int price = 0, stock = 0;

        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == id) {
                name = cursor.getString(1);
                category = cursor.getString(2);
                price = cursor.getInt(3);
                stock = cursor.getInt(4);
                break;
            }
        }
        cursor.close();

        View view = getLayoutInflater().inflate(R.layout.dialog_product, null);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtCategory = view.findViewById(R.id.edtCategory);
        EditText edtPrice = view.findViewById(R.id.edtPrice);
        EditText edtStock = view.findViewById(R.id.edtStock);

        edtName.setText(name);
        edtCategory.setText(category);
        edtPrice.setText(String.valueOf(price));
        edtStock.setText(String.valueOf(stock));

        new AlertDialog.Builder(this)
                .setTitle("Sửa sản phẩm")
                .setView(view)
                .setPositiveButton("Cập nhật", (dialogInterface, i) -> {
                    dbHelper.updateProduct(id,
                            edtName.getText().toString().trim(),
                            edtCategory.getText().toString().trim(),
                            Integer.parseInt(edtPrice.getText().toString()),
                            Integer.parseInt(edtStock.getText().toString()));
                    loadProductRecycler();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void showAddProductDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_product, null);
        EditText edtName = view.findViewById(R.id.edtName);
        EditText edtCategory = view.findViewById(R.id.edtCategory);
        EditText edtPrice = view.findViewById(R.id.edtPrice);
        EditText edtStock = view.findViewById(R.id.edtStock);

        new AlertDialog.Builder(this)
                .setTitle("Thêm sản phẩm")
                .setView(view)
                .setPositiveButton("Thêm", (dialogInterface, i) -> {
                    String name = edtName.getText().toString().trim();
                    String category = edtCategory.getText().toString().trim();
                    int price = Integer.parseInt(edtPrice.getText().toString());
                    int stock = Integer.parseInt(edtStock.getText().toString());

                    dbHelper.insertProduct(name, category, price, stock);
                    loadProductRecycler();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
