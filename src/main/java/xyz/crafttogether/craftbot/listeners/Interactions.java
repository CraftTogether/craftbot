package xyz.crafttogether.craftbot.listeners;

import xyz.crafttogether.craftbot.Config;
import xyz.crafttogether.craftbot.CraftBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.stream.Stream;

public class Interactions extends ListenerAdapter {

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getGuildById(CraftBot.getConfig().getGuild())
                .getTextChannelById(CraftBot.getConfig().getButtonRoles().getChannel())
                .retrieveMessageById(CraftBot.getConfig().getButtonRoles().getMessage()).queue(message -> {
                    final Stream<String> neededIds = CraftBot.getConfig().getButtonRoles().getRoles().stream().map(e -> e.getId());
                    final Stream<String> existingIds = message.getActionRows().stream()
                            .flatMap(actionRow -> actionRow.getComponents().stream())
                            .map(Component::getId);

                    if (!existingIds.equals(neededIds)) {
                        final Stream<Button> buttons = CraftBot.getConfig().getButtonRoles().getRoles().stream().map(e -> Button.primary(e.getId(), e.getLabel()));
                        message.editMessageComponents(ActionRow.of(buttons.toList())).queue();
                    }
                });
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        final Optional<Config.ButtonRoles.Role> buttonRole = CraftBot.getConfig().getButtonRoles().getRoles()
                .stream()
                .filter(e -> e.getId().equals(event.getComponentId()))
                .findFirst();

        if (buttonRole.isEmpty()) {
            event.reply("Button not configured, please report to an admin.").setEphemeral(true).queue();
        } else {
            final String roleId = buttonRole.get().getRole();
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
