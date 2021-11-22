package Rambot.Discord;

import Rambot.Discord.CommandBuilder.CommandManager;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class Listener extends ListenerAdapter {
    private String[] guildJoin = Config.get("guild_member_join").replace("\\E","").replace("\\Q","").split("@");
    private String[] guildRemove = Config.get("guild_member_remove").replace("\\E","").replace("\\Q","").split("@");
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager;

    public Listener(EventWaiter waiter) {
        manager = new CommandManager(waiter);
    }
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        LOGGER.info("{} is ready",event.getJDA().getSelfUser().getAsTag());
    }
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {
        int idLang=0;
        try {
            idLang = getIdLang(event.getGuild().getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String[] messages = guildJoin[idLang].split("_");
        Random rand = new Random();
        int number = rand.nextInt(messages.length);
        MessageBuilder join = new MessageBuilder();
        join.setContent(messages[number].replace("%MEMBER%", event.getMember().getAsMention()));
        event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();
    }
    /* @Override
     public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent event) {
         int idLang = 0;
         try {
             idLang = getIdLang(event.getGuild().getId());
         } catch (SQLException e) {
             e.printStackTrace();
         }
         String[] messages = guildRemove[idLang].split("_");
         Random rand = new Random();
         int number = rand.nextInt(messages.length);
         MessageBuilder join = new MessageBuilder();
         System.out.println("test:"+messages[number]);
         join.setContent(messages[number].replace("%MEMBER%", event.getMember().getAsMention()));
         event.getGuild().getDefaultChannel().sendMessage(join.build()).queue();
     }*/
    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        event.getGuild().getDefaultChannel().sendMessage("\uD83D\uDC4B").queue();
        try {
            DatabaseFunction.ServRegister(event.getGuild().getId(),event.getGuild().getName());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        if (user.isBot() || event.isWebhookMessage()){
            return;
        }
        String prefix = Config.get("prefix");
        String raw = event.getMessage().getContentRaw();

        if(raw.equalsIgnoreCase(prefix+"shutdown")
                && user.getId().equals(Config.get("OWNER_ID"))){
            LOGGER.info("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown();

            return;
        }
        if (raw.startsWith(prefix)){
            try {
                manager.handle(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    private int getIdLang(String guildId) throws SQLException {
        String res="";
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT `idLangue` FROM `server` WHERE id="+guildId);
        while (rs.next()) {
            res += rs.getString("idLangue");
        }
        return Integer.parseInt(res);
    }
}
