package Model;

public class Order {
    private int id;
    private String customerEmail;
    private String date;
    private int total;
    private String status;

    public Order(int id, String customerEmail, String date, int total, String status) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.date = date;
        this.total = total;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getDate() {
        return date;
    }

    public int getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }
}