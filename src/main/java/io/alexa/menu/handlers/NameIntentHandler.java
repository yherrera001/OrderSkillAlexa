package io.alexa.menu.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.*;
import io.alexa.menu.service.DynamoService;
import io.alexa.menu.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class NameIntentHandler implements RequestHandler {

    private final Logger logger = LoggerFactory.getLogger(NameIntentHandler.class);
    private ProductService productService;
    private DynamoService dynamoService;

    public NameIntentHandler(){
        this.dynamoService = new DynamoService();
        this.productService = new ProductService();
    }

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("NameIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {
        String firstName = getCustomerFirstNameSlotValue(handlerInput);
        String firstLetterOfLastName = getCustomerFirstInitialOfLastNameSlotValue(handlerInput);
        Map<String,Object> sessionAttr = handlerInput.getAttributesManager().getSessionAttributes();
        if (firstName != null) sessionAttr.put("CUSTOMER_NAME",String.format("%s %s.",firstName,firstLetterOfLastName));
        String speechText = String.format("Okay %s, if you would like to hear the menu say menu, or if you would like to place an order then do so.", firstName, firstLetterOfLastName);
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Yasmin's restaurant", "Order Something")
                .withShouldEndSession(false)
                .withReprompt(speechText)
                .build();
    }


    private String getCustomerFirstNameSlotValue(HandlerInput handlerInput)
    {
        Request request = handlerInput.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;

        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot customerFirstNameSlot = slots.get("customerFirstName");
        if(customerFirstNameSlot != null
                && customerFirstNameSlot.getResolutions() != null
                && customerFirstNameSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
            // Store the user's favorite color in the Session and create response.
            return customerFirstNameSlot.getValue();
        }
        return null;
    }

    private String getCustomerFirstInitialOfLastNameSlotValue(HandlerInput handlerInput)
    {
        Request request = handlerInput.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;

        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot customerFirstInitialOfLastNameSlot = slots.get("customerFirstInitialOfLastName");
        if(customerFirstInitialOfLastNameSlot != null
                && customerFirstInitialOfLastNameSlot.getResolutions() != null
                && customerFirstInitialOfLastNameSlot.getResolutions().toString().contains("ER_SUCCESS_MATCH")) {
            // Store the user's favorite color in the Session and create response.
            return customerFirstInitialOfLastNameSlot.getValue();
        }
        return null;
    }
}
