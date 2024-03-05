package br.com.thin.buttons;

import br.com.thin.annotations.Button;
import br.com.thin.audioplayer.TrackScheduler;
import br.com.thin.utils.PlayerUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

@Button
public class ShuffleButton extends ActionButton {

    public ShuffleButton() {
        super("shuffle", "shuffle", ButtonStyle.SECONDARY, false, Emoji.fromUnicode("U+1F500"));
    }

    /**Default use case for the shuffle button.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(ButtonInteractionEvent event) {
        if (this.isNotOnCorrectChannel(event)) return;
        if (this.isNotCurrentlyPlaying(event)) return;

        TrackScheduler trackScheduler = PlayerUtils.getTrackScheduler(event.getGuild());
        List<AudioTrack> trackList = new ArrayList<>(trackScheduler.getQueue());
        Collections.shuffle(trackList);
        LinkedBlockingQueue<AudioTrack> shuffledTracks = new LinkedBlockingQueue<>(trackList);
        trackScheduler.setQueue(shuffledTracks);

        event.replyEmbeds(this.createEmbed()).queue();
    }

    /**Creates the embed that is sent on the text channel
     * @return embed with the result of the operation
     * */
    private MessageEmbed createEmbed() {
        return new EmbedBuilder()
                .setTitle("**Queue shuffled**")
                .setColor(Color.LIGHT_GRAY)
                .build();
    }
}
