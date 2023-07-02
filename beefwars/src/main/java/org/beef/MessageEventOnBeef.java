package org.beef;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class MessageEventOnBeef extends ListenerAdapter {
    private Channel channel;
    private Timer timer;

    public MessageEventOnBeef(Channel channel) {
        this.channel = channel;
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Dotenv config = Dotenv.load();
        if (event.getChannel().equals(channel)) {
            // Cancel previous timer if it exists
            if (timer != null) {
                timer.cancel();
                System.out.println("message sent. Beef will go on.");
                event.getJDA().removeEventListener(MessageEventOnBeef.this);
            }

            // Create a new timer
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // This code will run after 5 minutes of inactivity
                    System.out.println("10 minutes passed without a new message.");
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(BotCommands.beefers.get(0)), event.getGuild().getRoleById(config.get("ROLE"))).queue();
                    event.getGuild().removeRoleFromMember(UserSnowflake.fromId(BotCommands.beefers.get(1)), event.getGuild().getRoleById(config.get("ROLE"))).queue();
                    BotCommands.isBeef = false;
                    BotCommands.beefRemaining.clear();
                    BotCommands.beefers.clear();
                    BotCommands.beefend = 0;

                    // Delete the MessageEventOnBeef object
                    event.getJDA().removeEventListener(MessageEventOnBeef.this);
                }
            }, TimeUnit.MINUTES.toMillis(10));
        }
    }
}