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
public class SkipButton extends ActionButton {

    public SkipButton() {
        super("skip", "skip", ButtonStyle.SECONDARY, false, Emoji.fromUnicode("U+23ED"));
    }

    /**Default use case for the skip button.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(ButtonInteractionEvent event) {
        if (this.isNotOnCorrectChannel(event)) return;
        if (this.isNotCurrentlyPlaying(event)) return;

        AudioPlayer audioPlayer = PlayerUtils.getAudioPlayer(event.getGuild());
        AudioTrack skippedTrack = audioPlayer.getPlayingTrack();
        audioPlayer.stopTrack();

        event.replyEmbeds(this.createEmbed(skippedTrack)).queue();
    }

    /**Creates the embed that is sent on the text channel
     * @return embed with the result of the operation
     * */
    private MessageEmbed createEmbed(AudioTrack skippedTrack) {
        String description = String.format("%s was skipped.\n", skippedTrack.getInfo().title);

        return new EmbedBuilder()
                .setTitle("**Skipping track**")
                .appendDescription(description)
                .setColor(Color.LIGHT_GRAY)
                .build();
    }
}
