package Rambot.Discord;


import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Rambot {
    public static String owner = DataConfig.get("OWNER");
    public static String url = DataConfig.get("URL");
    public static String user = DataConfig.get("USER");
    public static String pwd = DataConfig.get("PWD");
    public static String prefix = DataConfig.get("PREFIX");
    public static Connection connection;
    //host bot :
    private static String token = DataConfig.get("TOKEN");
    //local bot :
    //private static String token = "";
    public static void main(String[] args)throws LoginException{
        try {
            connection = DriverManager.getConnection(url,user,pwd);
            System.out.println("Connexion effective à la base de données !");
        } catch (SQLException e){
            System.out.println("Echec de la connecion de la base de données !");
            e.printStackTrace();
        }
        EventWaiter waiter = new EventWaiter();
        JDABuilder builder = JDABuilder.createDefault(token);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        builder.enableIntents(GatewayIntent.GUILD_MESSAGE_REACTIONS);
        builder.setActivity(Activity.watching("to enjoy life"));
        builder.addEventListeners(new Listener(waiter),waiter);
        builder.build();
        /*JDABuilder builder =JDABuilder.createDefault(token);
        EventWaiter waiter = new EventWaiter();
        builder.setActivity(Activity.watching("to enjoy life"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS);
        //EVENTS
        //builder.addEventListeners(new GuildMemberLeave());
        //COMMANDS
        builder.build();*/
    }
}
