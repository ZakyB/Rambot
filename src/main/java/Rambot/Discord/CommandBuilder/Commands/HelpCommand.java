package Rambot.Discord.CommandBuilder.Commands;

import Rambot.Discord.CommandBuilder.CommandManager;
import Rambot.Discord.CommandBuilder.CommandContext;
import Rambot.Discord.CommandBuilder.ICommand;
import Rambot.Discord.Config;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.SQLException;
import java.util.List;

public class HelpCommand implements ICommand {
    private final CommandManager manager;
    private String[] ListCommand = Config.get("List_of_commands").replace("\\E","").replace("\\Q","").split("-");
    private String[] NothingFound = Config.get("nothing_found_for").replace("\\E","").replace("\\Q","").split("-");
    private String[] HelpHelp = Config.get("help_help").replace("\\E","").replace("\\Q","").split("-");
    public HelpCommand(CommandManager manager) {
        this.manager = manager;
    }
    @Override
    public void handle(CommandContext ctx) throws SQLException {
        final int idLang = ctx.getIdLang();
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();


        if (args.isEmpty()){
            StringBuilder builder = new StringBuilder();
            builder.append(ListCommand[idLang]+"\n");
            manager.getCommands().stream().map(ICommand::getName).forEach(
                    (it)->builder.append("`").append(Config.get("prefix")).append(it).append("`\n")
            );
            channel.sendMessage(builder.toString()+"").queue();
            return;
        }
        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command==null){
            channel.sendMessage(NothingFound[idLang]+" "+search).queue();
            return;
        }
        channel.sendMessage(command.getHelp(ctx)).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp(CommandContext ctx) throws SQLException {
        final int idLang = ctx.getIdLang();
        return HelpHelp[idLang]+"\n" +
                "Usage: `-help [command]`";
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands","cmds","commandlist");
    }
}
