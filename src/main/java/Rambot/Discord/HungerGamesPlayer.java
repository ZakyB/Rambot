package Rambot.Discord;

public class HungerGamesPlayer {
    int id;
    String name;
    String avatarUrl;
    int tribute;
    boolean alive;

    public HungerGamesPlayer(int id,String name,String avatarUrl){
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.tribute = 0;
        this.alive = true;
    }
    //getters
    public int getId () {return id;}
    public String getName() {return name;}
    public String getAvatarUrl() {return avatarUrl;}
    public int getTribute() {return tribute;}
    public boolean getAlive() {return alive;}
    //setters
    public void setId(int id){this.id=id;}
    public void setName(String name){this.name=name;}
    public void setTribute(int tribute){this.tribute=tribute;}
    public void setAlive(boolean alive){this.alive=alive;}
    //functions
    public void addTribute(int n){this.tribute=this.tribute+n;}
    //BLOODBATH EVENT
    public String BB1(HungerGamesPlayer player){
        player.addTribute(1);
        return player.name+" grabs a shovel.";
    }
    //public String BB2


}
