package vn.edu.tlu.cse.vvc.mycartman;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapter.PhoneAdapter;
import model.CartItem;
import model.Phone;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView imgCart;
    private PhoneAdapter phoneAdapter;
    private List<Phone> phoneList;
    private Map<String, CartItem> cartMap;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ Khởi tạo Firebase nếu cần
        FirebaseApp.initializeApp(this);

        recyclerView = findViewById(R.id.recyclerView);
        imgCart = findViewById(R.id.imgCart);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        phoneList = new ArrayList<>();
        cartMap = new HashMap<>();
        phoneAdapter = new PhoneAdapter(this, phoneList, cartMap);
        recyclerView.setAdapter(phoneAdapter);

        // ✅ GÁN giá trị cho biến toàn cục (không khai báo lại với `DatabaseReference`)
        dbRef = FirebaseDatabase
                .getInstance("https://mycartman-f6fad-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("Phone");

        loadPhonesFromRealtimeDB();

        imgCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra("cartMap", (Serializable) cartMap);
            startActivity(intent);
        });
    }

    private void loadPhonesFromRealtimeDB() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phoneList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Phone phone = child.getValue(Phone.class);
                    if (phone != null) {
                        phoneList.add(phone);
                        Log.d("DEBUG_PHONE", "Loaded: " + phone.getName());
                    }
                }
                phoneAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
                Log.e("FirebaseDB", "loadPhones: ", error.toException());
            }
        });
    }
}
