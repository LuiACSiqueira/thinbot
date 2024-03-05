package br.com.thin.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.util.List;

public interface Command {
    void execute(SlashCommandInteractionEvent event) throws InterruptedException, IOException;

    String getName();
    String getDescription();
    List<OptionData> getOptions();
}
