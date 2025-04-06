package vn.edu.tlu.cse.vvc.quanlyphongkham;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import Adapter.DoctorAppointmentAdapter;
import Model.Appointment;
import java.util.ArrayList;
import java.util.Calendar;

public class DoctorActivity extends AppCompatActivity {

    TextView txtTitle;
    EditText edtFilterDate;
    RecyclerView recyclerAppointments;
    Button btnLogout, btnAll;

    DBHelper dbHelper;
    String doctorEmail;
    ArrayList<Appointment> appointments;
    DoctorAppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        txtTitle = findViewById(R.id.txtTitle);
        edtFilterDate = findViewById(R.id.edtFilterDate);
        recyclerAppointments = findViewById(R.id.recyclerAppointments);
        btnLogout = findViewById(R.id.btnLogout);
        btnAll = findViewById(R.id.btnAll);

        dbHelper = new DBHelper(this);
        doctorEmail = getIntent().getStringExtra("email");

        recyclerAppointments.setLayoutManager(new LinearLayoutManager(this));
        loadAppointments(null);

        edtFilterDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, y, m, d) -> {
                String date = String.format("%04d-%02d-%02d", y, m + 1, d);
                edtFilterDate.setText(date);
                loadAppointments(date);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnAll.setOnClickListener(v -> {
            edtFilterDate.setText("");
            loadAppointments(null);
        });

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void loadAppointments(String filterDate) {
        appointments = dbHelper.getAppointmentsForDoctor(doctorEmail, filterDate);
        adapter = new DoctorAppointmentAdapter(this, appointments, dbHelper, this::refresh);
        recyclerAppointments.setAdapter(adapter);
    }

    private void refresh() {
        String filterDate = edtFilterDate.getText().toString().trim();
        loadAppointments(filterDate.isEmpty() ? null : filterDate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}