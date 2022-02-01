package xyz.crafttogether.craftbot.listeners;

import xyz.crafttogether.craftbot.CraftBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class Interactions extends ListenerAdapter {

    private final String devUpdates = "development_updates_role";

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getGuildById(CraftBot.getConfig().getGuildId())
                .getTextChannelById(CraftBot.getConfig().getInteractionsChannel())
                .retrieveMessageById(CraftBot.getConfig().getInteractionMessage()).queue(message -> {
                    final Stream<String> ids = message.getActionRows().stream()
                            .flatMap(actionRow -> actionRow.getComponents().stream())
                            .map(Component::getId);
                    if (ids.noneMatch(id -> id.equals(devUpdates))) {
                        message.editMessageComponents(ActionRow.of(Button.primary(devUpdates, "Development Updates"))).queue();
                    }
                });
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if (event.getComponentId().equals(devUpdates)) {
            final String roleId = CraftBot.getConfig().getRoleId();
            final Role role = event.getGuild().getRoleById(roleId);
            assert role != null;

            // Member already has the role
            if (event.getMember().getRoles().stream().map(Role::getId).anyMatch(roleId::equals)) {
                event.getGuild().removeRoleFromMember(event.getMember(), role).queue();
                event.reply("You no longer have " + role.getAsMention() + ".").setEphemeral(true).queue();
            } else {
                event.getGuild().addRoleToMember(event.getMember(), role).queue();
                event.reply("You now have " + role.getAsMention() + ".").setEphemeral(true).queue();
            }
        }
    }
}
