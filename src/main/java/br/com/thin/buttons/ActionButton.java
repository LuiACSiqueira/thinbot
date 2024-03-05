package br.com.thin.buttons;

import br.com.thin.audioplayer.PlayerManager;
import br.com.thin.exceptions.ExceptionEmbedManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;

import java.util.Objects;

/**
 * Abstract button class.
 * Common button methods will be in here
 * */
public abstract class ActionButton extends ButtonImpl {

    protected ActionButton(String id, String label, ButtonStyle style, boolean disabled, Emoji emoji) {
        super(id, label, style, disabled, emoji);
    }

    public abstract void execute(ButtonInteractionEvent event);

    /**
     * Checks if both the user who created the event and the bot
     * are and/or should be on the same audio channel.
     * If the required conditions are not met,
     * an embed will be sent to the text channel informing
     * the user.
     * @param event discord's text channel event
     * */
    protected boolean isNotOnCorrectChannel(ButtonInteractionEvent event) {
        Member user = event.getMember();
        GuildVoiceState userVoiceState = user.getVoiceState();
        GuildVoiceState selfVoiceState = event.getGuild().getSelfMember()
                .getVoiceState();

        if (!userVoiceState.inAudioChannel()) {
            MessageEmbed embed = ExceptionEmbedManager
                    .create(ExceptionEmbedManager.Channel.USER_NOT_IN_AUDIO_CHANNEL, this.getLabel(), user);

            event.replyEmbeds(embed).queue();
            return true;
        }

        if (!selfVoiceState.inAudioChannel()) {
            MessageEmbed embed = ExceptionEmbedManager
                    .create(ExceptionEmbedManager.Channel.SELF_NOT_IN_AUDIO_CHANNEL, this.getLabel());

            event.replyEmbeds(embed).queue();
            return true;
        }

        if (selfVoiceState.getChannel() != userVoiceState.getChannel()) {
            MessageEmbed embed = ExceptionEmbedManager
                    .create(ExceptionEmbedManager.Channel.USER_NOT_IN_SAME_AUDIO_CHANNEL, this.getLabel(), user);

            event.replyEmbeds(embed).queue();
            return true;
        }

        return false;
    }

    /**Checks if the bot is currently playing any audio track.
     * In case it is not, an embed will be sent to the text channel.
     * @param event discord's text channel event
     * */
    protected boolean isNotCurrentlyPlaying(ButtonInteractionEvent event) {
        AudioTrack currentTrack = PlayerManager.get()
                .getGuildMusicManager(event.getGuild())
                .getTrackScheduler()
                .getAudioPlayer()
                .getPlayingTrack();

        if (Objects.isNull(currentTrack)) {
            MessageEmbed embed = ExceptionEmbedManager.create(ExceptionEmbedManager.Player.NOT_PLAYING_TRACK, this.getLabel());
            event.replyEmbeds(embed);

            return true;
        }

        return false;
    }
}