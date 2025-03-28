package vn.edu.tlu.cse.vvc.menuapp;

public class Food {
    private int id;
    private String name;
    private int price;
    private String description;
    private String image;

    // Constructor đầy đủ tham số
    public Food(int id, String name, int price, String description, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    // Getter
    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImage() { return image; }
}
