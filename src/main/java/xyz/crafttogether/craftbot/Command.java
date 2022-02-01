package xyz.crafttogether.craftbot;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public interface Command {
    void invoke(SlashCommandEvent event);
}
