package xyz.crafttogether.craftbot.listeners;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ICategorizableChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import xyz.crafttogether.craftbot.CraftBot;

public class CustomVoiceCall extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        System.out.println("JOINED");
        if (!event.getGuild().getId().equals(CraftBot.getConfig().getGuildId())) return;
        if (!event.getChannelJoined().getId().equals(CraftBot.getConfig().getVoiceChannelId())) return;

        final VoiceChannel channel = ((ICategorizableChannel) event.getChannelJoined())
                .getParentCategory()
                .createVoiceChannel(event.getMember().getEffectiveName())
                .addMemberPermissionOverride(event.getMember().getIdLong(), Permission.ALL_VOICE_PERMISSIONS, Permission.getRaw())
                .complete();
        event.getGuild().moveVoiceMember(event.getMember(), channel).queue();
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        System.out.println("huuuh");
        if (!event.getGuild().getId().equals(CraftBot.getConfig().getGuildId())) return;
        System.out.println(1);
        if (event.getChannelLeft().getMembers().size() != 0) return;
        System.out.println(2);
        if (event.getChannelLeft().getId().equals(CraftBot.getConfig().getVoiceChannelId())) return;
        System.out.println(3);

        final VoiceChannel hub = event.getGuild().getVoiceChannelById(CraftBot.getConfig().getVoiceChannelId());
        System.out.println(hub.getParentCategoryId().equals(((ICategorizableChannel) event.getVoiceState()).getParentCategoryId()));
        if (hub.getParentCategoryId().equals(((ICategorizableChannel) event.getVoiceState()).getParentCategoryId())) {
            event.getChannelLeft().delete().queue();
        }
    }
}
