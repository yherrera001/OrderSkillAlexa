package io.alexa.menu.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ui.SsmlOutputSpeech;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class MenuIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput handlerInput) {
        return handlerInput.matches(intentName("MenuIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput handlerInput) {

        String firstSentence = "Here at Yasmin's restaurant we have the following items on the menu: ";
        String secondSentence = "We have chicken sandwiches for $9.00, ";
        String thirdSentence = "burgers for $10.00, ";
        String fourthSentence = "salads for $12.00, ";
        String fifthSentence = "lastly, we also have all pepsi products and bottled waters for $3.00";
        String speechText = String.format("<speak> %s <break time=\"1s\"/>%s <audio src=\"soundbank://soundlibrary/animals/amzn_sfx_chicken_cluck_01\"/><break time=\"1s\"/> %s <break time=\"1s\"/> %s and %s</speak>",firstSentence,secondSentence,thirdSentence,fourthSentence,fifthSentence);
        return handlerInput.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Yasmin's restaurant", firstSentence + secondSentence + thirdSentence + fourthSentence + fifthSentence)
                .withShouldEndSession(false)
                .withReprompt(speechText)
                .build();

    }
}
