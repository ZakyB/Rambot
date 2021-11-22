package Rambot.Discord.CommandBuilder;

import Rambot.Discord.Rambot;
import me.duncte123.botcommons.commands.ICommandContext;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final List<String> args;

    public CommandContext(GuildMessageReceivedEvent event, List<String> args) {
        this.event = event;
        this.args = args;
    }

    @Override
    public Guild getGuild() {
        return this.getEvent().getGuild();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }
    public int getIdLang() throws SQLException {
        String res="";
        Statement statement = Rambot.connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT `idLangue` FROM `server` WHERE id="+this.getGuild().getId());
        while (rs.next()) {
            res += rs.getString("idLangue");
        }
        return Integer.parseInt(res)-1;
    }
    public List<String> getArgs(){
        return this.args;
    }
}
