package vn.edu.tlu.cse.dvc.menuapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SuaMonAnActivity extends AppCompatActivity {
    private EditText edtTenMon, edtGia, edtMoTa;
    private ImageView imgAnhMinhHoa;
    private Button btnChonAnh, btnLuu;
    private DatabaseHelper dbHelper;
    private int id;
    private int selectedImageId = 0;

    // Danh sách ID tài nguyên drawable
    private final int[] imageIds = {
            R.drawable.phobo,
            R.drawable.comga,
            R.drawable.buncha,
            R.drawable.goicuon,
            R.drawable.banhmi
    };
    private final String[] imageNames = {"Phở bò", "Cơm gà", "Bún chả", "Gỏi cuốn", "Bánh mì"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suamonan);

        edtTenMon = findViewById(R.id.edtTenMon);
        edtGia = findViewById(R.id.edtGia);
        edtMoTa = findViewById(R.id.edtMoTa);
        imgAnhMinhHoa = findViewById(R.id.imgAnhMinhHoa);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        btnLuu = findViewById(R.id.btnLuu);
        dbHelper = new DatabaseHelper(this);

        id = getIntent().getIntExtra("id", -1);
        String tenMon = getIntent().getStringExtra("tenMon");
        int gia = getIntent().getIntExtra("gia", 0);
        String moTa = getIntent().getStringExtra("moTa");
        selectedImageId = getIntent().getIntExtra("anhMinhHoa", 0);

        edtTenMon.setText(tenMon);
        edtGia.setText(String.valueOf(gia));
        edtMoTa.setText(moTa);

        // Hiển thị ảnh hiện tại
        if (selectedImageId != 0) {
            imgAnhMinhHoa.setImageResource(selectedImageId);
        }

        // Xử lý chọn ảnh
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SuaMonAnActivity.this);
                builder.setTitle("Chọn ảnh minh họa");
                builder.setItems(imageNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedImageId = imageIds[which];
                        imgAnhMinhHoa.setImageResource(selectedImageId);
                    }
                });
                builder.show();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenMonMoi = edtTenMon.getText().toString();
                String giaStr = edtGia.getText().toString();
                String moTaMoi = edtMoTa.getText().toString();

                if (tenMonMoi.isEmpty() || giaStr.isEmpty() || moTaMoi.isEmpty() || selectedImageId == 0) {
                    Toast.makeText(SuaMonAnActivity.this, "Vui lòng nhập đầy đủ thông tin và chọn ảnh", Toast.LENGTH_SHORT).show();
                } else {
                    int giaMoi = Integer.parseInt(giaStr);
                    if (dbHelper.suaMonAn(id, tenMonMoi, giaMoi, moTaMoi, selectedImageId)) {
                        Toast.makeText(SuaMonAnActivity.this, "Sửa món thành công", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(SuaMonAnActivity.this, "Sửa món thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}