package org.beef;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter {
    private final Dotenv config;

    public Bot() throws LoginException, InterruptedException {
        config = Dotenv.load();
        String token = config.get("TOKEN");
        String channel = config.get("CHANNEL");
        JDA jda = JDABuilder.createDefault(token)
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new BotCommands())
                .setStatus(OnlineStatus.ONLINE)
                .setActivity(Activity.watching("You"))
                .build().awaitReady();
        jda.upsertCommand("beef", "beef war will begin")
                .addOption(OptionType.MENTIONABLE, "person", "person you want to beef with")

                .queue();
        jda.upsertCommand("beefend", "end beef war :)")
                .queue();
        jda.upsertCommand("beeflock", "In case a beefer is being a turd")
                        .queue();

        jda.getGuildById(channel);
    }

    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
        } catch (LoginException | InterruptedException e) {
            System.out.println(e);
        }

    }
}