package io.alexa.menu.food;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private final List<Product> itemsOrdered;
    private Double totalOfOrder = 0.00;
    private String customerName;

    public Order(){
        this.itemsOrdered = new ArrayList<>();
    }

    public void addItemToOrder(Product product) {
        this.itemsOrdered.add(product);
        this.totalOfOrder += product.getPrice();
    }

//    public void removeItemToOrder(Product product) {
//        this.itemsOrdered.remove(product);
//        this.totalOfOrder -= product.getPrice();
//    }

    public List<Product> getItemsOrdered() {
        return itemsOrdered;
    }

    public Double getTotalOfOrder() {
        return totalOfOrder;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
