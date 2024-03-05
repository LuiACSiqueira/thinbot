package br.com.thin.buttons;

import br.com.thin.annotations.Button;
import br.com.thin.audioplayer.TrackScheduler;
import br.com.thin.utils.PlayerUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;

@Button
public class RepeatButton extends ActionButton {
    private static final int DEFAULT_REPEAT = 1;

    public RepeatButton() {
        super("repeat", "repeat", ButtonStyle.SECONDARY, false, Emoji.fromUnicode("U+1F502"));
    }

    /**Default use case for the repeat button.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(ButtonInteractionEvent event) {
        if (this.isNotOnCorrectChannel(event)) return;
        if (this.isNotCurrentlyPlaying(event)) return;

        TrackScheduler trackScheduler = PlayerUtils.getTrackScheduler(event.getGuild());
        trackScheduler.setTimesRepeatingTrack(DEFAULT_REPEAT);

        AudioTrack currentTrack = trackScheduler.getAudioPlayer().getPlayingTrack();
        event.replyEmbeds(this.createEmbed(currentTrack)).queue();
    }

    /**Creates the embed that is sent on the text channel
     * @param currentTrack audio track currently being played
     * @return embed with the current audio track details
     * */
    private MessageEmbed createEmbed(AudioTrack currentTrack) {
        AudioTrackInfo info = currentTrack.getInfo();
        String description = String.format("%s once.", info.title);

        return new EmbedBuilder()
                .setTitle("**Repeating track**")
                .setDescription(description)
                .setColor(Color.LIGHT_GRAY)
                .build();
    }
}
