package Rambot.Discord.CommandBuilder.Commands;

import Rambot.Discord.CommandBuilder.CommandContext;
import Rambot.Discord.CommandBuilder.ICommand;
import Rambot.Discord.Config;
import Rambot.Discord.DatabaseFunction;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.SQLException;
import java.util.List;

public class Profile implements ICommand {
    private String[] ProfileDesc = Config.get("prof_desc").replace("\\E","").replace("\\Q","").split("-");
    private String[] ProfileHelp = Config.get("prof_help").replace("\\E","").replace("\\Q","").split("-");
    private String[] MissingArguments = Config.get("missing_arguments").replace("\\E","").replace("\\Q","").split("-");
    @Override
    public void handle(CommandContext ctx) throws SQLException {
        final String idServ = ctx.getGuild().getId();
        final TextChannel channel = ctx.getChannel();
        final Message message = ctx.getMessage();
        Member member = ctx.getMember();
        final List<String> args = ctx.getArgs();
        final int idLang = ctx.getIdLang();
        String idBot;
        if (args.size()>0) {
            if (message.getMentionedMembers().isEmpty()) {
                channel.sendMessage(MissingArguments[idLang]).queue();
                return;
            }else {
                member = message.getMentionedMembers().get(0);
            }
        }
        if (member.getId().equals("676872120206360596")){
            idBot="0";
        }
        else {
            idBot = DatabaseFunction.getIdBot(member.getId(), idServ);
        }
        String profile = DatabaseFunction.getProfile(idBot);
        String infoProfile[] = profile.split("-");
        String[] inputProfileDesc = ProfileDesc[idLang].split("_");
        String name = member.getEffectiveName();
        String link = member.getUser().getAvatarUrl();

        EmbedBuilder embedProfile = new EmbedBuilder();
        embedProfile.setColor(0xff3923);
        embedProfile.setTitle(inputProfileDesc[0].replace("%VAR%",name));
        embedProfile.setDescription(inputProfileDesc[1]+ infoProfile[0] +"\n"+ inputProfileDesc[2] + infoProfile[2] +"\n"+ inputProfileDesc[3] + infoProfile[3]+" RambotCoin"
                +"\n"+ inputProfileDesc[4]+"\n"+inputProfileDesc[5]);
        embedProfile.setThumbnail(link);
        channel.sendMessage(embedProfile.build()).queue();


    }

    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public String getHelp(CommandContext ctx) throws SQLException {
        final int idLang = ctx.getIdLang();
        return ProfileHelp[idLang]
                +"\nUsage: `-profile (<@user>)`";
    }
}
