package br.com.thin.commands.music;

import br.com.thin.audioplayer.TrackScheduler;
import br.com.thin.annotations.SlashCommand;
import br.com.thin.utils.PlayerUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SlashCommand
public class Repeat extends MusicCommand {
    private static final int DEFAULT_REPEAT = 1;

    public Repeat() {
        super("repeat", "Repeats current track");
    }

    /**Default use case for the repeat command.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (this.isNotOnCorrectChannel(event)) return;
        if (this.isNotCurrentlyPlaying(event)) return;

        TrackScheduler trackScheduler = PlayerUtils.getTrackScheduler(event.getGuild());

        int times = Optional.ofNullable(event.getOption("times"))
                .map(OptionMapping::getAsInt)
                .orElse(DEFAULT_REPEAT);

        trackScheduler.setTimesRepeatingTrack(times);

        AudioTrack currentTrack = trackScheduler.getAudioPlayer().getPlayingTrack();
        event.replyEmbeds(this.createEmbed(currentTrack, times)).queue();
    }

    /**Creates the embed that is sent on the text channel
     * @param currentTrack audio track currently being played
     * @param times times that the current track should be repeated
     * @return embed with the result of the operation
     * */
    private MessageEmbed createEmbed(AudioTrack currentTrack, int times) {
        AudioTrackInfo info = currentTrack.getInfo();
        String description = String.format("%s %d times.", info.title, times);

        return new EmbedBuilder()
                .setTitle("**Repeating track**")
                .setDescription(description)
                .setColor(Color.LIGHT_GRAY)
                .build();
    }

    @Override
    public List<OptionData> getOptions() {
        OptionData optionData = new OptionData(OptionType.INTEGER, "times", "Times the track should repeat", false)
                .setMinValue(1);

        return Collections.singletonList(optionData);
    }
}