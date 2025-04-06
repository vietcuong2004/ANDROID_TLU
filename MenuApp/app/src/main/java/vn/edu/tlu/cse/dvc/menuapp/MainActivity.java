package vn.edu.tlu.cse.dvc.menuapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.tlu.cse.dvc.menuapp.DatabaseHelper;
import vn.edu.tlu.cse.dvc.menuapp.QuanLyThucDonActivity;

public class MainActivity extends AppCompatActivity {
    private EditText edtTenDangNhap, edtMatKhau;
    private Button btnDangNhap, btnDangKy, btnXemThu;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangNhap = findViewById(R.id.btnDangNhap);
        btnDangKy = findViewById(R.id.btnDangKy);
        btnXemThu = findViewById(R.id.btnXemThu);
        dbHelper = new DatabaseHelper(this);

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenDangNhap = edtTenDangNhap.getText().toString();
                String matKhau = edtMatKhau.getText().toString();
                if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbHelper.kiemTraDangNhap(tenDangNhap, matKhau)) {
                        Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, QuanLyThucDonActivity.class);
                        intent.putExtra("isLoggedIn", true);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        btnXemThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuanLyThucDonActivity.class);
                intent.putExtra("isLoggedIn", false);
                startActivity(intent);
            }
        });
    }
}