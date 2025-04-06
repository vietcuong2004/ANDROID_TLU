package Model;

public class Order {
    private int id;
    private String customerEmail;
    private String orderDate;
    private int total;
    private String status;

    public Order(int id, String customerEmail, String orderDate, int total, String status) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.orderDate = orderDate;
        this.total = total;
        this.status = status;
    }

    public Order() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
