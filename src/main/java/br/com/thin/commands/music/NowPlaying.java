package br.com.thin.commands.music;

import br.com.thin.annotations.SlashCommand;
import br.com.thin.utils.ButtonUtils;
import br.com.thin.utils.PlayerUtils;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SlashCommand
public class NowPlaying extends MusicCommand {
    private static final String YOUTUBE_ID_REGEX = "(?<=v=|youtu\\.be/)[\\w-]{11}";
    private static final String THUMBNAIL_URL = "http://img.youtube.com/vi/%s/default.jpg";

    public NowPlaying() {
        super("nowplaying", "Shows current track");
    }

    /**Default use case for the now playing command.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (this.isNotOnCorrectChannel(event)) return;
        if (this.isNotCurrentlyPlaying(event)) return;

        Guild guild = event.getGuild();
        event.replyEmbeds(this.createTrackPlayingEmbed(guild))
                .addActionRow(ButtonUtils.getAll()).queue();
    }

    /**Creates the embed that is sent on the text channel
     * @param guild discord guild where the event was created
     * @return embed with the details of the current audio track being played
     * */
    private MessageEmbed createTrackPlayingEmbed(Guild guild) {
        AudioTrack currentTrack = PlayerUtils.getCurrentTrack(guild);
        AudioTrackInfo info = currentTrack.getInfo();

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("**Now playing**")
                .setDescription("Title: " + info.title)
                .appendDescription("\nChannel: " + info.author)
                .setColor(Color.LIGHT_GRAY);

        String thumbnailUrl = this.getThumbnailUrl(info.uri);
        if (!thumbnailUrl.isEmpty()) {
            builder.setImage(thumbnailUrl);
        }

        return builder.build();
    }

    private String getThumbnailUrl(String url) {
        Pattern pattern = Pattern.compile(YOUTUBE_ID_REGEX);
        Matcher matcher = pattern.matcher(url);

        return matcher.find()
                ? String.format(THUMBNAIL_URL, matcher.group(0))
                : "";
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }
}
