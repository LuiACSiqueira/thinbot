package br.com.thin.commands.music;

import br.com.thin.audioplayer.PlayerManager;
import br.com.thin.audioplayer.TrackScheduler;
import br.com.thin.commands.Command;
import br.com.thin.exceptions.ExceptionEmbedManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Abstract music command class.
 * Common music command methods will be in here
 * */
public abstract class MusicCommand implements Command {
    protected final String name;
    protected final String description;

    public MusicCommand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public abstract void execute(SlashCommandInteractionEvent event) throws InterruptedException, IOException;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public abstract List<OptionData> getOptions();

    /**
     * Checks if both the user who created the event and the bot
     * are and/or should be on the same audio channel.
     * If the required conditions are not met,
     * an embed will be sent to the text channel informing
     * the user.
     * @param event discord's text channel event
     * */
    protected boolean isNotOnCorrectChannel(SlashCommandInteractionEvent event) {
        Member user = event.getMember();
        GuildVoiceState userVoiceState = user.getVoiceState();
        GuildVoiceState selfVoiceState = event.getGuild().getSelfMember()
                .getVoiceState();

        if (!userVoiceState.inAudioChannel()) {
            MessageEmbed embed = ExceptionEmbedManager
                    .create(ExceptionEmbedManager.Channel.USER_NOT_IN_AUDIO_CHANNEL, this.name, user);

            event.replyEmbeds(embed).queue();
            return true;
        }

        if (!selfVoiceState.inAudioChannel()) {
            MessageEmbed embed = ExceptionEmbedManager
                .create(ExceptionEmbedManager.Channel.SELF_NOT_IN_AUDIO_CHANNEL, this.name);

            event.replyEmbeds(embed).queue();
            return true;
        }

        if (selfVoiceState.getChannel() != userVoiceState.getChannel()) {
            MessageEmbed embed = ExceptionEmbedManager
                    .create(ExceptionEmbedManager.Channel.USER_NOT_IN_SAME_AUDIO_CHANNEL, this.name, user);

            event.replyEmbeds(embed).queue();
            return true;
        }

        return false;
    }

    /**Checks if the bot is currently playing any audio track.
     * In case it is not, an embed will be sent to the text channel.
     * @param event discord's text channel event
     * */
    protected boolean isNotCurrentlyPlaying(SlashCommandInteractionEvent event) {
        AudioTrack currentTrack = PlayerManager.get()
                .getGuildMusicManager(event.getGuild())
                .getTrackScheduler()
                .getAudioPlayer()
                .getPlayingTrack();

        if (Objects.isNull(currentTrack)) {
            MessageEmbed embed = ExceptionEmbedManager.create(ExceptionEmbedManager.Player.NOT_PLAYING_TRACK, this.name);
            event.replyEmbeds(embed);

            return true;
        }

        return false;
    }
}
