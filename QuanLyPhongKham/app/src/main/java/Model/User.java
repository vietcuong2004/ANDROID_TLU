package Model;

public class User {
    private int id;
    private String email;
    private String password;
    private String role;
    private String fullName;
    private String specialty;

    public User(int id, String email, String password, String role, String fullName, String specialty) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.specialty = specialty;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getFullName() {
        return fullName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}
