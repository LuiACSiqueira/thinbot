package br.com.thin.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

/**
 * Stores the track scheduler and audio forwarder for each discord guild
 * in which the bot is present
 * */
public class GuildMusicManager {
    private final TrackScheduler trackScheduler;
    private final AudioForwarder audioForwarder;

    public GuildMusicManager(AudioPlayerManager manager) {
        AudioPlayer audioPlayer = manager.createPlayer();
        this.trackScheduler = new TrackScheduler(audioPlayer);
        audioPlayer.addListener(this.trackScheduler);
        this.audioForwarder = new AudioForwarder(audioPlayer);
    }

    public TrackScheduler getTrackScheduler() {
        return this.trackScheduler;
    }

    public AudioForwarder getAudioForwarder() {
        return this.audioForwarder;
    }
}
