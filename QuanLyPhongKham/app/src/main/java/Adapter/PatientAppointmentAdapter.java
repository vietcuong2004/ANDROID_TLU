package Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import Model.Appointment;
import vn.edu.tlu.cse.vvc.quanlyphongkham.DBHelper;
import vn.edu.tlu.cse.vvc.quanlyphongkham.R;

public class PatientAppointmentAdapter extends RecyclerView.Adapter<PatientAppointmentAdapter.AppointmentViewHolder> {

    private Context context;
    private List<Appointment> appointmentList;
    private DBHelper dbHelper;
    private Runnable refreshCallback;

    public PatientAppointmentAdapter(Context context, List<Appointment> appointmentList, DBHelper dbHelper, Runnable refreshCallback) {
        this.context = context;
        this.appointmentList = appointmentList;
        this.dbHelper = dbHelper;
        this.refreshCallback = refreshCallback;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_patient, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appt = appointmentList.get(position);

        // Lấy tên bác sĩ từ email
        String doctorName = dbHelper.getUserName(appt.getDoctorEmail());

        holder.txtDate.setText("Ngày khám: " + appt.getDate());
        holder.txtTime.setText("Giờ khám: " + appt.getTime());
        holder.txtDoctor.setText("Bác sĩ: " + doctorName);
        holder.txtStatus.setText("Trạng thái: " + appt.getStatus());

        holder.btnCancel.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Hủy lịch hẹn")
                    .setMessage("Bạn có chắc chắn muốn hủy lịch khám này?")
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        dbHelper.deleteAppointment(appt.getId());
                        Toast.makeText(context, "Đã hủy lịch hẹn", Toast.LENGTH_SHORT).show();
                        if (refreshCallback != null) refreshCallback.run(); // reload lại danh sách
                    })
                    .setNegativeButton("Không", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtTime, txtDoctor, txtStatus;
        Button btnCancel;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTime = itemView.findViewById(R.id.txtTime);
            txtDoctor = itemView.findViewById(R.id.txtDoctor);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
