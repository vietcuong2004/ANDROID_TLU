package vn.edu.tlu.cse.dvc.menuapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import vn.edu.tlu.cse.dvc.menuapp.DatabaseHelper;
import vn.edu.tlu.cse.dvc.menuapp.R;

public class DangKyActivity extends AppCompatActivity {
    private EditText edtTenDangNhap, edtMatKhau;
    private Button btnDangKy;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangky);

        edtTenDangNhap = findViewById(R.id.edtTenDangNhap);
        edtMatKhau = findViewById(R.id.edtMatKhau);
        btnDangKy = findViewById(R.id.btnDangKy);
        dbHelper = new DatabaseHelper(this);

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenDangNhap = edtTenDangNhap.getText().toString();
                String matKhau = edtMatKhau.getText().toString();
                if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {
                    Toast.makeText(DangKyActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (dbHelper.themNhanVien(tenDangNhap, matKhau)) {
                        Toast.makeText(DangKyActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(DangKyActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
