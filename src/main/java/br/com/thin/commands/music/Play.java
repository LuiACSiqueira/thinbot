package br.com.thin.commands.music;

import br.com.thin.audioplayer.PlayerManager;
import br.com.thin.annotations.SlashCommand;
import br.com.thin.utils.ButtonUtils;
import br.com.thin.exceptions.ExceptionEmbedManager;
import br.com.thin.utils.PlayerUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SlashCommand
public class Play extends MusicCommand {
    private static final String YOUTUBE_REGEX = "^((?:https?:)?//)?((?:www|m)\\.)?(youtube\\.com|youtu.be)(/(?:[\\w\\-]+\\?v=|embed/|v/)?)([\\w\\-]+)(\\S+)?$";
    private static final String YOUTUBE_ID_REGEX = "(?<=v=|youtu\\.be/)[\\w-]{11}";
    private static final String THUMBNAIL_URL = "http://img.youtube.com/vi/%s/default.jpg";

    public Play() {
        super("play", "Plays a track from its title or youtube URL");
    }

    /**Default use case for the play command.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(SlashCommandInteractionEvent event) throws InterruptedException {
        if (this.isNotOnCorrectChannel(event)) return;

        String term = event.getOption("term").getAsString();

        if (!this.termIsYoutubeUrl(term)) {
            term = "ytsearch:" + term;
        }

        Guild guild = event.getGuild();
        PlayerManager playerManager = PlayerManager.get();
        boolean isPlaying = PlayerUtils.getAudioPlayer(guild).getPlayingTrack() != null;
        playerManager.play(guild, term);
        event.replyEmbeds(this.createEmbed()).queue();

        if (!isPlaying) {
            this.sendTrackPlayingEmbed(event);
        }
    }

    /**Creates the embed that is sent on the text channel
     * @param event discord's text channel event
     * @return embed with the result of the operation
     * */
    private void sendTrackPlayingEmbed(SlashCommandInteractionEvent event) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        Guild guild = event.getGuild();
        TextChannel textChannel = event.getChannel().asTextChannel();
        textChannel.sendMessageEmbeds(this.createTrackPlayingEmbed(guild)).addActionRow(ButtonUtils.getAll()).queue();
    }

    private boolean termIsYoutubeUrl(String term) {
        Pattern pattern = Pattern.compile(YOUTUBE_REGEX);
        Matcher matcher = pattern.matcher(term);

        return matcher.find();
    }

    private MessageEmbed createEmbed() {
        return new EmbedBuilder()
                .setTitle("**Loading track**")
                .setColor(Color.LIGHT_GRAY)
                .build();
    }

    /**Creates the embed that is sent on the text channel
     * @param guild discord guild where the event was created
     * @return embed with the result of the operation
     * */
    private MessageEmbed createTrackPlayingEmbed(Guild guild) {
        AudioTrack currentTrack = PlayerUtils.getCurrentTrack(guild);
        AudioTrackInfo info = currentTrack.getInfo();

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("**Playing track**")
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

    /**Overrides common isNotOnCorrectChannel method
     * with validations specific to this use case
     * */
    @Override
    protected boolean isNotOnCorrectChannel(SlashCommandInteractionEvent event) {
        Member user = event.getMember();
        GuildVoiceState userVoiceState = user.getVoiceState();

        if (!userVoiceState.inAudioChannel()) {
            MessageEmbed embed = ExceptionEmbedManager
                    .create(ExceptionEmbedManager.Channel.USER_NOT_IN_AUDIO_CHANNEL, this.name, user);

            event.replyEmbeds(embed).queue();
            return true;
        }


        GuildVoiceState selfVoiceState = event.getGuild().getSelfMember()
                .getVoiceState();
        if (!selfVoiceState.inAudioChannel()) {
            event.getGuild().getAudioManager().openAudioConnection(userVoiceState.getChannel());
            return false;
        }

        if (selfVoiceState.getChannel() != userVoiceState.getChannel()) {
            MessageEmbed embed = ExceptionEmbedManager
                    .create(ExceptionEmbedManager.Channel.USER_NOT_IN_SAME_AUDIO_CHANNEL, this.name, user);

            event.replyEmbeds(embed).queue();
            return true;
        }

        return false;
    }

    @Override
    public List<OptionData> getOptions() {
        OptionData optionData = new OptionData(OptionType.STRING, "term", "Track title or youtube URL", true)
                .setMinLength(1)
                .setMaxLength(255);

        return Collections.singletonList(optionData);
    }
}
