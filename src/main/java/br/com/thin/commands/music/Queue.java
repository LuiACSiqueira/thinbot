package br.com.thin.commands.music;

import br.com.thin.audioplayer.TrackScheduler;
import br.com.thin.annotations.SlashCommand;
import br.com.thin.utils.PlayerUtils;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SlashCommand
public class Queue extends MusicCommand {

    public Queue() {
        super("queue", "Display current queue");
    }

    /**Default use case for the queue command.
     * @param event discord's text channel event
     * */
    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if (this.isNotOnCorrectChannel(event)) return;

        TrackScheduler trackScheduler = PlayerUtils.getTrackScheduler(event.getGuild());
        List<AudioTrack> trackQueue = new ArrayList<>(trackScheduler.getQueue());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("**Current queue:**");
        embedBuilder.setColor(Color.LIGHT_GRAY);

        /*
        Creates the embed that is sent on the text channel
         */
        for (int i = 0; i < trackQueue.size(); i++) {
            AudioTrackInfo info = trackQueue.get(i).getInfo();
            String embedFieldContent = String.format("**%d.** %s \n by %s \n\n", i+1, info.title, info.author);
            embedBuilder.appendDescription(embedFieldContent);
        }

        event.replyEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }
}
