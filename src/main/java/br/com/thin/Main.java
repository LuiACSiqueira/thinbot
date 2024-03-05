package br.com.thin;

import br.com.thin.configs.ConfigLoader;
import br.com.thin.listeners.ButtonListener;
import br.com.thin.listeners.CommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Main {

    /**
    * Instantiates JDA client
    **/
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, InterruptedException, IOException {
        JDA jda = JDABuilder.createDefault(ConfigLoader.getDiscordAppToken())
                .addEventListeners(new CommandListener())
                .addEventListeners(new ButtonListener())
                .enableIntents(GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                .build();
        jda.awaitReady();
    }
}