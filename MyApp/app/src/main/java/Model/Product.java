package Model;

public class Product {
    private int id;
    private String name;
    private String brand;
    private int price;
    private int stock;
    private String note;
    private int quantity;

    public Product(int id, String name, String brand, int price, int stock, String note) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.stock = stock;
        this.note = note;
    }

    // Getter
    public int getId() { return id; }
    public String getName() { return name; }
    public String getBrand() { return brand; }
    public int getPrice() { return price; }
    public int getStock() { return stock; }
    public String getNote() { return note; }
    public int getQuantity() { return quantity; }
    public int getTotal() { return quantity * price; }

    // Setter
    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setPrice(int price) { this.price = price; }
    public void setStock(int stock) { this.stock = stock; }
    public void setNote(String note) { this.note = note; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
