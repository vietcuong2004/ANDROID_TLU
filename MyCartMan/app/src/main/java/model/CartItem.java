package model;

public class CartItem {
    private Phone phone;
    private int quantity;

    public CartItem() {
        // Bắt buộc có constructor rỗng nếu lưu trong Firebase
    }

    public CartItem(Phone phone, int quantity) {
        this.phone = phone;
        this.quantity = quantity;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        if (phone != null) {
            return quantity * phone.getPrice();
        } else {
            return 0;
        }
    }
}
