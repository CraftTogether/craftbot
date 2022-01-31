package com.github.crafttogether.craftbot;

public class Config {
    private final String token;
    private final String guildId;
    private final String roleId;
    private final String interactionsChannel;
    private final String interactionMessage;

    public Config(String token, String guildId, String roleId, String interactionsChannel, String interactionMessage) {
        this.token = token;
        this.guildId = guildId;
        this.roleId = roleId;
        this.interactionsChannel = interactionsChannel;
        this.interactionMessage = interactionMessage;
    }

    public String getToken() {
        return token;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getInteractionMessage() {
        return interactionMessage;
    }

    public String getInteractionsChannel() {
        return interactionsChannel;
    }

}
