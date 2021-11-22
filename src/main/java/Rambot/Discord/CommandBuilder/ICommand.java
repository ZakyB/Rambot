package Rambot.Discord.CommandBuilder;

import java.sql.SQLException;
import java.util.List;

public interface ICommand {
    void handle(CommandContext ctx) throws SQLException;

    String getName();

    String getHelp(CommandContext ctx) throws SQLException;

    default List<String> getAliases() {
        return List.of();
    }
}
