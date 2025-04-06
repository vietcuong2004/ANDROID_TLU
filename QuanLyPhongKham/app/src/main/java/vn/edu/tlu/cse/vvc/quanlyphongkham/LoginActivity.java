package vn.edu.tlu.cse.vvc.quanlyphongkham;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtEmail, edtPassword;
    Button btnLogin;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);

        dbHelper = new DBHelper(this);

        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkLogin(email, password)) {
                String role = dbHelper.getUserRole(email);
                Toast.makeText(this, "Đăng nhập thành công (" + role + ")", Toast.LENGTH_SHORT).show();

                if (role.equalsIgnoreCase("Bác sĩ")) {
                    Intent intent = new Intent(this, DoctorActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, PatientActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                }

                finish(); // Đóng màn login sau khi đăng nhập
            } else {
                Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
