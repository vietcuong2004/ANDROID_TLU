package vn.edu.tlu.cse.vvc.quanlyphongkham;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import Adapter.PatientAppointmentAdapter;
import Model.Appointment;
import java.util.ArrayList;
import java.util.Calendar;

public class PatientActivity extends AppCompatActivity {

    TextView txtTitle;
    Button btnBook, btnLogout;
    RecyclerView recyclerView;
    DBHelper dbHelper;
    String userEmail;
    ArrayList<Appointment> appointments;
    PatientAppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        txtTitle = findViewById(R.id.txtTitle);
        btnBook = findViewById(R.id.btnBook);
        btnLogout = findViewById(R.id.btnLogout);
        recyclerView = findViewById(R.id.recyclerAppointments);

        dbHelper = new DBHelper(this);
        userEmail = getIntent().getStringExtra("email");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadAppointments();

        btnBook.setOnClickListener(v -> showBookingDialog());

        btnLogout.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void loadAppointments() {
        appointments = dbHelper.getAppointmentsForPatient(userEmail);
        adapter = new PatientAppointmentAdapter(this, appointments, dbHelper, this::loadAppointments);
        recyclerView.setAdapter(adapter);
    }

    private void showBookingDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_book_appointment, null);
        EditText edtDate = view.findViewById(R.id.edtDate);
        EditText edtTime = view.findViewById(R.id.edtTime);
        Spinner spinnerDoctor = view.findViewById(R.id.spinnerDoctor);
        Button btnConfirm = view.findViewById(R.id.btnConfirmBooking);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        // DatePicker
        edtDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view1, y, m, d) -> {
                edtDate.setText(String.format("%04d-%02d-%02d", y, m + 1, d));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        // TimePicker
        edtTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this, (view1, h, m) -> {
                edtTime.setText(String.format("%02d:%02d", h, m));
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();
        });

        // Load danh sách bác sĩ (tên)
        ArrayList<String> doctorNames = dbHelper.getAllDoctorNames();
        ArrayAdapter<String> spnAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, doctorNames);
        spinnerDoctor.setAdapter(spnAdapter);

        // Xử lý khi bấm nút "Xác nhận"
        btnConfirm.setOnClickListener(v -> {
            String date = edtDate.getText().toString().trim();
            String time = edtTime.getText().toString().trim();
            String doctorName = spinnerDoctor.getSelectedItem().toString();
            String doctorEmail = dbHelper.getEmailByDoctorName(doctorName);

            if (date.isEmpty() || time.isEmpty() || doctorEmail.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            dbHelper.addAppointment(date, time, doctorEmail, userEmail);
            Toast.makeText(this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            loadAppointments(); // refresh RecyclerView
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
    }
}
