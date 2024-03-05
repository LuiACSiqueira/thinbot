package br.com.thin.buttons;

import br.com.thin.annotations.Button;
import br.com.thin.audioplayer.TrackScheduler;
import br.com.thin.utils.PlayerUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;

@Button
public class StopButton extends ActionButton {

    public StopButton() {
        super("stop", "stop", ButtonStyle.SECONDARY, false, Emoji.fromUnicode("U+23F9"));
    }

    /**Default use case for the stop button.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(ButtonInteractionEvent event) {
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
}
