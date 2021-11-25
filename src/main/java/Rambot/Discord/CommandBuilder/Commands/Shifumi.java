package Rambot.Discord.CommandBuilder.Commands;

import Rambot.Discord.CommandBuilder.CommandContext;
import Rambot.Discord.CommandBuilder.ICommand;
import Rambot.Discord.Config;
import Rambot.Discord.DatabaseFunction;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Shifumi implements ICommand {
    private static final String ROCK = "\uD83E\uDEA8";
    private static final String PAPER = "\uD83E\uDDFB";
    private static final String SCISSORS = "\u2702\uFE0F";
    private final EventWaiter waiter;

    public Shifumi(EventWaiter waiter){
        this.waiter=waiter;
    }
    private String[] Wait = Config.get("wait").replace("\\E","").replace("\\Q","").split("-");
    private String[] ShifumiHelp = Config.get("shifumi_help").replace("\\E","").replace("\\Q","").split("_");
    private String[] Shifumi = Config.get("shifumi").replace("\\E","").replace("\\Q","").split("-");
    private String[] ShifumiStart = Config.get("shifumi_start").replace("\\E","").replace("\\Q","").split("-");
    List<String> ChannelOnGoing = new ArrayList<String>();
    @Override
    public void handle(CommandContext ctx) throws SQLException {
        final TextChannel channel = ctx.getChannel();
        final int idLang = ctx.getIdLang();
        final String[] ShifumiRes = Shifumi[idLang].split("_");
        if (ChannelOnGoing.contains(channel.toString())) {
            channel.sendMessage(Wait[idLang]).queue();
        } else {
            ChannelOnGoing.add(channel.toString());

            channel.sendMessage(ShifumiStart[idLang])
                    .queue((message) -> {
                        message.addReaction(ROCK).queue();
                        message.addReaction(PAPER).queue();
                        message.addReaction(SCISSORS).queue();

                        this.waiter.waitForEvent(
                                GuildMessageReactionAddEvent.class,
                                (e) -> e.getMessageIdLong() == message.getIdLong() && !e.getUser().isBot(),
                                (e) -> {
                                    channel.sendMessage("SHI").queue();
                                    channel.sendMessage("FU").queue();
                                    channel.sendMessage("MI").queue();
                                    String reaction = e.getReactionEmote().getName();
                                    int emoteBot = (int) (Math.random() * 3);
                                    Boolean shifumi;
                                    switch (emoteBot){
                                        case 0:
                                            channel.sendMessage(ROCK).queue();
                                            break;
                                        case 1:
                                            channel.sendMessage(PAPER).queue();
                                            break;
                                        case 2:
                                            channel.sendMessage(SCISSORS).queue();
                                            break;
                                    }
                                    int emote = 0;
                                    switch (reaction){
                                        case ROCK:
                                            emote = 0;
                                            break;
                                        case PAPER:
                                            emote = 1;
                                            break;
                                        case SCISSORS:
                                            emote = 2;
                                            break;
                                        default:
                                            emote = 3;
                                            channel.sendMessage("???").queue();
                                    }
                                    if (!(emote == 3)) {
                                        shifumi = DatabaseFunction.shifumi(emote, emoteBot);
                                        if (shifumi==null){
                                            channel.sendMessage(ShifumiRes[0]).queue();
                                        } else if (shifumi==true){
                                            channel.sendMessage(ShifumiRes[1]).queue();
                                        } else {
                                            channel.sendMessage(ShifumiRes[2]).queue();
                                        }
                                    }
                                    ChannelOnGoing.remove(channel.toString());

                                },
                                20L, TimeUnit.SECONDS,
                                () -> {
                                    channel.sendMessage("\uD83D\uDE34").queue();
                                    ChannelOnGoing.remove(channel.toString());
                                });


                    });
        }
    }
    @Override
    public String getName() {
        return "shifumi";
    }

    @Override
    public String getHelp(CommandContext ctx) throws SQLException {
        final int idLang = ctx.getIdLang();
        return ShifumiHelp[idLang]
                +"\nUsage :`-shifumi`";
    }
}
