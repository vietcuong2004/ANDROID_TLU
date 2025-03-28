package vn.edu.tlu.cse.vvc.menuapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class AddFoodActivity extends AppCompatActivity {

    EditText edtName, edtPrice, edtDescription;
    Button btnConfirm;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        btnConfirm = findViewById(R.id.btnConfirm);
        database = new Database(this);

        btnConfirm.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String desc = edtDescription.getText().toString().trim();

            if (name.isEmpty() || priceStr.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int price = Integer.parseInt(priceStr);
            boolean success = database.addMenuItem(name, price, desc);
            if (success) {
                Toast.makeText(this, "Đã thêm món!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Lỗi khi thêm món!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
