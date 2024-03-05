package br.com.thin.exceptions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

/**
 * Catch all class that instantiates all error embeds
 * */
public class ExceptionEmbedManager {

    public static MessageEmbed create(ErrorType errorType, String commandName) {
        String title = String.format("**%s command failed.**", commandName);
        String description = String.format(errorType.getIdentifier());

        return new EmbedBuilder()
                .setTitle(title)
                .appendDescription(description)
                .setColor(Color.RED)
                .build();
    }

    public static MessageEmbed create(ErrorType errorType, String commandName, Member user) {
        String title = String.format("**%s command failed.**", commandName);
        String description = String.format(errorType.getIdentifier(), user.getEffectiveName());

        return new EmbedBuilder()
                .setTitle(title)
                .appendDescription(description)
                .setColor(Color.RED)
                .build();
    }

    public enum Channel implements ErrorType {
        USER_NOT_IN_AUDIO_CHANNEL("%s is not in an audio channel."),
        USER_NOT_IN_SAME_AUDIO_CHANNEL("%s is not in the same audio channel as me."),
        SELF_NOT_IN_AUDIO_CHANNEL("I'm not currently in an audio channel.");

        private final String identifier;

        Channel(String identifier) {
            this.identifier = identifier;
        }

        @Override
        public String getIdentifier() {
            return this.identifier;
        }
    }

    public enum Player implements ErrorType {
        NOT_PLAYING_TRACK("I'm not currently playing any tracks.");

        private final String identifier;

        Player(String identifier) {
            this.identifier = identifier;
        }

        public String getIdentifier() {
            return this.identifier;
        }
    }
}
