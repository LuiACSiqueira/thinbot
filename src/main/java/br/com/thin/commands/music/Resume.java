package br.com.thin.commands.music;

import br.com.thin.annotations.SlashCommand;
import br.com.thin.utils.PlayerUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@SlashCommand
public class Resume extends MusicCommand {

    public Resume() {
        super("resume", "Resumes current track.");
    }

    /**Default use case for the resume command.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (this.isNotOnCorrectChannel(event)) return;
        if (this.isNotCurrentlyPlaying(event)) return;

        AudioPlayer audioPlayer = PlayerUtils.getAudioPlayer(event.getGuild());
        AudioTrack currentTrack = audioPlayer.getPlayingTrack();

        audioPlayer.setPaused(false);
        event.replyEmbeds(this.createEmbed(currentTrack)).queue();
    }

    /**Creates the embed that is sent on the text channel
     * @param audioTrack audio track currently being played
     * @return embed with the result of the operation
     * */
    private MessageEmbed createEmbed(AudioTrack audioTrack) {
        String description = String.format("%s was resumed.", audioTrack.getInfo().title);
        return new EmbedBuilder()
                .setTitle("**Track resumed**")
                .setDescription(description)
                .setColor(Color.LIGHT_GRAY)
                .build();
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }
}
