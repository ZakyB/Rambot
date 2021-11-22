package Rambot.Discord;

public class Utilisateur  {
    String idUser;
    String idServ;
    String userName;
    String servName;

    public Utilisateur(String idUser, String idServ, String userName, String servName) {
        this.idUser = idUser;
        this.idServ = idServ;
        this.userName = userName;
        this.servName = servName;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getIdServ() {
        return idServ;
    }

    public String getUserName() {
        return userName;
    }

    public String getServName() {
        return servName;
    }
    public String[] getArrayUser() {
        String [] ArrayUser = {idUser,idServ,userName,servName};
        return ArrayUser;
    }
    public String toString(){
        return "idUser:"+getIdUser()+" idServ"+idServ+" userName:"+userName+" servName:"+servName;
    }
}
