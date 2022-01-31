package com.github.crafttogether.craftbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class SystemCommands extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getGuild().getId().equals(CraftBot.getConfig().getGuildId())) {
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
