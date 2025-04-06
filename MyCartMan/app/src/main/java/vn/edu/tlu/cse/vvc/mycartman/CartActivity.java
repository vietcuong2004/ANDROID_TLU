package vn.edu.tlu.cse.vvc.mycartman;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Adapter.CartAdapter;
import model.CartItem;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTotal;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCart);
        tvTotal = findViewById(R.id.tvTotal);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Nhận dữ liệu giỏ hàng từ MainActivity
        Map<String, CartItem> cartMap = (Map<String, CartItem>) getIntent().getSerializableExtra("cartMap");

        if (cartMap != null) {
            cartItemList = new ArrayList<>(cartMap.values());
        } else {
            cartItemList = new ArrayList<>();
        }

        cartAdapter = new CartAdapter(cartItemList);
        recyclerView.setAdapter(cartAdapter);

        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (CartItem item : cartItemList) {
            total += item.getTotalPrice();
        }
        tvTotal.setText("Tổng cộng: ฿ " + String.format("%.2f", total));
    }
}
