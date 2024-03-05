package br.com.thin.listeners;

import br.com.thin.annotations.Button;
import br.com.thin.buttons.ActionButton;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dedicated listener to all button related text channel events
 * */
public class ButtonListener extends ListenerAdapter {
    private final List<ActionButton> buttons = new ArrayList<>();

    /**
     * Reflection used to add all @Button annotated class
     * to the list of ActionButton buttons.
     * */
    public ButtonListener() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Class<?>> buttonTypeList = new ArrayList<>(
                new Reflections("br.com.thin.buttons")
                        .getTypesAnnotatedWith(Button.class)
        );
        for (Class<?> clazz : buttonTypeList) {
            this.buttons.add((ActionButton) clazz.getDeclaredConstructor().newInstance());
        }
    }

    /**
     * For every button text channel events, this method will
     * only use it's relevant use case
     * */
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        for (ActionButton button : this.buttons) {
            if (event.getButton().getId().equals(button.getId())) {
                button.execute(event);
                return;
            }
        }
    }
}
