package vn.edu.tlu.cse.vvc.menuapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    EditText edtNewUsername, edtNewPassword;
    Button btnRegister;

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // đảm bảo layout này có tồn tại

        // Ánh xạ view
        edtNewUsername = findViewById(R.id.edtNewUsername);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        btnRegister = findViewById(R.id.btnRegister);

        database = new Database(this);

        btnRegister.setOnClickListener(v -> {
            String username = edtNewUsername.getText().toString().trim();
            String password = edtNewPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isSuccess = database.register(username, password);
            if (isSuccess) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
