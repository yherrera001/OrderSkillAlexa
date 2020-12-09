package io.alexa.menu;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import io.alexa.menu.handlers.*;


public class RestaurantStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new FallbackIntentHandler(),
                        new MenuIntentHandler(),
                        new OrderIntentHandler(),
                        new NameIntentHandler(),
                        new ConfirmationIntentHandler())
                // Add your skill id below
                .withSkillId("amzn1.ask.skill.0c00c6fa-6a26-4700-8c7f-01210f1f971d")
                .build();
    }

    public RestaurantStreamHandler() {
        super(getSkill());
    }

}
