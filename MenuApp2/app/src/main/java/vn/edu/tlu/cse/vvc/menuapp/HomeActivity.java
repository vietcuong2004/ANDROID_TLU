package vn.edu.tlu.cse.vvc.menuapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerViewMenu;
    MenuAdapter menuAdapter;
    Database database;
    List<Food> menuList;

    Button btnLogin, btnRegister, btnAddFood, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ view
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        btnAddFood = findViewById(R.id.btnAddFood);
        btnLogout = findViewById(R.id.btnLogout);
        recyclerViewMenu = findViewById(R.id.recyclerViewMenu);

        database = new Database(this);

        // Lấy danh sách món ăn từ DB
        menuList = database.getAllMenuItems();
        recyclerViewMenu.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter(this, menuList);
        recyclerViewMenu.setAdapter(menuAdapter);

        // Kiểm tra trạng thái đăng nhập
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLoggedIn = prefs.getBoolean("logged_in", false);

        if (isLoggedIn) {
            btnLogin.setVisibility(View.GONE);
            btnRegister.setVisibility(View.GONE);
            btnAddFood.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
        } else {
            btnLogin.setVisibility(View.VISIBLE);
            btnRegister.setVisibility(View.VISIBLE);
            btnAddFood.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
        }

        // Đăng nhập
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Đăng ký
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Thêm món (mở form AddFoodActivity)
        btnAddFood.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddFoodActivity.class);
            startActivity(intent);
        });

        // Đăng xuất
        btnLogout.setOnClickListener(v -> {
            prefs.edit().putBoolean("logged_in", false).apply();
            recreate(); // Refresh lại giao diện
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Khi quay lại từ AddFoodActivity → cập nhật danh sách
        menuList.clear();
        menuList.addAll(database.getAllMenuItems());
        menuAdapter.notifyDataSetChanged();
    }
}
