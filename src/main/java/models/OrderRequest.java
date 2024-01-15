package models;

import java.util.ArrayList;
import java.util.List;

public class OrderRequest {
    private List<String> ingredients = new ArrayList<String>();
    public void setIngredient(String ingredient) {
        ingredients.add(ingredient);
    }
}
