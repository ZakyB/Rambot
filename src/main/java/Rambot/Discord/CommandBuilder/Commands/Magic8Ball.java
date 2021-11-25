
package Rambot.Discord.CommandBuilder.Commands;
import Rambot.Discord.CommandBuilder.CommandContext;
import Rambot.Discord.CommandBuilder.ICommand;
import Rambot.Discord.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Magic8Ball implements ICommand {
    private String[] M8ballHelp = Config.get("8ball_help").replace("\\E","").replace("\\Q","").split("-");
    private String[] answer = Config.get("answer").replace("\\E","").replace("\\Q","").split("@");
    private String[] doute = Config.get("doute").replace("\\E","").replace("\\Q","").split("@");
    int n = 0;
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
        String[] answerLang = answer[idLang].split("-");
        rand = (int) (Math.random() * ((answerLang.length)));
        ctx.getChannel().sendMessage(answerLang[rand].replace("\\n","\n")).queue();
        n = n + 1;
        if (n == 10) {
            String[] douteLang = doute[idLang].split("-");
            rand = (int) (Math.random() * ((douteLang.length)));
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for(int t=0;t<douteLang.length;t++){
                ctx.getChannel().sendMessage(douteLang[t]).queue();
            }
            //ctx.getChannel().sendMessage(douteLang[rand]).queue();
            n = 0;
        }

    }

    @Override
    public String getHelp(CommandContext ctx) throws SQLException {
        final int idLang = ctx.getIdLang();
        return M8ballHelp[idLang]+"\n"
                +"Usage: `-8ball [question]`";
    }

    @Override
    public String getName() {
        return "8ball";
    }


}
