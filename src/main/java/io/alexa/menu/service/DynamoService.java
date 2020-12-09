package io.alexa.menu.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import io.alexa.menu.food.Product;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DynamoService {

    private final DynamoDB dynamoDB = new DynamoDB(AmazonDynamoDBClientBuilder.standard().build());

    public DynamoService(){

    }

    public void addItemToDB(Item item) {
        Table table = dynamoDB.getTable("Orders");
        table.putItem(item);
    }

    public Item getItemFromDB(String value){
        Table table = dynamoDB.getTable("Orders");
        return table.getItem("ID",value);
    }
}
