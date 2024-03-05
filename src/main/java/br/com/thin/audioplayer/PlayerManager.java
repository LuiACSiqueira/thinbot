package br.com.thin.audioplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> guildMusicManagerMap = new HashMap<>();;
    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    private PlayerManager() {
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
    }

    /**
     * Stores a music manager for every guild that the bot is present in
     * */
    public GuildMusicManager getGuildMusicManager(Guild guild) {
        return this.guildMusicManagerMap.computeIfAbsent(guild.getIdLong(), (guildId) -> this.addGuild(guild));
    }

    private GuildMusicManager addGuild(Guild guild) {
        GuildMusicManager musicManager = new GuildMusicManager(this.audioPlayerManager);
        guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());

        return musicManager;
    }

    /**
     * Selects the correct music manager to play the requested audio track
     * @param guild discord guild in which the event was created
     * @param trackSearchTerm requested track's youtube url or search term
     * */
    public void play(Guild guild, String trackSearchTerm) {
        GuildMusicManager guildMusicManager = this.getGuildMusicManager(guild);
        this.audioPlayerManager.loadItemOrdered(guildMusicManager, trackSearchTerm, new AudioLoadHandler(guildMusicManager));
    }

    public static PlayerManager get() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
