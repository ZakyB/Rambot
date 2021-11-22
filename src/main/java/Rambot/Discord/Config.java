package Rambot.Discord;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.configure().directory("assets").filename("text.env").load();

    public static String get(String key){
        return dotenv.get(key.toUpperCase());
    }
}
