
package Rambot.Discord.CommandBuilder.Commands;
import Rambot.Discord.CommandBuilder.CommandContext;
import Rambot.Discord.CommandBuilder.ICommand;
import Rambot.Discord.Config;
import net.dv8tion.jda.api.JDA;

import java.sql.SQLException;

public class PingPong implements ICommand {
    private String[] PingHelp = Config.get("ping_help").replace("\\E","").replace("\\Q","").split("-");

    @Override
    public void handle(CommandContext ctx) throws SQLException {
        final int idLang = ctx.getIdLang();
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                        .sendMessageFormat("Pong: %sms",ping,jda.getGatewayPing()).queue());

    }

    @Override
    public String getHelp(CommandContext ctx) throws SQLException {
        final int idLang = ctx.getIdLang();
        return PingHelp[1];
    }

    @Override
    public String getName() {
        return "ping";
    }


}
