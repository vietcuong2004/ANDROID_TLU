package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import Model.Appointment;
import vn.edu.tlu.cse.vvc.quanlyphongkham.DBHelper;
import vn.edu.tlu.cse.vvc.quanlyphongkham.R;

public class DoctorAppointmentAdapter extends RecyclerView.Adapter<DoctorAppointmentAdapter.ViewHolder> {

    Context context;
    ArrayList<Appointment> appointments;
    DBHelper dbHelper;
    Runnable refreshCallback;

    public DoctorAppointmentAdapter(Context context, ArrayList<Appointment> appointments, DBHelper dbHelper, Runnable refreshCallback) {
        this.context = context;
        this.appointments = appointments;
        this.dbHelper = dbHelper;
        this.refreshCallback = refreshCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_doctor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appt = appointments.get(position);

        String patientName = dbHelper.getUserName(appt.getPatientEmail());
        holder.txtPatientName.setText(patientName);
        holder.txtDate.setText("Ngày: " + appt.getDate());
        holder.txtTime.setText("Giờ: " + appt.getTime());
        holder.txtStatus.setText("Trạng thái: " + appt.getStatus());
        holder.edtNote.setText(appt.getNote() != null ? appt.getNote() : "");

        holder.btnConfirm.setOnClickListener(v -> {
            String note = holder.edtNote.getText().toString().trim();
            dbHelper.updateAppointmentStatus(appt.getId(), "Đã xác nhận", note);
            Toast.makeText(context, "Đã xác nhận lịch khám", Toast.LENGTH_SHORT).show();

            // Cập nhật trạng thái trực tiếp không cần refresh lại adapter
            appt.setStatus("Đã xác nhận");
            appt.setNote(note);
            notifyItemChanged(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPatientName, txtDate, txtTime, txtStatus;
        EditText edtNote;
        Button btnConfirm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPatientName = itemView.findViewById(R.id.txtPatientName);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            edtNote = itemView.findViewById(R.id.edtNote);
            btnConfirm = itemView.findViewById(R.id.btnConfirm);
        }
    }
}
