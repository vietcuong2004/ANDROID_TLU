package Model;

public class Appointment {
    private int id;
    private String date;
    private String time;
    private String doctorEmail;
    private String patientEmail;
    private String status;
    private String note;
    private String doctorName;

    public Appointment(int id, String date, String time,
                       String doctorEmail, String patientEmail,
                       String status, String note) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.doctorEmail = doctorEmail;
        this.patientEmail = patientEmail;
        this.status = status;
        this.note = note;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDoctorEmail() {
        return doctorEmail;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public String getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDoctorEmail(String doctorEmail) {
        this.doctorEmail = doctorEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDoctorName() {
        return doctorName;
    }
}
