package br.com.thin.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioForwarder implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer byteBuffer;
    private final MutableAudioFrame mutableAudioFrame;

    public AudioForwarder(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.byteBuffer = ByteBuffer.allocate(1024);
        this.mutableAudioFrame = new MutableAudioFrame();
        this.mutableAudioFrame.setBuffer(this.byteBuffer);
    }

    @Override
    public boolean canProvide() {
        return this.audioPlayer.provide(this.mutableAudioFrame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return this.byteBuffer.flip();
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
