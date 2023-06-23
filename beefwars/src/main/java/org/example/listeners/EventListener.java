package org.example.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if(event.getName().equals("bears")){

            //Send a message in response to the command being run
            event.reply("ʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔʕ •ᴥ•ʔ").queue();
        }
    }
}
