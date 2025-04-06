package vn.edu.tlu.cse.vvc.cuahangtienloi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // giao diện đăng nhập

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        dbHelper = new DBHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.checkLogin(email, password)) {
                    String role = dbHelper.getUserRole(email);
                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công (" + role + ")", Toast.LENGTH_SHORT).show();

                    if (role.equalsIgnoreCase("Nhân viên")) {
                        Intent intent = new Intent(LoginActivity.this, StaffActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    }

                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Sai email hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
