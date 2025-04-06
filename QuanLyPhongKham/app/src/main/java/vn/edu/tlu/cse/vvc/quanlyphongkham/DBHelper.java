package vn.edu.tlu.cse.vvc.quanlyphongkham;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import java.util.ArrayList;
import Model.Appointment;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ClinicDB.db";
    public static final int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Email TEXT UNIQUE, " +
                "Password TEXT, " +
                "Role TEXT, " +
                "FullName TEXT, " +
                "Specialty TEXT)");

        db.execSQL("CREATE TABLE Appointments (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Date TEXT, " +
                "Time TEXT, " +
                "DoctorEmail TEXT, " +
                "PatientEmail TEXT, " +
                "Status TEXT, " +
                "Note TEXT)");

        insertSampleData(db);
    }

    private void insertSampleData(SQLiteDatabase db) {
        // Thêm bác sĩ
        db.execSQL("INSERT INTO Users VALUES (1, 'doctor1@clinic.com', '123456', 'Bác sĩ', 'Dr. Nguyễn Văn A', 'Nội tổng quát')");
        db.execSQL("INSERT INTO Users VALUES (2, 'doctor2@clinic.com', 'abc123', 'Bác sĩ', 'Dr. Trần Thị B', 'Nhi khoa')");
        db.execSQL("INSERT INTO Users VALUES (3, 'doctor3@clinic.com', 'qwerty', 'Bác sĩ', 'Dr. Lê Minh C', 'Tai - Mũi - Họng')");
        db.execSQL("INSERT INTO Users VALUES (8, 'admin1', '123456', 'Bác sĩ', 'Nguyễn Văn H', 'Nội tổng quát')");

        // Thêm bệnh nhân
        db.execSQL("INSERT INTO Users VALUES (4, 'patient1@email.com', '987654', 'Bệnh nhân', 'Lê Văn D', '-')");
        db.execSQL("INSERT INTO Users VALUES (5, 'patient2@email.com', '654321', 'Bệnh nhân', 'Trần Thị E', '-')");
        db.execSQL("INSERT INTO Users VALUES (6, 'patient3@email.com', '321654', 'Bệnh nhân', 'Nguyễn Văn F', '-')");
        db.execSQL("INSERT INTO Users VALUES (7, 'user1', '123456', 'Bệnh nhân', 'Nguyễn Văn G', '-')");

        // Thêm các lịch hẹn
        db.execSQL("INSERT INTO Appointments VALUES (1, '2025-04-10', '08:00', 'doctor1@clinic.com', 'patient1@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (2, '2025-04-10', '09:30', 'doctor2@clinic.com', 'patient2@email.com', 'Đã xác nhận', 'Kiểm tra sốt cao')");
        db.execSQL("INSERT INTO Appointments VALUES (3, '2025-04-11', '10:00', 'doctor3@clinic.com', 'patient3@email.com', 'Chưa xác nhận', '-')");
        db.execSQL("INSERT INTO Appointments VALUES (4, '2025-04-11', '14:00', 'doctor1@clinic.com', 'patient2@email.com', 'Đã xác nhận', 'Theo dõi huyết áp')");
        db.execSQL("INSERT INTO Appointments VALUES (5, '2025-04-12', '16:30', 'doctor2@clinic.com', 'patient1@email.com', 'Chưa xác nhận', '-')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Appointments");
        onCreate(db);
    }

    // Đăng nhập
    public boolean checkLogin(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE Email = ? AND Password = ?", new String[]{email, password});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public String getUserRole(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Role FROM Users WHERE Email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            String role = cursor.getString(0);
            cursor.close();
            return role;
        }
        cursor.close();
        return "";
    }

    public String getUserName(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT FullName FROM Users WHERE Email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        cursor.close();
        return "";
    }

    // Thêm lịch hẹn mới
    public void addAppointment(String date, String time, String doctorEmail, String patientEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Date", date);
        values.put("Time", time);
        values.put("DoctorEmail", doctorEmail);
        values.put("PatientEmail", patientEmail);
        values.put("Status", "Chưa xác nhận");
        values.put("Note","");
        db.insert("Appointments", null, values);
    }

    // Lấy lịch hẹn của bệnh nhân
    public ArrayList<Appointment> getAppointmentsForPatient(String patientEmail) {
        ArrayList<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Appointments WHERE PatientEmail = ?", new String[]{patientEmail});
        if (cursor.moveToFirst()) {
            do {
                list.add(new Appointment(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Xóa lịch hẹn
    public void deleteAppointment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Appointments", "ID = ?", new String[]{String.valueOf(id)});
    }

    // Lấy tất cả tên bác sĩ
    public ArrayList<String> getAllDoctorNames() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT FullName FROM Users WHERE Role = 'Bác sĩ'", null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy email bác sĩ từ tên
    public String getEmailByDoctorName(String fullName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Email FROM Users WHERE FullName = ?", new String[]{fullName});
        if (cursor.moveToFirst()) {
            String email = cursor.getString(0);
            cursor.close();
            return email;
        }
        cursor.close();
        return "";
    }

    // Cập nhật trạng thái và ghi chú
    public void updateAppointmentStatus(int id, String status, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Status", status);
        values.put("Note", note);
        db.update("Appointments", values, "ID = ?", new String[]{String.valueOf(id)});
    }

    // Lấy danh sách lịch hẹn của bác sĩ, có thể lọc theo ngày
    public ArrayList<Appointment> getAppointmentsForDoctor(String doctorEmail, String filterDate) {
        ArrayList<Appointment> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if (filterDate == null || filterDate.isEmpty()) {
            cursor = db.rawQuery("SELECT * FROM Appointments WHERE DoctorEmail = ?", new String[]{doctorEmail});
        } else {
            cursor = db.rawQuery("SELECT * FROM Appointments WHERE DoctorEmail = ? AND Date = ?", new String[]{doctorEmail, filterDate});
        }

        if (cursor.moveToFirst()) {
            do {
                list.add(new Appointment(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                ));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
}
