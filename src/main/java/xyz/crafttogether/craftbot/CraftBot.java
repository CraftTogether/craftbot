package xyz.crafttogether.craftbot;

import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import xyz.crafttogether.craftbot.commands.TestCommand;
import xyz.crafttogether.craftbot.listeners.CustomVoiceCall;
import xyz.crafttogether.craftbot.listeners.Interactions;
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
    private static Config config;
    private static final Logger logger = LoggerFactory.getLogger(CraftBot.class);
    private static CommandHandler handler = new CommandHandler();
    private static JDA jda;

    public static Config getConfig() {
        return config;
    }

    private static Config loadConfig() throws FileNotFoundException {
        logger.info("Loading configuration");
        final Toml toml = new Toml().read(new FileInputStream("config.toml"));
        System.out.println(toml.getString("token"));
        config = new Config(
                toml.getString("token"),
                toml.getString("guildId"),
                toml.getString("roleId"),
                toml.getString("interactionsChannel"),
                toml.getString("interactionsMessage"),
                toml.getString("systemCommandsRole"),
                toml.getString("voiceChannelId")
        );
        logger.info("Successfully loaded configuration");
        return config;
    }

    private static void generateConfig() throws IOException {
        InputStream defaultConfig = CraftBot.class.getResourceAsStream("/config.toml");
        assert defaultConfig != null;
        Files.copy(defaultConfig, Path.of("config.toml"));
    }

    public static void main(String[] args) throws LoginException, InterruptedException {
        try {
            loadConfig();
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
                .enableIntents(GatewayIntent.GUILD_VOICE_STATES)
                .enableCache(CacheFlag.VOICE_STATE)
                .addEventListeners(handler, new SystemCommands(), new Interactions(), new CustomVoiceCall())
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
