package Rambot.Discord.CommandBuilder.Commands;

import Rambot.Discord.CommandBuilder.CommandContext;
import Rambot.Discord.CommandBuilder.ICommand;
import Rambot.Discord.Config;
import Rambot.Discord.DatabaseFunction;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.SQLException;
import java.util.List;

public class SetLang implements ICommand {
    private String[] MissingArguments = Config.get("missing_arguments").replace("\\E","").replace("\\Q","").split("-");
    private String[] MissingPermissions = Config.get("missing_permissions").replace("\\E","").replace("\\Q","").split("-");
    private String[] LangUpdate = Config.get("lang_update").replace("\\E","").replace("\\Q","").split("-");
    private String[] SetLangHelp = Config.get("setlang_help").replace("\\E","").replace("\\Q","").split("-");
    private String[] ArrayIdLang = Config.get("id_lang").replace("\\E","").replace("\\Q","").split("-");


    @Override
    public void handle(CommandContext ctx) throws SQLException {
        final String idServ = ctx.getGuild().getId();
        final int idLang = ctx.getIdLang();
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        final Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();
        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            channel.sendMessage(MissingPermissions[idLang]).queue();
            return;
        }
        if (args.size() <1) {
            channel.sendMessage(MissingArguments[idLang]).queue();
            return;
        }
        int newIdLang = Integer.parseInt(args.get(0));
        Boolean perform = DatabaseFunction.setLang(idServ,newIdLang);
        if (perform == true){
            channel.sendMessage(LangUpdate[newIdLang-1]).queue();
        }else{
            channel.sendMessage("ERROR X.X").queue();
        }

    }

    @Override
    public String getName() {
        return "setlang";
    }

    @Override
    public String getHelp(CommandContext ctx) throws SQLException {
        final int idLang = ctx.getIdLang();
        return SetLangHelp[idLang]+"\n"+
                ArrayIdLang[idLang]+"\n"+
                "Usage :`-setlang <n°lang>`";
    }
}
