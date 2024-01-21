package models;
import java.util.List;

public class Order {
    private List<String> ingredients;
    private String status;
    private String _id;
    private String number;
    private String createdAt;
    private String updatedAt;

    public Order(List<String> ingredients, String status, String _id, String number, String createdAt, String updatedAt) {
        this.ingredients = ingredients;
        this.status = status;
        this._id = _id;
        this.number = number;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public List<String> getIngredients() {
        return ingredients;
    }
}
