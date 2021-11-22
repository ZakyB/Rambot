
package Rambot.Discord.CommandBuilder.Commands;
import Rambot.Discord.CommandBuilder.CommandContext;
import Rambot.Discord.CommandBuilder.ICommand;
import Rambot.Discord.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.SQLException;
import java.util.List;

public class Magic8Ball implements ICommand {
    private String[] PingHelp = Config.get("ping_help").replace("\\E","").replace("\\Q","").split("-");
    private String[] answer = Config.get("answer").replace("\\E","").replace("\\Q","").split("-");

    @Override
    public void handle(CommandContext ctx) throws SQLException {
        final List<String> args = ctx.getArgs();
        final TextChannel channel = ctx.getChannel();
        if (args.size() <1) {
            channel.sendMessage("9").queue();
            return;
        }
        final int idLang = ctx.getIdLang();
        int rand;
        String newLine = System.getProperty("line.separator");
        String[] answer1 = { "¯\\_(ツ)_/¯","Certainement", "Sans doutes", "Yep, définitivement", "Sans doutes", "I guess yes",
                "Les signes sont pour", "Mise sur ça oui", "Bonne perspective", "🇾 🇪 🇸", "Why not", "Je sais po",
                "Idk buddy", "Bip Boop service en panne x.x", "Je sais pas repose la question plus tard",
                "Nope,définitivement.", "Les anciens sont pas trop d'accord", "Tu mises sur le mauvais cheval mon pote",
                "HAHAHAHAHAHAHAHAHHAHAHAHAHAHAHAHAHAHAHAHAHHAAHHAHAHAHAHA" + newLine + "non.", "La réponse est non", };
        String[] doute = {"C'est marrant de voir que vous m'écoutez" , "Vous savez,c'est juste une boule que je lance ._." ,
                "*Cache le protocole RNG*" , "(ils sont si naïf)" , "*sifflote*" , "Vous savez je fais que jeter une boule hein" ,
                "Je trouve vous me prenez un peu trop au sérieux quand meme" };
        rand = (int) (Math.random() * ((answer1.length)));
        ctx.getChannel().sendMessage(answer1[rand]).queue();
        /*JDA jda = ctx.getJDA();

        jda.getRestPing().queue(
                (ping) -> ctx.getChannel()
                        .sendMessageFormat("Pong: %sms",ping,jda.getGatewayPing()).queue());*/

    }

    @Override
    public String getHelp(CommandContext ctx) throws SQLException {
        final int idLang = ctx.getIdLang();
        return PingHelp[1];
    }

    @Override
    public String getName() {
        return "8ball";
    }


}
