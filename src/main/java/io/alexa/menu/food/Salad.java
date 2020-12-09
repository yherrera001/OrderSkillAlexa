package io.alexa.menu.food;

import java.util.ArrayList;
import java.util.List;

public class Salad extends Product{

    private final List<Condiments> condimentsList = new ArrayList<>();

    public Salad(){
        super("Salad", 12.00, "Appetizer");
    }

//    public void removeCondiment(String nameOfCondiment){
//        this.condimentsList.remove(nameOfCondiment);
//    }
//
//    public void addCondiment(Condiments newCondiment){
//        this.condimentsList.add(newCondiment);
//    }
//
//    public List<Condiments> getCondimentsList() {
//        return condimentsList;
//    }
}
