package Rambot.Discord;

import io.github.cdimascio.dotenv.Dotenv;

public class DataConfig {
    private static final Dotenv dotenv = Dotenv.configure().directory("assets").filename(".env").load();

    public static String get(String key){
        return dotenv.get(key.toUpperCase());
    }
}
