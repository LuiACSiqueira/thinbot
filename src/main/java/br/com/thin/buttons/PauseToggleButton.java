package br.com.thin.buttons;

import br.com.thin.annotations.Button;
import br.com.thin.utils.PlayerUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.awt.*;

@Button
public class PauseToggleButton extends ActionButton {
    public PauseToggleButton() {
        super("pauseToggle", "toggle", ButtonStyle.SECONDARY, false, Emoji.fromUnicode("U+23EF"));
    }

    /**Default use case for the pause toggle button.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(ButtonInteractionEvent event) {
        if (this.isNotOnCorrectChannel(event)) return;
        if (this.isNotCurrentlyPlaying(event)) return;

        AudioPlayer audioPlayer = PlayerUtils.getAudioPlayer(event.getGuild());
        AudioTrack currentTrack = audioPlayer.getPlayingTrack();

        audioPlayer.setPaused(!audioPlayer.isPaused());
        event.replyEmbeds(this.createEmbed(currentTrack, audioPlayer.isPaused())).queue();
    }

    /**Creates the embed that is sent on the text channel
     * @param audioTrack audio track currently being played
     * @param paused is audio player currently paused
     * @return embed with the result of the operation
     * */
    private MessageEmbed createEmbed(AudioTrack audioTrack, boolean paused) {
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("**Pause toggle**")
                .setColor(Color.LIGHT_GRAY);
        String resumedDescription = String.format("%s was resumed.", audioTrack.getInfo().title);
        String pausedDescription = String.format("%s was paused.", audioTrack.getInfo().title);

        return paused
            ? builder.setDescription(pausedDescription).build()
            : builder.setDescription(resumedDescription).build();
    }
}
