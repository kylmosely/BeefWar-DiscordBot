package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BotCommands extends ListenerAdapter {
    public static boolean isBeef = false;
    public static int beefend = 0;
    public static String UserId;
    public static String mentionedId;
    public static ArrayList<String> beefers = new ArrayList<String>();
    public static ArrayList<String> beefRemaining = new ArrayList<>();

    public static HashMap<String, List<Role>> getMemberRoles(Guild guild) {
        HashMap<String, List<Role>> userRolesMap = new HashMap<>();

        List<Member> members = guild.getMemberCache().asList();
        for (Member member : members) {
            String userId = member.getId();
            List<Role> roles = member.getRoles();
            userRolesMap.put(userId, roles);
        }
        return userRolesMap;
    }


    public BotCommands() {
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Dotenv config = Dotenv.load();
        if (event.getName().equals("beef")) {
            if (!isBeef) {
                User user = event.getUser();
                UserId = event.getUser().getId();

                mentionedId = Objects.requireNonNull(event.getOption("person")).getAsString();

                beefers.add(UserId);
                beefers.add(mentionedId);
                beefRemaining.add(UserId);
                beefRemaining.add(mentionedId);

                // 0: User 1:mentioned
                Member mentionedName = Objects.requireNonNull(event.getOption("person")).getAsMember();
                Objects.requireNonNull(event.getGuild()).addRoleToMember(UserSnowflake.fromId(mentionedId), Objects.requireNonNull(event.getGuild().getRoleById(config.get("ROLE")))).queue();
                event.getGuild().addRoleToMember(UserSnowflake.fromId(UserId), Objects.requireNonNull(event.getGuild().getRoleById(config.get("ROLE")))).queue();

                assert mentionedName != null;
                event.reply("Beef War Started With " + mentionedName.getAsMention() + " And " + user.getAsMention()).queue();
                isBeef = true;
                System.out.println("started, isBeef = " + isBeef);

                HashMap<String, List<Role>> userRolesMap = getMemberRoles(Objects.requireNonNull(event.getGuild()));
                event.getJDA().addEventListener(new JoinListener(userRolesMap));
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
                // add another end operation
                event.getGuild().removeRoleFromMember(UserSnowflake.fromId(beefers.get(0)), event.getGuild().getRoleById(config.get("ROLE"))).queue();
                event.getGuild().removeRoleFromMember(UserSnowflake.fromId(beefers.get(1)), event.getGuild().getRoleById(config.get("ROLE"))).queue();
                System.out.println("beef ended, isBeef = " + isBeef);
                beefers.clear();
                beefRemaining.clear();
                beefend = 0;
            }
        } else if (event.getName().equals("beeflock")){
            if (isBeef == true && beefers.contains(event.getUser().getId())){
                event.getJDA().addEventListener(new MessageEventOnBeef(event.getChannel()));
                event.reply("Beef will end in 10 minutes if no new message is sent").queue();
            }
        }

        else{
            event.reply("Cannot end beef. Either you are not a beefer, or you have already ended beef.");
        }
    }

}


class Rolez {
    private String name;
    private long id;

    public Rolez(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public long getId() {
        return id;
    }
}

// TODO: set an Option for what to beef about