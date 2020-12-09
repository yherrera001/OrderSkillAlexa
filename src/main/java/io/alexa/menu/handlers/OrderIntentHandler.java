package io.alexa.menu.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.alexa.menu.food.*;
import io.alexa.menu.service.DynamoService;
import io.alexa.menu.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class OrderIntentHandler implements RequestHandler {

    private final Logger logger = LoggerFactory.getLogger(OrderIntentHandler.class);
    private ProductService productService;
    private DynamoService dynamoService;
    private boolean isOrderSuccessful = false;

    public OrderIntentHandler() {
        this.dynamoService = new DynamoService();
        this.productService = new ProductService();
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("OrderIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {

        String speechText = "";
        String meal = getFoodSlotValue(handlerInput);
        String drink = getDrinkSlotValue(handlerInput);
        String sessionId = handlerInput.getRequestEnvelope().getSession().getSessionId();
        String requestId = handlerInput.getRequestEnvelope().getRequest().getRequestId();
        Map<String, Object> sessionAttr = handlerInput.getAttributesManager().getSessionAttributes();
        String customerName = (String) sessionAttr.getOrDefault("CUSTOMER_NAME", "John.D");
        Order order =  convertingObjectToOrderObject(sessionAttr.getOrDefault("ORDER", new Order()));


        if (order.getTotalOfOrder() == 0.00) {
            logger.info("Processing a new order for {}", customerName);
            sessionAttr.put("ORDER", order);

        } else {
            logger.info("Updating order for {}", customerName);
        }
        speechText = processOrder(meal, drink, sessionAttr, order);


        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Yasmin's restaurant", "Order Something")
                .withShouldEndSession(isOrderSuccessful)
                .withReprompt(speechText)
                .build();

    }

    private String processOrder(String meal, String drink, Map<String, Object> sessionAttr, Order order) {
        String speechText = "";
        if (meal != null && drink != null) {
            Product productMeal = getProduct(meal);
            Product productDrink = getDrink(drink);


            if (productMeal != null && meal.toLowerCase().contains("chicken sandwich") && productDrink != null) {
                ChickenSandwich chickenSandwich = (ChickenSandwich) productMeal;
                Beverage beverage = (Beverage) productDrink;
                order.addItemToOrder(chickenSandwich);
                order.addItemToOrder(beverage);
                sessionAttr.put("MEAL_" + order.getItemsOrdered().indexOf(chickenSandwich), meal);
                sessionAttr.put("DRINK_" + order.getItemsOrdered().indexOf(beverage), drink);

            } else if (productMeal != null && meal.toLowerCase().contains("salad") && productDrink != null) {
                Salad salad = (Salad) productMeal;
                Beverage beverage = (Beverage) productDrink;
                order.addItemToOrder(salad);
                order.addItemToOrder(beverage);
                sessionAttr.put("MEAL_" + order.getItemsOrdered().indexOf(salad), meal);
                sessionAttr.put("DRINK_" + order.getItemsOrdered().indexOf(beverage), drink);
            } else {
                Burger burger = (Burger) productMeal;
                Beverage beverage = (Beverage) productDrink;
                order.addItemToOrder(burger);
                order.addItemToOrder(beverage);
                sessionAttr.put("MEAL_" + order.getItemsOrdered().indexOf(burger), meal);
                sessionAttr.put("DRINK_" + order.getItemsOrdered().indexOf(beverage), drink);


            }

            StringBuilder stringBuilder = new StringBuilder();

            for (Product p : order.getItemsOrdered()) {
                stringBuilder.append(String.format("a %s for $%s <break time=\"1s\"/>", p.getName(), p.getPrice()));
            }
            sessionAttr.replace("ORDER", order);
            speechText = String.format("<speak> Here is what I got down for your order: %s. This totals your order to $%s. Will this complete your order? </speak>", stringBuilder.toString(), order.getTotalOfOrder());
            return speechText;

        } else if (meal != null) {


            Product product = getProduct(meal);

            if (product != null && meal.toLowerCase().contains("chicken")) {
                ChickenSandwich chickenSandwich = (ChickenSandwich) product;

                order.addItemToOrder(chickenSandwich);
                sessionAttr.put("MEAL_" + order.getItemsOrdered().indexOf(chickenSandwich), meal);

            } else if (product != null && meal.toLowerCase().contains("salad")) {
                Salad salad = (Salad) product;

                order.addItemToOrder(salad);

                sessionAttr.put("MEAL_" + order.getItemsOrdered().indexOf(salad), meal);

            } else {
                Burger burger = (Burger) product;

                order.addItemToOrder(burger);
                sessionAttr.put("MEAL_" + order.getItemsOrdered().indexOf(burger), meal);

            }

            StringBuilder stringBuilder = new StringBuilder();

            for (Product p : order.getItemsOrdered()) {
                stringBuilder.append(String.format("a %s for $%s <break time=\"1s\"/>", p.getName(), p.getPrice()));
            }
            sessionAttr.replace("ORDER", order);
            speechText = String.format("<speak> Here is what I got down for your order: %s. This totals your order to $%s. Will this complete your order? </speak>", stringBuilder.toString(), order.getTotalOfOrder());

            return speechText;

        } else if (drink != null) {
            Product productDrink = getDrink(drink);


            if (productDrink != null) {
                Beverage beverage = (Beverage) productDrink;
                order.addItemToOrder(beverage);
                sessionAttr.put("DRINK_" + order.getItemsOrdered().indexOf(beverage), drink);
            }

            StringBuilder stringBuilder = new StringBuilder();

            for (Product p : order.getItemsOrdered()) {
                stringBuilder.append(String.format("a %s for $%s <break time=\"1s\"/>", p.getName(), p.getPrice()));
            }
            sessionAttr.replace("ORDER", order);
            speechText = String.format("<speak> Here is what I got down for your order: %s. This totals your order to $%s. Will this complete your order? </speak>", stringBuilder.toString(), order.getTotalOfOrder());
            return speechText;

        } else {
            return "Unable to understand your order.";
        }
    }

    private String getFoodSlotValue(HandlerInput handlerInput) {
        Request request = handlerInput.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;

        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot foodSlot = slots.get("food");
        if (foodSlot != null
                && foodSlot.getResolutions() != null
                && foodSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
            // Store the user's favorite color in the Session and create response.

            return foodSlot.getValue();
        }
        logger.info("value is null");
        return null;
    }


    private String getDrinkSlotValue(HandlerInput handlerInput) {
        Request request = handlerInput.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;

        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        Slot drinkSlot = slots.get("drink");

        if (drinkSlot != null
                && drinkSlot.getResolutions() != null
                && drinkSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
            // Store the user's favorite color in the Session and create response.
            return drinkSlot.getValue().toLowerCase();
        }
        return null;
    }


    private Product getProduct(String slotValue) {
        return productService.findFoodProduct(slotValue);
    }

    private Product getDrink(String slotValue) {
        return productService.findDrinkProduct(slotValue);
    }

    private Order convertingObjectToOrderObject(Object singleObject) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(singleObject, Order.class);
    }
}
