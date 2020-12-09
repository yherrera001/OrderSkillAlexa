package io.alexa.menu.food;

public class Product {

    private String name;
    private Double price;
    private String mealType;

    public Product(){
    }

    public Product(String name, Double price, String mealType) {
        this.name = name;
        this.price = price;
        this.mealType = mealType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", mealType='" + mealType + '\'' +
                '}';
    }
}
