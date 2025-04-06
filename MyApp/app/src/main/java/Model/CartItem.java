package Model;
public class CartItem {
    private int id;
    private String customerEmail;
    private int productId;
    private int quantity;
    private int total;

    public CartItem(int id, String customerEmail, int productId, int quantity, int total) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.productId = productId;
        this.quantity = quantity;
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getTotal() {
        return total;
    }
}

