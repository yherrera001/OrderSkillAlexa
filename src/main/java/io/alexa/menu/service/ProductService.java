package io.alexa.menu.service;

import io.alexa.menu.food.*;

public class ProductService {

    public ProductService (){

    }

    public Product findFoodProduct(String slotValue) {

        switch (slotValue) {
            case "burger":
                return new Burger();
            case "salad":
                return new Salad();
            default:
                return new ChickenSandwich();
        }
    }

    public Beverage findDrinkProduct(String slotValue){

        switch (slotValue) {
            case "pepsi":
                return new Beverage("Pepsi");
            case "sierra mist":
                return new Beverage("Sierra Mist");
            case "mountain dew":
                return new Beverage("Mountain Dew");
            case "root beer":
                return new Beverage("Root Beer");
            default:
                return new Beverage("Bottled Water");
        }
    }
}
