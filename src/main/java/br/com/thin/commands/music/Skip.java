package br.com.thin.commands.music;

import br.com.thin.annotations.SlashCommand;
import br.com.thin.utils.PlayerUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SlashCommand
public class Skip extends MusicCommand {
    private final static int DEFAULT_SKIP = 1;
    public Skip() {
        super("skip", "Skips current track.");
    }

    /**Default use case for the skip command.
     * @param event discord's text channel event
     * */
    public void execute(SlashCommandInteractionEvent event) {
        if (this.isNotOnCorrectChannel(event)) return;
        if (this.isNotCurrentlyPlaying(event)) return;

        int times = Optional.ofNullable(event.getOption("number"))
                .map(OptionMapping::getAsInt)
                .orElse(DEFAULT_SKIP);

        AudioPlayer audioPlayer = PlayerUtils.getAudioPlayer(event.getGuild());
        List<AudioTrack> skippedTracks = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            skippedTracks.add(audioPlayer.getPlayingTrack());
            audioPlayer.stopTrack();
        }

        event.replyEmbeds(this.createEmbed(skippedTracks)).queue();
    }

    /**Creates the embed that is sent on the text channel
     * @return embed with the result of the operation
     * */
    private MessageEmbed createEmbed(List<AudioTrack> skippedTracks) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("**Skipping track**");
        builder.setColor(Color.LIGHT_GRAY);

        for (AudioTrack track : skippedTracks) {
            String description = String.format("%s was skipped.\n", track.getInfo().title);
            builder.appendDescription(description);
        }

        return builder.build();
    }

    @Override
    public List<OptionData> getOptions() {
        OptionData optionData = new OptionData(OptionType.INTEGER, "number", "Number of tracks to be skipped", false)
                .setMinValue(1);

        return Collections.singletonList(optionData);
    }
}