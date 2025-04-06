package Model;

public class CartItem {
    private int id;
    private String customerEmail;
    private String productName;
    private int quantity;
    private int total;

    public CartItem(int id, String customerEmail, String productName, int quantity, int total) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.productName = productName;
        this.quantity = quantity;
        this.total = total;
    }

    public CartItem() {}

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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
