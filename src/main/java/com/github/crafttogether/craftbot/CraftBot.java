package com.github.crafttogether.craftbot;

import com.github.crafttogether.craftbot.commands.TestCommand;
import com.moandjiezana.toml.Toml;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CraftBot {
    private static final Logger logger = LoggerFactory.getLogger(CraftBot.class);
    private static CommandHandler handler = new CommandHandler();
    private static JDA jda;

    private static Config parseConfig() throws FileNotFoundException {
        logger.info("Loading configuration");
        Toml toml = new Toml().read(new FileInputStream("config.toml"));
        Config config =  new Config()
                .setToken(toml.getString("token"));
        logger.info("Successfully loaded configuration");
        return config;
    }

    private static void generateConfig() throws IOException {
        InputStream defaultConfig = CraftBot.class.getResourceAsStream("/config.toml");
        assert defaultConfig != null;
        Files.copy(defaultConfig, Path.of("config.toml"));
    }

    public static void main(String[] args) throws LoginException, IOException, InterruptedException {
        Config config = null;
        try {
            config = parseConfig();
        } catch (FileNotFoundException e) {
            logger.error("Could not find config.toml, attempting to generate one...");
            try {
                generateConfig();
                logger.info("Successfully generated default config, exiting...");
                return;
            } catch (IOException e2) {
                logger.error("Failed to generate new config, exiting...");
                e2.printStackTrace();
                return;
            }
        }

        assert config != null;
        jda = JDABuilder.createLight(config.getToken())
                .addEventListeners(handler)
                .build()
                .awaitReady();

        // Add commands
        addSlashCommand("test", "test command", new TestCommand());
    }

    public static void addSlashCommand(String name, String description, Command command) {
        jda.upsertCommand(name, description).queue();
        handler.addCommand(name, command);
    }
}
