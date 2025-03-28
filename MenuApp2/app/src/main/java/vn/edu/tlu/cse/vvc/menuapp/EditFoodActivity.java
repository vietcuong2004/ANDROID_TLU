package vn.edu.tlu.cse.vvc.menuapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class EditFoodActivity extends AppCompatActivity {

    EditText edtName, edtPrice, edtDescription;
    Button btnConfirm;
    Database database;

    int foodId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);

        edtName = findViewById(R.id.edtName);
        edtPrice = findViewById(R.id.edtPrice);
        edtDescription = findViewById(R.id.edtDescription);
        btnConfirm = findViewById(R.id.btnConfirm);

        database = new Database(this);

        // Nhận dữ liệu từ intent
        Intent intent = getIntent();
        foodId = intent.getIntExtra("id", -1);
        String name = intent.getStringExtra("name");
        int price = intent.getIntExtra("price", 0);
        String description = intent.getStringExtra("description");

        // Gán lên các ô nhập
        edtName.setText(name);
        edtPrice.setText(String.valueOf(price));
        edtDescription.setText(description);

        btnConfirm.setOnClickListener(v -> {
            String newName = edtName.getText().toString().trim();
            String priceStr = edtPrice.getText().toString().trim();
            String newDesc = edtDescription.getText().toString().trim();

            if (newName.isEmpty() || priceStr.isEmpty() || newDesc.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int newPrice = Integer.parseInt(priceStr);
            boolean updated = database.updateMenuItem(foodId, newName, newPrice, newDesc);
            if (updated) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
