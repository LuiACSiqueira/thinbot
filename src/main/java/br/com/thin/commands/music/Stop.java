package br.com.thin.commands.music;

import br.com.thin.audioplayer.TrackScheduler;
import br.com.thin.annotations.SlashCommand;
import br.com.thin.utils.PlayerUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@SlashCommand
public class Stop extends MusicCommand {

    public Stop() {
        super("stop", "Stops the bot from playing the tracks");
    }

    /**Default use case for the stop command.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (this.isNotOnCorrectChannel(event)) return;
        if (this.isNotCurrentlyPlaying(event)) return;

        TrackScheduler trackScheduler = PlayerUtils.getTrackScheduler(event.getGuild());
        trackScheduler.getQueue().clear();
        trackScheduler.getAudioPlayer().stopTrack();

        event.replyEmbeds(this.createEmbed()).queue();
    }

    /**Creates the embed that is sent on the text channel
     * @return embed with the result of the operation
     * */
    private MessageEmbed createEmbed() {
        return new EmbedBuilder()
                .setTitle("**Stopping**")
                .setDescription("Stopped the tracks enqueued.")
                .setColor(Color.LIGHT_GRAY)
                .build();
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }
}
