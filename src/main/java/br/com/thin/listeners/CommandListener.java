package br.com.thin.listeners;

import br.com.thin.commands.Command;
import br.com.thin.annotations.SlashCommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dedicated listener to all command related text channel events
 * */
public class CommandListener extends ListenerAdapter {
    private final List<Command> commands = new ArrayList<>();

    /**
     * Reflection used to add all @Command annotated class
     * to the list of Command commands.
     * */
    public CommandListener() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Class<?>> commandTypeList = new ArrayList<>(
                new Reflections("br.com.thin.commands")
                .getTypesAnnotatedWith(SlashCommand.class)
        );
        for (Class<?> clazz : commandTypeList) {
            this.commands.add((Command) clazz.getDeclaredConstructor().newInstance());
        }
    }

    /**
     * When the bot starts up, this method will add all the commands
     * to the discord guild which it was added to
     * */
    @Override
    public void onReady(ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            for (Command command : this.commands) {
                CommandCreateAction commandCreateAction = guild.upsertCommand(command.getName(), command.getDescription());
                if (!command.getOptions().isEmpty()) {
                    commandCreateAction.addOptions(command.getOptions());
                }
                commandCreateAction.queue();
            }
        }
    }

    /**
     * For every command text channel events, this method will
     * only use it's relevant use case
     * */
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        for (Command command : this.commands) {
            if (command.getName().equals(event.getName())) {
                try {
                    command.execute(event);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return;
            }
        }
    }
}
