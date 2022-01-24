package com.github.crafttogether.craftbot.commands;

import com.github.crafttogether.craftbot.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public class TestCommand implements Command {
    @Override
    public void invoke(SlashCommandEvent event) {
        event.reply("Hello there").setEphemeral(true).queue();
    }
}
