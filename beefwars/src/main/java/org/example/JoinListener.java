package org.example;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class JoinListener extends ListenerAdapter {
    private HashMap<String, List<Role>> userRolesMap;

    public static List<Long> getIdsFromHashmap(HashMap<String, List<Role>> hashmap, String key) {
        List<Role> roles = hashmap.get(key);
        List<Long> ids = new ArrayList<>();

        if (roles != null && !roles.isEmpty()) {
            for (Role role : roles) {
                ids.add(Long.valueOf(role.getId()));
            }
        }

        return ids;
    }

    public JoinListener(HashMap<String, List<Role>> userRolesMap) {
        this.userRolesMap = userRolesMap;
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        String userIdJoin = event.getUser().getId();
        if(userRolesMap.containsKey(userIdJoin)){
            List<Long> ids = getIdsFromHashmap(userRolesMap, userIdJoin);
            for (Long id : ids){
                Role role = event.getGuild().getRoleById(id);
                System.out.println(id);
                assert role != null;
                event.getGuild().addRoleToMember(Objects.requireNonNull(event.getGuild().getMemberById(userIdJoin)), role).queue();
            }
            System.out.println("Join Listener");

        }
    }
}
