package br.com.thin.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.Collection;

/**
 * Handles the audio tracks loader results
 * */
public class AudioLoadHandler implements AudioLoadResultHandler {
    private final GuildMusicManager guildMusicManager;

    public AudioLoadHandler(GuildMusicManager guildMusicManager) {
        this.guildMusicManager = guildMusicManager;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {
        this.guildMusicManager.getTrackScheduler().queue(audioTrack);
    }

    /**
     * Method only called when playing a track with search terms rather
     * than the direct url.
     *
     * @param audioPlaylist youtube search results
     */
    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        AudioTrack firstTrack = audioPlaylist.getTracks().get(0);
        this.guildMusicManager.getTrackScheduler().queue(firstTrack);
    }

    @Override
    public void noMatches() {
        // do nothing
    }

    @Override
    public void loadFailed(FriendlyException e) {
        // do nothing
    }
}
