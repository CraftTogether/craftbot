package xyz.crafttogether.craftbot.commands;

import xyz.crafttogether.craftbot.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class TestCommand implements Command {
    @Override
    public void invoke(SlashCommandEvent event) {
        event.reply("Hello there").setEphemeral(true).queue();
    }
}
