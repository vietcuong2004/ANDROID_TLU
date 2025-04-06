package vn.edu.tlu.cse.dvc.menuapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import vn.edu.tlu.cse.dvc.menuapp.adapter.MonAnAdapter;
import vn.edu.tlu.cse.dvc.menuapp.model.MonAn;

public class QuanLyThucDonActivity extends AppCompatActivity {
    private EditText edtTenMon, edtGia, edtMoTa;
    private ImageView imgAnhMinhHoa;
    private Button btnChonAnh, btnThemMon, btnDangXuat;
    private ListView danhSachThucDon;
    private DatabaseHelper dbHelper;
    private List<MonAn> monAnList;
    private MonAnAdapter adapter;
    private boolean isLoggedIn;
    private int selectedImageId = 0; // Lưu ID tài nguyên drawable đã chọn

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
        setContentView(R.layout.activity_quanlythucdon);

        // Khởi tạo các thành phần giao diện
        edtTenMon = findViewById(R.id.edtTenMon);
        edtGia = findViewById(R.id.edtGia);
        edtMoTa = findViewById(R.id.edtMoTa);
        imgAnhMinhHoa = findViewById(R.id.imgAnhMinhHoa);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        btnThemMon = findViewById(R.id.btnThemMon);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        danhSachThucDon = findViewById(R.id.danhSachThucDon);
        dbHelper = new DatabaseHelper(this);

        isLoggedIn = getIntent().getBooleanExtra("isLoggedIn", false);

        // Ẩn các trường nhập liệu nếu chưa đăng nhập
        if (!isLoggedIn) {
            edtTenMon.setVisibility(View.GONE);
            edtGia.setVisibility(View.GONE);
            edtMoTa.setVisibility(View.GONE);
            imgAnhMinhHoa.setVisibility(View.GONE);
            btnChonAnh.setVisibility(View.GONE);
            btnThemMon.setVisibility(View.GONE);
        }

        // Tải danh sách món ăn
        taiDanhSachMonAn();

        // Xử lý chọn ảnh
        btnChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyThucDonActivity.this);
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

        btnThemMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenMon = edtTenMon.getText().toString();
                String giaStr = edtGia.getText().toString();
                String moTa = edtMoTa.getText().toString();

                if (tenMon.isEmpty() || giaStr.isEmpty() || moTa.isEmpty() || selectedImageId == 0) {
                    Toast.makeText(QuanLyThucDonActivity.this, "Vui lòng nhập đầy đủ thông tin và chọn ảnh", Toast.LENGTH_SHORT).show();
                } else {
                    int gia = Integer.parseInt(giaStr);
                    if (dbHelper.themMonAn(tenMon, gia, moTa, selectedImageId)) {
                        Toast.makeText(QuanLyThucDonActivity.this, "Thêm món thành công", Toast.LENGTH_SHORT).show();
                        taiDanhSachMonAn();
                        edtTenMon.setText("");
                        edtGia.setText("");
                        edtMoTa.setText("");
                        imgAnhMinhHoa.setImageResource(android.R.drawable.ic_menu_gallery);
                        selectedImageId = 0;
                    } else {
                        Toast.makeText(QuanLyThucDonActivity.this, "Thêm món thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        danhSachThucDon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!isLoggedIn) {
                    Toast.makeText(QuanLyThucDonActivity.this, "Vui lòng đăng nhập để chỉnh sửa/xóa", Toast.LENGTH_SHORT).show();
                    return;
                }

                MonAn monAn = monAnList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyThucDonActivity.this);
                builder.setTitle("Chọn hành động");
                builder.setItems(new String[]{"Sửa", "Xóa"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Sửa món ăn
                            Intent intent = new Intent(QuanLyThucDonActivity.this, SuaMonAnActivity.class);
                            intent.putExtra("id", monAn.getId());
                            intent.putExtra("tenMon", monAn.getTenMon());
                            intent.putExtra("gia", monAn.getGia());
                            intent.putExtra("moTa", monAn.getMoTa());
                            intent.putExtra("anhMinhHoa", monAn.getAnhMinhHoa());
                            startActivityForResult(intent, 1);
                        } else {
                            // Xóa món ăn
                            if (dbHelper.xoaMonAn(monAn.getId())) {
                                Toast.makeText(QuanLyThucDonActivity.this, "Xóa món thành công", Toast.LENGTH_SHORT).show();
                                taiDanhSachMonAn();
                            } else {
                                Toast.makeText(QuanLyThucDonActivity.this, "Xóa món thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.show();
            }
        });

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuanLyThucDonActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void taiDanhSachMonAn() {
        monAnList = dbHelper.layTatCaMonAn();
        adapter = new MonAnAdapter(this, monAnList);
        danhSachThucDon.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            taiDanhSachMonAn();
        }
    }
}