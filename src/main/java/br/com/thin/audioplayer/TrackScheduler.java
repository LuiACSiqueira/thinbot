package br.com.thin.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Manages the order the tracks will be played
 * and plays next track when current track is over
 * */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer audioPlayer;
    private BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private int timesRepeatingTrack = 0;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    /**
     * Plays next track when current track is over, unless timesRepeatingTrack
     * is set to anything but more than 0.
     * @param player audio player for relevant guild
     * @param track next track that should be played
     * @param endReason unused
     * */
    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (this.timesRepeatingTrack > 0) {
            player.startTrack(track.makeClone(), false);
            this.timesRepeatingTrack--;
        } else {
            player.startTrack(this.queue.poll(), false);
        }
    }

    /**
     * Adds track to queue
     * @param track audio track that should be added to queue
     * */
    public void queue(AudioTrack track) {
        if (!this.audioPlayer.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    public AudioPlayer getAudioPlayer() {
        return this.audioPlayer;
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return this.queue;
    }

    public void setQueue(BlockingQueue<AudioTrack> queue) {
        this.queue = queue;
    }

    public void setTimesRepeatingTrack(int timesRepeatingTrack) {
        this.timesRepeatingTrack = timesRepeatingTrack;
    }
}