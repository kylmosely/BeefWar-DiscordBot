package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class BotCommands extends ListenerAdapter {
    private Dotenv config;
    private boolean isBeef = false;
    private Member mentionedName;
    private User user;

    public BotCommands() {
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        config = Dotenv.load();
        if (event.getName().equals("beef")) {

            if (!isBeef) {
                user = event.getUser();

                String UserId = event.getUser().getId();
                String mentionedId = event.getOption("person").getAsString();
                mentionedName = event.getOption("person").getAsMember();
                event.getGuild().addRoleToMember(UserSnowflake.fromId(mentionedId), event.getGuild().getRoleById(config.get("ROLE"))).queue();
                event.getGuild().addRoleToMember(UserSnowflake.fromId(UserId), event.getGuild().getRoleById(config.get("ROLE"))).queue();

                event.reply("Beef War Started With " + mentionedName.getAsMention() + " And " + user.getAsMention()).queue();
                isBeef = true;
            } else {
                event.reply("Sorry, another beef is happening right now.").queue();
            }
            ;
        } else if (event.getName().equals("beefend")){
            event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(config.get("ROLE"))).queue();
            event.reply("Done").queue();
            isBeef = false;
        }
    }
}