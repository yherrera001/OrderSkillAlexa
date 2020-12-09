package io.alexa.menu.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.alexa.menu.food.Order;
import io.alexa.menu.service.DynamoService;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.sessionAttribute;

public class ConfirmationIntentHandler implements RequestHandler {

    private DynamoService dynamoService;
    public ConfirmationIntentHandler(){
        this.dynamoService = new DynamoService();
    }
    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("ConfirmationIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {

        String userRespondWithNo = getNoSlotValue(handlerInput);
        String userRespondWithYes = getYesSlotValue(handlerInput);
        String speechText = "";
        boolean isOrderSuccessful = false;

        if (userRespondWithNo != null){
            speechText = "Ok, what else would you like to order?";
        } else if(userRespondWithYes != null) {
            speechText = "Ok, thank you for ordering " + handlerInput.getRequestEnvelope().getSession().getAttributes().get("CUSTOMER_NAME");
            isOrderSuccessful = true;
            String sessionId = handlerInput.getRequestEnvelope().getSession().getSessionId();
            Map<String, Object> sessionAttr = handlerInput.getAttributesManager().getSessionAttributes();
            Order order = convertingObjectToOrderObject(sessionAttr.getOrDefault("ORDER", new Order()));
            dynamoService.addItemToDB(convertOrderToItem(order,sessionId));
        }
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Yasmin's restaurant", "Order Something")
                .withShouldEndSession(isOrderSuccessful)
                .withReprompt(speechText)
                .build();
    }

    private Item convertOrderToItem(Order order,String sessionId) {
        return new Item()
                .withPrimaryKey("ID",sessionId)
                .withJSON("ORDER", new Gson().toJson(order));
    }

    private String getNoSlotValue(HandlerInput handlerInput) {
        Request request = handlerInput.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;

        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot noSlot = slots.get("No");
        if (noSlot != null
                && noSlot.getResolutions() != null
                && noSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
            // Store the user's favorite color in the Session and create response.

            return noSlot.getValue();
        }
        return null;
    }

    private String getYesSlotValue(HandlerInput handlerInput) {
        Request request = handlerInput.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;

        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot yesSlot = slots.get("Yes");
        if (yesSlot != null
                && yesSlot.getResolutions() != null
                && yesSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
            // Store the user's favorite color in the Session and create response.

            return yesSlot.getValue();
        }
        return null;
    }

    private Order convertingObjectToOrderObject(Object singleObject) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(singleObject, Order.class);
    }
}
