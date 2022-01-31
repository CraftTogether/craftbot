package com.github.crafttogether.craftbot;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Some secret commands mostly used to set up the bot.
 * These commands are not slash commands, instead use the bot mention as a prefix.
 */
public class SystemCommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getGuild().getId().equals(CraftBot.getConfig().getGuildId())) {
            if (event.getMember().getRoles().stream().map(Role::getId).noneMatch(CraftBot.getConfig().getSystemCommandsRole()::equals)) return;

            String message = event.getMessage().getContentRaw();
            if (!message.matches("<@!?" + event.getJDA().getSelfUser().getId() + ">.*")) return;
            if (message.startsWith("<@!")) message = message.replace("<@!", "<@");
            final String botMention = event.getJDA().getSelfUser().getAsMention();

            final String[] split = message.replaceFirst(botMention, "").split("\s+", 2);
            final String command = split[0];
            final String arguments = split[1];

            switch (command) {
                case "write":
                    onWriteCommand(event, arguments);
            }

        }
    }

    private void onWriteCommand(MessageReceivedEvent event, String arguments) {
        event.getChannel().sendMessage(arguments).queue();
    }

}
