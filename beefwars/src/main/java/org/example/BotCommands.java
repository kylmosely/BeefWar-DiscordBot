package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class BotCommands extends ListenerAdapter {
    private boolean isBeef = false;
    private int beefend = 0;
    ArrayList<String> beefers = new ArrayList<String>();
    ArrayList<String> beefRemaining = new ArrayList<>();
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

                beefers.add(UserId);
                beefers.add(mentionedId);
                beefRemaining.add(UserId);
                beefRemaining.add(mentionedId);

                // 0: User 1:mentioned
                System.out.println(UserId + mentionedId);
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
        } else if (event.getName().equals("beefend")) {
            if (beefRemaining.contains(event.getUser().getId())){
                beefend = beefend + 1;
                beefRemaining.remove(event.getUser().getId());
                event.reply(beefend + "/" + beefers.size() + " Have agreed to end beef").queue();
            }
            System.out.println(beefend);
            if(beefRemaining.isEmpty()){
                isBeef = false;
                event.getChannel().sendMessage("Beef is over. Hope we can now be friends :)");
                // add another end operation
                event.getGuild().removeRoleFromMember(UserSnowflake.fromId(beefers.get(0)), event.getGuild().getRoleById(config.get("ROLE"))).queue();
                event.getGuild().removeRoleFromMember(UserSnowflake.fromId(beefers.get(1)), event.getGuild().getRoleById(config.get("ROLE"))).queue();
                System.out.println("beef ended, isBeef = " + isBeef);
                beefers.clear();
                beefRemaining.clear();
                beefend = 0;
            }
        } else{
            event.reply("Cannot end beef. Either you are not a beefer, or you have already ended beef.");
        }
    }
}

// TODO: set an Option for what to beef about
