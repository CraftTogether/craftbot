package com.github.crafttogether.craftbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;

public class CraftBot {
    private static JDA jda;

    public static void main(String[] args) throws LoginException {
        jda = JDABuilder.createLight("")
                .build();
    }
}
