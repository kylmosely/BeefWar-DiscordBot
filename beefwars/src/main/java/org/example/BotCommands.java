package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class BotCommands extends ListenerAdapter {
    private boolean isBeef = false;

    public BotCommands() {
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Dotenv config = Dotenv.load();
        if (event.getName().equals("beef")) {

            if (!isBeef) {
                User user = event.getUser();

                String UserId = event.getUser().getId();
                String mentionedId = Objects.requireNonNull(event.getOption("person")).getAsString();
                Member mentionedName = Objects.requireNonNull(event.getOption("person")).getAsMember();
                Objects.requireNonNull(event.getGuild()).addRoleToMember(UserSnowflake.fromId(mentionedId), Objects.requireNonNull(event.getGuild().getRoleById(config.get("ROLE")))).queue();
                event.getGuild().addRoleToMember(UserSnowflake.fromId(UserId), Objects.requireNonNull(event.getGuild().getRoleById(config.get("ROLE")))).queue();
                assert mentionedName != null;
                event.reply("Beef War Started With " + mentionedName.getAsMention() + " And " + user.getAsMention()).queue();
                isBeef = true;
                System.out.println("started, isBeef = " + isBeef);
            } else {
                event.reply("Sorry, another beef is happening right now.").queue();
            }
            ;
        } else if (event.getName().equals("beefend")){
            Objects.requireNonNull(event.getGuild()).removeRoleFromMember(Objects.requireNonNull(event.getMember()), Objects.requireNonNull(event.getGuild().getRoleById(config.get("ROLE")))).queue();
            event.reply("Done").queue();
            isBeef = false;
            System.out.println("ended, isBeef = "+isBeef);
        }
    }
}