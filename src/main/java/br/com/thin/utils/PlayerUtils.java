package br.com.thin.utils;

import br.com.thin.audioplayer.PlayerManager;
import br.com.thin.audioplayer.TrackScheduler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;

public class PlayerUtils {
    public static AudioPlayer getAudioPlayer(Guild guild) {
        return PlayerManager.get()
                .getGuildMusicManager(guild)
                .getTrackScheduler()
                .getAudioPlayer();
    }

    public static TrackScheduler getTrackScheduler(Guild guild) {
        return PlayerManager.get()
                .getGuildMusicManager(guild)
                .getTrackScheduler();
    }

    public static AudioTrack getCurrentTrack(Guild guild) {
        return PlayerManager.get()
                .getGuildMusicManager(guild)
                .getTrackScheduler()
                .getAudioPlayer()
                .getPlayingTrack();
    }

}
