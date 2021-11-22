package Rambot.Discord.CommandBuilder;

import Rambot.Discord.CommandBuilder.Commands.*;
import Rambot.Discord.Config;
import Rambot.Discord.DatabaseFunction;
import Rambot.Discord.Utilisateur;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager(EventWaiter waiter){
        addCommand(new HelpCommand(this));
        addCommand(new PingPong());
        addCommand(new Shifumi(waiter));
        addCommand(new SetLang());
        addCommand(new Magic8Ball());
        /*addCommand(new HelpCommand(this));
        addCommand(new SetLang());
        addCommand(new PingPong());
        addCommand(new Profile());
        addCommand(new Inventory());
        addCommand(new InventoryIcone());
        addCommand(new ArmyEmote());
        addCommand(new Shop());
        addCommand(new Buy());
        addCommand(new Roll());
        addCommand(new Clear());
        addCommand(new Kick());
        addCommand(new Shifumi(waiter));*/
    }

    private void addCommand(ICommand cmd){
        boolean nameFound = this.commands.stream().anyMatch((it)->it.getName().equalsIgnoreCase(cmd.getName()));

        if (nameFound){
            throw new IllegalArgumentException("A command with this name is already present :^)");
        }
        commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return commands;
    }
    @Nullable
    public ICommand getCommand(String search){
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands){
            if(cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)){
                return cmd;
            }
        }
        return null;
    }
    List<String> confirmedUser = new ArrayList<String>();
    public void handle(GuildMessageReceivedEvent event) throws SQLException {
        Utilisateur utilisateur = new Utilisateur (event.getAuthor().getId(), event.getGuild().getId() ,
                event.getAuthor().getName() , event.getGuild().getName());
        if (!confirmedUser.contains(utilisateur.getIdUser()+utilisateur.getIdServ())){
            try {
                boolean check = DatabaseFunction.checkUser(utilisateur.getIdServ(),utilisateur.getIdUser());
                if (check == false) {
                    DatabaseFunction.register(utilisateur.getIdServ(), utilisateur.getIdUser(),
                            utilisateur.getUserName());
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            confirmedUser.add(utilisateur.getIdUser()+utilisateur.getIdServ());
        }
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)"+ Pattern.quote(Config.get("prefix")),"")
                .split("\\s");

        String invoke=split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);
        if (cmd != null){
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1,split.length);

            CommandContext ctx = new CommandContext(event,args);

            cmd.handle(ctx);
        }
    }
}
