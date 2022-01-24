package com.github.crafttogether.craftbot;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class CommandHandler extends ListenerAdapter {

    private HashMap<String, Command> commands = new HashMap<>();

    public void addCommand(String commandName, Command command) {
        commands.put(commandName, command);
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        Command command = commands.getOrDefault(event.getName(), null);
        if (command == null) {
            event.reply("Command could not be found").setEphemeral(true).queue();
        } else {
            command.invoke(event);
        }
    }
}
